package ai.medusa.anticheat.check.impl.combat.velocity;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.config.ConfigValue;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;

/**
 * Created on 11/23/2020 Package ai.medusa.anticheat.check.impl.combat.velocity by infinitesm
 */
@CheckInfo(name = "Velocity (A)", experimental = true, description = "Checks for vertical velocity.")
public final class VelocityA extends Check {

    private static final ConfigValue minVelPct = new ConfigValue(
            ConfigValue.ValueType.INTEGER, "minimum-velocity-percentage"
    );
    private static final ConfigValue maxVelPct = new ConfigValue(
            ConfigValue.ValueType.INTEGER, "maximum-velocity-percentage"
    );

    public VelocityA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying()) {
            if (data.getVelocityProcessor().getTicksSinceVelocity() < 5) {
                final double deltaY = data.getPositionProcessor().getDeltaY();
                final double velocityY = data.getVelocityProcessor().getVelocityY();

                debug("dy=" + deltaY + " vy=" + velocityY);
                if (velocityY > 0) {
                    final int percentage = (int) Math.round((deltaY * 100.0) / velocityY);

                    final boolean exempt = isExempt(
                            ExemptType.LIQUID, ExemptType.PISTON, ExemptType.CLIMBABLE,
                            ExemptType.UNDER_BLOCK, ExemptType.TELEPORT, ExemptType.FLYING,
                            ExemptType.WEB, ExemptType.STEPPED
                    );

                    final boolean invalid = !exempt
                            && (percentage < minVelPct.getInt() || percentage > maxVelPct.getInt());

                    if (invalid) {
                        if (++buffer > 5) {
                            buffer = 0;
                            fail("vy=" + velocityY + " pct=" + percentage);
                        }
                    } else {
                        buffer = 0;
                    }
                }
            }
        }
    }
}
