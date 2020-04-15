package fr.devkrazy.polyglote;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Language {

    private String name;
    private HashMap<String, String> messages;

    public Language(String name, JavaPlugin plugin) {
        this.name = name;
        this.messages = new HashMap<>();

    }
}
