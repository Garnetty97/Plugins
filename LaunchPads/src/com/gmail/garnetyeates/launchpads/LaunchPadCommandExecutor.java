package com.gmail.garnetyeates.launchpads;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import static com.gmail.garnetyeates.launchpads.LaunchPads.chatPrefix;

public class LaunchPadCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {
		if (cmd.getName().equalsIgnoreCase("launchpad")) {
			if (args.length != 1) return false;
			else {
				if (!args[0].equalsIgnoreCase("list") && !(sender instanceof Player)) {
					sender.sendMessage(chatPrefix + "Only players can use this command");
				} else if (!args[0].equalsIgnoreCase("list")) {
					Player player = (Player) sender;
					if (args[0].equalsIgnoreCase("create")) {
						commandCreate(player);
					} else if (args[0].equalsIgnoreCase("sll") || args[0].equalsIgnoreCase("setlaunchlocation")) {
						commandSetLaunchLocation(player);
					} else if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete")
							|| args[0].equalsIgnoreCase("remove")) {
						commandDelete(player);
					} else if (args[0].equalsIgnoreCase("edit")) {
						commandEdit(player);
					} else if (args[0].equalsIgnoreCase("editclosest") || args[0].equalsIgnoreCase("editclose")) {
						commandEditClosest(player);
					} else if (args[0].equalsIgnoreCase("stopediting") || args[0].equalsIgnoreCase("se")) {
						commandStopEditing(player);
					} 
				} else if (args[0].equalsIgnoreCase("list") && sender instanceof Player) {
					commandList(sender);
				} else {
					sender.sendMessage(chatPrefix + "That's not a fucking command.");
				}
			}
		}
		return true;
	}
	
	private void commandStopEditing(Player player) {
		for (LaunchPad pad : LaunchPad.getLaunchPads()) {
			if (pad.hasEditor() && pad.getEditor().equals(player)) {
				pad.stopEditing();
				if (pad.getLaunchLocation() == null) pad.terminate();
			}
		}
	}

	private void commandList(CommandSender sender) {
		if (!LaunchPad.getLaunchPads().isEmpty()) {
			for (LaunchPad pad : LaunchPad.getLaunchPads()) {
				int x = pad.getLocation().getBlockX();
				int y = pad.getLocation().getBlockY();
				int z = pad.getLocation().getBlockZ();
				Bukkit.getServer().broadcastMessage(chatPrefix + x + ", " + y + ", " + z);
			}
		} else {
			sender.sendMessage(chatPrefix + "There are no launchpads. Go fucking make some.");
		}
	}
	
	private void commandEditClosest(Player player) {
		LaunchPad closest = null;
		Location loc = player.getLocation();
		if (LaunchPad.whatPadAmIEditing(player) == null) {
			if (!LaunchPad.getLaunchPads().isEmpty()) {
				for (LaunchPad pad : LaunchPad.getLaunchPads()) {
					double closestLoc = ((closest == null) ? 1e50 : closest.getLocation().distance(loc));
					if (pad.getLocation().distance(loc) < closestLoc) closest = pad;
				}
			} else {
				player.sendMessage(chatPrefix + "Ain't none dem launchpads dog");
				return;
			}
			
			if (!(closest == null)) {
				closest.setEditor(player);
				Location cLoc = closest.getLocation();
				player.teleport(new Location(cLoc.getWorld(), cLoc.getBlockX(), cLoc.getBlockY(), cLoc.getBlockZ()));
				player.performCommand("tp " + cLoc.getBlockX() + " " + cLoc.getBlockY() + " " + cLoc.getBlockZ());	
				player.sendMessage(chatPrefix + "Now editing the closest launchpad to you!");
			} else {
				player.sendMessage(chatPrefix + "You're way too far away. Like way too far.");
			}		
		} else {
			player.sendMessage(chatPrefix + "You need to stop editing in order to issue this command. Type /stopediting or /se");
		}
	}

	private void commandSetLaunchLocation(Player player) {
		if (LaunchPad.whatPadAmIEditing(player) == null) {
			player.sendMessage(chatPrefix + "You are not editing any launchpads!");
		} else {
			player.sendMessage(chatPrefix + "You set the new launch location. No longer editing.");
			LaunchPad padEditing = LaunchPad.whatPadAmIEditing(player);
			if (padEditing.setLaunchLocation(player.getLocation())) {
				padEditing.stopEditing();
			} else {
				player.sendMessage(chatPrefix + "The launch location needs to be in the same world.");
			}			
		}
	}

	private void commandCreate(Player player) {
		if (LaunchPad.whatPadAmIEditing(player) == null) {
			boolean alreadyAPadHere = false;
			for (LaunchPad pad : LaunchPad.getLaunchPads()) {
				if (pad.hasEditor() && pad.getEditor().equals(player)) pad.stopEditing();
				if (!pad.hasLaunchLocation()) pad.terminate();
				if (LaunchPad.compareLocation(pad.getLocation(), player.getLocation())) {
					alreadyAPadHere = true;
					break;
				}
			}
			if (alreadyAPadHere == true) {
				new LaunchPad(player);
				player.sendMessage(chatPrefix + "You are now editing a new launchpad. Type /launchpad"
						+ " delete to remove it or /launchpad sll to set/change the launch location.");
			} else {
				player.sendMessage(chatPrefix + "You're re ah id, there's already a launchpad here");
			}
		} else {
			player.sendMessage(chatPrefix + "You need to stop editing before you can issue this command. Type /se or /stopediting");
		}
		
	}

	private void commandEdit(Player player) {
		if (!LaunchPad.getLaunchPads().isEmpty()) {
			outer: if (LaunchPad.whatPadAmIEditing(player) != null) {
				player.sendMessage(chatPrefix + "You are already editing a launchpad, cunt.");
			} else {
				for (LaunchPad pad : LaunchPad.getLaunchPads()) {
					if (LaunchPad.compareLocation(pad.getLocation(), player.getLocation())) {
						player.sendMessage(chatPrefix + "You are now editing the launchpad at this location.");
						pad.setEditor(player);
						break outer;
					}
				}
				player.sendMessage(chatPrefix + "No launchpad found here.");
			}
		} else {
			player.sendMessage(chatPrefix + "There are no launchpads. Go fucking make some.");
		}
	}

	private void commandDelete(Player player) {
		if (LaunchPad.whatPadAmIEditing(player) == null) {
			player.sendMessage(chatPrefix + "You are not editing any launchpads!");
		} else {
			LaunchPad padEditing = LaunchPad.whatPadAmIEditing(player);
			padEditing.terminate();
			player.sendMessage(chatPrefix + "Launchpad successfully removed.");
		}
	}
}
