package org.iapetusservers.sharedVillagers;

import java.util.HashSet;
import java.util.UUID;

public class DebugSettings {

    private boolean console = false;

    private HashSet<UUID> players = new HashSet<>();

    boolean showDebug() {
        return isConsole() || !getPlayers().isEmpty();
    }

    public boolean isConsole() {
        return console;
    }

    public void setConsole(boolean console) {
        this.console = console;
    }

    public HashSet<UUID> getPlayers() {
        return players;
    }

    public void setPlayers(HashSet<UUID> players) {
        this.players = players;
    }
}
