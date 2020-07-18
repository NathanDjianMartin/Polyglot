package fr.devkrazy.polyglot.language;

import fr.devkrazy.polyglot.utils.CustomConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Language {

    private String isoCode;
    private PluginLanguageAssets pluginLanguageAssets;
    private HashMap<String, String> messages;
    private CustomConfig languageConfig;

    public Language(String isoCode, PluginLanguageAssets pluginLanguageAssets) {
        this.isoCode = isoCode;
        this.pluginLanguageAssets = pluginLanguageAssets;
        this.messages = new HashMap<>();
        JavaPlugin plugin = this.pluginLanguageAssets.getJavaPlugin();


        // Creates the config from the correct file name
        String fileName = "language_" + this.isoCode + ".yml";
        this.languageConfig = new CustomConfig(plugin, fileName);

        // Associates each message key with its corresponding message from the custom config
        for (String messageKey : this.pluginLanguageAssets.getMessagesKeys()) {
            String message = this.languageConfig.getConfig().getString(messageKey);
            if (message != null) {
                messages.put(messageKey, message);
            } else {
                // Associates an error message if it isn't set in the config file
                messages.put(messageKey, "§cThe message §4" + messageKey + "§c is not defined in the file §4" + fileName + "§c. Please report this message to a staff member.");
                plugin.getLogger().warning("The message " + messageKey + " is not defined in the file " + fileName);
            }
        }
    }

    /**
     * Returns a message in the current language using the message's key used in the language file
     * containing the messages for this language.
     * @param messageKey the message's key
     * @return the message
     */
    public String getMessage(String messageKey) {
        return this.messages.get(messageKey);
    }

    /**
     * Returns the custom config of the current language.
     * @return the custom config instance
     */
    public CustomConfig getLanguageConfig() {
        return this.languageConfig;
    }
}
