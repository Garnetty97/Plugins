package com.gmail.garnetyeates.launchpads;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LaunchPadListener implements Listener {

	@EventHandler
	public void ON_PRESSUREPAD_STEP(PlayerInteractEvent event) {
		if (event.getAction() == Action.PHYSICAL) {
			Location pressurePlateLoc = event.getClickedBlock().getLocation();
			if (!LaunchPad.getLaunchPads().isEmpty()) {
				for (LaunchPad pad : LaunchPad.getLaunchPads()) {
					if (LaunchPad.compareLocation(pad.getLocation(), pressurePlateLoc)) {
						pad.launch(event.getPlayer());
						break;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void ON_LOGOUT(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (LaunchPad.whatPadAmIEditing(player) != null) {
			LaunchPad pad = LaunchPad.whatPadAmIEditing(player);
			if (pad.getLaunchLocation() == null) {
				pad.terminate();
			} else {
				pad.stopEditing();
			}
		}
	}
	
}
