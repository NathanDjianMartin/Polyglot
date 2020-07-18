package fr.devkrazy.polyglot;

import fr.devkrazy.polyglot.language.LanguageManager;
import fr.devkrazy.polyglot.utils.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LanguageCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CustomConfig playersLanguageConfig = Polyglot.getPlayersLanguagesConfig();
        LanguageManager lm = LanguageManager.getInstance();

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("set")) {
                if (sender.hasPermission("polyglot.language.set")) {
                    if (args.length == 3) {
                        Player player = Bukkit.getPlayer(args[1]);
                        if (player != null) {
                            lm.setPlayerLanguageISOCode(player.getUniqueId(), args[2]);
                            playersLanguageConfig.getConfig().set(player.getUniqueId().toString(), args[2]);
                            sender.sendMessage("§e" + player.getName() + "§6 language is now §e" + args[2] + "§6.");
                        } else {
                            sender.sendMessage("§4" + args[1] + "§c is either offline or doesn't exist. Please try with an online player.");
                        }
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    sender.sendMessage("§cYou don't have permission for this command.");
                }
            }

            if (args[0].equalsIgnoreCase("see")) {
                if (sender.hasPermission("polyglot.language.see")) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player != null) {
                        String playerLang = LanguageManager.getInstance().getPlayerLanguageISOCode(player);
                        sender.sendMessage("§e" + player.getName() + "§6 language is §e" + playerLang + "§6.");
                    } else {
                        sender.sendMessage("§4" + args[1] + "§c is either offline or doesn't exist. Please try with an online player.");
                    }
                    return true;
                } else {
                    sender.sendMessage("§cYou don't have permission for this command.");
                }
            }

            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("polyglot.language.reload")) {
                    if (args.length == 1) {
                        playersLanguageConfig.reload();
                        CustomConfig.reloadAllLanguageConfigs();
                        sender.sendMessage("§6Reloaded §eall§6 config files.");
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    sender.sendMessage("§cYou don't have permission for this command.");
                }
            }

            if (args[0].equalsIgnoreCase("save")) {
                if (sender.hasPermission("polyglot.language.save")) {
                    if (args.length == 1) {
                        playersLanguageConfig.save();
                        CustomConfig.saveAllLanguageConfigs();
                        sender.sendMessage("§6Saved the §eall§6 config files.");
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    sender.sendMessage("§cYou don't have permission for this command.");
                }
            }

            if (args[0].equalsIgnoreCase("help")) {
                if (sender.hasPermission("polyglot.language.help")) {
                    if (args.length == 1) {
                        sender.sendMessage("§6Polyglot §e\"/language\"§6 command help :");
                        sender.sendMessage("§6- /language set <player> <language ISO code> §7sets a player's language");
                        sender.sendMessage("§6- /language see <player> §7displays a player's language");
                        sender.sendMessage("§6- /language reload §7reloads all the config files");
                        sender.sendMessage("§6- /language save §7saves all config files");
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    sender.sendMessage("§cYou don't have permission for this command.");
                }
            }
        }

        return false;
    }
}
