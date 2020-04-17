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

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("set")) {
                if (args.length == 3) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player != null) {
                        lm.setLanguageName(player.getUniqueId(), args[2]);
                        playersLanguageConfig.getConfig().set(player.getUniqueId().toString(), args[2]);
                        sender.sendMessage("§a" + player.getName() + " language is now " + args[2] + ".");
                    } else {
                        sender.sendMessage("§c" + args[1] + " is either offline or doesn't exist. Please try with an online player.");
                    }
                    return true;
                } else {
                    return false;
                }
            }

            if (args[0].equalsIgnoreCase("see")) {
                Player player = Bukkit.getPlayer(args[1]);
                if (player != null) {
                    String playerLang = playersLanguageConfig.getConfig().getString(player.getUniqueId().toString());
                    sender.sendMessage("§a" + player.getName() + " language is " + playerLang);
                } else {
                    sender.sendMessage("§c" + args[1] + " is either offline or doesn't exist. Please try with an online player.");
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (args.length == 1) {
                    playersLanguageConfig.reload();
                    sender.sendMessage("§aReloaded the players_languages.yml config.");
                    return true;
                } else {
                    return false;
                }
            }

            if (args[0].equalsIgnoreCase("save")) {
                if (args.length == 1) {
                    playersLanguageConfig.save();
                    sender.sendMessage("§aSaved the players_languages.yml config.");
                    return true;
                } else {
                    return false;
                }
            }

            if (args[0].equalsIgnoreCase("help")) {
                if (args.length == 1) {
                    sender.sendMessage("§6Polyglot §e\"/language\"§6 command help :");
                    sender.sendMessage("§6- /language set <player> <language ISO code> §7sets a player's language");
                    sender.sendMessage("§6- /language see <player> §7displays a player's language");
                    sender.sendMessage("§6- /language reload §7reloads the players language config");
                    sender.sendMessage("§6- /language save §7saves the players language config");
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }
}
