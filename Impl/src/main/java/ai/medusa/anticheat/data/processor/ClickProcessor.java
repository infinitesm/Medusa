package ai.medusa.anticheat.data.processor;

import lombok.Getter;
import ai.medusa.anticheat.data.PlayerData;

@Getter
public final class ClickProcessor {

    private final PlayerData data;
    private long lastSwing = -1;
    private long delay;

    public ClickProcessor(final PlayerData data) {
        this.data = data;
    }

    public void handleArmAnimation() {
        if (!data.getActionProcessor().isDigging() && !data.getActionProcessor().isPlacing()) {
            if (lastSwing > 0) {
                delay = System.currentTimeMillis() - lastSwing;
            }
            lastSwing = System.currentTimeMillis();
        }
    }
}
