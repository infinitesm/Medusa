package ai.medusa.anticheat.command.impl;

import ai.medusa.anticheat.Medusa;
import ai.medusa.anticheat.command.CommandInfo;
import ai.medusa.anticheat.command.MedusaCommand;
import ai.medusa.anticheat.config.Config;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.util.ColorUtil;
import io.github.retrooper.packetevents.PacketEvents;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandInfo(name = "info", syntax = "<player>", purpose = "Returns information about the players client.")
public final class Info extends MedusaCommand {
    @Override
    protected boolean handle(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (args.length == 2) {
            final Player player = Bukkit.getPlayer(args[1]);

            if (player != null) {
                final PlayerData playerData = Medusa.INSTANCE.getPlayerDataManager().getPlayerData(player);

                if (playerData != null) {
                    sendRetardedNewLine(sender);
                    sendMessage(sender, Config.ACCENT_ONE + "Information for &c" + playerData.getPlayer().getName() + Config.ACCENT_ONE + ".");
                    sendRetardedNewLine(sender);
                    sendMessage(sender, Config.ACCENT_TWO + "&oGeneral information:");
                    sendMessage(sender, Config.ACCENT_ONE + "Latency → " + Config.ACCENT_TWO + PacketEvents.get().getPlayerUtils().getPing(playerData.getPlayer()) + "ms");
                    sendMessage(sender, Config.ACCENT_ONE + "Checks amount → " + Config.ACCENT_TWO + playerData.getChecks().size());
                    sendMessage(sender, Config.ACCENT_ONE + "Sensitivity → " + Config.ACCENT_TWO + playerData.getRotationProcessor().getSensitivity() + "%");
                    final String clientBrand = playerData.getClientBrand() == null ? "&cCould not resolve client brand for this player." : playerData.getClientBrand();
                    sendMessage(sender, ColorUtil.translate(Config.ACCENT_ONE  + "Client brand: → " + Config.ACCENT_TWO + clientBrand));
                    sendRetardedNewLine(sender);
                    return true;
                }
            }
        }
        return false;
    }
}
