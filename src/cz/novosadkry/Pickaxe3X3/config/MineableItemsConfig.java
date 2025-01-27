package cz.novosadkry.Pickaxe3X3.config;

import com.google.common.io.Files;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MineableItemsConfig {
    public List<Material> pickaxeItems = Arrays.asList(
            Material.STONE,
            Material.COBBLESTONE,
            Material.SANDSTONE,
            Material.DIORITE,
            Material.ANDESITE,
            Material.GRANITE,
            Material.END_STONE,
            Material.GLOWSTONE,
            Material.COAL_ORE,
            Material.IRON_ORE,
            Material.GOLD_ORE,
            Material.EMERALD_ORE,
            Material.LAPIS_ORE,
            Material.DIAMOND_ORE,
            Material.REDSTONE_ORE,
            Material.NETHER_QUARTZ_ORE
    );

    public static MineableItemsConfig load() {
        File file = new File(String.join(File.separator, ".", "plugins", "Pickaxe3X3", "mineable.yml"));
        YamlConfiguration yamlConfig;

        MineableItemsConfig mineableItemsConfig = new MineableItemsConfig();

        if (!file.exists()) {
            try {
                Files.createParentDirs(file);
                file.createNewFile();

                List<String> items = new ArrayList<String>();
                for (Material m : mineableItemsConfig.pickaxeItems) {
                    items.add(m.toString());
                }

                yamlConfig = YamlConfiguration.loadConfiguration(file);
                yamlConfig.set("pickaxe", items);
                yamlConfig.save(file);

                System.out.println("[Pickaxe3X3] Unable to find configuration file mineable.yml, switching to default settings");
            } catch (IOException e) {
                System.out.println("[Pickaxe3X3] Error occurred while creating configuration file mineable.yml");
            }
        }

        try {
            yamlConfig = YamlConfiguration.loadConfiguration(file);
            List<Material> items = new ArrayList<>();

            for (String s : yamlConfig.getStringList("pickaxe")) {
                try {
                    Material m = Material.getMaterial(s);
                    items.add(m);
                } catch (IllegalArgumentException e) {
                    System.out.println("[Pickaxe3X3] Material not found for '" + s + "'");
                }
            }

            System.out.println("[Pickaxe3X3] Successfully loaded configuration file mineable.yml");
            mineableItemsConfig.pickaxeItems = items;
        } catch (Exception e) {
            System.out.println("[Pickaxe3X3] Error occurred while reading configuration file mineable.yml, switching to default settings");
            e.printStackTrace();
        }

        return mineableItemsConfig;
    }
}
