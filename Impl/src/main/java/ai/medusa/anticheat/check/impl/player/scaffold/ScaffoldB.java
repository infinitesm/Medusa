package ai.medusa.anticheat.check.impl.player.scaffold;

import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;
import ai.medusa.anticheat.util.PlayerUtil;
import io.github.retrooper.packetevents.packetwrappers.play.in.blockplace.WrappedPacketInBlockPlace;
import org.bukkit.potion.PotionEffectType;

@CheckInfo(name = "Scaffold (B)", description = "Checks for tower hacks.", experimental = true)
public final class ScaffoldB extends Check {

    private int movements;
    private int lastX, lastY, lastZ;
    public ScaffoldB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isBlockPlace()) {
            final WrappedPacketInBlockPlace wrapper = new WrappedPacketInBlockPlace(packet.getRawPacket());

            if (PlayerUtil.getPotionLevel(data.getPlayer(), PotionEffectType.JUMP) > 0) return;
            if (data.getPositionProcessor().getDeltaY() <= 0) return;

            if (!(wrapper.getBlockPosition().getX() == 1 && wrapper.getBlockPosition().getY() == 1 && wrapper.getBlockPosition().getZ() != 1)) {
                if (data.getPlayer().getItemInHand().getType().isBlock()) {
                    if (lastX == wrapper.getBlockPosition().getX() && wrapper.getBlockPosition().getY() > lastY && lastZ == wrapper.getBlockPosition().getZ()) {
                        if (movements < 7) {
                            if (++buffer > 2) {
                                fail("ticks=" + movements);
                            }
                        } else {
                            buffer = 0;
                        }
                        movements = 0;
                    }
                    lastX = wrapper.getBlockPosition().getX();
                    lastY = wrapper.getBlockPosition().getY();
                    lastZ = wrapper.getBlockPosition().getZ();
                }
            }
        } else if (packet.isFlying()) {
            ++movements;
        }
    }
}
