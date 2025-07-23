package ai.medusa.anticheat.check.impl.combat.aimassist;

import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;
import ai.medusa.anticheat.util.MathUtil;

/**
 * Created 12/06/2020 Package ai.medusa.anticheat.check.impl.combat.aimassist by infinitesm
 */

@CheckInfo(name = "AimAssist (E)", description = "Checks for a valid sensitivity in the rotation.")
public final class AimAssistE extends Check {

    public AimAssistE(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isRotation()) {
            final float deltaPitch = data.getRotationProcessor().getDeltaPitch();
            final float lastDeltaPitch = data.getRotationProcessor().getLastDeltaPitch();

            final long expandedDeltaPitch = (long) (deltaPitch * MathUtil.EXPANDER);
            final long expandedLastDeltaPitch = (long) (lastDeltaPitch * MathUtil.EXPANDER);

            final long gcd = MathUtil.getGcd(expandedDeltaPitch, expandedLastDeltaPitch);

            final boolean exempt = deltaPitch == 0
                    || lastDeltaPitch == 0
                    || isExempt(ExemptType.CINEMATIC);

            debug("gcd=" + gcd + " cinematic=" + isExempt(ExemptType.CINEMATIC) + " buf=" + buffer);

            if (!exempt && gcd < 131072L) {
                if (++buffer > 10) {
                    fail("gcd=" + gcd + " buffer=" + buffer);
                }
            } else {
                buffer = Math.max(0, buffer - 1);
            }
        }
    }
}
