package ai.medusa.anticheat.check.impl.movement.fly;

import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;

/**
 * Created on 11/17/2020 Package ai.medusa.anticheat.check.impl.movement.fly by infinitesm
 */

@CheckInfo(name = "Fly (A)", description = "Checks for gravity.")
public final class FlyA extends Check {

    public FlyA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            final double deltaY = data.getPositionProcessor().getDeltaY();
            final double lastDeltaY = data.getPositionProcessor().getLastDeltaY();

            final boolean onGround = data.getPositionProcessor().getAirTicks() <= 5;

            final double prediction = Math.abs((lastDeltaY - 0.08) * 0.98F) < 0.005 ? -0.08 * 0.98F : (lastDeltaY - 0.08) * 0.98F;
            final double difference = Math.abs(deltaY - prediction);

            final boolean exempt = isExempt(
                    ExemptType.TELEPORT, ExemptType.NEAR_VEHICLE, ExemptType.FLYING,
                    ExemptType.INSIDE_VEHICLE, ExemptType.VELOCITY
            );

            final boolean invalid = !exempt
                    && difference > 0.001D
                    && !onGround
                    && !(data.getPositionProcessor().getY() % 0.5 == 0 && data.getPositionProcessor().isOnGround() && lastDeltaY < 0);

            debug("posY=" + data.getPositionProcessor().getY() + " dY=" + deltaY + " at=" + data.getPositionProcessor().getAirTicks());

            if (invalid) {
                buffer += buffer < 50 ? 10 : 0;
                if (buffer > 20) {
                    fail(String.format("diff=%.4f, buffer=%.2f, at=%o", difference, buffer, data.getPositionProcessor().getAirTicks()));
                }
            } else {
                buffer = Math.max(buffer - 0.75, 0);
            }
        }
    }
}
