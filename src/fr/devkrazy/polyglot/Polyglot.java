package fr.devkrazy.polyglot;

import fr.devkrazy.polyglot.utils.CustomConfig;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Polyglot extends JavaPlugin {

    private static CustomConfig playersLanguages;

    @Override
    public void onEnable() {
        playersLanguages = new CustomConfig(this, "players_languages.yml");
        this.getCommand("language").setExecutor(new LanguageCommand());
        this.getLogger().log(Level.INFO, "Polyglot successfully enabled!");
    }

    @Override
    public void onDisable() {
        CustomConfig.saveAllLanguageConfigs();
        playersLanguages.save();
        this.getLogger().log(Level.INFO, "Polyglot successfully disabled!");
    }

    /**
     * @return the players_langage config
     */
    public static CustomConfig getPlayersLanguagesConfig() {
        return playersLanguages;
    }

    public static void main(String[] args) {
        File languageFile = new File("Le groupe {0} et {1} existent sur le serveur, {2} !");
        Pattern pattern = Pattern.compile("(\\{[0-9]\\})");
        Matcher matcher = pattern.matcher(languageFile.getName());
        int i = 0;
        while (matcher.find()) {
            System.out.println(matcher.group(i));
            i++;
        }
    }
}
