package ai.medusa.anticheat.check.impl.combat.killaura;

import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;

@CheckInfo(name = "KillAura (G)", description = "Checks for large head movements without decelerating.", experimental = true)
public class KillAuraG extends Check {

    public KillAuraG(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosLook() && isExempt(ExemptType.COMBAT)) {
            final double deltaXz = data.getPositionProcessor().getDeltaXZ();
            final double lastDeltaXz = data.getPositionProcessor().getLastDeltaXZ();
            final double accelXz = Math.abs(deltaXz - lastDeltaXz);

            final float deltaYaw = data.getRotationProcessor().getDeltaYaw();

            debug("accel=" + accelXz + " dY=" + deltaYaw);

            if (deltaYaw > 35 && accelXz < 0.00001) {
                fail(String.format("accel=%.6f, dY=%.2f", accelXz, deltaYaw));
            }
        }
    }
}
