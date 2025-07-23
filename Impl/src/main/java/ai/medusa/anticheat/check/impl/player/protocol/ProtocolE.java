package ai.medusa.anticheat.check.impl.player.protocol;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.helditemslot.WrappedPacketInHeldItemSlot;

/**
 * Created on 11/14/2020 Package ai.medusa.anticheat.check.impl.player.Protocol by infinitesm
 */


@CheckInfo(name = "Protocol (E)", description = "Checks for flaws in scaffold/auto-tool hacks.")
public final class ProtocolE extends Check {

    private int lastSlot = -1;

    public ProtocolE(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isHeldItemSlot()) {
            final WrappedPacketInHeldItemSlot wrapper = new WrappedPacketInHeldItemSlot(packet.getRawPacket());

            final int slot = wrapper.getCurrentSelectedSlot();

            if (slot == lastSlot) {
                fail();
            }

            lastSlot = slot;
        }
    }
}
