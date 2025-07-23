package ai.medusa.anticheat.check.impl.combat.aimassist;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;

/**
 * Created 10/24/2020 Package ai.medusa.anticheat.check.impl.combat.aim by infinitesm
 */

@CheckInfo(name = "AimAssist (C)", description = "Checks for constant rotation.", experimental = true)
public final class AimAssistC extends Check {

    public AimAssistC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isRotation()) {
            final float deltaYaw = data.getRotationProcessor().getDeltaYaw();
            final float deltaPitch = data.getRotationProcessor().getDeltaPitch();

            final double yawAccel = data.getRotationProcessor().getJoltYaw();
            final double pitchAccel = data.getRotationProcessor().getJoltPitch();

            final boolean exempt = isExempt(ExemptType.TELEPORT, ExemptType.CINEMATIC);
            final boolean invalidYaw = yawAccel < 0.1 && Math.abs(deltaYaw) > 1.5F;
            final boolean invalidPitch = pitchAccel < 0.1 && Math.abs(deltaPitch) > 1.5F;

            final String info = String.format(
                    "dY=%.2f, dP=%.2f, yA=%.2f, pA=%.2f",
                    deltaYaw, deltaPitch, yawAccel, pitchAccel
            );

            debug(info);

            if ((invalidPitch || invalidYaw) && !exempt) {
                if (++buffer > 8) {
                    fail(info);
                }
            } else {
                buffer -= buffer > 0 ? 1 : 0;
            }
        }
    }
}
