package ai.medusa.anticheat.listener;

import ai.medusa.anticheat.Medusa;
import ai.medusa.anticheat.data.PlayerData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public final class BukkitEventListener implements Listener {

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        final PlayerData data = Medusa.INSTANCE.getPlayerDataManager().getPlayerData(event.getPlayer());
        if (data != null) {
            data.getActionProcessor().handleBukkitBlockBreak();
        }
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        final PlayerData data = Medusa.INSTANCE.getPlayerDataManager().getPlayerData(event.getPlayer());
        if (data != null) {
            data.getActionProcessor().handleInteract(event);
        }
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        final PlayerData data = Medusa.INSTANCE.getPlayerDataManager().getPlayerData(event.getPlayer());
        if (data != null) {
            data.getActionProcessor().handleBukkitPlace();
        }
    }
}
