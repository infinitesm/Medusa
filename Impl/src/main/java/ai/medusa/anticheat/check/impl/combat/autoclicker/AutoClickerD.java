package ai.medusa.anticheat.check.impl.combat.autoclicker;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;
import ai.medusa.anticheat.util.MathUtil;
import ai.medusa.anticheat.util.type.Pair;

import java.util.ArrayDeque;
import java.util.List;

/**
 * Created by Elevated.
 * @link https://github.com/ElevatedDev/Frequency/blob/master/src/main/java/xyz/elevated/frequency/check/impl/autoclicker/AutoClickerE.java
 */

@CheckInfo(name = "AutoClicker (D)", experimental = true, description = "Checks for consistency.")
public final class AutoClickerD extends Check {

    private final ArrayDeque<Integer> samples = new ArrayDeque<>();
    private int ticks;

    public AutoClickerD(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isArmAnimation() && !isExempt(ExemptType.AUTO_CLICKER)) {
            if (ticks < 4) {
                samples.add(ticks);
            }

            if (samples.size() == 20) {
                final Pair<List<Double>, List<Double>> outlierPair = MathUtil.getOutliers(samples);

                final int outliers = outlierPair.getX().size() + outlierPair.getY().size();
                final int duplicates = (int) (samples.size() - samples.stream().distinct().count());

                debug("outliers=" + outliers + " dupl=" + duplicates);

                if (outliers < 2 && duplicates > 16) {
                    if ((buffer += 10) > 50) {
                        fail("outliers=" + outliers + " dupl=" + duplicates);
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
