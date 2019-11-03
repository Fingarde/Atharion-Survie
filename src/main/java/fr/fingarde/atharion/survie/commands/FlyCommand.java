package fr.fingarde.atharion.survie.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor
{
    String usage = "§bUsage: §r/fly §7[player] [on|off]";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0)
        {

            Player player = (Player) sender;

            String state = "activé";
            if (player.getAllowFlight()) { state = "désactivé"; }

            player.setAllowFlight(!player.getAllowFlight());

            player.sendMessage("§aFly mode §e" + state + "§a.");

            return true;
        }
        else
        {
            sender.sendMessage(usage);
            return false;
        }
    }
}
