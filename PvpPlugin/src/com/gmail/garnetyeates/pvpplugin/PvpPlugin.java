package com.gmail.garnetyeates.pvpplugin;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.garnetyeates.pvpplugin.arrowrain.ArrowRainCommandExecutor;
import com.gmail.garnetyeates.pvpplugin.arrowrain.ArrowRainListener;

public class PvpPlugin extends JavaPlugin implements CommandExecutor {
	
	private Server server = Bukkit.getServer();
	
	public static Random random = new Random();
	
	@Override
	public void onEnable() {
		server.getPluginManager().registerEvents(new ArrowRainListener(), this);
		getCommand("arrowrain").setExecutor(new ArrowRainCommandExecutor());
		getCommand("nuke").setExecutor(new ArrowRainCommandExecutor());
	}
	
	@Override
	public void onDisable() {
		
	}
	
}
