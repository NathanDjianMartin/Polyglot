package fr.devkrazy.polyglote.language;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;
import java.util.UUID;

public class LanguageManager {

    /**
     * This class is a singleton and it manages the player's UUID and their associated Language,
     * and, the plugins with their associated PluginLanguageManager
     */

    private static LanguageManager instance;
    private HashMap<UUID, Language> playerLanguages;
    private HashMap<JavaPlugin, PluginLanguageManager> plugins;


    public LanguageManager() {
        this.playerLanguages = new HashMap<>();
        this.plugins = new HashMap<>();
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    // = = = Players = = =

    /**
     * Sets a player's language using it's unique id.
     * @param uuid the player's unique id
     * @param language the player's future language
     */
    public void setLanguage(UUID uuid, Language language) {
        this.playerLanguages.put(uuid, language);
    }

    /**
     * Returns a player's language using it's unique id.
     * @param uuid the player's unique id
     * @return the player's language
     */
    public Language getLanguage(UUID uuid) {
        return this.playerLanguages.get(uuid);
    }

    // = = = Plugins = = =

    /**
     * Register a plugin by givin it's pluginLanguageAssets so that the LanguageManager can
     * @param plugin
     * @param pluginLanguageAssets
     */
    public void registerPlugin(JavaPlugin plugin, PluginLanguageAssets pluginLanguageAssets) {
        this.plugins.put(plugin, new PluginLanguageManager(pluginLanguageAssets));
    }

    public PluginLanguageManager getPluginLanguageManager(JavaPlugin plugin) {
        return this.plugins.get(plugin);
    }
}
