package ai.medusa.anticheat.manager;

import ai.medusa.anticheat.check.Check;
import ai.medusa.anticheat.check.impl.combat.aimassist.*;
import ai.medusa.anticheat.check.impl.combat.killaura.*;
import ai.medusa.anticheat.check.impl.movement.motion.*;
import ai.medusa.anticheat.check.impl.player.protocol.*;
import ai.medusa.anticheat.check.impl.movement.speed.SpeedA;
import ai.medusa.anticheat.check.impl.movement.speed.SpeedB;
import ai.medusa.anticheat.check.impl.combat.autoclicker.AutoClickerA;
import ai.medusa.anticheat.check.impl.combat.autoclicker.AutoClickerB;
import ai.medusa.anticheat.check.impl.combat.autoclicker.AutoClickerC;
import ai.medusa.anticheat.check.impl.combat.autoclicker.AutoClickerD;
import ai.medusa.anticheat.check.impl.combat.reach.ReachA;
import ai.medusa.anticheat.check.impl.combat.hitbox.HitBoxA;
import ai.medusa.anticheat.check.impl.combat.reach.ReachB;
import ai.medusa.anticheat.check.impl.combat.velocity.VelocityA;
import ai.medusa.anticheat.check.impl.movement.fastclimb.FastClimbA;
import ai.medusa.anticheat.check.impl.movement.fly.FlyA;
import ai.medusa.anticheat.check.impl.movement.fly.FlyB;
import ai.medusa.anticheat.check.impl.movement.fly.FlyC;
import ai.medusa.anticheat.check.impl.movement.jesus.JesusA;
import ai.medusa.anticheat.check.impl.movement.jesus.JesusB;
import ai.medusa.anticheat.check.impl.movement.noslow.NoSlowA;
import ai.medusa.anticheat.check.impl.movement.phase.PhaseA;
import ai.medusa.anticheat.check.impl.movement.speed.SpeedC;
import ai.medusa.anticheat.check.impl.player.hand.HandA;
import ai.medusa.anticheat.check.impl.player.inventory.InventoryA;
import ai.medusa.anticheat.check.impl.player.inventory.InventoryB;
import ai.medusa.anticheat.check.impl.player.scaffold.ScaffoldB;
import ai.medusa.anticheat.check.impl.player.scaffold.ScaffoldA;
import ai.medusa.anticheat.check.impl.player.scaffold.ScaffoldC;
import ai.medusa.anticheat.check.impl.player.scaffold.ScaffoldD;
import ai.medusa.anticheat.check.impl.player.timer.TimerA;
import ai.medusa.anticheat.check.impl.player.timer.TimerB;
import ai.medusa.anticheat.check.impl.player.timer.TimerC;
import ai.medusa.anticheat.config.Config;
import ai.medusa.anticheat.data.PlayerData;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public final class CheckManager {

    public static final Class<?>[] CHECKS = new Class[] {
            AimAssistA.class,
            AimAssistB.class,
            AimAssistC.class,
            AimAssistD.class,
            AimAssistE.class,
            AimAssistF.class,
            AimAssistG.class,
            AimAssistH.class,
            AimAssistI.class,
            AutoClickerA.class,
            AutoClickerB.class,
            AutoClickerC.class,
            AutoClickerD.class,
            KillAuraA.class,
            KillAuraB.class,
            KillAuraC.class,
            KillAuraD.class,
            KillAuraE.class,
            KillAuraF.class,
            KillAuraG.class,
            ReachA.class,
            HitBoxA.class,
            ReachB.class,
            VelocityA.class,
            FastClimbA.class,
            FlyA.class,
            FlyB.class,
            FlyC.class,
            JesusA.class,
            JesusB.class,
            MotionA.class,
            MotionB.class,
            MotionC.class,
            MotionD.class,
            SpeedA.class,
            SpeedB.class,
            MotionE.class,
            SpeedC.class,
            NoSlowA.class,
            PhaseA.class,
            TimerA.class,
            TimerB.class,
            TimerC.class,
            HandA.class,
            InventoryA.class,
            InventoryB.class,
            ScaffoldA.class,
            ScaffoldB.class,
            ScaffoldC.class,
            ScaffoldD.class,
            ProtocolA.class,
            ProtocolB.class,
            ProtocolC.class,
            ProtocolD.class,
            ProtocolE.class,
            ProtocolF.class,
            ProtocolG.class,
            ProtocolH.class,
            ProtocolI.class,
            ProtocolJ.class
    };

    private static final List<Constructor<?>> CONSTRUCTORS = new ArrayList<>();

    public static List<Check> loadChecks(final PlayerData data) {
        final List<Check> checkList = new ArrayList<>();
        for (Constructor<?> constructor : CONSTRUCTORS) {
            try {
                checkList.add((Check) constructor.newInstance(data));
            } catch (Exception exception) {
                System.err.println("Failed to load checks for " + data.getPlayer().getName());
                exception.printStackTrace();
            }
        }
        return checkList;
    }

    public static void setup() {
        for (Class<?> clazz : CHECKS) {
            if (Config.ENABLED_CHECKS.contains(clazz.getSimpleName())) {
                try {
                    CONSTRUCTORS.add(clazz.getConstructor(PlayerData.class));
                    //Bukkit.getLogger().info(clazz.getSimpleName() + " is enabled!");
                } catch (NoSuchMethodException exception) {
                    exception.printStackTrace();
                }
            } else {
                Bukkit.getLogger().info(clazz.getSimpleName() + " is disabled!");
            }
        }
    }
}

