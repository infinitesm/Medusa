package ai.medusa.anticheat.listener;

import ai.medusa.anticheat.Medusa;
import ai.medusa.anticheat.data.PlayerData;

import io.github.retrooper.packetevents.event.PacketListenerDynamic;
import ai.medusa.anticheat.packet.Packet;
import io.github.retrooper.packetevents.event.impl.PacketPlayReceiveEvent;
import io.github.retrooper.packetevents.event.impl.PacketPlaySendEvent;


public final class NetworkListener extends PacketListenerDynamic {

    @Override
    public void onPacketPlayReceive(final PacketPlayReceiveEvent event) {
        final PlayerData data = Medusa.INSTANCE.getPlayerDataManager().getPlayerData(event.getPlayer());

        handle: {
            if (data == null) break handle;

            Medusa.INSTANCE.getPacketExecutor().execute(() -> Medusa.INSTANCE.getReceivingPacketProcessor()
                    .handle(data, new Packet(Packet.Direction.RECEIVE, event.getNMSPacket(), event.getPacketId())));
        }
    }

    @Override
    public void onPacketPlaySend(final PacketPlaySendEvent event) {
        final PlayerData data = Medusa.INSTANCE.getPlayerDataManager().getPlayerData(event.getPlayer());

        handle: {
            if (data == null) break handle;

            Medusa.INSTANCE.getPacketExecutor().execute(() -> Medusa.INSTANCE.getSendingPacketProcessor()
                    .handle(data, new Packet(Packet.Direction.SEND, event.getNMSPacket(), event.getPacketId())));
        }
    }
}
