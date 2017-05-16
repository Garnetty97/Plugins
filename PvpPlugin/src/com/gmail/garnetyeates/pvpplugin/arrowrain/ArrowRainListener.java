package com.gmail.garnetyeates.pvpplugin.arrowrain;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.gmail.garnetyeates.pvpplugin.PvpPlugin;

import net.md_5.bungee.api.ChatColor;

public class ArrowRainListener implements Listener {

	@EventHandler
	public void ON_ARROW_STORM_INITIATE(PlayerInteractEvent e) {
		if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (e.getPlayer().getInventory().getItemInMainHand() != null) {
				Player p = e.getPlayer();
				ItemStack hand = p.getInventory().getItemInMainHand();
				if (hand != null && hand.hasItemMeta() && hand.getItemMeta().getDisplayName().equalsIgnoreCase(PvpPlugin.arrowStormName)) {
					e.setCancelled(true);
					Location loc = p.getLocation();
					if (loc.getPitch() > -85 && loc.getPitch() < 85) {
						ArrowRainTask art = new ArrowRainTask(p, loc.getDirection(), loc, 20);
						art.rainHell();
						if (p.getGameMode() != GameMode.CREATIVE) {
							if (hand.getAmount() == 1) {
								p.getInventory().setItemInMainHand(null);
							} else {
								hand.setAmount(hand.getAmount() - 1);
								p.getInventory().setItemInMainHand(hand);
							}
						}

					} else {
						p.sendMessage(ChatColor.RED + "You cannot be looking too high up or down to use Arrow Rain");
					}
				}
			}
		}
		
	}

	@EventHandler
	public void ON_ARROW_HIT(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Arrow) {
			Arrow a = (Arrow) e.getDamager();
			int code = a.hashCode();
			if (ArrowRainTask.getArrowMap().containsKey(code)) {
				e.setDamage(e.getDamage() * 3);
				PotionEffect slownessII = new PotionEffect(PotionEffectType.SLOW, 60, 1);
				if (e.getEntity() instanceof Player) ((Player) e.getEntity()).addPotionEffect(slownessII);
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
