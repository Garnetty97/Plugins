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
	
	/**
	 * Constructs a new LaunchPad at the location of the player who created it. This constructor is called
	 * by the /LaunchPad create command.
	 * @param creater The player who created the LaunchPad
	 */
	public LaunchPad(Player creater) {
		editing = creater.getUniqueId();
		Location loc = creater.getLocation();
		padLocation = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
		launchPads.add(this);
	}
	
	/**
	 * This constructor is called when LaunchPads are loaded into the server upon startup or plugin reloading.
	 * @param padLocation The Location of the LaunchPad itself (the pressure plate location)
	 * @param launchLocation The Location that the player gets launched towards when they step on the pressure plate
	 */
	public LaunchPad(Location padLocation, Location launchLocation) {
		this.padLocation = padLocation;
		this.launchLocation = launchLocation;
		launchPads.add(this);
	}
	
	/**
	 * Fetches a list of all of the active LaunchPads. Any LaunchPads that are terminated get removed from this list,
	 * @return a ArrayList of the currently active LaunchPads
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<LaunchPad> getLaunchPads() {
		return (ArrayList<LaunchPad>) launchPads.clone();
	}
	
	/**
	 * Fetches the LaunchPad that the proposed Player is editing, if any
	 * @param p The player who will be inspected to see what LaunchPad they are editing
	 * @return The LaunchPad they are editing, or null if they are not editing one
	 */
	public static LaunchPad whatPadAmIEditing(Player p) {
		for (LaunchPad pad : launchPads) {
			if (pad.hasEditor() && pad.getEditor().equals(p)) return pad;
		} return null;
	}
	
	/**
	 * This method is solely used for comparing whether or not 2 Locations have the same block X, Y, and Z. It
	 * is not restricted to just LaunchPads which is why it is a static method
	 * @param loc1 One of the two locations to be compared
	 * @param loc2 The other of the two locations to be compared
	 * @return true if these locations fall in the same spot
	 */
	public static boolean compareLocation(Location loc1, Location loc2) {
		if (loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockY() == loc2.getBlockY()
				&& loc1.getBlockZ() == loc2.getBlockZ()) return true;
		else return false;
	}
	
	/**
	 * Sets the Location that players will be launched towards when they step on the pressure pad connected
	 * to this LaunchPad
	 * @param loc The Location for the players to get launched towards
	 * @return A location that players will be launched towards when they step on the pressure pad connected to this
	 * launchpad. Redundant as FUCK.
	 */
	public boolean setLaunchLocation(Location loc) {
		if (this.getLocation().getWorld() == loc.getWorld()) {
			launchLocation = loc;
			return true;
		} else return false;	
	}
	
	/**
	 * This will make it so no player is editing this launchpad anymore.
	 * @return true if a player was actually editing this launchpad
	 */
	public boolean stopEditing() {
		if (editing == null) return false;
		else {
			editing = null;
			return true;
		}
	}
	
	/**
	 * Checks to see if a launch location is setup for this LaunchPad. LaunchPads that don't have a
	 * launch location are terminated when the server reloads, or if a player is able to edit another launchpad
	 * by any means. Usually they will be told that they can't edit a new launchpad until they set a launch location
	 * or remove the launchpad they are editing. The exceptions are /launchpad create and /launchpad editclosest
	 * @return true if the launchpad has a launch location
	 */
	public boolean hasLaunchLocation() {
		return (launchLocation == null) ? false : true;
	}
	
	/**
	 * Checks to see if this LaunchPad is currently being edited
	 * @return true if this LaunchPad is currently being edited
	 */
	public boolean hasEditor() {
		if (editing == null) return false;
		else return true;
	}
	
	/**
	 * Sets the editor of this LaunchPad to the given player
	 * @param p The player who will now be editing this LaunchPad
	 */
	public void setEditor(Player p) {
		this.editing = p.getUniqueId();
	}
	
	/**
	 * Obtains the current editor of this LaunchPad
	 * @return the player who is editing this LaunchPad, null if there is none
	 */
	public Player getEditor() {
		if (hasEditor()) return Bukkit.getPlayer(editing);
		else return null;
	}
	
	/**
	 * Obtains the location of this LaunchPad
	 * @return the location of this LaunchPad
	 */
	public Location getLocation() {
		return padLocation.clone();
	}
	
	/**
	 * Obtains the location that players will be launched towards if they activate the LaunchPad
	 * via pressure plate
	 * @return
	 */
	public Location getLaunchLocation() {
		if (launchLocation == null) return null;
		else return launchLocation.clone();
	}
	
	/**
	 * Removes this LaunchPad. Rest in peace.
	 */
	public void terminate() {
		launchPads.remove(this);
		editing = null;
	}
	
	/**
	 * Launches the given player towards the launch location. Bye bye.
	 * @param player The player to be launched.
	 */
	public void launch(Player player) {
		Vector thisWay = new Vector();
		thisWay.setX((launchLocation.getX() - padLocation.getX()) / 6);
		thisWay.setY((launchLocation.getY() - padLocation.getY()) / 6);
		thisWay.setZ((launchLocation.getZ() - padLocation.getZ()) / 6);
		
		Location loc = player.getLocation();
		loc.setY(loc.getY() + 1.5);
		player.teleport(loc);
		player.setVelocity(thisWay);
		
	}
}
