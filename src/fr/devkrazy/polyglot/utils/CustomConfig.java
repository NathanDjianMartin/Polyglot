package fr.devkrazy.polyglot.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.logging.Level;

public class CustomConfig {

    private JavaPlugin plugin;
    private String name;
    private File customConfigFile;
    private FileConfiguration customConfig;

    public CustomConfig(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.reload();
    }

    /**
     *
     */
    public void reload() {
        if (customConfigFile == null) {
            // Creates an EMPTY file object in case there isn't one
            // This happens the first time or if the file has been deleted
            customConfigFile = new File(plugin.getDataFolder(), this.name);
        }
        // Loads the file into the FileConfiguration instance (delegates to Bukkit)
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        // Look for defaults in the jar
        try {
            Reader defConfigStream = new InputStreamReader(plugin.getResource(this.name), "UTF8");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                customConfig.setDefaults(defConfig);
            }
        } catch (UnsupportedEncodingException e) {
            // The server admin must provide UTF8 files!
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        if (customConfig == null) {
            reload();
        }
        return customConfig;
    }

    public void saveCustomConfig() {
        if (customConfig == null || customConfigFile == null) {
            return;
        }
        try {
            getConfig().save(customConfigFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }

    public void saveDefaultConfig() {
        if (customConfigFile == null) {
            customConfigFile = new File(plugin.getDataFolder(), "language_FR.yml");
        }
        if (!customConfigFile.exists()) {
            plugin.saveResource("language_FR.yml", false);
        }
    }
}
