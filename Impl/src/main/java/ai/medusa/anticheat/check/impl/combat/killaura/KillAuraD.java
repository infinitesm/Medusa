package ai.medusa.anticheat.check.impl.combat.killaura;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;

import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;
import org.bukkit.Location;

/**
 * Created on 10/24/2020 Package ai.medusa.anticheat.check.impl.combat.killaura by infinitesm
 */

@CheckInfo(name = "KillAura (D)", description = "Checks for high accuracy.")
public final class KillAuraD extends Check {

    private int hits, swings;
    private Location lastLocation;
    private Float lastYaw, lastPitch;

    public KillAuraD(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {

            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());
            final float yaw = data.getRotationProcessor().getYaw() % 360F;
            final float pitch = data.getRotationProcessor().getPitch();
            final boolean isMoving = data.getPositionProcessor().getDeltaXZ() != 0;
            if (wrapper.getEntity() != null) {
                if (lastLocation != null && lastYaw != null && lastPitch != null) {
                    if (wrapper.getEntity().getLocation().distance(lastLocation) > 0.1
                            && lastYaw != yaw && lastPitch != pitch && isMoving
                            && wrapper.getEntity().getWorld() == data.getPlayer().getWorld()) {
                        ++hits;
                    }
                }
                lastLocation = wrapper.getEntity().getLocation();
            }
            lastYaw = yaw;
            lastPitch = pitch;
        } else if (packet.isArmAnimation()) {
            debug("swings=" + swings);
            if (++swings >= 50) {
                final double accuracy = hits/swings;
                debug("accuracy=" + accuracy);
                if (hits > 40) {
                    if ((buffer += 10) > 20) {
                        fail("accuracy=" + accuracy);
                    }
                } else {
                    buffer = Math.max(buffer - 2, 0);
                }
                hits = swings = 0;
            }
        }
    }
}
