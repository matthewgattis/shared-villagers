package org.iapetusservers.sharedVillagers;

import com.google.gson.JsonArray;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlayerVillagerGossipInteract implements Listener {

    private final Console console;

    public PlayerVillagerGossipInteract(Console console) {
        this.console = console;
    }

    @EventHandler
    public void onPlayerEntityInteract(PlayerInteractEntityEvent event) throws IOException {
        Entity entity = event.getRightClicked();
        if (entity.getType() != EntityType.VILLAGER)
            return;

        if (console.getSettings().get().showDebug())
            showDebugInfo(event);

        Villager villager = (Villager) entity;

        List<UUID> uuids = getVillagerGossipMajorPositivePlayerUuids(villager);

        if (uuids.isEmpty())
            return;

        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();
        boolean playerMatch = uuids.contains(playerUuid);

        if (!playerMatch && player.hasPermission("sharedvillagers.sharecure"))
            addVillagerGossipMajorPositiveForPlayerUuid(villager, playerUuid);
    }

    public List<UUID> getVillagerGossipMajorPositivePlayerUuids(Villager villager) {
        return NBT.get(villager, nbt -> {
            return nbt.getCompoundList("Gossips").toListCopy().stream()
                    .filter(x -> Objects.equals(x.getString("Type"), "major_positive"))
                    .map(x -> x.getUUID("Target")).toList();
        });
    }

    public void addVillagerGossipMajorPositiveForPlayerUuid(Villager villager, UUID playerUuid) {
        NBT.modify(villager, nbt -> {
                    var gossip = nbt.getCompoundList("Gossips").addCompound();
                    gossip.setUUID("Target", playerUuid);
                    gossip.setString("Type", "major_positive");
                    gossip.setInteger("Value", 20);
                }
        );
    }

    private void showDebugInfo(PlayerInteractEntityEvent event) throws IOException {
        Debug debug = new Debug();

        Player player = event.getPlayer();

        try {
            debug.log("event", "PlayerInteractEntityEvent");

            Villager villager = (Villager) event.getRightClicked();
            debug.log("villagerUuid", villager.getUniqueId().toString());

            List<UUID> uuids = getVillagerGossipMajorPositivePlayerUuids(villager);
            {
                JsonArray arr = new JsonArray();
                uuids.forEach(x -> arr.add(x.toString()));
                debug.log("majorPositiveUuids", arr);
            }

            UUID playerUuid = player.getUniqueId();
            debug.log("playerUuid", playerUuid.toString());

            debug.log("permission", player.hasPermission("sharedvillagers.sharecure"));

            debug.log("containsPlayer", uuids.contains(playerUuid));

        } finally {
            console.write(debug.toJson());
        }
    }
}
