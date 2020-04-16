package fr.devkrazy.polyglot;

import fr.devkrazy.polyglot.language.LanguageManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Polyglot extends JavaPlugin {

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
