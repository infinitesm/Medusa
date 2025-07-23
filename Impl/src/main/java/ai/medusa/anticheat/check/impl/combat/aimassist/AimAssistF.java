package ai.medusa.anticheat.check.impl.combat.aimassist;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;

/**
 * Created 11/14/2020 Package ai.medusa.anticheat.check.impl.combat.aim by infinitesm
 */

@CheckInfo(name = "AimAssist (F)", description = "Checks for impossible rotation.", experimental = true)
public final class AimAssistF extends Check {

    public AimAssistF(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isRotation()) {
            final float deltaYaw = data.getRotationProcessor().getDeltaYaw();
            final float deltaPitch = data.getRotationProcessor().getDeltaPitch();

            debug("tp=" + isExempt(ExemptType.TELEPORT));

            if (deltaPitch == 0 && deltaYaw == 0 && !isExempt(ExemptType.TELEPORT)) fail();
        }
    }
}
