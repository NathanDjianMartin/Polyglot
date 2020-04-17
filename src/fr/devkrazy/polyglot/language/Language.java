package fr.devkrazy.polyglot.language;

import fr.devkrazy.polyglot.utils.CustomConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Level;

public class Language {

    private String name;
    private PluginLanguageAssets pluginLanguageAssets;
    private HashMap<String, String> messages;
    private CustomConfig languageConfig;

    public Language(String name, PluginLanguageAssets pluginLanguageAssets) {
        this.name = name;
        this.pluginLanguageAssets = pluginLanguageAssets;
        this.messages = new HashMap<>();
        JavaPlugin plugin = this.pluginLanguageAssets.getJavaPlugin();


        // Creates the config from the correct file name
        this.languageConfig = new CustomConfig(plugin, "language_" + this.name + ".yml");

        // Associates each message key with its corresponding message from the custom config
        for (String messageKey : this.pluginLanguageAssets.getMessagesKeys()) {
            String message = this.languageConfig.getConfig().getString(messageKey);
            // Associates a message even using its key to retrieve the message from the language file
            if (message != null) {
                messages.put(messageKey, message);
            } else {
                messages.put(messageKey, messageKey + " is not defined for the language " + this.name + ", please report this message to a staff member.");
                plugin.getLogger().log(Level.WARNING, "The message " + messageKey + " has no associated message in the language " + this.name + ".");
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
        if (this.messages.containsKey(messageKey)) {
            return this.messages.get(messageKey);
        } else {
            this.pluginLanguageAssets.getJavaPlugin().getLogger().log(Level.SEVERE, "The message " + messageKey + " doesn't exist in language " + this.name + ".");
            return "§cThe message " + messageKey + " doesn't exist in language "+ this.name + " . Please report this immediately yo a staff member.";
        }
    }

    /**
     * Returns the custom config of the current language.
     * @return the custom config instance
     */
    public CustomConfig getLanguageConfig() {
        return this.languageConfig;
    }
}
