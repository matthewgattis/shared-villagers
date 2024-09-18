package org.iapetusservers.sharedVillagers;

import org.bukkit.plugin.java.JavaPlugin;

public final class SharedVillagers extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new PlayerVillagerGossipInteract(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
