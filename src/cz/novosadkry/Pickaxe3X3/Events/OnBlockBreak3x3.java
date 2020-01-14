package cz.novosadkry.Pickaxe3X3.Events;

import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.FlagPermissions;
import cz.novosadkry.Pickaxe3X3.Config.MainCfg;
import cz.novosadkry.Pickaxe3X3.Config.MineableItems;
import cz.novosadkry.Pickaxe3X3.EventHandlers.BlockBreak3x3Event;
import cz.novosadkry.Pickaxe3X3.Main;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.List;

public class OnBlockBreak3x3 implements Listener {
    public BlockFace getBlockFace(Player player) {
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, MainCfg.getBlockFaceCheckRange());
        if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding()) return null;
        Block targetBlock = lastTwoTargetBlocks.get(1);
        Block adjacentBlock = lastTwoTargetBlocks.get(0);
        return targetBlock.getFace(adjacentBlock);
    }

    @EventHandler
    public void onBlockBreak3x3(BlockBreak3x3Event event) {
        Player player = event.getBlockBreakEvent().getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        Block baseBlock = event.getBlockBreakEvent().getBlock();
        BlockFace blockFace = getBlockFace(player);

        if (blockFace != null) {
            List<Material> mineable;

            // Get corresponding item list
            if (item.getType().toString().contains("PICKAXE"))
                mineable = MineableItems.getPickaxeItems();
            else
                mineable = Collections.emptyList();

            // Return if base-block isn't mineable or can't be broken by item-in-hand
            if (!mineable.contains(baseBlock.getType()) || baseBlock.getDrops(item).size() < 1)
                return;

            // Get surrounding blocks and break them with item-in-hand
            Block[][] blocksToDestroy = getSurroundingBlocks(baseBlock, player, event.getRows(), event.getColumns());
            int count = breakBlocks(mineable, baseBlock, blocksToDestroy, player, item);

            // Change the durability of the item-in-hand
            if (item.getItemMeta() instanceof Damageable) {
                if (player.getGameMode() != GameMode.CREATIVE) {
                    Damageable damageMeta = (Damageable)item.getItemMeta();
                    damageMeta.setDamage(damageMeta.getDamage() + count);
                    item.setItemMeta((ItemMeta)damageMeta);

                    // Destroy item-in-hand if durability drops below zero
                    if (item.getType().getMaxDurability() - damageMeta.getDamage() < 1) {
                        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                        player.spawnParticle(Particle.ITEM_CRACK, player.getEyeLocation(), 10, 0.3, 0.5, 0.3, item);
                        player.getInventory().remove(item);
                    }
                }
            }
        }
    }

    public Block[][] getSurroundingBlocks(Block baseBlock, Player player, int rows, int columns) {
        Block[][] blocksToDestroy = new Block[rows][columns];
        int offsetX = (1-columns)/2;
        int offsetY = (rows-1)/2;

        // Get player's face direction
        Vector playerFace = player.getEyeLocation().getDirection();
        BlockFace blockFace = getBlockFace(player);

        // Starts from upper-left corner and continues to lower-right corner
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                // Check on which face of base-block is player looking at
                switch (blockFace) {
                    case EAST:
                    case WEST:
                        blocksToDestroy[y][x] = baseBlock.getRelative(0, offsetY-y, x+offsetX);
                        break;

                    case NORTH:
                    case SOUTH:
                        blocksToDestroy[y][x] = baseBlock.getRelative(x+offsetX, offsetY-y, 0);
                        break;

                    case UP:
                    case DOWN:
                        blocksToDestroy[y][x] = Math.abs(playerFace.getX()) > Math.abs(playerFace.getZ())
                                ? baseBlock.getRelative(offsetY-y, 0, x+offsetX)
                                : baseBlock.getRelative(x+offsetX, 0, offsetY-y);
                        break;
                }
            }
        }

        return blocksToDestroy;
    }

    public int breakBlocks(List<Material> mineable, Block baseBlock, Block[][] blocksToDestroy, Player player, ItemStack item) {
        int count = 0;

        for (int y = 0; y < blocksToDestroy.length; y++) {
            for (int x = 0; x < blocksToDestroy[y].length; x++) {
                // Continue if block-to-destroy isn't mineable
                if (!mineable.contains(blocksToDestroy[y][x].getType()))
                    continue;

                // Continue if block-to-destroy can't be broken by item-in-hand
                if (blocksToDestroy[y][x].getDrops(item).size() < 1)
                    continue;

                // Check if item-in-hand has enough durability
                if (item.getItemMeta() instanceof Damageable) {
                    if (player.getGameMode() != GameMode.CREATIVE) {
                        Damageable damageMeta = (Damageable)item.getItemMeta();

                        // (count + 1) -- We also need to count the middle block
                        if (item.getType().getMaxDurability() - damageMeta.getDamage() < count + 1)
                            break;
                    }
                }

                boolean hasPerms = true;
                if (Main.residence != null) {
                    // Check if player has permissions to destroy a certain block inside a residence
                    FlagPermissions perms = Main.residence.getPermsByLoc(blocksToDestroy[y][x].getLocation());
                    hasPerms = perms.playerHas(player, Flags.destroy, true);
                }

                if (hasPerms)
                {

                    // Check if item-in-hand has silk touch
                    if (item.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH))
                    {
                        Material m = blocksToDestroy[y][x].getType();
                        blocksToDestroy[y][x].setType(Material.AIR);
                        blocksToDestroy[y][x].getLocation().getWorld().dropItemNaturally(
                                blocksToDestroy[y][x].getLocation(),
                                new ItemStack(m, 1));
                    }

                    else
                        blocksToDestroy[y][x].breakNaturally(item);

                    count++;
                }
            }
        }

        return count;
    }
}
