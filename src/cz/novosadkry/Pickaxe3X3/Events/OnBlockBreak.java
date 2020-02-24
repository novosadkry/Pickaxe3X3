package cz.novosadkry.Pickaxe3X3.Events;

import cz.novosadkry.Pickaxe3X3.EventHandlers.BlockBreak3x3Event;
import cz.novosadkry.Pickaxe3X3.Main;
import cz.novosadkry.Pickaxe3X3.Methods.BlockBreak3x3Logic;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class OnBlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        try {
            if (item.getItemMeta().getLore().size() > 0) {
                for (String s : item.getItemMeta().getLore()) {
                    if (s.startsWith(Main.mainConfig.prefix)) {
                        String[] split = s.split(" ")[1].split("x");
                        int columns = Integer.parseInt(split[0]);
                        int rows = Integer.parseInt(split[1]);

                        Block baseBlock = event.getBlock();
                        Block[] blocks = new BlockBreak3x3Logic(player, baseBlock, rows, columns).run();

                        Bukkit.getPluginManager().callEvent(new BlockBreak3x3Event(event, rows, columns, blocks));
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
