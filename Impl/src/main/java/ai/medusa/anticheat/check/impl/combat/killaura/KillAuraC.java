package ai.medusa.anticheat.check.impl.combat.killaura;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.packet.Packet;

/**
 * Created on 10/24/2020 Package ai.medusa.anticheat.check.impl.combat.killaura by infinitesm
 */
 
@CheckInfo(name = "KillAura (C)", description = "Checks for multi-aura.")
public final class KillAuraC extends Check {

    public KillAuraC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isUseEntity()) {
            final int targets = data.getCombatProcessor().getCurrentTargets();

            debug("tg="+ targets);
            if (targets > 1) fail("tg=" + targets);
        }
    }
}
