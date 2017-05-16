package com.gmail.garnetyeates.pvpplugin.arrowrain;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.garnetyeates.pvpplugin.PvpPlugin;

public class ArrowRain {

	private static JavaPlugin plugin = JavaPlugin.getPlugin(PvpPlugin.class);
	
	public static void init() {
		Bukkit.getPluginManager().registerEvents(new ArrowRainListener(), plugin);
		plugin.getCommand("arrowrain").setExecutor(new ArrowRainCommandExecutor());

	}
	
}
