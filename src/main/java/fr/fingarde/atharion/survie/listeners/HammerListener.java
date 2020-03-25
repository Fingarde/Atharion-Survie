package fr.fingarde.atharion.survie.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class HammerListener implements Listener {
    private static HashMap<Player, BlockFace> map = new HashMap<>();

    @EventHandler
    public void onClick(PlayerInteractEvent event)
    {
        if(event.getBlockFace() == null) return;
       map.put(event.getPlayer(), event.getBlockFace());
    }


    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer() == null) return;

        if (event.getPlayer().getInventory().getItemInMainHand() == null) return;

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        if (item.getItemMeta() == null) return;

        ItemMeta meta = item.getItemMeta();

        if (!meta.getLocalizedName().contains("hammer") && !meta.getLocalizedName().contains("excavator")) return;

        Block block = event.getBlock();
        Player player = event.getPlayer();
        Location location = block.getLocation();
        if (!map.containsKey(player)) return;

        ArrayList<Block> blockToBreaks = new ArrayList<>();
        switch (map.get(player)) {
            case UP:
            case DOWN:
                for (int x = -1; x < 2; x++) {
                    for (int z = -1; z < 2; z++) {
                        blockToBreaks.add(location.getWorld().getBlockAt(location.getBlockX() + x, location.getBlockY(), location.getBlockZ() + z));
                    }
                }
                break;
            case EAST:
            case WEST:
                for (int y = -1; y < 2; y++) {
                    for (int z = -1; z < 2; z++) {
                        blockToBreaks.add(location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + y, location.getBlockZ() + z));
                    }
                }
                break;
            case NORTH:
            case SOUTH:
                for (int y = -1; y < 2; y++) {
                    for (int x = -1; x < 2; x++) {
                        blockToBreaks.add(location.getWorld().getBlockAt(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ()));
                    }
                }
                break;
        }

        blockToBreaks.remove(block);

        for (Block blockToBreak : blockToBreaks) {
            switch (block.getType()) {
                case DIRT:
                case GRASS_BLOCK:
                    switch (blockToBreak.getType()) {
                        case DIRT:
                        case GRASS_BLOCK:
                            blockToBreak.breakNaturally(item);
                            break;
                    }
                    break;
                case ANDESITE:
                case DIORITE:
                case GRANITE:
                case STONE:
                case IRON_ORE:
                case COAL_ORE:
                case DIAMOND_ORE:
                case EMERALD_ORE:
                case GOLD_ORE:
                case LAPIS_ORE:
                case REDSTONE_ORE:
                    switch (blockToBreak.getType()) {
                        case ANDESITE:
                        case DIORITE:
                        case GRANITE:
                        case STONE:
                        case IRON_ORE:
                        case COAL_ORE:
                        case DIAMOND_ORE:
                        case EMERALD_ORE:
                        case GOLD_ORE:
                        case LAPIS_ORE:
                        case REDSTONE_ORE:
                            blockToBreak.breakNaturally(item);
                            break;
                    }
                    break;
                default:
                    if (block.getType().equals(blockToBreak.getType())) blockToBreak.breakNaturally(item);
                    break;
            }
        }
    }
}
