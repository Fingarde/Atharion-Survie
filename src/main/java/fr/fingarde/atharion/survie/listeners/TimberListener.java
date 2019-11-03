package fr.fingarde.atharion.survie.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TimberListener implements Listener
{
    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        if(!event.getBlock().getType().name().contains("_LOG")) return;

        if(event.getPlayer() == null) return;
        if(!event.getPlayer().isSneaking()) return;
        if(event.getPlayer().getInventory().getItemInMainHand() == null) return;

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        if(!item.getType().name().contains("_AXE")) return;
        if(item.getItemMeta() == null) return;


        ArrayList<Block> blocksToCheck = new ArrayList<Block>();
        ArrayList<Block> blocksToBreak = new ArrayList<Block>();

        blocksToCheck.add(event.getBlock());

        while (blocksToCheck.size() > 0)
        {
            for(int y = 0 ; y < 2 ; y++)
            {
                for(int x = -1 ; x < 2 ;x++)
                {
                    for(int z = -1 ; z < 2 ; z++)
                    {
                        Block blockToCheck = blocksToCheck.get(0).getRelative(x, y, z);
                        if(blockToCheck.getType() == event.getBlock().getType())
                        {
                            if(event.getBlock().getLocation().distance(blockToCheck.getLocation()) > 30) continue;
                            if(blocksToBreak.contains(blockToCheck)) continue;
                            if(blocksToCheck.contains(blockToCheck)) continue;

                            blocksToCheck.add(blockToCheck);
                        }
                    }
                }
            }

            blocksToBreak.add(blocksToCheck.get(0));
            blocksToCheck.remove(blocksToCheck.get(0));
        }

        int newDurability = ((Damageable)item.getItemMeta()).getDamage() + blocksToBreak.size();

        Damageable meta = (Damageable) item.getItemMeta();

        meta.setDamage(newDurability);

        item.setItemMeta((ItemMeta) meta);

        for(Block block : blocksToBreak)
        {
            block.breakNaturally(item);
        }
    }
}
