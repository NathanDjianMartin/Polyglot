package fr.devkrazy.polyglote;

import fr.devkrazy.polyglote.language.LanguageManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Polyglote extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getLogger().log(Level.INFO, "Polyglot successfully enabled!");
    }

    @Override
    public void onDisable() {
        this.getLogger().log(Level.INFO, "Polyglot successfully disabled!");
    }

    /**
     * Returns Polyglot's LanguageManager to allow client plugins to register them to the manager.
     * @return Polyglot's LanguageManager
     */
    public static LanguageManager getLanguageManager() {
        return LanguageManager.getInstance();
    }

}
