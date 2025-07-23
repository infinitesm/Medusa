package ai.medusa.exampleapi;

import ai.medusa.exampleapi.listener.ExampleListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(new ExampleListener(), this);
        Bukkit.broadcastMessage("Started ExamplePlugin using MedusaAPI");
    }

    @Override
    public void onDisable() {
    }
}
