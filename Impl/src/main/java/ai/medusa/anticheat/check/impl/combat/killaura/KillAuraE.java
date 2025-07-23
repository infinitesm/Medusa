package ai.medusa.anticheat.check.impl.combat.killaura;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;

/**
 * Created on 07/01/2020 Package luna.anticheat.checks.player.badpackets by infinitesm
 */
@CheckInfo(name = "KillAura (E)", description = "Checks for no swing modules.")
public final class KillAuraE extends Check {

    private int hits;

    public KillAuraE(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {
            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());
            if (wrapper.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                debug("hits=" + hits);
                if (++hits > 2) {
                    fail("ticks=" + hits);
                }
            }
        } else if (packet.isArmAnimation()) {
            hits = 0;
        }
    }
}
