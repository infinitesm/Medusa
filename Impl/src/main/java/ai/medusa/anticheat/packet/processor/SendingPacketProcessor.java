package ai.medusa.anticheat.packet.processor;

import ai.medusa.anticheat.data.PlayerData;
import io.github.retrooper.packetevents.packetwrappers.play.out.entityvelocity.WrappedPacketOutEntityVelocity;
import ai.medusa.anticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.out.explosion.WrappedPacketOutExplosion;
import io.github.retrooper.packetevents.packetwrappers.play.out.position.WrappedPacketOutPosition;

public final class SendingPacketProcessor  {

    public void handle(final PlayerData data, final Packet packet) {
        if (packet.isVelocity()) {
            final WrappedPacketOutEntityVelocity wrapper = new WrappedPacketOutEntityVelocity(packet.getRawPacket());
            if (wrapper.getEntity() == data.getPlayer()) {
                data.getVelocityProcessor().handle(wrapper.getVelocityX(), wrapper.getVelocityY(), wrapper.getVelocityZ());
            }
        }
        if (packet.isExplosion()) {
            final WrappedPacketOutExplosion wrapper = new WrappedPacketOutExplosion(packet.getRawPacket());
            data.getVelocityProcessor().handle(wrapper.getPlayerMotionX(), wrapper.getPlayerMotionY(), wrapper.getPlayerMotionZ());
        }
        if (packet.isOutPosition()) {
            final WrappedPacketOutPosition wrapper = new WrappedPacketOutPosition(packet.getRawPacket());
            data.getPositionProcessor().handleServerPosition(wrapper);
        }
        if (!data.getPlayer().hasPermission("medusa.bypass") || data.getPlayer().isOp()) {
            data.getChecks().forEach(check -> check.handle(packet));
        }
    }
}
