package ai.medusa.anticheat.check.impl.player.protocol;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.abilities.WrappedPacketInAbilities;

/**
 * Created on 11/14/2020 Package ai.medusa.anticheat.check.impl.player.Protocol by infinitesm
 */


@CheckInfo(name = "Protocol (B)", description = "Checks for spoofed abilities packets.")
public final class ProtocolB extends Check {

    public ProtocolB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isAbilities()) {
            final WrappedPacketInAbilities wrapper = new WrappedPacketInAbilities(packet.getRawPacket());

            final boolean invalid = wrapper.isFlightAllowed() && !data.getPlayer().getAllowFlight();

            if (invalid) {
                fail();
            }
        }
    }
}
