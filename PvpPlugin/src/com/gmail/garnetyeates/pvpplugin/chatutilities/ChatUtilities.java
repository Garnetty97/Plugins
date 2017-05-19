package com.gmail.garnetyeates.pvpplugin.chatutilities;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.garnetyeates.pvpplugin.PvpPlugin;

import net.md_5.bungee.api.ChatColor;

public class ChatUtilities implements CommandExecutor {

	private static JavaPlugin plugin = JavaPlugin.getPlugin(PvpPlugin.class);
	
	private static String prefix = ChatColor.GRAY + "[" + ChatColor.RED + "ChatUtilities"+ ChatColor.GRAY + "] "; 
	
	public static void init() {
		plugin.getCommand("clearchat").setExecutor(new ChatUtilities());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String name, String[] arg3) {
		if (cmd.getName().equalsIgnoreCase("clearchat")) {
			for (int i = 0; i < 100; i++) Bukkit.getServer().broadcastMessage("");
			Bukkit.getServer().broadcastMessage(prefix + "Chat has been cleared");
		}
			
		
		
		return true;
	}
 
	
	
}
