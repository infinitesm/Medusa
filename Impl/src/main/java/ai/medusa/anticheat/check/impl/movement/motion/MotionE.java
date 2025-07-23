package ai.medusa.anticheat.check.impl.movement.motion;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;

/**
 * Created on 11/17/2020 Package ai.medusa.anticheat.check.impl.movement.speed by infinitesm
 */
@CheckInfo(name = "Motion (E)", description = "Checks for switching direction mid-air.")
public final class MotionE extends Check {

    public MotionE(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            final double deltaX = data.getPositionProcessor().getDeltaX();
            final double lastDeltaX = data.getPositionProcessor().getLastDeltaX();

            final double deltaZ = data.getPositionProcessor().getDeltaZ();
            final double lastDeltaZ = data.getPositionProcessor().getLastDeltaZ();

            final double absDeltaX = Math.abs(deltaX);
            final double absDeltaZ = Math.abs(deltaZ);

            final double absLastDeltaX = Math.abs(lastDeltaX);
            final double absLastDeltaZ = Math.abs(lastDeltaZ);

            if (data.getPositionProcessor().getAirTicks() > 2 && !isExempt(ExemptType.VELOCITY, ExemptType.NEAR_VEHICLE, ExemptType.TELEPORT, ExemptType.FLYING)) {
                final boolean xSwitched = (deltaX > 0 && lastDeltaX < 0) || (deltaX < 0 && lastDeltaX > 0);
                final boolean zSwitched = (deltaZ > 0 && lastDeltaZ < 0) || (deltaZ < 0 && lastDeltaZ > 0);

                if (xSwitched) {
                    if (Math.abs(absDeltaX - absLastDeltaX) > 0.05) {
                        if (++buffer > 1.25) {
                            fail();
                        }
                    }
                } else {
                    buffer = Math.max(buffer - 0.05, 0);
                }
                if (zSwitched) {
                    if (Math.abs(absDeltaZ - absLastDeltaZ) > 0.05) {
                        if (++buffer > 1.25) {
                            fail();
                        }
                    }
                } else {
                    buffer = Math.max(buffer - 0.05, 0);
                }
            }
        }
    }
}
