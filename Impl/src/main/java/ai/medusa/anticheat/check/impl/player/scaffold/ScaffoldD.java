package ai.medusa.anticheat.check.impl.player.scaffold;

import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;

@CheckInfo(name = "Scaffold (D)", description = "Checks for SafeWalk scaffold.", experimental = true)
public final class ScaffoldD extends Check {

    private int placedTicks;

    private double lastLastAccel, lastAccel;

    public ScaffoldD(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            if (++placedTicks < 5 && isBridging() && !data.getActionProcessor().isSneaking()) {
                final double accel = data.getPositionProcessor().getDeltaXZ() - data.getPositionProcessor().getLastDeltaXZ();

                debug("accel=" + accel);
                if (accel < 0.0001 && lastAccel > 0.05 && lastLastAccel < 0.0001) {
                    if (++buffer > 3) {
                        fail();
                        buffer = 0;
                    }
                }

                lastLastAccel = lastAccel;
                lastAccel = accel;
            }
        } else if (packet.isBlockPlace()) {
            if (data.getPlayer().getItemInHand().getType().isBlock()) placedTicks = 0;
        }
    }
}
