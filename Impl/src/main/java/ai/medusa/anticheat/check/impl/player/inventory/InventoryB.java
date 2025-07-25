package ai.medusa.anticheat.check.impl.player.inventory;

import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.windowclick.WrappedPacketInWindowClick;

@CheckInfo(name = "Inventory (B)", description = "Checks for moving inventory items too quickly.", experimental = true)
public final class InventoryB extends Check {

    private int movements;

    public InventoryB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isWindowClick()) {
            final WrappedPacketInWindowClick wrapper = new WrappedPacketInWindowClick(packet.getRawPacket());

            if (wrapper.getMode() == 1) {
                if (movements <= 1) {
                    if (++buffer > 5) {
                        fail("ticks=" + movements + " mode=1");
                    }
                } else {
                    buffer -= buffer > 0 ? 0.5 : 0;
                }
            } else if (wrapper.getMode() == 4) {
                if (movements == 0) {
                    if (++buffer > 5) {
                        fail("ticks=" + movements + " mode=4");
                    }
                } else {
                    buffer -= buffer > 0 ? 0.5 : 0;
                }
            }

            movements = 0;
        } else if (packet.isFlying()) {
            ++movements;
        }
    }
}
