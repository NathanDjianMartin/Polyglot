package fr.devkrazy.polyglot;

import fr.devkrazy.polyglot.language.LanguageManager;
import fr.devkrazy.polyglot.utils.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LanguageCommand implements CommandExecutor {

    //TODO Utilsier l'API Mojang pour récupérer le UUID d'un player via le web et parser le JSON que ça retourne.
    // Apparement il faut utiliser un autre thread car le serveur s'arrête de tourner tant qu'il n'a pas la réponse
    // Faire aussi en sorte que l'API ne soit appelée que si le joueur n'est pas online, si il est online
    // on récupère directement son UUID via l'instance Player.
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CustomConfig playersLanguageConfig = Polyglot.getPlayersLanguagesConfig();
        LanguageManager lm = LanguageManager.getInstance();
        if (args[0].equalsIgnoreCase("set")) {
            if (args.length == 3) {
                Player player = Bukkit.getPlayer(args[1]);
                if (player != null) {
                    lm.setLanguageName(player.getUniqueId(), args[2].toUpperCase()); //TODO check que uppercase casse rien
                    sender.sendMessage("§c" + player.getName() + "'s (" + player.getUniqueId() + ") language is now " + args[2].toUpperCase() + ".");
                } else {
                    sender.sendMessage("§c" + args[1] + " is either offline or doesn't exist. Please try with an online player.");
                }
            } else {
                return false;
            }
        }
        return false;
    }
}
