package ai.medusa.anticheat.check.impl.movement.speed;

import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;
import ai.medusa.anticheat.util.PlayerUtil;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(name = "Speed (C)", description = "Basic verbose speed check.", experimental = true)
public final class SpeedC extends Check {

    public SpeedC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            final double deltaXZ = data.getPositionProcessor().getDeltaXZ();

            final double maxSpeed = getSpeed(0.4);

            final boolean exempt = isExempt(
                    ExemptType.TELEPORT, ExemptType.JOINED, ExemptType.PISTON, ExemptType.VELOCITY,
                    ExemptType.INSIDE_VEHICLE, ExemptType.FLYING, ExemptType.SLIME, ExemptType.UNDER_BLOCK,
                    ExemptType.ICE
            );

            debug("dxz-ms" + (deltaXZ-maxSpeed) + " buffer=" + buffer);
            if (deltaXZ > maxSpeed && !exempt) {
                buffer += buffer < 15 ? 1 : 0;
                if (buffer > 10) {
                    fail("deltaXZ=" + deltaXZ + " max=" + maxSpeed);
                    buffer /= 2;
                }
            } else {
                buffer -= buffer > 0 ? 0.25 : 0;
            }
        }
    }

    private double getSpeed(double movement) {
        if (PlayerUtil.getPotionLevel(data.getPlayer(), PotionEffectType.SPEED) > 0) {
            movement *= 1.0D + 0.2D * (double)(PlayerUtil.getPotionLevel(data.getPlayer(), PotionEffectType.SPEED));
        }
        return movement;
    }
}
