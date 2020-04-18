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
     * its own instance of these languages. The PluginLanguageManager's role is to make sure that each supported
     * language has a Language instance.
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
            Pattern pattern = Pattern.compile("language_([a-zA-Z][a-zA-Z]).yml");
            Matcher matcher = pattern.matcher(languageFile.getName());
            String languageName = "--";
            if (matcher.find()) {
                languageName = matcher.group(1); // "xx", the ISO 639-1 part of the file name language_xx.
            } else {
                this.plugin.getLogger().log(Level.SEVERE, "The file " + languageFile.getName() + " is badly named. Must be language_xx.yml");
            }
            this.languages.put(languageName, new Language(languageName, this.pluginLanguageAssets));
        }
    }

    /**
     * Returns a Language instance using it's ISO 639-1 code
     * @param key the language's name
     * @return the Language instance
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

    /**
     * Returns a message in a specified language
     * @param languageName the language ISO 639-1 code
     * @param messageKey the message key
     * @return the message in the correct language
     */
    private String getMessage(String messageKey, String languageName) {
        if (this.languages.containsKey(languageName)) {
            return this.getLanguage(languageName).getMessage(messageKey);
        } else {
            return "The language " + languageName + " in the plugin " + this.plugin.getName() + " doesn't exist.";
        }
    }

    /**
     * Sends a message to a player in the correct language.
     * @param player the target of the message
     * @param messageKey the message key
     */
    public void sendMessage(Player player, String messageKey) {
        String languageName = this.languageManager.getLanguageName(player.getUniqueId());
        String message = this.getMessage(messageKey, languageName);
        player.sendMessage(message);
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
        String languageName = this.languageManager.getLanguageName(player.getUniqueId());
        String message = this.getMessage(messageKey, languageName);
        player.sendMessage(String.format(message, parameters));
    }
}
