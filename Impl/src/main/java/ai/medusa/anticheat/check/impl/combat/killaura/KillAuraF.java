package ai.medusa.anticheat.check.impl.combat.killaura;

import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@CheckInfo(name = "KillAura (F)", experimental = true, description = "Checks for hit occlusion (wallhit).")
public final class KillAuraF extends Check {

    private Location lastAttackerLocation;
    private float lastYaw, lastPitch;

    public KillAuraF(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {
            final Entity target = data.getCombatProcessor().getTarget();
            final Player attacker = data.getPlayer();

            if (target == null || attacker == null) return;
            if (target.getWorld() != attacker.getWorld()) return;

            final Location attackerLocation = attacker.getLocation();

            final float yaw = data.getRotationProcessor().getYaw() % 360F;
            final float pitch = data.getRotationProcessor().getPitch();

            if (lastAttackerLocation != null) {
                final boolean check = yaw != lastYaw &&
                        pitch != lastPitch &&
                        attackerLocation.distance(lastAttackerLocation) > 0.1;

                debug("buffer=" + buffer);

                if (check && !attacker.hasLineOfSight(target)) {
                    if ((buffer += 10) > 50) {
                        fail("buffer=" + buffer);
                    }
                } else {
                    buffer = Math.max(buffer - 20, 0);
                }
            }

            lastAttackerLocation = attacker.getLocation();

            lastYaw = yaw;
            lastPitch = pitch;
        }
    }
}
