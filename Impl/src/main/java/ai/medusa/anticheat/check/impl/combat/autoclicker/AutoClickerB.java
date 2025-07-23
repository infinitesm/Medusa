package ai.medusa.anticheat.check.impl.combat.autoclicker;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;
import ai.medusa.anticheat.util.MathUtil;

import java.util.ArrayDeque;

/**
 * Created on 12/30/2020 Package ai.medusa.anticheat.check.impl.combat.autoclicker by infinitesm
 */
@CheckInfo(name = "AutoClicker (B)", description = "Simple change in statistics check.")
public final class AutoClickerB extends Check {

    private int ticks;
    private double lastDev, lastSkew, lastKurt;
    private ArrayDeque<Integer> samples = new ArrayDeque<>();

    public AutoClickerB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isArmAnimation() && !isExempt(ExemptType.AUTO_CLICKER)) {
            if (ticks < 4) {
                samples.add(ticks);
            }

            if (samples.size() == 120) {
                final double deviation = MathUtil.getStandardDeviation(samples);
                final double skewness = MathUtil.getSkewness(samples);
                final double kurtosis = MathUtil.getKurtosis(samples);

                final double deltaDeviation = Math.abs(deviation - lastDev);
                final double deltaSkewness = Math.abs(skewness - lastSkew);
                final double deltaKurtosis = Math.abs(kurtosis - lastKurt);

                debug("dd=" + deltaDeviation + " ds=" + deltaSkewness + " dk=" + deltaKurtosis);

                if (deltaDeviation < 0.01 || deltaSkewness < 0.01 || deltaKurtosis < 0.01) {
                    if ((buffer += 10) > 50) {
                        fail("dd=" + deltaDeviation + " ds=" + deltaSkewness + " dk=" + deltaKurtosis);
                    }
                } else {
                    buffer = Math.max(buffer - 8, 0);
                }

                lastDev = deviation;
                lastSkew = skewness;
                lastKurt = kurtosis;

                samples.clear();
            }

            ticks = 0;
        } else if (packet.isFlying()) {
            ++ticks;
        }
    }
}
