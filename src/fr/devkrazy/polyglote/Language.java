package fr.devkrazy.polyglote;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Language {

    private String name;
    private HashMap<String, String> messages;
    private CustomConfig languageConfig;

    public Language(String name, JavaPlugin plugin) {
        this.name = name;
        this.messages = new HashMap<>();
        languageConfig = new CustomConfig(plugin, "language_" + this.name + ".yml"); //language_XX.yml

    }

    private void readFromConfig() {
        for (String key : languageConfig.getConfig().getKeys(false)) {
            this.messages.put(key, languageConfig.getConfig().getString(key));
        }
    }

    public void updateMessages() {
        this.languageConfig.reload();
        this.readFromConfig();
    }
}
