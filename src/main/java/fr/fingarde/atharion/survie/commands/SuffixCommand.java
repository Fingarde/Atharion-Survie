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

public class SuffixCommand implements CommandExecutor, TabCompleter
{
    String usage = "§bUsage: §r/suffix §a<suffix|reset> \n§r/suffix §a<player> §7[suffix|reset]";

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

            String suffix = "";
            for (int i = 0; i < args.length; i++)
            {
                if(victim != sender && i == 0) continue;

                suffix += " " + args[i];
            }

            if(suffix == "") {  sender.sendMessage(usage); return false; }

            suffix = suffix.substring(1);
            suffix = suffix.replaceAll("&", "§");

            boolean silent = false;
            if (suffix.endsWith(" -s")) { silent = true ; suffix = suffix.substring(0, suffix.length() - 3); }


            if (suffix.equalsIgnoreCase("reset")) { suffix = ""; }
            User user = User.getFromUUID(victim.getUniqueId());

            sender.sendMessage("§aLe suffix de §e" + victim.getDisplayName() + "§a a été défini sur §e" + suffix + "§a.");

            user.setSuffix(suffix);

            if (victim != sender)
            {
                String name = (sender instanceof Player) ? ((Player) sender).getDisplayName() : sender.getName();

                if (!silent) { victim.sendMessage("§aVotre suffix a été défini sur §e" + suffix + "§a par §e" + name); }
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
