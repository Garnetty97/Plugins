package com.gmail.garnetyeates.pvpplugin;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.garnetyeates.pvpplugin.arrowrain.ArrowRainCommandExecutor;
import com.gmail.garnetyeates.pvpplugin.arrowrain.ArrowRainListener;

import net.md_5.bungee.api.ChatColor;

public class PvpPlugin extends JavaPlugin implements CommandExecutor {
	
	private Server server = Bukkit.getServer();
	
	public static Random random = new Random();
	
	public static String arrowStormName = ChatColor.GOLD + "" + ChatColor.BOLD + "Arrow Storm";
	
	@Override
	public void onEnable() {
		server.getPluginManager().registerEvents(new ArrowRainListener(), this);
		getCommand("arrowrain").setExecutor(new ArrowRainCommandExecutor());
		getCommand("nuke").setExecutor(new ArrowRainCommandExecutor());
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public static boolean isMyInventoryFull(Player p) {
		if (p.getInventory().firstEmpty() == -1) return true;
		else return false;
	}
	
}
