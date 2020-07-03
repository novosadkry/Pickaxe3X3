package cz.novosadkry.Pickaxe3X3;

import com.gamingmesh.jobs.Jobs;
import cz.novosadkry.Pickaxe3X3.config.MainConfig;
import cz.novosadkry.Pickaxe3X3.config.MineableItemsConfig;
import cz.novosadkry.Pickaxe3X3.enchantments.AreaEnchantment;
import cz.novosadkry.Pickaxe3X3.events.OnBlockBreak;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Jobs jobs = null;

    public static MainConfig mainConfig;
    public static MineableItemsConfig mineableItemsConfig;

    @Override
    public void onLoad() {
        mainConfig = MainConfig.load();
        mineableItemsConfig = MineableItemsConfig.load();
    }

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().isPluginEnabled("Jobs"))
        {
            jobs = Jobs.getInstance();
            System.out.println("[Pickaxe3X3] 'Jobs' found");
        }

        AreaEnchantment.register();
        System.out.println("[Pickaxe3X3] AreaEnchantment registered");

        getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
    }
}
