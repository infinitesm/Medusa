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

@CheckInfo(name = "Timer (A)", description = "Checks for game speed which is too fast.")
public final class TimerA extends Check {

    private static final ConfigValue maxTimerSpeed = new ConfigValue(ConfigValue.ValueType.DOUBLE, "max-timer-speed");
    private final EvictingList<Long> samples = new EvictingList<>(50);
    private long lastFlyingTime;

    public TimerA(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying() && !isExempt(ExemptType.TPS)) {
            final long now = now();
            final long delta = now - lastFlyingTime;

            if (delta > 1) {
                samples.add(delta);
            }

            if (samples.isFull()) {
                final double average = MathUtil.getAverage(samples);
                final double speed = 50 / average;

                debug(String.format("speed=%.4f, delta=%o, buffer=%.2f", speed, delta, buffer));

                if (speed >= maxTimerSpeed.getDouble()) {
                    if (++buffer > 30) {
                        buffer = Math.min(buffer, 50);
                        fail(String.format("speed=%.4f, delta=%o, buffer=%.2f", speed, delta, buffer));
                    }
                } else {
                    buffer = Math.max(0, buffer - 1);
                }
            }

            lastFlyingTime = now;
        } else if (packet.isOutPosition()) {
            samples.add(135L); //Magic value. 100L doesn't completely fix it for some reason.
        }
    }
}
