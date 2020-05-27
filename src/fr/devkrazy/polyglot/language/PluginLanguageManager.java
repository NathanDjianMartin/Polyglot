package fr.devkrazy.polyglot.language;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PluginLanguageManager {

    /**
     * A PluginLanguageManager manages every language of a plugin. For each supported language, a plugin has
     * its own instance of a language. The PluginLanguageManager's role is to make sure that each supported
     * language has a Language instance. It is also used to retrieve or send messages to players
     */

    private HashMap<String, Language> languages;
    private PluginLanguageAssets pluginLanguageAssets;
    private LanguageManager languageManager;
    private JavaPlugin plugin;

    public PluginLanguageManager(PluginLanguageAssets pluginLanguageAssets) {
        this.languages = new HashMap<>();
        this.pluginLanguageAssets = pluginLanguageAssets;
        this.languageManager = LanguageManager.getInstance();
        this.plugin = this.pluginLanguageAssets.getJavaPlugin();

        for (File languageFile : this.pluginLanguageAssets.getLanguagesFiles()) {
            Pattern pattern = Pattern.compile("language_([a-z][a-z]).yml");
            Matcher matcher = pattern.matcher(languageFile.getName());
            String languageISOCode = "--";
            if (matcher.find()) {
                languageISOCode = matcher.group(1); // "xx", the ISO 639-1 part of the file name language_xx.
                this.languages.put(languageISOCode, new Language(languageISOCode, this.pluginLanguageAssets));
            } else {
                this.plugin.getLogger().log(Level.SEVERE, "The file " + languageFile.getName() + " is badly named. Must be language_xx.yml");
            }
        }
    }


    // = = = Languages = = = //

    /**
     * Returns a Language instance using it's ISO 639-1 code
     * @param key the language's name
     * @return the Language instance if it exists; null otherwise
     */
    public Language getLanguage(String key) {
        return this.languages.get(key);
    }

    /**
     * @return a Collection of all the registered languages
     */
    public Collection<Language> getLanguages() {
        return this.languages.values();
    }


    // = = = Messages = = = //

    /**
     * Returns a message in a specified language
     * @param languageISOCode the language ISO 639-1 code
     * @param messageKey the message key
     * @return the message in the correct language
     */
    private String getMessage(String messageKey, String languageISOCode) {
        if (this.languages.containsKey(languageISOCode)) {
            return this.getLanguage(languageISOCode).getMessage(messageKey);
        } else {
            return "The language " + languageISOCode + " in the plugin " + this.plugin.getName() + " doesn't exist.";
        }
    }

    /**
     * Returns a message with parameters in a specified language
     * @param messageKey the message key
     * @param languageISOCode the language ISO 639-1 code
     * @param parameters the parameters to inject in the message
     * @return the message with the included parameters in the correct language
     */
    private String getMessageWithParameters(String messageKey, String languageISOCode, Object... parameters) {
        return String.format(this.getMessage(messageKey, languageISOCode), parameters);
    }

    /**
     * Returns a message in a player's language.
     * @param player the player whose language will be used to retrieve the message
     * @param messageKey the message key
     * @return the message in the correct language for the player
     */
    public String getMessage(Player player, String messageKey) {
        String languageISOCode = this.languageManager.getPlayerLanguageISOCode(player.getUniqueId());
        return this.getMessage(messageKey, languageISOCode);
    }

    /**
     * Returns a message with parameters in a player's language.
     * This method uses Java's String.format method to inject the parameters. The message should
     * respect Java String format convention.
     * @param player the player whose language will be used to retrieve the message
     * @param messageKey the message key
     * @param parameters the parameters to inject in the message
     * @return the message with the included parameters in the player's language
     */
    public String getMessageWithParameters(Player player, String messageKey, Object... parameters) {
        String languageISOCode = this.languageManager.getPlayerLanguageISOCode(player.getUniqueId());
        return this.getMessageWithParameters(messageKey, languageISOCode, parameters);
    }

    /**
     * Sends a message to a player in the correct language.
     * @param player the target of the message
     * @param messageKey the message key
     */
    public void sendMessage(Player player, String messageKey) {
        player.sendMessage(this.getMessage(player, messageKey));
    }

    /**
     * Sends a message with parameters to a player in the correct language.
     * This method uses Java's String.format method to inject the parameters. The initial message should
     * respect Java String format convention.
     * @param player the target of the message
     * @param messageKey the message key
     * @param parameters the parameters to inject in the message
     */
    public void sendMessageWithParameters(Player player, String messageKey, Object... parameters) {
        player.sendMessage(this.getMessageWithParameters(player, messageKey, parameters));
    }
}
