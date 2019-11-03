package fr.fingarde.atharion.survie.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerListener implements Listener
{
    @EventHandler
    public void onDeath(PlayerDeathEvent event)
    {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta meta = item.getItemMeta();

        ((SkullMeta) meta).setOwningPlayer(event.getEntity());

        item.setItemMeta(meta);

        event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), item);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if(event.getTo().getWorld().getName().equalsIgnoreCase("world_the_end") && event.getTo().getY() < -30) {
            event.getPlayer().teleport(new Location(Bukkit.getWorld("world"), event.getTo().getX(), 270, event.getTo().getZ()));
        }

        if(event.getTo().getWorld().getName().equalsIgnoreCase("world") && event.getTo().getY() > 280) {
            event.getPlayer().teleport(new Location(Bukkit.getWorld("world_the_end"), event.getTo().getX(), -10, event.getTo().getZ()));
        }
    }
}
