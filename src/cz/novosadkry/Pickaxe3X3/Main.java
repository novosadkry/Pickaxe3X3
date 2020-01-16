package cz.novosadkry.Pickaxe3X3;

import com.bekvon.bukkit.residence.Residence;
import cz.novosadkry.Pickaxe3X3.Config.MainConfig;
import cz.novosadkry.Pickaxe3X3.Config.MineableItemsConfig;
import cz.novosadkry.Pickaxe3X3.Events.OnBlockBreak;
import cz.novosadkry.Pickaxe3X3.Events.OnBlockBreak3x3;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Residence residence = null;

    public static MainConfig mainConfig;
    public static MineableItemsConfig mineableItemsConfig;

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().isPluginEnabled("Residence"))
            residence = (Residence) getServer().getPluginManager().getPlugin("Residence");

        mainConfig = MainConfig.load();
        mineableItemsConfig = MineableItemsConfig.load();

        getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
        getServer().getPluginManager().registerEvents(new OnBlockBreak3x3(), this);
    }
}
