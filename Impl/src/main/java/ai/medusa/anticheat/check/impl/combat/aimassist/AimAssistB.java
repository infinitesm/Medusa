package ai.medusa.anticheat.check.impl.combat.aimassist;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;

/**
 * Created on 11/14/2020 Package ai.medusa.anticheat.check.impl.combat.aim by infinitesm
 */

@CheckInfo(name = "AimAssist (B)", description = "Checks for rounded rotation.")
public final class AimAssistB extends Check {

    public AimAssistB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isRotation()) {
            final float deltaYaw = data.getRotationProcessor().getDeltaYaw() % 360F;
            final float deltaPitch = data.getRotationProcessor().getDeltaPitch();

            final boolean exempt = isExempt(ExemptType.TELEPORT);

            final boolean invalid = !exempt
                    && (deltaPitch % 1 == 0 || deltaYaw % 1 == 0) && deltaPitch != 0 && deltaYaw != 0;

            debug(String.format("teleport=%b, buffer=%.2f", exempt, buffer));

            if (invalid) {
                if (++buffer > 4) {
                    fail(String.format("buffer=%.2f", buffer));
                }
            } else {
                buffer = Math.max(0, buffer - 0.25);
            }
        }
    }
}
