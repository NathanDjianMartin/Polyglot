package fr.devkrazy.polyglot;

import fr.devkrazy.polyglot.utils.CustomConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class Polyglot extends JavaPlugin {

    private static CustomConfig playersLanguages;

    @Override
    public void onEnable() {
        playersLanguages = new CustomConfig(this, "players_languages.yml"); // TODO use database
        this.getCommand("language").setExecutor(new LanguageCommand());
        this.getServer().getPluginManager().registerEvents(new Events(), this);
        this.getLogger().info("Polyglot successfully enabled!");
    }

    @Override
    public void onDisable() {
        CustomConfig.saveAllLanguageConfigs();
        playersLanguages.save();
        this.getLogger().info("Polyglot successfully disabled!");
    }

    /**
     * @return the players_langages config
     */
    public static CustomConfig getPlayersLanguagesConfig() {
        return playersLanguages;
    }
}
