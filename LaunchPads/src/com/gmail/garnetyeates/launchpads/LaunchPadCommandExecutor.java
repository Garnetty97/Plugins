package com.gmail.garnetyeates.launchpads;

import org.bukkit.Bukkit;
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
						LaunchPad padCheck = null;
						for (LaunchPad pad : LaunchPad.getLaunchPads()) {
							if (LaunchPad.compareLocation(pad.getLocation(), player.getLocation())) {
								padCheck = pad;
								break;
							}
						}
						if (padCheck == null) {
							new LaunchPad(player);
							player.sendMessage(chatPrefix + "You are now editing a new launchpad. Type /launchpad"
									+ " delete to remove it or /launchpad sll to set/change the launch location.");
						} else {
							player.sendMessage(chatPrefix + "You're re ah id, there's already a launchpad here");
						}
					} else if (args[0].equalsIgnoreCase("sll") || args[0].equalsIgnoreCase("setlaunchlocation")) {
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
					} else if (args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("delete")
							|| args[0].equalsIgnoreCase("remove")) {
						if (LaunchPad.whatPadAmIEditing(player) == null) {
							player.sendMessage(chatPrefix + "You are not editing any launchpads!");
						} else {
							LaunchPad padEditing = LaunchPad.whatPadAmIEditing(player);
							padEditing.terminate();
							player.sendMessage(chatPrefix + "Launchpad successfully removed.");
						}
					} else if (args[0].equalsIgnoreCase("edit")) {
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
				} else if (args[0].equalsIgnoreCase("list") && sender instanceof Player) {
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
				} else {
					sender.sendMessage(chatPrefix + "That's not a fucking command.");
				}

			}
		}
		return true;
	}

}
