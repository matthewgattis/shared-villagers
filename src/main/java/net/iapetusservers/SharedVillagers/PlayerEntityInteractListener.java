package net.iapetusservers.SharedVillagers;

import com.destroystokyo.paper.entity.villager.Reputation;
import com.destroystokyo.paper.entity.villager.ReputationType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.UUID;

public class PlayerEntityInteractListener implements Listener
{
    @EventHandler
    public void onPlayerEntityInteract(PlayerInteractEntityEvent event)
    {
        UUID uuid = event.getPlayer().getUniqueId();
        Entity entity = event.getRightClicked();

        if (entity.getType() == EntityType.VILLAGER)
        {
            Villager villager = (Villager) entity;

            if (villager.getReputation(uuid).getReputation(ReputationType.MAJOR_POSITIVE) != 0)
                return;

            boolean setMajorPositive = villager.getReputations().entrySet().stream()
                .filter(x -> !x.getKey().equals(uuid))
                .anyMatch(x ->
                    x.getValue().getReputation(ReputationType.MAJOR_POSITIVE) != 0);

            if (setMajorPositive)
            {
                Reputation reputation = villager.getReputation(uuid);
                reputation.setReputation(ReputationType.MAJOR_POSITIVE, 20);
                villager.setReputation(uuid, reputation);
            }
        }
    }
}
