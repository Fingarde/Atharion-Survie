package fr.fingarde.atharion.survie.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

public class CropListener implements Listener
{

    @EventHandler
    public void onJump(PlayerInteractEvent event)
    {
        if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.FARMLAND)
            event.setCancelled(true);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event)
    {
        if(event.getClickedBlock() == null) return;
        if(!isCrop(event.getClickedBlock().getType())) return;

        Block block = event.getClickedBlock();
        Ageable ageable = (Ageable) event.getClickedBlock().getBlockData();

        if(ageable.getAge() != ageable.getMaximumAge()) return;

        Location location = event.getClickedBlock().getLocation();

        for(ItemStack item : block.getDrops(event.getItem()))
        {
            location.getWorld().dropItemNaturally(location, item);
        }


        ageable.setAge(0);

        event.getClickedBlock().setBlockData(ageable);
    }

    private boolean isCrop(Material material)
    {
        switch (material)
        {
            case WHEAT:
            case POTATOES:
            case CARROTS:
            case BEETROOTS:
                return true;
        }

        return false;
    }
}
