package fr.devkrazy.polyglot;

import fr.devkrazy.polyglot.language.LanguageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LanguageManager.getInstance().loadPlayerLanguage(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Polyglot.getPlayersLanguagesConfig().save();
        LanguageManager.getInstance().unsetPlayerLanguage(player.getUniqueId());
    }
}
