package org.iapetusservers.sharedVillagers;

import com.google.gson.JsonArray;
import de.tr7zw.nbtapi.NBT;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlayerVillagerGossipInteract implements Listener {

    @EventHandler
    public void onPlayerEntityInteract(PlayerInteractEntityEvent event) throws IOException {
        Player player = event.getPlayer();

        DebugLog debug = new DebugLog();

        try {
            debug.getLog().addProperty("event", "PlayerInteractEntityEvent");

            Entity entity = event.getRightClicked();
            if (entity.getType() != EntityType.VILLAGER)
                return;

            debug.getLog().addProperty("villagerUuid", entity.getUniqueId().toString());

            // List of player UUIDs in villager "gossips" with "major_positive" type.
            List<UUID> uuids = NBT.get(entity, nbt -> {
                return nbt.getCompoundList("Gossips").toListCopy().stream()
                        .filter(x -> Objects.equals(x.getString("Type"), "major_positive"))
                        .map(x -> x.getUUID("Target")).toList();
            });

            {
                JsonArray arr = new JsonArray();
                uuids.stream().map(UUID::toString).forEach(arr::add);
                debug.getLog().add("playerUuids", arr);
            }

            boolean hasMajorPositive = !uuids.isEmpty();

            debug.getLog().addProperty("hasMajorPositive", hasMajorPositive);

            if (!hasMajorPositive)
                return;

            UUID playerUuid = event.getPlayer().getUniqueId();

            debug.getLog().addProperty("playerUuid", playerUuid.toString());

            // See if we (player) are in the list.
            boolean playerMatch = uuids.stream()
                    .anyMatch(x -> x.equals(playerUuid));

            debug.getLog().addProperty("playerMatch", playerMatch);

            if (!playerMatch) {
                NBT.modify(entity, nbt -> {
                            var gossip = nbt.getCompoundList("Gossips").addCompound();
                            gossip.setUUID("Target", playerUuid);
                            gossip.setString("Type", "major_positive");
                            gossip.setInteger("Value", 20);
                        }
                );

                debug.addMessage("Adding major positive.");

                boolean added = NBT.get(entity, nbt -> {
                    return nbt.getCompoundList("Gossips").toListCopy().stream()
                            .filter(x -> Objects.equals(x.getString("Type"), "major_positive"))
                            .map(x -> x.getUUID("Target"))
                            .filter(Objects::nonNull)
                            .anyMatch(x -> x.equals(playerUuid));
                });

                debug.getLog().addProperty("added", added);

            } else
                debug.addMessage("Major positive already added.");

        } finally {
            player.sendMessage(debug.getJson());

        }
    }
}
