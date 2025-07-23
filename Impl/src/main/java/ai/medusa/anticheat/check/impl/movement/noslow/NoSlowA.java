package ai.medusa.anticheat.check.impl.movement.noslow;

import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;
import io.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.packetwrappers.play.out.helditemslot.WrappedPacketOutHeldItemSlot;

@CheckInfo(name = "NoSlow (A)", description = "Checks for NoSlow modules.", experimental = true)
public final class NoSlowA extends Check {

    public NoSlowA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isPosition()) {
            if (data.getActionProcessor().isSprinting() && data.getPlayer().isBlocking()) {
                debug("buffer=" + buffer);
                if (++buffer > 10) {
                    fail("buffer=" + buffer);
                    final int slot = data.getActionProcessor().getHeldItemSlot() == 8 ? 1 : 8;
                    final WrappedPacketOutHeldItemSlot wrapper = new WrappedPacketOutHeldItemSlot(slot);
                    PacketEvents.get().getPlayerUtils().sendPacket(data.getPlayer(), wrapper);
                    buffer /= 2;
                }
            } else {
                buffer -= buffer > 0 ? 0.25 : 0;
            }
        }
    }
}
