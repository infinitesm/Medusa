package ai.medusa.anticheat.check.impl.combat.aimassist;

import ai.medusa.api.check.CheckInfo;
import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import ai.medusa.anticheat.packet.Packet;
import ai.medusa.anticheat.util.MathUtil;
import ai.medusa.anticheat.util.type.EvictingList;

/**
 * Created on 11/15/2020 Package ai.medusa.anticheat.check.impl.combat.aim by infinitesm
 */

@CheckInfo(name = "AimAssist (G)" , description = "Checks for extremely smooth rotations.", experimental = true)
public final class AimAssistG extends Check {

    private final EvictingList<Float> yawAccelSamples = new EvictingList<>(20);
    private final EvictingList<Float> pitchAccelSamples = new EvictingList<>(20);

    public AimAssistG(final PlayerData data) {
        super(data);
    }

    @Override
    public void handle(final Packet packet) {
        if (packet.isRotation() && !isExempt(ExemptType.CINEMATIC)) {
            final float yawAccel = data.getRotationProcessor().getJoltYaw();
            final float pitchAccel = data.getRotationProcessor().getJoltPitch();

            yawAccelSamples.add(yawAccel);
            pitchAccelSamples.add(pitchAccel);

            if (yawAccelSamples.isFull() && pitchAccelSamples.isFull()) {
                final double yawAccelAverage = MathUtil.getAverage(yawAccelSamples);
                final double pitchAccelAverage = MathUtil.getAverage(pitchAccelSamples);

                final double yawAccelDeviation = MathUtil.getStandardDeviation(yawAccelSamples);
                final double pitchAccelDeviation = MathUtil.getStandardDeviation(pitchAccelSamples);

                final boolean exemptRotation = data.getRotationProcessor().getDeltaYaw() < 1.5F;

                final boolean averageInvalid = yawAccelAverage < 1 || pitchAccelAverage < 1 && !exemptRotation;
                final boolean deviationInvalid = yawAccelDeviation < 5 && pitchAccelDeviation > 5 && !exemptRotation;

                debug(String.format(
                        "yaa=%.2f, paa=%.2f, yad=%.2f, pad=%.2f, buf=%.2f",
                        yawAccelAverage, pitchAccelAverage, yawAccelDeviation, pitchAccelDeviation, buffer
                ));

                if (averageInvalid && deviationInvalid) {
                    buffer = Math.min(buffer + 1, 20);
                    if (buffer > 8) {
                        fail(String.format(
                                "yaa=%.2f, paa=%.2f, yad=%.2f, pad=%.2f",
                                yawAccelAverage, pitchAccelAverage, yawAccelDeviation, pitchAccelDeviation
                        ));
                    }
                } else {
                    buffer -= buffer > 0 ? 0.75 : 0;
                }
            }
        }
    }
}
