package ai.medusa.anticheat.data;

import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.data.processor.*;
import ai.medusa.anticheat.data.processor.*;
import ai.medusa.anticheat.data.processor.*;
import ai.medusa.anticheat.data.processor.*;
import ai.medusa.anticheat.util.type.EvictingList;
import ai.medusa.anticheat.util.type.Pair;
import lombok.Getter;
import lombok.Setter;
import ai.medusa.anticheat.exempt.ExemptProcessor;
import ai.medusa.anticheat.manager.CheckManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import java.util.List;

@Getter
@Setter
public final class PlayerData {

    private final Player player;
    private String clientBrand;
    private int totalViolations, combatViolations, movementViolations, playerViolations;
    private final long joinTime = System.currentTimeMillis();
    private final List<Check> checks = CheckManager.loadChecks(this);
    private final EvictingList<Pair<Location, Integer>> targetLocations = new EvictingList<>(40);

    private final ExemptProcessor exemptProcessor = new ExemptProcessor(this);
    private final CombatProcessor combatProcessor = new CombatProcessor(this);
    private final ActionProcessor actionProcessor = new ActionProcessor(this);
    private final ClickProcessor clickProcessor = new ClickProcessor(this);
    private final PositionProcessor positionProcessor = new PositionProcessor(this);
    private final RotationProcessor rotationProcessor = new RotationProcessor(this);
    private final VelocityProcessor velocityProcessor = new VelocityProcessor(this);

    public PlayerData(final Player player) {
        this.player = player;
    }
}
