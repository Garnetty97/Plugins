package com.gmail.garnetyeates.pvpplugin.arrowrain;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.gmail.garnetyeates.pvpplugin.PvpPlugin;

import net.md_5.bungee.api.ChatColor;

public class ArrowRainTask implements Runnable {
	
	private static final HashMap<Integer, Player> ARROWMAP = new HashMap<>();
	
	private static JavaPlugin plugin = JavaPlugin.getPlugin(PvpPlugin.class);
	
	private int id = 0;
	private int iterator = 0;
	private int maxIterations = 0;
	private Location shootLoc;
	private Player shooter;
	private Vector[] velocities = new Vector[1];
	private ArrayList<Arrow> arrows = new ArrayList<>();
	
	public ArrowRainTask(Player player, Vector direction, Location shootLoc, int amount) {
		
		double suitableSpeed = 2;
		double speed = Math.abs(direction.getX()) + Math.abs(direction.getZ());
		int tries = 0;
		while (speed < suitableSpeed - (suitableSpeed / 10) && tries < 200) {
			direction.multiply(4);
			speed = Math.abs(direction.getX()) + Math.abs(direction.getZ());
			tries++;
		}
		int tries2 = 0;
		while (speed > suitableSpeed + (suitableSpeed / 10) && tries2 < 200) {
			direction.multiply(0.95);
			speed = Math.abs(direction.getX()) + Math.abs(direction.getZ());
			tries2++;
		}
		
		double pitch = shootLoc.getPitch();
		double distanceMultiplier = 1;
		if (pitch > 25) distanceMultiplier = (95 - pitch) / 115;
		if (pitch < -25) distanceMultiplier = (130 - pitch) / 75;
		direction.multiply(distanceMultiplier);
		
		if (distanceMultiplier > 1) shootLoc.setY(shootLoc.getY() + distanceMultiplier * 2);
	
		DecimalFormat format = new DecimalFormat();
		format.setMaximumFractionDigits(2);
		String multiplierString = format.format(distanceMultiplier);
		
		player.sendMessage(ChatColor.GREEN + "Launched an arrow storm at your location with a distance multiplier of "
				+ ChatColor.GOLD + multiplierString + ChatColor.GREEN + ".");
		
		velocities = new Vector[amount];
		maxIterations = amount;
		shooter = player;
		this.shootLoc = new Location(shootLoc.getWorld(), shootLoc.getX(), shootLoc.getY() + 20, shootLoc.getZ());
		final double XDIR = direction.getX();
		final double ZDIR = direction.getZ();
		double yDir = -1;	
		final double INCREMENT = (Math.abs(yDir) - 0.1) / amount;
		for (int i = 0; i < velocities.length; i++) {
			velocities[i] = new Vector(XDIR, yDir, ZDIR);
			yDir += INCREMENT;
		}
	}

	@Override
	public void run() {
		if (iterator < maxIterations) {
			Vector vec = velocities[iterator];
			float spread = 6;
			for (int i = 0; i < 15; i++) {
				Arrow a = shootLoc.getWorld().spawnArrow(shootLoc, vec, 4.0F, spread);
				spread = spread * 0.9f;
				a.setShooter(shooter);
				a.setCritical(true);
				a.setFireTicks(80);
				ARROWMAP.put(a.hashCode(), shooter);
				arrows.add(a);
				a.setCustomName("Arrow Rain");
				if (iterator % 2 == 0) shooter.playSound(shooter.getLocation(), Sound.ENTITY_ARROW_SHOOT, 0.1F, 1.0F);
			} 
			iterator++;
		} else {
			Bukkit.getServer().getScheduler().cancelTask(id);
		}
	}
	
	private void removeArrowsAfterDelay() {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			@Override
			public void run() {
				for (Arrow a : arrows) {
					a.remove();
					ARROWMAP.remove(a.hashCode());
				}
				arrows.clear();	
			}
		}, 200);
	}
	
	public void rainHell() {
		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, 1);
		removeArrowsAfterDelay();
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<Integer, Player> getArrowMap() {
		return (HashMap<Integer, Player>) ARROWMAP.clone();
	}

}
