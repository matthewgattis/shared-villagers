package org.iapetusservers.sharedVillagers;

import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.util.Objects;
import java.util.function.Supplier;

public class Console {

    private final Supplier<DebugSettings> settings;

    public Console(Supplier<DebugSettings> debugSettings) {
        this.settings = debugSettings;
    }

    public void write(String message) {
        if (message.isEmpty())
            return;

        if (getSettings().get().isConsole())
            Bukkit.getConsoleSender().sendMessage("§7" + message);

        if (!getSettings().get().getPlayers().isEmpty()) {
            Server server = Bukkit.getServer();

            getSettings().get().getPlayers().stream()
                    .map(server::getPlayer)
                    .filter(Objects::nonNull)
                    .forEach(x -> x.sendMessage("§7" + message));
        }
    }

    public Supplier<DebugSettings> getSettings() {
        return settings;
    }
}
