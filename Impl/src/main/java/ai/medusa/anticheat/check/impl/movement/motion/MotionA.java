package ai.medusa.anticheat.check.impl.movement.motion;

import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;

/**
 * Created on 11/17/2020 Package ai.medusa.anticheat.check.impl.movement.motion by infinitesm
 */

@CheckInfo(name = "Motion (A)", description = "Checks for constant vertical movement.")
public final class MotionA extends Check {

    public MotionA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            final boolean inAir = data.getPositionProcessor().isInAir();

            final double deltaY = data.getPositionProcessor().getDeltaY();
            final double lastDeltaY = data.getPositionProcessor().getLastDeltaY();

            final double acceleration = Math.abs(deltaY - lastDeltaY);

            final boolean exempt = isExempt(
                    ExemptType.JOINED, ExemptType.TRAPDOOR, ExemptType.VELOCITY,
                    ExemptType.FLYING, ExemptType.WEB, ExemptType.TELEPORT,
                    ExemptType.LIQUID, ExemptType.SLIME, ExemptType.CLIMBABLE,
                    ExemptType.UNDER_BLOCK, ExemptType.SLAB, ExemptType.STAIRS
            );

            if (acceleration == 0.0 && inAir && !exempt) {
                if ((buffer += 4) > 16) {
                    fail(String.format("dy=%.2f", deltaY));
                }
            } else {
                buffer = Math.max(buffer - 1, 0);
            }
        }
    }
}
