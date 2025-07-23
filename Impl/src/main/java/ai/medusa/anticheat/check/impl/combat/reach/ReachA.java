package ai.medusa.anticheat.check.impl.combat.reach;

import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.Medusa;
import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.config.ConfigValue;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;
import ai.medusa.anticheat.util.HitboxExpansion;
import ai.medusa.anticheat.util.PlayerUtil;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

/**
 * Created on 10/26/2020 Package ai.medusa.anticheat.check.impl.combat.reach by infinitesm
 *
 * Fix stupid HitboxExpansion using NMS.
 */

@CheckInfo(name = "Reach (A)", description = "Checks for attacking distance.")
public final class ReachA extends Check {

    private static final ConfigValue maxReach = new ConfigValue(ConfigValue.ValueType.DOUBLE, "max-reach");
    private static final ConfigValue maxThreshold = new ConfigValue(ConfigValue.ValueType.DOUBLE, "max-threshold");
    private static final ConfigValue thresholdDecay = new ConfigValue(ConfigValue.ValueType.DOUBLE, "threshold-decay");
    private static final ConfigValue maxLatency = new ConfigValue(ConfigValue.ValueType.LONG, "max-latency");

    public ReachA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {
            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());

            final Entity target = data.getCombatProcessor().getTarget();
            final Entity lastTarget = data.getCombatProcessor().getLastTarget();

            if (wrapper.getAction() != WrappedPacketInUseEntity.EntityUseAction.ATTACK
                    || data.getPlayer().getGameMode() != GameMode.SURVIVAL
                    || !(target instanceof LivingEntity)
                    || target != lastTarget
                    || !data.getTargetLocations().isFull()
                    || PlayerUtil.getPing(data.getPlayer()) > (maxLatency.getLong() < 0 ? Integer.MAX_VALUE : maxLatency.getLong())) return;

            final int ticks = Medusa.INSTANCE.getTickManager().getTicks();
            final int pingTicks = NumberConversions.floor(data.getActionProcessor().getPing() / 50.0) + 3;

            final Vector player = data.getPlayer().getLocation().toVector().setY(0);

            final double distance = data.getTargetLocations().stream()
                    .filter(pair -> Math.abs(ticks - pair.getY() - pingTicks) < 3)
                    .mapToDouble(pair -> {
                        final Vector victim = pair.getX().toVector().setY(0);
                        final double expansion = HitboxExpansion.getExpansion(target);
                        return player.distance(victim) - expansion;
                    }).min().orElse(0);

            if (distance == 0) return;

            debug("dist=" + distance + " buffer=" + buffer + " pt=" + pingTicks);
            if (distance > maxReach.getDouble()) {
                if (++buffer > maxThreshold.getDouble()) {
                    fail(String.format("reach=%.2f, buffer=%.2f", distance, buffer));
                }
            } else {
                buffer = Math.max(buffer - thresholdDecay.getDouble(), 0);
            }
        }
    }

}