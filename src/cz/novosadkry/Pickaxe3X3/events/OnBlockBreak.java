package cz.novosadkry.Pickaxe3X3.events;

import cz.novosadkry.Pickaxe3X3.Main;
import cz.novosadkry.Pickaxe3X3.enchantments.AreaEnchantment;
import cz.novosadkry.Pickaxe3X3.logic.BlockBreak3x3Logic;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OnBlockBreak implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (player.getGameMode() == GameMode.CREATIVE && !player.hasPermission(Main.mainConfig.permCreative))
            return;

        try {
            List<String> lore = item.getItemMeta().getLore();

            if (lore != null && lore.size() > 0) {
                for (String s : lore) {
                    if (s.startsWith(Main.mainConfig.lorePrefix)) {
                        String[] split = s.split(" ")[1].split("x");
                        int columns = Integer.parseInt(split[0]);
                        int rows = Integer.parseInt(split[1]);

                        Block baseBlock = event.getBlock();
                        new BlockBreak3x3Logic(player, baseBlock, rows, columns).run();

                        return;
                    }
                }
            }

            AreaEnchantment areaEnchant = AreaEnchantment.getInstance();

            if (item.getEnchantments().containsKey(areaEnchant)) {
                int level = item.getEnchantmentLevel(areaEnchant);

                int columns = 1 + (level * 2);
                int rows = 1 + (level * 2);

                Block baseBlock = event.getBlock();
                new BlockBreak3x3Logic(player, baseBlock, rows, columns).run();
            }
        }
        catch (NullPointerException ignored) { }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
