package ai.medusa.anticheat.check.impl.combat.autoclicker;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.config.ConfigValue;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;
import io.github.retrooper.packetevents.packetwrappers.play.in.useentity.WrappedPacketInUseEntity;

/**
 * Created on 10/24/2020 Package ai.medusa.anticheat.check.impl.combat.autoclicker by infinitesm
 */

@CheckInfo(name = "AutoClicker (A)", description = "Checks for attack speed.")
public final class AutoClickerA extends Check {

    private int ticks, cps;
    private static final ConfigValue maxCps = new ConfigValue(ConfigValue.ValueType.INTEGER, "max-cps");

    public AutoClickerA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying()) {
            if (++ticks >= 25) {
                debug("cps=" + cps);
                if (cps > maxCps.getInt() && !isExempt(ExemptType.AUTO_CLICKER)) {
                    fail("cps=" + cps);
                }
                ticks = cps = 0;
            }
        } else if (packet.isUseEntity()) {
            final WrappedPacketInUseEntity wrapper = new WrappedPacketInUseEntity(packet.getRawPacket());
            if (wrapper.getAction() == WrappedPacketInUseEntity.EntityUseAction.ATTACK) {
                ++cps;
            }
        }
    }
}
