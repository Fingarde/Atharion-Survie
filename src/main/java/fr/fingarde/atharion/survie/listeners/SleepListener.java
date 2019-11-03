package fr.fingarde.atharion.survie.listeners;

import fr.fingarde.atharion.survie.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class SleepListener implements Listener
{
    ArrayList<Player> sleepers = new ArrayList<Player>();

    @EventHandler
    public void onSleep(PlayerBedEnterEvent event)
    {
        if(event.isCancelled()) return;

        Player player = event.getPlayer();
        int nbPlayer = player.getWorld().getPlayers().size();

        sleepers.add(player);
        int percent = Math.round(((float) sleepers.size() / (float) nbPlayer) * 100);

        Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.RED + " est en train de dormir (" + sleepers.size() + "/" + nbPlayer + " - " + ChatColor.YELLOW + percent + "%" + ChatColor.RED + ")");

        if(percent >= 50)
        {
            Bukkit.broadcastMessage(ChatColor.AQUA + "Il y a assez de joueurs qui dorment pour passer la nuit");

            for(Player onlinePlayer : Bukkit.getOnlinePlayers())
            {
                if(onlinePlayer.isSleeping()) onlinePlayer.setStatistic(Statistic.TIME_SINCE_REST, 0);
            }

            final World world = event.getPlayer().getWorld();

            world.setStorm(false);
            world.setThundering(false);

            new BukkitRunnable()
            {
                @Override
                public void run()
                {
                    if(sleepers.size() <= 0)
                    {
                        sleepers.clear();
                        this.cancel();
                        return;
                    }

                    world.setFullTime(world.getFullTime() + 750);
                }
            }.runTaskTimer(Main.getInstance(), 0 , 5);
        }
    }

    @EventHandler
    public void onExitSleep(PlayerBedLeaveEvent event)
    {
        if(sleepers.contains(event.getPlayer())) sleepers.remove(event.getPlayer());
    }
}
