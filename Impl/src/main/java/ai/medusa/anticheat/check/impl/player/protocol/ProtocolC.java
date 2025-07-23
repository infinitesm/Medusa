package ai.medusa.anticheat.check.impl.player.protocol;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.flying.WrappedPacketInFlying;

/**
 * Created on 11/14/2020 Package ai.medusa.anticheat.check.impl.player.Protocol by infinitesm
 */


@CheckInfo(name = "Protocol (C)", description = "Checks for flying packet sequence.")
public final class ProtocolC extends Check {

    private int streak;

    public ProtocolC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying()) {
            final WrappedPacketInFlying wrapper = new WrappedPacketInFlying(packet.getRawPacket());

            if (wrapper.isPosition() || data.getPlayer().isInsideVehicle()) {
                streak = 0;
                return;
            }

            if (++streak > 20) {
                fail("streak=" + streak);
            }
        } else if (packet.isSteerVehicle()) {
            streak = 0;
        }
    }
}
