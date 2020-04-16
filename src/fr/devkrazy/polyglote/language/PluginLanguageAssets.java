package fr.devkrazy.polyglote.language;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Set;

public interface PluginLanguageAssets {

    /**
     * This interface has to be implemented by each plugin using Polyglot.
     * By implementing this interface, the client plugin allows Polyglot to manage its messages keys and
     * messages files, thus, giving the client plugin the correct message in the correct language when it needs it.
     * It also allows Polyglot to access the client plugin's JavaPlugin instance.
     */

    /**
     * Returns a Set containing the plugin's messages keys
     * @return the messages keys
     */
    Set<String> getMessagesKeys();

    /**
     * Returns a Set containing the plugin's languages files
     * @return the languages files
     */
    Set<File> getLanguagesFiles();

    /**
     * Returns the plugin's instance of JavaPlugin.
     * @return JavaPlugin instance
     */
    JavaPlugin getJavaPlugin();
}
