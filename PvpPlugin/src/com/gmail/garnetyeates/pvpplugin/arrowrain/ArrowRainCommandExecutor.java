package com.gmail.garnetyeates.pvpplugin.arrowrain;

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
			if (p.isOp()) ArrowRain.giveArrowStormItem(p);	
			else p.sendMessage(ChatColor.RED + "You do not have permission to use this command");
		}	
		return true;
	}

	
	
	

}
