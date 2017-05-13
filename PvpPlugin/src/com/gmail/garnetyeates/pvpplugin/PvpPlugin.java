package com.gmail.garnetyeates.pvpplugin;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.garnetyeates.pvpplugin.arrowrain.ArrowRainCommandExecutor;
import com.gmail.garnetyeates.pvpplugin.arrowrain.ArrowRainListener;

public class PvpPlugin extends JavaPlugin implements CommandExecutor {
	
	private Server server = Bukkit.getServer();
	
	@Override
	public void onEnable() {
		server.getPluginManager().registerEvents(new ArrowRainListener(), this);
		getCommand("arrowrain").setExecutor(new ArrowRainCommandExecutor());
	}
	
	@Override
	public void onDisable() {
		
	}
	
}
