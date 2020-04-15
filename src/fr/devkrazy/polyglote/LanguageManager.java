package fr.devkrazy.polyglote;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;

public class LanguageManager {

    private String path;
    private HashMap<String, Language> languages;

    /**
     * @param path the path of the language_XX files in the resource folder
     * @param plugin the instance of the JavaPlugin (your main class)
     */
    public LanguageManager(String path, JavaPlugin plugin) {
        this.path = path;
        this.languages = new HashMap<>();
    }

    /**
     * Returns a Language instance using it's name, e.g: FR for French
     * @param key the language's name
     * @return the Language instance
     */
    public Language getLanguage(String key) {
        return this.languages.get(key);
    }
}
