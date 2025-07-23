package ai.medusa.anticheat.check.impl.player.timer;

import ai.medusa.anticheat.check.Check;
import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.config.ConfigValue;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;

@CheckInfo(name = "Timer (C)", description = "Balance based timer check.", experimental = true)
public final class TimerC extends Check {

    private static final ConfigValue maxBal = new ConfigValue(ConfigValue.ValueType.LONG, "maximum-balance");
    private static final ConfigValue balReset = new ConfigValue(ConfigValue.ValueType.LONG, "balance-reset");
    private static final ConfigValue balSubOnTp = new ConfigValue(
            ConfigValue.ValueType.LONG, "balance-subtraction-on-teleport"
    );
    private long lastFlyingTime = 0L;
    private long balance = 0L;

    public TimerC(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isFlying() && !isExempt(ExemptType.JOINED, ExemptType.TPS)) {
            if (lastFlyingTime != 0L) {
                final long now = now();
                balance += 50L;
                balance -= now - lastFlyingTime;
                if (balance > maxBal.getLong()) {
                    fail("balance=" + balance);
                    balance = balReset.getLong();
                }
            }
            lastFlyingTime = now();
        } else if (packet.isOutPosition()) {
            balance -= balSubOnTp.getLong();
        }
    }
}
