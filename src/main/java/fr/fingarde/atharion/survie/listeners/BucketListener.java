package fr.fingarde.atharion.survie.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class BucketListener implements Listener {
    @EventHandler
    public void onUse(PlayerBucketFillEvent event)
    {
        if(event.getPlayer() == null) return;

        if(event.getPlayer().getInventory().getItemInMainHand() == null) return;

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();


        if(item.getItemMeta() == null) return;

        ItemMeta meta = item.getItemMeta();

        if (meta.getLore() == null) return;
        if(!meta.getLocalizedName().equalsIgnoreCase("extra_bucket")) return;

        String modeLigne = null;
        for (String ligne : meta.getLore())
        {
            if (ligne.startsWith("§rMode: "))
            {
                modeLigne = ligne;
                break;
            }
        }

        if (modeLigne == null) return;

        for (int i = 0; i < modeLigne.length(); i++)
        {
            if (modeLigne.charAt(i) == '§')
            {
                modeLigne = modeLigne.replaceAll(modeLigne.substring(i, i + 2), "");
            }
        }

        int mode = Integer.valueOf(modeLigne.split("x")[2]) / 2;

        if(event.getBlockClicked() == null) return; // PB
        Block block = event.getBlockClicked();

        event.setCancelled(true);

        Location location = block.getLocation();
        ArrayList<Block> blockToBreaks = new ArrayList<>();

        for(int x = -mode; x < (mode + 1); x++)
        {
            for(int z = -mode; z < (mode + 1); z++)
            {
                for(int y = -mode; y < (mode + 1); y++)
                {
                    blockToBreaks.add(location.getWorld().getBlockAt(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z));
                }
            }
        }

        Material blockType = block.getType();

        for(Block blockToBreak : blockToBreaks)
        {
            switch (blockType)
            {
                case LAVA:
                    switch (blockToBreak.getType())
                    {
                        case LAVA:
                            blockToBreak.setType(Material.AIR);
                    }
                case WATER:
                case KELP:
                case KELP_PLANT:
                case SEAGRASS:
                case TALL_SEAGRASS:
                    switch (blockToBreak.getType())
                    {
                        case WATER:
                        case KELP:
                        case KELP_PLANT:
                        case SEAGRASS:
                        case TALL_SEAGRASS:
                            blockToBreak.setType(Material.AIR);
                    }
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event)
    {
        if(!event.getPlayer().isSneaking()) return;
        if(event.getItem() == null) return;
        if(event.getItem().getType() != Material.BUCKET) return;

        ItemStack item = event.getItem();

        if(item.getItemMeta() == null) return;

        ItemMeta meta = item.getItemMeta();

        if (meta.getLore() == null) return;

        String modeLigne = null;
        for (String ligne : meta.getLore())
        {
            if (ligne.startsWith("§rMode: "))
            {
                modeLigne = ligne;
                break;
            }
        }

        if (modeLigne == null) return;

        for (int i = 0; i < modeLigne.length(); i++)
        {
            if (modeLigne.charAt(i) == '§')
            {
                modeLigne = modeLigne.replaceAll(modeLigne.substring(i, i + 2), "");
            }
        }

        String mode = modeLigne.split("x")[2];
        String newMode = null;

        switch (mode)
        {
            case "3":
                newMode = "5";
                break;
            case "5":
                newMode = "9";
                break;
            case "9":
                newMode = "3";
                break;
            default:
                newMode = "3";
        }

        ArrayList<String> lore = new ArrayList<>();

        for (String ligne : item.getItemMeta().getLore())
        {
            if (ligne.startsWith("§rMode: "))
            {
                lore.add("§rMode: §e" + newMode + "x" + newMode + "x" + newMode);
            } else
            {
                lore.add(ligne);
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

    }
}
