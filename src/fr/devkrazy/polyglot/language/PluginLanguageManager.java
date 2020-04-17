package fr.devkrazy.polyglot.language;

import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
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

    public PluginLanguageManager(PluginLanguageAssets pluginLanguageAssets) {
        this.languages = new HashMap<>();
        this.pluginLanguageAssets = pluginLanguageAssets;

        for (File languageFile : this.pluginLanguageAssets.getLanguagesFiles()) {
            Pattern pattern = Pattern.compile("language_([a-zA-Z][a-zA-Z]).yml");
            Matcher matcher = pattern.matcher(languageFile.getName());
            String languageName = "--";
            if (matcher.find()) {
                languageName = matcher.group(1); // "XX", the ISO 639-1 part of the file name language_XX.
            } else {
                this.pluginLanguageAssets.getJavaPlugin().getLogger().log(Level.WARNING, "The file " + languageFile.getName() + " is badly named. Must be language_XX.yml");
            }
            languageName = languageName.toUpperCase(); //TODO check que ça ne casse rien
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
     * Sends a message to a player in the correct language.
     * @param player the player
     * @param messageKey the message key
     */
    public void sendMessage(Player player, String messageKey) {
        UUID uuid = player.getUniqueId();
        String playerLanguageName = LanguageManager.getInstance().getLanguageName(uuid);
        Language playerLanguage = this.getLanguage(playerLanguageName);

        player.sendMessage(playerLanguage.getMessage(messageKey));
    }

    /**
     * Sends a message to a player in the correct language with the possibility to add
     * Object instances to replace the placeholders of the message.
     * The number of objects must be equal to the number of placeholders in the message.
     * In addition the given object's toString() method should be correctly implemented
     * because this method will call the toString() method.
     * @param player the player
     * @param messageKey the message key
     */
    public void sendFormattedMessage(Player player, String messageKey, Object[] data) {
        UUID uuid = player.getUniqueId();
        String playerLanguageName = LanguageManager.getInstance().getLanguageName(uuid);
        Language playerLanguage = this.getLanguage(playerLanguageName);
        String message = playerLanguage.getMessage(messageKey);

        Pattern pattern = Pattern.compile("");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {

        }
    }
}
