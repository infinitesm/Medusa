package ai.medusa.anticheat.command.impl;

import ai.medusa.anticheat.Medusa;
import ai.medusa.anticheat.command.CommandInfo;
import ai.medusa.anticheat.command.MedusaCommand;
import ai.medusa.anticheat.config.Config;
import ai.medusa.anticheat.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "violations", syntax = "<player>", purpose = "Describes violations for the player.")
public final class Violations extends MedusaCommand {
    @Override
    protected boolean handle(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length == 2) {
            final Player player = Bukkit.getPlayer(args[1]);

            if (player != null) {
                final PlayerData playerData = Medusa.INSTANCE.getPlayerDataManager().getPlayerData(player);

                if (playerData != null) {
                    sendRetardedNewLine(sender);
                    sendMessage(sender, Config.ACCENT_ONE + "Violations for &c" + playerData.getPlayer().getName() + Config.ACCENT_ONE + ".");
                    sendRetardedNewLine(sender);
                    sendMessage(sender, Config.ACCENT_ONE + "Total check violations → " + Config.ACCENT_TWO + playerData.getTotalViolations());
                    sendMessage(sender, Config.ACCENT_ONE + "Combat check violations → " + Config.ACCENT_TWO + playerData.getCombatViolations());
                    sendMessage(sender, Config.ACCENT_ONE + "Movement check violations → " + Config.ACCENT_TWO + playerData.getMovementViolations());
                    sendMessage(sender, Config.ACCENT_ONE + "Player check violations → " + Config.ACCENT_TWO + playerData.getPlayerViolations());
                    sendRetardedNewLine(sender);
                    return true;
                }
            }
        }
        return false;
    }
}
