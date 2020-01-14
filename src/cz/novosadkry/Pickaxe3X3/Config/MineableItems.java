package cz.novosadkry.Pickaxe3X3.Config;

import com.google.common.io.Files;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MineableItems {
    private static List<Material> pickaxeItems = Arrays.asList(
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

    public static List<Material> getPickaxeItems() {
        return pickaxeItems;
    }

    public static void load() {
        File file = new File(String.join(File.separator, ".", "plugins", "Pickaxe3X3", "mineable.yml"));
        YamlConfiguration config;

        if (!file.exists()) {
            try {
                Files.createParentDirs(file);
                file.createNewFile();

                List<String> items = new ArrayList<String>();
                for (Material m : pickaxeItems) {
                    items.add(m.toString());
                }

                config = YamlConfiguration.loadConfiguration(file);
                config.set("pickaxe", items);
                config.save(file);

                System.out.println("[Pickaxe3X3] Unable to find configuration file mineable.yml, switching to default settings");
            } catch (IOException e) {
                System.out.println("[Pickaxe3X3] Error occurred while creating configuration file mineable.yml");
            }
        }

        try {
            config = YamlConfiguration.loadConfiguration(file);
            List<Material> items = new ArrayList<>();

            for (String s : (List<String>)config.get("pickaxe")){
                Material m = Material.matchMaterial(s);
                if (m != null)
                    items.add(m);
            }

            System.out.println("[Pickaxe3X3] Successfully loaded configuration file mineable.yml");
            pickaxeItems = items;
        } catch (Exception e) {
            System.out.println("[Pickaxe3X3] Error occurred while reading configuration file mineable.yml, switching to default settings");
        }
    }
}
