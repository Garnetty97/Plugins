package com.gmail.garnetyeates.pvpplugin.arrowrain;

import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ArrowRainListener implements Listener {

	@EventHandler
	public void ON_ARROW_HIT(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Arrow) {
			Arrow a = (Arrow) e.getDamager();
			int code = a.hashCode();
			if (ArrowRainTask.getArrowMap().containsKey(code)) {
				e.setDamage(e.getDamage() * 3);
				if (ArrowRainTask.getArrowMap().get(e.getDamager()).equals(e.getEntity())) {
					e.setCancelled(true);
				}
			}
		}
	}
	
	
}
