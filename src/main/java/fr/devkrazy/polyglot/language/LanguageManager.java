package fr.devkrazy.polyglot.language;

import fr.devkrazy.polyglot.Polyglot;
import fr.devkrazy.polyglot.utils.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

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
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.loadPlayerLanguage(player.getUniqueId());
        }
    }

    // = = = Players = = =

    /**
     * Sets a player's language using it's unique id and the language ISO 639-1 code.
     * @param uuid the player's unique id
     * @param languageISOCode the player's future language
     */
    public void setPlayerLanguageISOCode(UUID uuid, String languageISOCode) {
        this.playerLanguages.put(uuid, languageISOCode);
    }

    /**
     * Loads a player's language from the config and saves the config.
     * @param uuid the player's unique id
     */
    public void loadPlayerLanguage(UUID uuid) {
        CustomConfig playersLanguageConfig = Polyglot.getPlayersLanguagesConfig();
        String playerLanguage = playersLanguageConfig.getConfig().getString(uuid.toString());

        // This happens if the player is not in the players_languages.yml file.
        if (playerLanguage == null) {
            // Sets the player default language to the config and saves it.
            playerLanguage = "en"; //TODO use a default value stored in a config file
            playersLanguageConfig.getConfig().set(uuid.toString(), playerLanguage);
            playersLanguageConfig.save();
        }
        this.setPlayerLanguageISOCode(uuid, playerLanguage);
    }

    /**
     * Unsets a player's language.
     * @param uuid the player's unique id
     */
    public void unsetPlayerLanguage(UUID uuid) {
        this.playerLanguages.remove(uuid);
    }

    /**
     * Returns a player's language name using it's unique id.
     * @param player the player
     * @return the player's language
     */
    public String getPlayerLanguageISOCode(Player player) {
        UUID uuid = player.getUniqueId();
        return this.playerLanguages.get(uuid);
    }

    // = = = Plugins = = =

    /**
     * Registers a plugin by giving it's pluginLanguageAssets.
     * @param plugin
     * @param pluginLanguageAssets
     */
    public PluginLanguageManager registerPlugin(JavaPlugin plugin, PluginLanguageAssets pluginLanguageAssets) {
        PluginLanguageManager pluginLanguageManager = new PluginLanguageManager(pluginLanguageAssets);
        this.plugins.put(plugin, pluginLanguageManager);
        return pluginLanguageManager;
    }

    /**
     * @return a Collection containing all the registered PluginLanguageManagers.
     */
    public Collection<PluginLanguageManager> getPluginLanguageManagers() {
        return this.plugins.values();
    }

    /**
     * @return the LanguageManager's instance.
     */
    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }
}
