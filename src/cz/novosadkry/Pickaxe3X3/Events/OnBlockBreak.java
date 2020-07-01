package cz.novosadkry.Pickaxe3X3.Events;

import cz.novosadkry.Pickaxe3X3.Logic.BlockBreak3x3Logic;
import cz.novosadkry.Pickaxe3X3.Main;
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

        if (player.getGameMode() == GameMode.CREATIVE && !player.hasPermission("pickaxe3x3.use.creative"))
            return;

        try {
            List<String> lore = item.getItemMeta().getLore();

            if (lore != null && lore.size() > 0) {
                if (lore.contains(Main.mainConfig.lock))
                    return;

                for (String s : lore) {
                    if (s.startsWith(Main.mainConfig.prefix)) {
                        String[] split = s.split(" ")[1].split("x");
                        int columns = Integer.parseInt(split[0]);
                        int rows = Integer.parseInt(split[1]);

                        Block baseBlock = event.getBlock();
                        new BlockBreak3x3Logic(player, baseBlock, rows, columns).run();

                        break;
                    }
                }
            }
        }
        catch (NullPointerException ignored) { }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
