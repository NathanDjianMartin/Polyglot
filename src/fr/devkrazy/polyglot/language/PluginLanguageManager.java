package fr.devkrazy.polyglot.language;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PluginLanguageManager {

    /**
     * A PluginLanguageManager manages every language of a plugin. For each supported language, a plugin has
     * its own instance of these languages. The PluginLanguageManager's role is to make sure that each supported
     * language has a Language instance.
     */

    private HashMap<String, Language> languages;

    public PluginLanguageManager(PluginLanguageAssets pluginLanguageAssets) {
        this.languages = new HashMap<>();

        for (File languageFile : pluginLanguageAssets.getLanguagesFiles()) {
            Pattern pattern = Pattern.compile("language_([a-zA-Z][a-zA-Z])");
            Matcher matcher = pattern.matcher(languageFile.getName());
            String languageName = matcher.group(1); // "XX", the ISO 639-1 part of the file name language_XX.

            this.languages.put(languageName, new Language(languageName, pluginLanguageAssets));
        }
    }

    /**
     * Returns a Language instance using it's ISO 639-1 code
     * @param key the language's name
     * @return the Language instance
     */
    private Language getLanguage(String key) {
        return this.languages.get(key);
    }

    /**
     *
     * @param languageName
     * @param messageKey
     * @return
     */
    public String getMessage(String languageName, String messageKey) {
        return this.getLanguage(languageName).getMessage(messageKey);
    }
}
