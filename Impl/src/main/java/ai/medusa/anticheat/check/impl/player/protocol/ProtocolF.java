package ai.medusa.anticheat.check.impl.player.protocol;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.steervehicle.WrappedPacketInSteerVehicle;

/**
 * Created on 11/14/2020 Package ai.medusa.anticheat.check.impl.player.Protocol by infinitesm
 */


@CheckInfo(name = "Protocol (F)", description = "Checks for a common exploit in disabler modules.")
public final class ProtocolF extends Check {

    public ProtocolF(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isSteerVehicle()) {
            final WrappedPacketInSteerVehicle wrapper = new WrappedPacketInSteerVehicle(packet.getRawPacket());

            final boolean unmount = wrapper.isDismount();

            final boolean invalid = data.getPlayer().getVehicle() == null && !unmount;

            if (invalid) {
                if (++buffer > 8) {
                    fail("buffer=" + buffer);
                    buffer /= 2;
                }
            } else {
                buffer = 0;
            }
        }
    }
}
