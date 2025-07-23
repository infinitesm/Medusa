package ai.medusa.anticheat.check.impl.player.protocol;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;

/**
 * Created on 11/10/2020 Package ai.medusa.anticheat.check.impl.player.scaffold by infinitesm
 */

@CheckInfo(name = "Protocol (G)", description = "Checks for packet order.")
public final class ProtocolG extends Check {

    private long lastBlockPlace;
    private boolean placedBlock;

    public ProtocolG(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isBlockPlace()) {
            lastBlockPlace = now();
            placedBlock = true;
        } else if (packet.isFlying()) {
            if (placedBlock) {
                final long delay = now() - lastBlockPlace;
                final boolean invalid = !data.getActionProcessor().isLagging() && delay > 25;

                if (invalid) {
                    if (++buffer > 5) {
                        fail("delay=" + delay + " buffer=" + buffer);
                    }
                } else {
                    buffer = Math.max(buffer - 2, 0);
                }
            }
            placedBlock = false;
        }
    }
}
