package ai.medusa.anticheat.command.impl;

import ai.medusa.anticheat.Medusa;
import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.command.CommandInfo;
import ai.medusa.anticheat.command.MedusaCommand;
import ai.medusa.anticheat.data.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "debug", syntax = "<check|none>", purpose = "Debug information from a specified check.")
public class Debug extends MedusaCommand {
    @Override
    protected boolean handle(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (sender instanceof Player) {
            final PlayerData data = Medusa.INSTANCE.getPlayerDataManager().getPlayerData(((Player) sender).getPlayer());

            if (data != null) {
                if (args.length == 3) {
                    final String checkName = args[1] + " " + args[2];

                    for (Check check : data.getChecks()) {
                        if (check.getCheckInfo().name().equals(checkName)) {
                            check.setDebugging(true);
                            sender.sendMessage(ChatColor.GREEN + "Debugging " + checkName + "!");
                        } else {
                            check.setDebugging(false);
                        }
                    }
                    return true;
                } else if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("none")) {
                        for (Check check : data.getChecks()) {
                            check.setDebugging(false);
                        }
                        sender.sendMessage(ChatColor.RED + "Turned off debugging!");
                        return true;
                    }
                }
            }
        }
        sender.sendMessage(ChatColor.RED + "Invalid check!");
        return false;
    }
}
