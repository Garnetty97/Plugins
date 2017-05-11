package com.gmail.garnetyeates.launchpads;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class LaunchPads extends JavaPlugin {

	// TODO make it so they can use /launchpad editclosest/editclose/ec
	
	public static String chatPrefix = ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "LaunchPads" + ChatColor.WHITE + "] ";
	
	private File launchPadDataFile = new File("LaunchPadData.yaml");
	
	@Override
	public void onEnable() {
		if (!launchPadDataFile.exists()) {
			try { launchPadDataFile.createNewFile(); }
			catch (IOException e) { e.printStackTrace(); }
		}
		getCommand("launchpad").setExecutor(new LaunchPadCommandExecutor());
		getServer().getPluginManager().registerEvents(new LaunchPadListener(), this);
		loadCourses();
	}

	public void onDisable() {
		writeCoursesToFile();
	}
	
	private void loadCourses() {
		try {
			int numLoaded = 0;
			getConfig().load(launchPadDataFile);
			for (String s : getConfig().getStringList("LaunchPads")) {
				String[] argument = s.split(",");
				int locX = Integer.parseInt(argument[0]);
				int locY = Integer.parseInt(argument[1]);
				int locZ = Integer.parseInt(argument[2]);
				double launchLocX = Double.parseDouble(argument[3]);
				double launchLocY = Double.parseDouble(argument[4]);
				double launchLocZ = Double.parseDouble(argument[5]);
				String worldName = argument[6];
				World world = null;
				for (World w : Bukkit.getWorlds()) {
					if (w.getName().equalsIgnoreCase(worldName)) {
						world = w;
						break;
					}
				}
				if (world == null) {
					getServer().broadcastMessage(chatPrefix + "There was a world name while error loading a launchpad");
				} else {
					numLoaded++;
					Location padLocation = new Location(world, locX, locY, locZ);
					Location launchLocation = new Location(world, launchLocX, launchLocY, launchLocZ);
					new LaunchPad(padLocation, launchLocation);
				}
			}
			String s = (numLoaded == 1) ? "LaunchPad" : "LaunchPads";
			getServer().broadcastMessage(chatPrefix + "Loaded " + numLoaded + " " + s + ".");
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	private void writeCoursesToFile() {
		if (!launchPadDataFile.exists()) {
			try { launchPadDataFile.createNewFile(); }
			catch (IOException e) { e.printStackTrace(); }
		}
		ArrayList<String> launchPadSave = new ArrayList<>();
		for (LaunchPad pad : LaunchPad.getLaunchPads()) {
			if (pad.getLocation() != null && pad.getLaunchLocation() != null) {
				int locX = pad.getLocation().getBlockX();
				int locY = pad.getLocation().getBlockY();
				int locZ = pad.getLocation().getBlockZ();
				double launchLocX = pad.getLaunchLocation().getX();
				double launchLocY = pad.getLaunchLocation().getY();
				double launchLocZ = pad.getLaunchLocation().getZ();
				World world = pad.getLocation().getWorld();
				launchPadSave.add(locX + "," + locY + "," + locZ + "," + launchLocX + "," + launchLocY + ","
						+ launchLocZ + "," + world.getName());
				getConfig().set("LaunchPads", launchPadSave);
	
				try { getConfig().save(launchPadDataFile); }	
				catch (IOException e) { e.printStackTrace(); }				
			} else {
				getServer().broadcastMessage(chatPrefix + "There was an error saving a LaunchPad");
			}
		}
	}
	
}
