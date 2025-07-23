package ai.medusa.anticheat.check.impl.combat.autoclicker;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;
import ai.medusa.anticheat.util.MathUtil;

import java.util.ArrayDeque;

@CheckInfo(name = "AutoClicker (C)", experimental = true, description = "Checks for rounded(ish) CPS.")
public final class AutoClickerC extends Check {

    private final ArrayDeque<Integer> samples = new ArrayDeque<>();
    private int ticks;

    public AutoClickerC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isArmAnimation() && !isExempt(ExemptType.AUTO_CLICKER)) {

           if (ticks < 4) {
               samples.add(ticks);
           }

           if (samples.size() == 50) {

               final double cps = MathUtil.getCps(samples);
               final double difference = Math.abs(Math.round(cps) - cps);

               debug("diff=" + difference + " cps=" + cps + " buf=" + buffer);

               if (difference < 0.001) {
                   if ((buffer += 10) > 25) {
                       fail("diff=" + difference);
                   }
               } else {
                   buffer = Math.max(buffer - 8, 0);
               }

               samples.clear();
           }

           ticks = 0;
        } else if (packet.isFlying()) {
            ++ticks;
        }
    }
}
