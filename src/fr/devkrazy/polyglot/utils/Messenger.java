package fr.devkrazy.polyglot.utils;

import org.bukkit.entity.Player;

public class Messenger {

    private static Messenger instance;

    public void sendMessage(Player player, String languageName) {

    }

    public static Messenger getInstance() {
        if (instance == null) {
            instance = new Messenger();
        }
        return instance;
    }
}
