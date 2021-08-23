package net.iapetusservers.SharedVillagers;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * SharedVillagers
 * Shares cured villager benefits with all players.
 *
 * @author iapetusservers
 * @version 0.1
 * @since 0.1
 */
public class PluginEntry extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(
                new PlayerEntityInteractListener(), this);
    }

    @Override
    public void onDisable()
    {
    }
}
