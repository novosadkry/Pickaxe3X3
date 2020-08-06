package cz.novosadkry.Pickaxe3X3;

import cz.novosadkry.Pickaxe3X3.config.MainConfig;
import cz.novosadkry.Pickaxe3X3.config.MineableItemsConfig;
import cz.novosadkry.Pickaxe3X3.enchantments.AreaEnchantment;
import cz.novosadkry.Pickaxe3X3.events.OnBlockBreak;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static MainConfig mainConfig;
    public static MineableItemsConfig mineableItemsConfig;

    @Override
    public void onLoad() {
        mainConfig = MainConfig.load();
        mineableItemsConfig = MineableItemsConfig.load();
    }

    @Override
    public void onEnable() {
        AreaEnchantment.register();
        System.out.println("[Pickaxe3X3] AreaEnchantment registered");

        getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
    }

    @Override
    public void onDisable() {
        AreaEnchantment.unregister();
        System.out.println("[Pickaxe3X3] AreaEnchantment unregistered");
    }
}
