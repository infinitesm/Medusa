package ai.medusa.anticheat.check.impl.movement.fly;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;

/**
 * Created on 11/17/2020 Package ai.medusa.anticheat.check.impl.movement.fly by infinitesm
 */

@CheckInfo(name = "Fly (B)", description = "Checks for jumping mid-air.")
public final class FlyB extends Check {

    private double lastAcceleration;

    public FlyB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition() && !isExempt(ExemptType.TELEPORT)) {
            final double acceleration = data.getPositionProcessor().getDeltaY() - data.getPositionProcessor().getLastDeltaY();
            final double airTicks = data.getPositionProcessor().getAirTicks();
            final double deltaY = data.getPositionProcessor().getDeltaY();

            final boolean invalid = !isExempt(
                    ExemptType.FLYING, ExemptType.VELOCITY, ExemptType.INSIDE_VEHICLE, ExemptType.NEAR_VEHICLE
            ) && lastAcceleration <= 0 && acceleration > 0 && deltaY > 0;

            debug("airTicks=" + airTicks + " accel=" + acceleration);
            if (airTicks > 10) {
                if (invalid) {
                    fail(String.format("accel=%.2f, at=%.2f", acceleration, airTicks));
                }
            }

            lastAcceleration = acceleration;
        }
    }
}
