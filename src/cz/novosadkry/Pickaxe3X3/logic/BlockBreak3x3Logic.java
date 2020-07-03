package cz.novosadkry.Pickaxe3X3.logic;

import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.actions.BlockActionInfo;
import com.gamingmesh.jobs.container.ActionType;
import com.gamingmesh.jobs.container.JobsPlayer;
import cz.novosadkry.Pickaxe3X3.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class BlockBreak3x3Logic {
    private static HashSet<UUID> lock = new HashSet<>();

    private Player player;
    private Block baseBlock;
    private int rows;
    private int columns;

    public BlockBreak3x3Logic(Player player, Block baseBlock, int rows, int columns) {
        this.player = player;
        this.baseBlock = baseBlock;
        this.rows = rows;
        this.columns = columns;
    }

    public Player getPlayer() {
        return player;
    }

    public Block getBaseBlock() {
        return baseBlock;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int run() {
        ItemStack item = player.getInventory().getItemInMainHand();
        BlockFace blockFace = getBlockFace(player);

        // Safety lock
        if (BlockBreak3x3Logic.lock.contains(player.getUniqueId()))
            return 0;

        if (blockFace != null) {
            List<Material> mineable;

            // Get corresponding item list
            if (item.getType().toString().contains("PICKAXE"))
                mineable = Main.mineableItemsConfig.pickaxeItems;
            else
                mineable = Collections.emptyList();

            // Return if base-block isn't mineable or can't be broken by item-in-hand
            if (!mineable.contains(baseBlock.getType()) || baseBlock.getDrops(item).size() < 1)
                return 0;

            // Get surrounding blocks and break them with item-in-hand
            int count = run(mineable, getSurroundingBlocks(blockFace), item);

            // Change the durability of the item-in-hand
            if (item.getItemMeta() instanceof Damageable) {
                if (player.getGameMode() != GameMode.CREATIVE) {
                    int damage = count;

                    // Get corresponding damage using the unbreaking formula
                    if (item.getItemMeta().hasEnchant(Enchantment.DURABILITY))
                    {
                        int level = item.getItemMeta().getEnchantLevel(Enchantment.DURABILITY);
                        float random = ThreadLocalRandom.current().nextFloat();

                        if (random > ((float)1 / (level + 1)))
                            damage = 0;
                    }

                    // Set damage to item-in-hand
                    Damageable damageMeta = (Damageable)item.getItemMeta();
                    damageMeta.setDamage(damageMeta.getDamage() + damage);

                    // Refresh item-in-hand
                    item.setItemMeta((ItemMeta)damageMeta);
                    player.getInventory().setItemInMainHand(item);

                    // Destroy item-in-hand if durability drops below zero
                    if (item.getType().getMaxDurability() - damageMeta.getDamage() < 1) {
                        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                        player.spawnParticle(Particle.ITEM_CRACK, player.getEyeLocation(), 10, 0.3, 0.5, 0.3, item);
                        player.getInventory().remove(item);
                    }
                }
            }

            return count;
        }

        return 0;
    }

    private int run(List<Material> mineable, Block[][] blocks, ItemStack item) {
        int canDestroy = -1;

        // Check how many items can item-in-hand destroy
        if (item.getItemMeta() instanceof Damageable) {
            if (player.getGameMode() != GameMode.CREATIVE) {
                Damageable damageMeta = (Damageable)item.getItemMeta();
                canDestroy = item.getType().getMaxDurability() - damageMeta.getDamage();
            }
        }

        // Add blocks to blocksToDestroy
        Block[] blocksToDestroy = getBlocksToDestroy(blocks, item, canDestroy, mineable);

        // Return if there are no blocks to destroy
        if (blocksToDestroy.length < 1)
            return 0;

        // Count the number of destroyed blocks
        int count = 0;

        // Add safety lock to prevent stack overflow
        lock.add(player.getUniqueId());

        // Check if it really is set
        if (lock.contains(player.getUniqueId()))
            // Break blocks in blocksToDestroy
            count = breakBlocks(blocksToDestroy, item);

        // Remove safety lock
        lock.remove(player.getUniqueId());

        return count;
    }

    private Block[][] getSurroundingBlocks(BlockFace blockFace) {
        Block[][] blocksToDestroy = new Block[rows][columns];
        int offsetX = (1-columns)/2;
        int offsetY = (rows-1)/2;

        // Get player's face direction
        Vector playerFace = player.getEyeLocation().getDirection();

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

    private Block[] getBlocksToDestroy(Block[][] blocks, ItemStack item, int canDestroy, List<Material> mineable) {
        List<Block> blocksToDestroy = new ArrayList<Block>();
        // Add blocks to blocksToDestroy
        for (int y = 0; y < blocks.length; y++) {
            for (int x = 0; x < blocks[y].length; x++) {
                // Break loop if item-in-hand doesn't have enough durability
                if (blocksToDestroy.size() >= canDestroy && canDestroy != -1)
                    break;

                /*
                   Continue if block-to-destroy is base-block
                            or block-to-destroy isn't mineable
                            or block-to-destroy can't be broken by item-in-hand
                */
                if (blocks[y][x].equals(baseBlock)
                        || !mineable.contains(blocks[y][x].getType())
                        || blocks[y][x].getDrops(item).size() < 1)
                    continue;

                blocksToDestroy.add(blocks[y][x]);
            }
        }

        return blocksToDestroy.toArray(new Block[0]);
    }

    private int breakBlocks(Block[] blocksToDestroy, ItemStack item) {
        int count = 0;

        for (Block block : blocksToDestroy) {
            BlockBreakEvent event = callBlockBreakEvent(player, block);

            if (Main.jobs != null) {
                JobsPlayer jPlayer = Jobs.getPlayerManager().getJobsPlayer(player);
                Jobs.action(jPlayer, new BlockActionInfo(block, ActionType.BREAK));
            }

            if (!event.isCancelled())
            {
                if (event.isDropItems() && player.getGameMode() == GameMode.SURVIVAL)
                    block.breakNaturally(item);
                else
                    block.setType(Material.AIR);

                count++;
            }
        }

        return count;
    }

    private BlockFace getBlockFace(Player player) {
        HashSet<Material> transparent = new HashSet<>();
        transparent.add(Material.WATER);
        transparent.add(Material.LAVA);
        transparent.add(Material.AIR);

        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(transparent, Main.mainConfig.blockFaceCheckRange);
        if (lastTwoTargetBlocks.size() != 2) return null;

        Block targetBlock = lastTwoTargetBlocks.get(1);
        Block adjacentBlock = lastTwoTargetBlocks.get(0);

        return targetBlock.getFace(adjacentBlock);
    }

    private BlockBreakEvent callBlockBreakEvent(Player player, Block block)
    {
        BlockBreakEvent event = new BlockBreakEvent(block, player);
        Bukkit.getPluginManager().callEvent(event);

        return event;
    }
}
