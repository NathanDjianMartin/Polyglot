package fr.devkrazy.polyglot.language;

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
                this.pluginLanguageAssets.getJavaPlugin().getLogger().log(Level.SEVERE, "The file " + languageFile.getName() + " is badly named. Must be language_XX.yml");
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
    public String getMessage(String messageKey, String languageName) {
        languageName = languageName;
        if (this.languages.containsKey(languageName)) {
            return this.languages.get(languageName).getMessage(messageKey);
        } else {
            this.pluginLanguageAssets.getJavaPlugin().getLogger().log(Level.SEVERE, "The language " + languageName + " doesn't exist.");
            return "§cThe language " + languageName + " doesn't exist. Please report this immediately yo a staff member.";
        }
    }
}
