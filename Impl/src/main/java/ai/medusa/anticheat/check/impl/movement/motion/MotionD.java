package ai.medusa.anticheat.check.impl.movement.motion;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;
import org.bukkit.util.Vector;

/**
 * Created on 11/17/2020 Package ai.medusa.anticheat.check.impl.movement.motion by infinitesm
 */

@CheckInfo(name = "Motion (D)", description = "Checks for sprint direction.")
public final class MotionD extends Check {

    private int teleportTicks;
    private int offGroundTicks;
    private final Vector direction = new Vector(0, 0, 0);

    public MotionD(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying()) {
            if (data.getActionProcessor().isSprinting() &&
                    data.getVelocityProcessor().getTicksSinceVelocity() > 15 &&
                    ++teleportTicks > 10 && !data.getPlayer().isFlying()) {
                final double deltaX = data.getPositionProcessor().getX() - data.getPositionProcessor().getLastX();
                final double deltaZ = data.getPositionProcessor().getZ() - data.getPositionProcessor().getLastZ();

                final double directionX = -Math.sin(data.getPlayer().getEyeLocation().getYaw() * 3.1415927F / 180.0F) * (float) 1 * 0.5F;
                final double directionZ = Math.cos(data.getPlayer().getEyeLocation().getYaw() * 3.1415927F / 180.0F) * (float) 1 * 0.5F;

                final Vector positionDifference = new Vector(deltaX, 0, deltaZ);

                if (data.getPlayer().isOnGround()) {
                    offGroundTicks = 0;
                    direction.setX(directionX);
                    direction.setY(0);
                    direction.setZ(directionZ);
                } else {
                    ++offGroundTicks;
                }

                final double angle = Math.toDegrees(positionDifference.angle(direction));

                final boolean invalid = !data.getPositionProcessor().isInLiquid()
                        && angle > 85
                        && data.getPositionProcessor().getDeltaXZ() > 0.25
                        && offGroundTicks < 8
                        && !isExempt(ExemptType.TELEPORT, ExemptType.VELOCITY);

                debug("angle=" + angle + " dxz=" + data.getPositionProcessor().getDeltaXZ() + " buffer=" + buffer);
                if (invalid) {
                    if (++buffer >= 8) {
                        fail(String.format("angle=%.2f, buffer=%.2f", angle, buffer));
                    }
                } else {
                    buffer = Math.max(buffer - 0.5, 0);
                }
            }
        } else if (packet.isTeleport()) {
            teleportTicks = 0;
        }
    }
}
