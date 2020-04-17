package fr.devkrazy.polyglot.language;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class LanguageManager {

    /**
     * This class is a singleton and it manages the player's UUID and their associated Language,
     * and, the plugins with their associated PluginLanguageManager
     */

    private static LanguageManager instance;
    private HashMap<UUID, String> playerLanguages;
    private HashMap<JavaPlugin, PluginLanguageManager> plugins;


    public LanguageManager() {
        this.playerLanguages = new HashMap<>();
        this.plugins = new HashMap<>();
    }

    // = = = Players = = =

    /**
     * Sets a player's language using it's unique id and the language ISO 639-1 code.
     * @param uuid the player's unique id
     * @param languageName the player's future language
     */
    public void setLanguageName(UUID uuid, String languageName) {
        this.playerLanguages.put(uuid, languageName);
    }

    /**
     * Returns a player's language name using it's unique id.
     * @param uuid the player's unique id
     * @return the player's language
     */
    public String getLanguageName(UUID uuid) {
        return this.playerLanguages.get(uuid);
    }

    // = = = Plugins = = =

    /**
     * Register a plugin by giving it's pluginLanguageAssets.
     * @param plugin
     * @param pluginLanguageAssets
     */
    public void registerPlugin(JavaPlugin plugin, PluginLanguageAssets pluginLanguageAssets) {
        this.plugins.put(plugin, new PluginLanguageManager(pluginLanguageAssets));
    }

    /**
     * Returns a plugin's associated PluginLanguageManager.
     * @param plugin the plugin
     * @return the PluginLanguageManager
     */
    public PluginLanguageManager getPluginLanguageManager(JavaPlugin plugin) {
        return this.plugins.get(plugin);
    }

    /**
     * @return a Collection containing all the registered PluginLanguageManagers
     */
    public Collection<PluginLanguageManager> getPluginLanguageManagers() {
        return this.plugins.values();
    }

    /**
     * @return the LanguageManager instance
     */
    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }
}
