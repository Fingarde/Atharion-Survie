package fr.fingarde.atharion.survie.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpeedCommand implements CommandExecutor
{
    String usage = "§bUsage: §r/speed §a<value> §7[player]";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 1 || args.length == 2)
        {
            Player victim = null;


            if(args.length == 2)
            {
                if (Bukkit.getPlayer(args[1]) != null)
                {
                    victim = Bukkit.getPlayer(args[1]);
                }
            }

              if(victim == null) { victim = (Player) sender; }

            if (victim != sender && !sender.isOp()) {sender.sendMessage("&cVous n'avez pas la permission");  return false; }

            float value;

            try
            {
                value = Float.valueOf(args[0]);
            }
            catch (NumberFormatException e)
            {
                sender.sendMessage(usage);

                return false;
            }

            value = (value / 10);

            String mode = "marche";

            if(victim.isFlying())
            {
                mode = "vol";
            }

            value += 0.1;

            if(value > 1) { value = 1; }

            if(value < 0) { value = 0; }


            if(mode.equals("marche"))
            {
                victim.setWalkSpeed(value);
            }
            else
            {
                victim.setFlySpeed(value);
            }

            if (victim != sender)
            {
                String name = (sender instanceof Player) ? ((Player) sender).getDisplayName() : sender.getName();

                sender.sendMessage("§aLa vitesse de §e" + mode + "§a de §e" + victim.getDisplayName() + "§a a été défini sur §e" + args[0]);
                victim.sendMessage("§aVotre vitesse de §e" + mode + "§a a été défini sur §e" + args[0] + "§a par §e" + name + "§a.");
            }
            else
            {
                sender.sendMessage("§aVotre vitesse de §e" + mode + "§a a été défini sur §e" + args[0]);
            }

            return true;
        }
        else
        {
            sender.sendMessage(usage);

            return false;
        }
    }
}
