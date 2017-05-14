package com.gmail.garnetyeates.pvpplugin.arrowrain;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import com.gmail.garnetyeates.pvpplugin.PvpPlugin;

public class ArrowRainListener implements Listener {

	@EventHandler
	public void ON_ARROW_HIT(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Arrow) {
			Arrow a = (Arrow) e.getDamager();
			int code = a.hashCode();
			if (ArrowRainTask.getArrowMap().containsKey(code)) {
				e.setDamage(e.getDamage() * 2);
				if (ArrowRainTask.getArrowMap().get(code).equals(e.getEntity())) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void ON_BLOCK_IGNITE(BlockIgniteEvent e) {
		if (e.getCause() == IgniteCause.EXPLOSION) {
			if (PvpPlugin.random.nextInt(8) != 0) e.setCancelled(true);
		}
		if (e.getCause() == IgniteCause.SPREAD) e.setCancelled(true);
	}
	
	@EventHandler
	public void ON_BLOCK_BURN(BlockBurnEvent e) {
		e.setCancelled(true);
	}

	
	
}
