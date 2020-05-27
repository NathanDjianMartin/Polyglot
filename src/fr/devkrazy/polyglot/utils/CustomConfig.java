/*
 * Copyright (c) 2020.
 *
 * This project (Holobroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 *
 * Holobroadcast cannot be copied and/or distributed without the express permission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package fr.devkrazy.polyglot.utils;

import fr.devkrazy.polyglot.language.Language;
import fr.devkrazy.polyglot.language.LanguageManager;
import fr.devkrazy.polyglot.language.PluginLanguageManager;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

public class CustomConfig {

    private JavaPlugin plugin;
    private Logger logger;
    private String name;
    private File configFile;
    private FileConfiguration config;

    public CustomConfig(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.logger = this.plugin.getLogger();
        this.name = name;
        this.configFile = new File(this.plugin.getDataFolder() + "/" + this.name);
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        this.saveDefaultConfig();
        this.reload();
        this.save();
    }

    /**
     * @return the FileConfiguration instance
     */
    public FileConfiguration getConfig() {
        if (this.config == null) {
            this.reload();
        }
        return this.config;
    }

    /**
     * Reloads the config.
     */
    public void reload() {
        try {
            this.config.load(this.configFile);
        } catch (InvalidConfigurationException e) {
            this.logger.severe("The configuration " + this.name + " is invalid. " + e.getMessage());
        } catch (FileNotFoundException e) {
            this.logger.severe("The file " + this.name + " was not found. " + e.getMessage());
        } catch (IOException e) {
            this.logger.severe("IOException with config " + this.name + ": " + e.getMessage());
        }
    }
    /**
     * Saves the config.
     */
    public void save() {
        try {
            this.config.save(configFile);
        } catch (IOException e) {
           this.logger.severe("Could not save config to " + this.name + ": " + e.getMessage());
        }
    }

    /**
     * Saves the default config (from the jar) to the data folder
     * if, and only if, the current config file does not exist.
     */
    public void saveDefaultConfig() {
        if (configFile == null) {
            // We create a new file instance so we can save the default config into this file
            configFile = new File(plugin.getDataFolder(), this.name);
        }
        if (!configFile.exists()) {
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
                config.reload();
                config.save();
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
