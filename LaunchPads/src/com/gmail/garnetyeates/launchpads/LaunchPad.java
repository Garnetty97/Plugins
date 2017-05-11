package com.gmail.garnetyeates.launchpads;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LaunchPad {

	private Location padLocation;
	private Location launchLocation;
	
	private UUID editing;
	
	private static ArrayList<LaunchPad> launchPads = new ArrayList<>();
	
	public LaunchPad(Player creater) {
		editing = creater.getUniqueId();
		padLocation = creater.getLocation();
		launchPads.add(this);
	}
	
	public LaunchPad(Location padLocation, Location launchLocation) {
		this.padLocation = padLocation;
		this.launchLocation = launchLocation;
		launchPads.add(this);
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<LaunchPad> getLaunchPads() {
		return (ArrayList<LaunchPad>) launchPads.clone();
	}
	
	public static LaunchPad whatPadAmIEditing(Player p) {
		for (LaunchPad pad : launchPads) {
			if (pad.hasEditor() && pad.getEditor().equals(p)) return pad;
		} return null;
	}
	
	public static boolean compareLocation(Location loc1, Location loc2) {
		if (loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockY() == loc2.getBlockY()
				&& loc1.getBlockZ() == loc2.getBlockZ()) return true;
		else return false;
	}
	
	public boolean setLaunchLocation(Location loc) {
		if (this.getLocation().getWorld() == loc.getWorld()) {
			launchLocation = loc;
			return true;
		} else return false;	
	}
	
	public boolean stopEditing() {
		if (editing == null) return false;
		else {
			editing = null;
			return true;
		}
	}
	
	public boolean hasEditor() {
		if (editing == null) return false;
		else return true;
	}
	
	public void setEditor(Player p) {
		this.editing = p.getUniqueId();
	}
	
	public Player getEditor() {
		if (hasEditor()) return Bukkit.getPlayer(editing);
		else return null;
	}
	
	public Location getLocation() {
		return padLocation.clone();
	}
	
	public Location getLaunchLocation() {
		if (launchLocation == null) return null;
		else return launchLocation.clone();
	}
	
	public void terminate() {
		launchPads.remove(this);
		editing = null;
	}
	
	public void launch(Player player) {
		Vector thisWay = new Vector();
		thisWay.setX((launchLocation.getX() - padLocation.getX()) / 6);
		thisWay.setY((launchLocation.getY() - padLocation.getY()) / 6);
		thisWay.setZ((launchLocation.getZ() - padLocation.getZ()) / 6);
		
		Location loc = player.getLocation();
		loc.setY(loc.getY() + 1.5
				 );
		player.teleport(loc);
		
		Bukkit.getServer().broadcastMessage(thisWay.getX() + " " + thisWay.getY() + " " + thisWay.getZ());
		player.setVelocity(thisWay);
		
	}
	
}
