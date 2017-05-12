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
						launchPadCreate(player);
					} else if (args[0].equalsIgnoreCase("sll") || args[0].equalsIgnoreCase("setlaunchlocation")) {
						launchPadSLL(player);
					} else if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete")
							|| args[0].equalsIgnoreCase("remove")) {
						launchPadDel(player);
					} else if (args[0].equalsIgnoreCase("edit")) {
						launchPadEdit(player);
					} else if (args[0].equalsIgnoreCase("editclosest") || args[0].equalsIgnoreCase("editclose")) {
						launchPadEditClosest(player);
					}
				} else if (args[0].equalsIgnoreCase("list") && sender instanceof Player) {
					launchPadList(sender);
				} else {
					sender.sendMessage(chatPrefix + "That's not a fucking command.");
				}
			}
		}
		return true;
	}

	private void launchPadList(CommandSender sender) {
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
	
	private void launchPadEditClosest(Player player) {
		LaunchPad closest = null;
		Location loc = player.getLocation();
		
		for (LaunchPad pad : LaunchPad.getLaunchPads()) {
			if (pad.hasEditor() && pad.getEditor().equals(player)) {
				pad.stopEditing();
			}
			if (!pad.hasLaunchLocation()) pad.terminate();
		}
		
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
	}

	private void launchPadSLL(Player player) {
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

	private void launchPadCreate(Player player) {
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
	}

	private void launchPadEdit(Player player) {
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

	private void launchPadDel(Player player) {
		if (LaunchPad.whatPadAmIEditing(player) == null) {
			player.sendMessage(chatPrefix + "You are not editing any launchpads!");
		} else {
			LaunchPad padEditing = LaunchPad.whatPadAmIEditing(player);
			padEditing.terminate();
			player.sendMessage(chatPrefix + "Launchpad successfully removed.");
		}
	}
}
