package ai.medusa.anticheat.check.impl.movement.motion;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;

/**
 * Created on 11/17/2020 Package ai.medusa.anticheat.check.impl.movement.motion by infinitesm
 */

@CheckInfo(name = "Motion (B)", description = "Checks for fast-fall cheats.")
public final class MotionB extends Check {

    public MotionB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            final double deltaY = data.getPositionProcessor().getDeltaY();

            debug("deltaY=" + deltaY);
            final boolean invalid = deltaY < -3.92 &&
                    !isExempt(ExemptType.TELEPORT, ExemptType.FLYING);

            if (invalid) fail(String.format("dy=%.2f", deltaY));
        }
    }
}
