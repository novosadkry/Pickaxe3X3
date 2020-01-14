package cz.novosadkry.Pickaxe3X3.Config;

import com.google.common.io.Files;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MainCfg {
    private static String prefix = "Area:";
    private static int blockFaceCheckRange = 100;

    public static String getPrefix() {
        return prefix;
    }

    public static int getBlockFaceCheckRange() {
        return blockFaceCheckRange;
    }

    public static void load() {
        File file = new File(String.join(File.separator, ".", "plugins", "Pickaxe3X3", "main.yml"));
        YamlConfiguration config;

        if (!file.exists()) {
            try {
                Files.createParentDirs(file);
                file.createNewFile();

                config = YamlConfiguration.loadConfiguration(file);
                config.set("prefix", prefix);
                config.set("blockFaceCheckRange", blockFaceCheckRange);
                config.save(file);

                System.out.println("[Pickaxe3X3] Unable to find configuration file main.yml, switching to default settings");
            } catch (IOException e) {
                System.out.println("[Pickaxe3X3] Error occurred while creating configuration file main.yml");
            }
        }

        try {
            config = YamlConfiguration.loadConfiguration(file);
            prefix = (String)config.get("prefix");
            blockFaceCheckRange = (Integer)config.get("blockFaceCheckRange");

            System.out.println("[Pickaxe3X3] Successfully loaded configuration file main.yml");
        } catch (Exception e) {
            System.out.println("[Pickaxe3X3] Error occurred while reading configuration file main.yml, switching to default settings");
        }
    }
}
