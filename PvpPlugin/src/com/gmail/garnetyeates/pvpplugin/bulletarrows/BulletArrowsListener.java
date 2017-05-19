package com.gmail.garnetyeates.pvpplugin.bulletarrows;

import java.util.List;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.gmail.garnetyeates.pvpplugin.PvpPlugin;

import net.md_5.bungee.api.ChatColor;

public class BulletArrowsListener implements Listener {

	// TODO make aliases for potion names
	// TODO make harming barrows hurt more (1.5 more hearts per lvl)

	@EventHandler
	public void ON_LEFT_CLICK(PlayerInteractEvent event) {
		if (event.getAction() == Action.LEFT_CLICK_AIR) {
			Player p = event.getPlayer();
			if (p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().hasItemMeta()) {
				ItemStack hand = p.getInventory().getItemInMainHand();
				ItemMeta meta = hand.getItemMeta();
				if (meta.hasLore()) {
					String lore = meta.getLore().get(0);
					String[] s = lore.split(" ");
					if (s.length == 4) {
						String potionEffect = ChatColor.stripColor(s[0]);
						String levString = s[1].substring(0, s[1].length() - 1);
						String durString = s[2];
						int duration = Integer.parseInt(durString) * 20;
						int level = RomanNumeral.fromString(levString).getAssociatedNumber();
						PotionEffectType type = PotionEffectType.getByName(potionEffect);
						PotionEffect effect = new PotionEffect(type, duration, level - 1);
						Location pLoc = p.getLocation();
						Vector dir = pLoc.getDirection();
						float x = (float) dir.getX();
						float y = (float) dir.getY();
						float z = (float) dir.getZ();
						pLoc.setX(pLoc.getX() + x);
						pLoc.setY(pLoc.getY() + y + 1.5);
						pLoc.setZ(pLoc.getZ() + z);
						TippedArrow a = pLoc.getWorld().spawnArrow(pLoc, dir, 4.5F, 0.0F, TippedArrow.class);
						a.setShooter(p);
						a.setCritical(true);
						a.setCustomName(p.getName() + "'s " + potionEffect + " " + levString + " barrow");
						a.addCustomEffect(effect, true);
						p.playSound(pLoc, Sound.ENTITY_ARROW_SHOOT, 10.0F, 1.4F);
					}
				}

			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void ON_ARROW_HIT(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof TippedArrow) {
			Entity hit = event.getEntity();
			Location hitLoc = hit.getLocation();
			TippedArrow a = (TippedArrow) event.getDamager();
			List<PotionEffect> effects = a.getCustomEffects();
			if (effects != null && !effects.isEmpty()) {
				for (PotionEffect effect : effects) {
					if (effect.getType().toString().toLowerCase().contains("harm")) {
						int level = effect.getAmplifier() + 1;
						float dmgIncrease = 1.5F * level;
						event.setDamage(event.getFinalDamage() + dmgIncrease);
					} else if (effect.getType().toString().toLowerCase().contains("heal")) {
						event.setCancelled(true);
						a.removeCustomEffect(PotionEffectType.HEAL);
						List<Entity> nearby = hit.getNearbyEntities(25, 10, 25);
						if (nearby != null && !nearby.isEmpty()) {
							for (Entity e : nearby) {
								if (e instanceof Player) {
									Player p = (Player) e;
									Location effectHitLoc = hitLoc.clone();
									effectHitLoc.setY(effectHitLoc.getY() + 2);
									final Location CONST_LOC = effectHitLoc.clone();
									for (int i = 0; i < 10; i++) {
										double xOffset = PvpPlugin.random.nextDouble();
										double zOffset = PvpPlugin.random.nextDouble();
										double yOffset = PvpPlugin.random.nextDouble() * 2;
										if (PvpPlugin.random.nextBoolean()) zOffset *= -1;
										if (PvpPlugin.random.nextBoolean()) xOffset *= -1;
										effectHitLoc.setX(effectHitLoc.getX() + xOffset);
										effectHitLoc.setY(effectHitLoc.getY() + yOffset);
										effectHitLoc.setZ(effectHitLoc.getZ() + zOffset);
										if (a.getShooter() instanceof Player) {
											Player shooter = (Player) a.getShooter();
											shooter.playEffect(effectHitLoc, Effect.HEART, 5);
											shooter.playSound(shooter.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5F, 2.0F);	
										}
										if (event.getEntity() instanceof Player) {
											((Player) event.getEntity()).playEffect(effectHitLoc, Effect.HEART, 5);
											((Player) event.getEntity()).playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5F, 2.0F);
										}
										if (!(a.getShooter() != null && p.equals(a.getShooter()))) p.playEffect(effectHitLoc, Effect.HEART, 5);
										effectHitLoc = CONST_LOC.clone();
									}
								}
							}
						}
						if (hit instanceof Player) {
							Player pHit = (Player) hit;
							int level = effect.getAmplifier() + 1;
							int heal = level * 4;
							if (heal + pHit.getHealth() > pHit.getMaxHealth()) pHit.setHealth(pHit.getMaxHealth());
							else pHit.setHealth(pHit.getHealth() + heal);		
						}
						a.remove();
					}
				}
			}
		}
	}
}


