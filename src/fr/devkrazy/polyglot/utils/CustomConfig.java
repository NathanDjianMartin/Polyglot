package fr.devkrazy.polyglot.utils;

import fr.devkrazy.polyglot.language.Language;
import fr.devkrazy.polyglot.language.LanguageManager;
import fr.devkrazy.polyglot.language.PluginLanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomConfig {

    private JavaPlugin plugin;
    private Logger logger;
    private String name;
    private File customConfigFile;
    private FileConfiguration customConfig;

    public CustomConfig(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.logger = this.plugin.getLogger();
        this.name = name;
        this.saveDefaultConfig();
        this.reload();
        this.save();
    }

    /**
     * @return the FileConfiguration instance
     */
    public FileConfiguration getConfig() {
        if (customConfig == null) {
            this.reload();
        }
        return customConfig;
    }

    /**
     * Reloads the config.
     */
    public void reload() {
        if (customConfigFile == null) {
            // Creates an EMPTY file object in case there isn't one
            // This happens the first time or if the file has been deleted
            customConfigFile = new File(plugin.getDataFolder(), this.name);
            try {
                customConfigFile.createNewFile();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error while creating the new file " + this.name + ": " + e.getMessage());
            }
        }
        // Loads the file into the FileConfiguration instance (delegates to Bukkit)
        customConfig = YamlConfiguration.loadConfiguration(customConfigFile);

        try {
            customConfig.load(customConfigFile);
        } catch (FileNotFoundException fnfe) {
            logger.log(Level.SEVERE, "File not found while loading config " + customConfig.getName() + ": " + fnfe.getMessage());
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, "IOException while loading config " + customConfig.getName() + ": " + ioe.getMessage());
        } catch (InvalidConfigurationException ice) {
            logger.log(Level.SEVERE, "Config " + customConfig.getName() + " is invalid: " + ice.getMessage());
        }

        // Look for defaults in the jar
        try {
            Reader defConfigStream = new InputStreamReader(plugin.getResource(this.name), "UTF8");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                customConfig.setDefaults(defConfig);
            }
        } catch (UnsupportedEncodingException e) {
            // The developer must provide UTF8 files!
            logger.log(Level.SEVERE, "The file " + this.name + " embedded in " + plugin.getName() + "'s jar is not UTF8: " + e.getMessage());
        }
    }

    /**
     * Saves the config.
     */
    public void save() {
        if (customConfig == null || customConfigFile == null) {
            // Either the customConfig of customConfigFile is null so we can't save the config
            return;
        }
        try {
            getConfig().save(customConfigFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + customConfigFile, ex);
        }
    }

    /**
     * Saves the default config (from the jar) to the data folder if the current config file does not exist.
     */
    public void saveDefaultConfig() {
        if (customConfigFile == null) {
            // We create a new file instance so we can save the default config into this file
            customConfigFile = new File(plugin.getDataFolder(), this.name);
        }
        if (!customConfigFile.exists()) {
            // If the filed doesn't exist we create a new one using the plugin's resource file
            plugin.saveResource(this.name, false);
        }
    }

    /**
     * Reloads all the language configs of all registered plugins.
     */
    public static void reloadAllLanguageConfigs() {
        LanguageManager lm = LanguageManager.getInstance();
        for (PluginLanguageManager plm : lm.getPluginLanguageManagers()) {
            for (Language language : plm.getLanguages()) {
                CustomConfig config = language.getLanguageConfig();
                config.saveDefaultConfig();
                config.reload();
                config.save();
                Bukkit.broadcastMessage("§4" + config.getConfig().getName() + " reoaded!");
            }
        }
    }

    /**
     * Saves all the language configs of all registered plugins.
     */
    public static void saveAllLanguageConfigs() {
        LanguageManager lm = LanguageManager.getInstance();
        for (PluginLanguageManager plm : lm.getPluginLanguageManagers()) {
            for (Language language : plm.getLanguages()) {
                language.getLanguageConfig().save();
            }
        }
    }
}
