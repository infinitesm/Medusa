package ai.medusa.anticheat.exempt;

import ai.medusa.anticheat.data.PlayerData;
import ai.medusa.anticheat.exempt.type.ExemptType;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.function.Function;

@RequiredArgsConstructor
public final class ExemptProcessor {

    private final PlayerData data;

    public boolean isExempt(final ExemptType exemptType) {
        return exemptType.getException().apply(data);
    }

    public boolean isExempt(final ExemptType... exemptTypes) {
        return Arrays.stream(exemptTypes).anyMatch(this::isExempt);
    }

    public boolean isExempt(final Function<PlayerData, Boolean> exception) {
        return exception.apply(data);
    }
}
