package fr.fingarde.atharion.survie.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class VillagerFeedListener implements Listener {
    @EventHandler
    public void onFeed(PlayerInteractAtEntityEvent event) {
        if(event.getHand() != EquipmentSlot.HAND) return;
        if(event.getRightClicked().getType() != EntityType.VILLAGER) return;
        if(event.getPlayer().getInventory().getItemInMainHand().getType() != Material.HAY_BLOCK) return;

        ((Villager) event.getRightClicked().getLocation().getWorld().spawnEntity(event.getRightClicked().getLocation(), EntityType.VILLAGER)).setBaby();
        event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
    }
}
