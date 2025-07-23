package ai.medusa.anticheat.check.impl.player.protocol;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.steervehicle.WrappedPacketInSteerVehicle;

/**
 * Created on 11/14/2020 Package ai.medusa.anticheat.check.impl.player.Protocol by infinitesm
 */


@CheckInfo(name = "Protocol (D)", description = "Checks for a common exploit in disabler modules.")
public final class ProtocolD extends Check {

    public ProtocolD(final PlayerData data) {
        super(data);
    }


    @Override
    public void handle(final Packet packet) {
        if (packet.isSteerVehicle()) {
            final WrappedPacketInSteerVehicle wrapper = new WrappedPacketInSteerVehicle(packet.getRawPacket());

            final float forwardValue = Math.abs(wrapper.getForwardValue());
            final float sideValue = Math.abs(wrapper.getSideValue());

            final boolean invalid = forwardValue > .98F || sideValue > .98F;

            if (invalid) {
                fail();
            }
        }
    }
}
