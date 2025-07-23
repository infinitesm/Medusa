package ai.medusa.anticheat.check.impl.combat.aimassist;

import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;
import ai.medusa.anticheat.util.MathUtil;
import ai.medusa.anticheat.util.type.EvictingList;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

@CheckInfo(name = "AimAssist (H)", description = "Checks for a generic flaw in aim assistance tools.", experimental = true)
public class AimAssistH extends Check {

    private final EvictingList<Double> differenceSamples = new EvictingList<>(25);

    public AimAssistH(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isRotation() && isExempt(ExemptType.COMBAT)) {
            final Player player = data.getPlayer();
            final Entity target = data.getCombatProcessor().getTarget();

            if (target != null) {
                final Location origin = player.getLocation().clone();
                final Vector end = target.getLocation().clone().toVector();

                final float optimalYaw = origin.setDirection(end.subtract(origin.toVector())).getYaw() % 360F;
                final float rotationYaw = data.getRotationProcessor().getYaw();
                final float deltaYaw = data.getRotationProcessor().getDeltaYaw();
                final float fixedRotYaw = (rotationYaw % 360F + 360F) % 360F;

                final double difference = Math.abs(fixedRotYaw - optimalYaw);

                if (deltaYaw > 3f) {
                    differenceSamples.add(difference);
                }
                if (differenceSamples.isFull()) {
                    final double average = MathUtil.getAverage(differenceSamples);
                    final double deviation = MathUtil.getStandardDeviation(differenceSamples);

                    final boolean invalid = average < 7 && deviation < 12;

                    if (invalid) {
                        if (++buffer > 15) {
                            fail(String.format("dev=%.2f, avg=%.2f, buf=%.2f", deviation, average, buffer));
                        }
                    } else {
                        buffer -= buffer > 0 ? 1 : 0;
                    }

                    debug("avg=" + average + " deviation=" + deviation + " buf=" + buffer);
                }
            }
        }
    }
}
