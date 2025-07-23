package ai.medusa.anticheat.check.impl.combat.aimassist;

import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;

@CheckInfo(name = "AimAssist (I)", description = "Checks for snappy rotations.", experimental = true)
public class AimAssistI extends Check {

    private float lastDeltaYaw, lastLastDeltaYaw;

    public AimAssistI(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isRotation()) {
            final float deltaYaw = data.getRotationProcessor().getDeltaYaw();

            debug("dy=" + deltaYaw + " ldy=" + lastDeltaYaw + " lldy=" + lastLastDeltaYaw);

            if (deltaYaw < 2F && lastDeltaYaw > 30F && lastLastDeltaYaw < 2F) {
                final double low = (deltaYaw + lastLastDeltaYaw) / 2;
                final double high = lastDeltaYaw;

                fail(String.format("low=%.2f, high=%.2f", low, high));
            }

            lastLastDeltaYaw = lastDeltaYaw;
            lastDeltaYaw = deltaYaw;
        }
    }
}
