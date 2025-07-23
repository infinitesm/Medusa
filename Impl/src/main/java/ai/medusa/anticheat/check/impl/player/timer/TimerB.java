package ai.medusa.anticheat.check.impl.player.timer;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.config.ConfigValue;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;
import ai.medusa.anticheat.util.MathUtil;
import ai.medusa.anticheat.util.type.EvictingList;

/**
 * Created on 11/13/2020 Package ai.medusa.anticheat.check.impl.player.timer by infinitesm
 */

@CheckInfo(name = "Timer (B)",  description = "Checks for game speed which is too slow.", experimental = true)
public final class TimerB extends Check {

    private static final ConfigValue minSpeed = new ConfigValue(ConfigValue.ValueType.DOUBLE, "minimum-timer-speed");
    private final EvictingList<Long> samples = new EvictingList<>(50);
    private long lastFlyingTime;


    public TimerB(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying() && !isExempt(ExemptType.TPS)) {
            final long now = now();
            final long delta = now - lastFlyingTime;

            samples.add(delta);
            if (samples.isFull()) {
                final double average = samples.stream().mapToDouble(value -> value).average().orElse(1);
                final double speed = 50 / average;

                final double deviation = MathUtil.getStandardDeviation(samples);

                if (speed <= minSpeed.getDouble() && deviation < 50.0) {
                    if (++buffer > 35) {
                        fail(String.format("speed=%.2f deviation=%.2f", speed, deviation));
                    }
                } else {
                    buffer /= 2;
                }
            }
            lastFlyingTime = now;
        }
    }
}
