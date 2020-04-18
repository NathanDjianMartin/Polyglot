package fr.devkrazy.polyglot;

import fr.devkrazy.polyglot.utils.CustomConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Polyglot extends JavaPlugin {

    private static CustomConfig playersLanguages;

    @Override
    public void onEnable() {
        playersLanguages = new CustomConfig(this, "players_languages.yml");
        this.getCommand("language").setExecutor(new LanguageCommand());
        this.getServer().getPluginManager().registerEvents(new Events(), this);
        this.getLogger().log(Level.INFO, "Polyglot successfully enabled!");
    }

    @Override
    public void onDisable() {
        CustomConfig.saveAllLanguageConfigs();
        playersLanguages.save();
        this.getLogger().log(Level.INFO, "Polyglot successfully disabled!");
    }

    /**
     * @return the players_langages config
     */
    public static CustomConfig getPlayersLanguagesConfig() {
        return playersLanguages;
    }

}
