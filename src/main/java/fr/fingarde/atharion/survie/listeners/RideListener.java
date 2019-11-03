package fr.fingarde.atharion.survie.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.spigotmc.event.entity.EntityMountEvent;

public class RideListener implements Listener
{

    @EventHandler
    public void onMount(EntityMountEvent event)
    {
        if(event.getMount().getVehicle() != null && event.getMount().getVehicle().getType() == EntityType.PLAYER)
        {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event)
    {
        if(event.getHand() == EquipmentSlot.OFF_HAND) return;

        switch (event.getRightClicked().getType())
        {
            case PLAYER:
                if(!((Player)event.getRightClicked()).isSneaking()) return;
                if(event.getPlayer().isInsideVehicle() && event.getPlayer().getVehicle().getType() == EntityType.PLAYER) return;
                if(event.getPlayer().getPassengers().size() < 0) return;

                event.getRightClicked().addPassenger(event.getPlayer());
                break;
            case VILLAGER:
            case CAT:
            case CHICKEN:
            case COW:
            case FOX:
            case PIG:
            case MULE:
            case WOLF:
            case LLAMA:
            case HORSE:
            case PANDA:
            case SHEEP:
            case DONKEY:
            case OCELOT:
            case RABBIT:
            case PARROT:
            case TURTLE:
            case SNOWMAN:
            case TRADER_LLAMA:
            case MUSHROOM_COW:
            case SKELETON_HORSE:
            case ZOMBIE_HORSE:
                event.setCancelled(true);

                if(!event.getPlayer().isSneaking()) return;

                if(event.getRightClicked().getVehicle() != null && event.getRightClicked().getVehicle().getType() == EntityType.PLAYER && (event.getPlayer() == event.getRightClicked().getVehicle()))
                {
                    event.getPlayer().eject();
                    break;
                }

                if(event.getPlayer().getPassengers().size() > 0) return;

                event.getPlayer().addPassenger(event.getRightClicked());

                break;
        }
    }

    @EventHandler
    public void onSuffocate(EntityDamageEvent event)
    {
        if(event.getEntity().getVehicle() == null) return;
        if(event.getEntity().getVehicle().getType() != EntityType.PLAYER) return;
        if(event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) event.setCancelled(true);
    }
}
