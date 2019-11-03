package fr.fingarde.atharion.survie.commands;

import fr.fingarde.atharion.survie.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class NickCommand implements CommandExecutor, TabCompleter
{
    String usage = "§bUsage: §r/nick §a<nick|reset> \n§r/nick §a<player> §7[nick|reset]";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length >= 1)
        {
            Player victim = null;

            if (Bukkit.getPlayer(args[0]) != null)
            {
                victim = Bukkit.getPlayer(args[0]);
            }

            if(victim == null) { victim = (Player) sender; }

            if (victim != sender && !sender.isOp()) { sender.sendMessage("&cVous n'avez pas la permission"); return false; }

            String nick = "";
            for (int i = 0; i < args.length; i++)
            {
                if(victim != sender && i == 0) continue;

                nick += " " + args[i];
            }

            if(nick == "") {  sender.sendMessage(usage); return false; }

            nick = nick.substring(1);
            nick = nick.replaceAll("&", "§");

            boolean silent = false;
            if (nick.endsWith(" -s")) { silent = true ; nick = nick.substring(0, nick.length() - 3); }

            if (nick.equalsIgnoreCase("reset")) { nick = ""; }
            User user = User.getFromUUID(victim.getUniqueId());

            sender.sendMessage("§aLe surnom de §e" + victim.getDisplayName() + "§a a été défini sur §e" + nick + "§a.");

            user.setNickname(nick);

            if (victim != sender)
            {
                String name = (sender instanceof Player) ? ((Player) sender).getDisplayName() : sender.getName();

                if (!silent) { victim.sendMessage("§aVotre surnom a été défini sur §e" + nick + "§a par §e" + name); }
            }

            return true;
        }
        else
        {
            sender.sendMessage(usage);
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        List<String> value = null;

        if (args.length == 1)
        {
            value = new ArrayList<>();
            List<String> args0Completer = new ArrayList<>();

            for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            {
                args0Completer.add(onlinePlayer.getName());
            }

            args0Completer.add("RESET");

            if (args[0].length() == 0)
            {
                value = args0Completer;
            }
            else
            {
                for (String args0 : args0Completer)
                {
                    if (args0.toLowerCase().startsWith(args[0].toLowerCase()))
                    {
                        value.add(args0);
                    }
                }
            }
        }

        return value;
    }
}
