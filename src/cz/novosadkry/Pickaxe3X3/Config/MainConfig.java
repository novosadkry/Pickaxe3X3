package cz.novosadkry.Pickaxe3X3.Config;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MainConfig {
    public String prefix = "Area:";
    public int blockFaceCheckRange = 100;

    public static MainConfig load() {
        File file = new File(String.join(File.separator, ".", "plugins", "Pickaxe3X3", "main.yml"));
        YamlConfiguration yamlConfig;

        MainConfig mainConfig = new MainConfig();

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();

                yamlConfig = YamlConfiguration.loadConfiguration(file);
                yamlConfig.set("prefix", mainConfig.prefix);
                yamlConfig.set("blockFaceCheckRange", mainConfig.blockFaceCheckRange);
                yamlConfig.save(file);

                System.out.println("[Pickaxe3X3] Unable to find configuration file main.yml, switching to default settings");
            } catch (IOException e) {
                System.out.println("[Pickaxe3X3] Error occurred while creating configuration file main.yml");
            }
        }

        try {
            yamlConfig = YamlConfiguration.loadConfiguration(file);
            mainConfig.prefix = (String)yamlConfig.get("prefix");
            mainConfig.blockFaceCheckRange = (Integer)yamlConfig.get("blockFaceCheckRange");

            System.out.println("[Pickaxe3X3] Successfully loaded configuration file main.yml");
        } catch (Exception e) {
            System.out.println("[Pickaxe3X3] Error occurred while reading configuration file main.yml, switching to default settings");
        }

        return mainConfig;
    }
}
