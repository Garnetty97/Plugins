package com.gmail.garnetyeates.launchpads;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.TabCompleteEvent;

public class LaunchPadListener implements Listener {

	@EventHandler
	public void ON_TAB_COMPLETE(TabCompleteEvent event) {
		if (event.getBuffer().toLowerCase().contains("launchpad ") || event.getBuffer().toLowerCase().contains("lp ")) {
			ArrayList<String> completions = new ArrayList<>();
			String buffer = event.getBuffer().toLowerCase();
			if (buffer.split(" ").length == 2) {
				String arg2 = buffer.split(" ")[1];
				if ("sll".substring(0, (arg2.length() > "sll".length()) ? 1 : arg2.length()).equalsIgnoreCase(arg2)) completions.add("sll");
				if ("create".substring(0, (arg2.length() > "create".length()) ? 1 : arg2.length()).equalsIgnoreCase(arg2)) completions.add("create");
				if ("remove".substring(0, (arg2.length() > "remove".length()) ? 1 : arg2.length()).equalsIgnoreCase(arg2)) completions.add("remove");
				if ("editclose".substring(0, (arg2.length() > "editclose".length()) ? 1 : arg2.length()).equalsIgnoreCase(arg2)) completions.add("editclose");
				if ("edit".substring(0, (arg2.length() > "edit".length()) ? 1 : arg2.length()).equalsIgnoreCase(arg2)) completions.add("edit");
				if ("list".substring(0, (arg2.length() > "list".length()) ? 1 : arg2.length()).equalsIgnoreCase(arg2)) completions.add("list");
				if ("stopediting".substring(0, (arg2.length() > "stopediting".length()) ? 1 : arg2.length()).equalsIgnoreCase(arg2)) completions.add("stopediting");
			} else if (buffer.split(" ").length == 1) {
				completions.add("sll");
				completions.add("create");
				completions.add("remove");
				completions.add("editclose");
				completions.add("edit");
				completions.add("list");
				completions.add("stopediting");
			}
			event.setCompletions(completions);
		} 
	}
	
	@EventHandler
	public void ON_FALL_DAMAGE(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			if ((event.getCause() == DamageCause.FALL && LaunchPad.fallImmunity.contains(event.getEntity())) ||
					event.getCause() == DamageCause.FALL && event.getEntity().getFallDistance() < 6) {
				event.setCancelled(true);
			} else if (event.getCause() == DamageCause.FALL) event.setDamage(event.getDamage() * 1.3);
			if (LaunchPad.fallImmunity.contains(event.getEntity())) LaunchPad.fallImmunity.remove(event.getEntity());
		}
	}
	
	@EventHandler
	public void ON_PRESSUREPAD_STEP(PlayerInteractEvent event) {
		if (event.getAction() == Action.PHYSICAL) {
			Location pressurePlateLoc = event.getClickedBlock().getLocation();
			if (!LaunchPad.getLaunchPads().isEmpty()) {
				for (LaunchPad pad : LaunchPad.getLaunchPads()) {
					if (LaunchPad.compareLocation(pad.getLocation(), pressurePlateLoc)) {
						if (!(pad.hasEditor() && pad.getEditor().equals(event.getPlayer())) && !event.getPlayer().isSneaking()) {
							pad.launch(event.getPlayer());
							event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ITEM_FIRECHARGE_USE,
									5.0f, 0.0f);
							break;
						}
						
					}
				}
			}
		}
	}
	
	@EventHandler
	public void ON_PRESSUREPAD_STEP(EntityInteractEvent e) {
			Location pressurePlateLoc = e.getBlock().getLocation();
			if (!LaunchPad.getLaunchPads().isEmpty()) {
				for (LaunchPad pad : LaunchPad.getLaunchPads()) {
					if (LaunchPad.compareLocation(pad.getLocation(), pressurePlateLoc)) {
							pad.launch(e.getEntity());
							break;
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
