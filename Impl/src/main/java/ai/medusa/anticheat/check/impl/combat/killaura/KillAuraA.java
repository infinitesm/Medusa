package ai.medusa.anticheat.check.impl.combat.killaura;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;

/**
 * Created on 10/24/2020 Package ai.medusa.anticheat.check.impl.combat.killaura by infinitesm
 */

@CheckInfo(name = "KillAura (A)", description = "Checks for packet order.")
public final class KillAuraA extends Check {

    private boolean usedEntity;
    private long lastUseEntityTime;

    public KillAuraA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {
            usedEntity = true;
            lastUseEntityTime = now();
        } else if (packet.isFlying()) {
            if (usedEntity) {
                final long delay = now() - lastUseEntityTime;
                final boolean invalid = !data.getActionProcessor().isLagging() && delay > 15;

                debug("delay=" + delay);
                if (invalid) {
                    if (++buffer > 2) {
                        fail(String.format("delay=%d", delay));
                    }
                } else {
                    buffer = Math.max(buffer - 0.15, 0);
                }
            }
            usedEntity = false;
        }
    }
}
