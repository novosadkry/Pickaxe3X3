package cz.novosadkry.Pickaxe3X3.Events;

import cz.novosadkry.Pickaxe3X3.Main;
import cz.novosadkry.Pickaxe3X3.Methods.BlockBreak3x3Logic;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OnBlockBreak implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (event.isCancelled())
            return;

        try {
            List<String> lore = item.getItemMeta().getLore();

            if (lore != null) {
                if (lore.size() > 0) {
                    for (String s : lore) {
                        if (s.startsWith(Main.mainConfig.prefix)) {
                            String[] split = s.split(" ")[1].split("x");
                            int columns = Integer.parseInt(split[0]);
                            int rows = Integer.parseInt(split[1]);

                            Block baseBlock = event.getBlock();
                            new BlockBreak3x3Logic(player, baseBlock, rows, columns).run();

                            event.setCancelled(true);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) { }
    }
}
