package cz.novosadkry.Pickaxe3X3;

import com.bekvon.bukkit.residence.Residence;
import com.gamingmesh.jobs.Jobs;
import cz.novosadkry.Pickaxe3X3.config.MainConfig;
import cz.novosadkry.Pickaxe3X3.config.MineableItemsConfig;
import cz.novosadkry.Pickaxe3X3.events.OnBlockBreak;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Residence residence = null;
    public static Jobs jobs = null;

    public static MainConfig mainConfig;
    public static MineableItemsConfig mineableItemsConfig;

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().isPluginEnabled("Residence"))
        {
            residence = Residence.getInstance();
            System.out.println("[Pickaxe3X3] Residence found");
        }

        if (getServer().getPluginManager().isPluginEnabled("Jobs"))
        {
            jobs = Jobs.getInstance();
            System.out.println("[Pickaxe3X3] Jobs found");
        }

        mainConfig = MainConfig.load();
        mineableItemsConfig = MineableItemsConfig.load();

        getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
    }
}
