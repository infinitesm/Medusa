package ai.medusa.anticheat.util.anticheat;

import ai.medusa.anticheat.Medusa;
import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.data.PlayerData;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public final class PunishUtil {

    public void punish(final Check check, final PlayerData data) {
        if (!check.getPunishCommand().isEmpty()) {
            Bukkit.getScheduler().runTask(Medusa.INSTANCE.getPlugin(), () ->
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), check.getPunishCommand()
                            .replaceAll("%player%", data.getPlayer().getName())
                            .replaceAll("%checkName%", check.getJustTheName())
                            .replaceAll("%checkType", String.valueOf(check.getType()))));
        }
    }
}
