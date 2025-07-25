package ai.medusa.exampleapi.listener;

import ai.medusa.api.check.MedusaCheck;
import ai.medusa.api.listener.MedusaFlagEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class ExampleListener implements Listener {
    @EventHandler
    public void onMedusaFlag(final MedusaFlagEvent event) {
        final Player player = event.getPlayer();
        final MedusaCheck check = event.getCheck();
        final boolean cancelled = event.isCancelled();

        Bukkit.broadcastMessage(player.getName() + " flagged " + check.getCheckInfo().name());
    }
}
