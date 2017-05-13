package com.gmail.garnetyeates.pvpplugin.arrowrain;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class ArrowRainCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String name, String[] arg3) {
		if (cmd.getName().equalsIgnoreCase("ArrowRain") && sender instanceof Player) {
			Player p = (Player) sender;
			Location loc = p.getLocation();
			if (loc.getPitch() > -35 && loc.getPitch() < 35) {
				ArrowRainTask aye = new ArrowRainTask(p, loc.getDirection(), loc, 20);
				aye.rainHell();
			} else {
				p.sendMessage(ChatColor.RED + "You cannot be looking too high up or down to use Arrow Rain");
			}
			
		}
		
		
		return true;
	}

}
