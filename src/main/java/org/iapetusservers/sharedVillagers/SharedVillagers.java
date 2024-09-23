package org.iapetusservers.sharedVillagers;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SharedVillagers extends JavaPlugin {

    private Settings settings = new Settings();
    private Console console = new Console(() -> settings.getDebug());

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(
                new PlayerVillagerGossipInteract(console), this);

        Objects.requireNonNull(this.getCommand("configure")).setExecutor(
                new Configure(() -> settings, x -> settings = x));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
