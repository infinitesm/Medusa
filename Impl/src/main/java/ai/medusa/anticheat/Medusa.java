package ai.medusa.anticheat;

import ai.medusa.anticheat.listener.BukkitEventListener;
import ai.medusa.anticheat.listener.ClientBrandListener;
import ai.medusa.anticheat.listener.NetworkListener;
import ai.medusa.anticheat.listener.JoinQuitListener;
import ai.medusa.anticheat.manager.CheckManager;
import ai.medusa.anticheat.manager.PlayerDataManager;
import ai.medusa.anticheat.manager.ThemeManager;
import ai.medusa.anticheat.manager.TickManager;
import ai.medusa.anticheat.manager.*;
import io.github.retrooper.packetevents.PacketEvents;
import lombok.Getter;
import ai.medusa.anticheat.command.CommandManager;
import ai.medusa.anticheat.config.Config;
import ai.medusa.anticheat.packet.processor.ReceivingPacketProcessor;
import ai.medusa.anticheat.packet.processor.SendingPacketProcessor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.messaging.Messenger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public enum Medusa {

    INSTANCE;

    private MedusaPlugin plugin;

    private long startTime;

    private final TickManager tickManager = new TickManager();
    private final ReceivingPacketProcessor receivingPacketProcessor = new ReceivingPacketProcessor();
    private final SendingPacketProcessor sendingPacketProcessor = new SendingPacketProcessor();
    private final PlayerDataManager playerDataManager = new PlayerDataManager();
    private final CommandManager commandManager = new CommandManager(this.getPlugin());
    private final ExecutorService packetExecutor = Executors.newSingleThreadExecutor();

    public void start(final MedusaPlugin plugin) {
        this.plugin = plugin;
        assert plugin != null : "Error while starting Medusa.";

        this.getPlugin().saveDefaultConfig();
        Config.updateConfig();

        CheckManager.setup();
        ThemeManager.setup();

        Bukkit.getOnlinePlayers().forEach(player -> this.getPlayerDataManager().add(player));

        getPlugin().saveDefaultConfig();
        getPlugin().getCommand("medusa").setExecutor(commandManager);

        tickManager.start();

        final Messenger messenger = Bukkit.getMessenger();
        messenger.registerIncomingPluginChannel(plugin, "MC|Brand", new ClientBrandListener());

        startTime = System.currentTimeMillis();

        Bukkit.getServer().getPluginManager().registerEvents(new BukkitEventListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new ClientBrandListener(), plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new JoinQuitListener(), plugin);

        PacketEvents.get().registerListener(new NetworkListener());
    }

    public void stop(final MedusaPlugin plugin) {
        this.plugin = plugin;
        assert plugin != null : "Error while shutting down Medusa.";

        tickManager.stop();
    }
}
