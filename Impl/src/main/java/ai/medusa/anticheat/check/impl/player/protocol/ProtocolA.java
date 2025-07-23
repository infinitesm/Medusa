package ai.medusa.anticheat.check.impl.player.protocol;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;

/**
 * Created on 11/14/2020 Package ai.medusa.anticheat.check.impl.player.Protocol by infinitesm
 */

@CheckInfo(name = "Protocol (A)", description = "Checks for invalid pitch rotation.")
public final class ProtocolA extends Check {

    public ProtocolA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isRotation()) {
            final float pitch = data.getRotationProcessor().getPitch();

            if (Math.abs(pitch) > 90) fail(String.format("pitch=%.2f", pitch));
        }
    }
}
