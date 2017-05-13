package com.gmail.garnetyeates.pvpplugin.arrowrain;

import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.gmail.garnetyeates.pvpplugin.PvpPlugin;

public class ArrowRainTask implements Runnable {
	
	private static final HashMap<Integer, Player> ARROWMAP = new HashMap<>();
	
	private Plugin plugin = JavaPlugin.getPlugin(PvpPlugin.class);
	private int id = 0;
	private int iterator = 0;
	private int maxIterations = 0;
	private Location shootLoc;
	private Player shooter;
	private Vector[] velocities = new Vector[1];
	private ArrayList<Arrow> arrows = new ArrayList<>();
	
	public ArrowRainTask(Player player, Vector direction, Location shootLoc, int amount) {
		outer: while (true) {
			if (Math.abs(direction.getX()) < 0.05 || Math.abs(direction.getZ()) < 0.05) {
				direction.multiply(1.1);
			} else break outer;
		}
		
		velocities = new Vector[amount];
		maxIterations = amount;
		shooter = player;
		this.shootLoc = new Location(shootLoc.getWorld(), shootLoc.getX(), shootLoc.getY() + 20, shootLoc.getZ());
		final double XDIR = direction.getX();
		final double ZDIR = direction.getZ();
		double yDir = -1;	
		final double INCREMENT = (Math.abs(yDir) + 0.2) / amount;
		for (int i = 0; i < velocities.length; i++) {
			velocities[i] = new Vector(XDIR, yDir, ZDIR);
			yDir += INCREMENT;
		}
		removeArrowsAfterDelay();
	}

	@Override
	public void run() {
		if (iterator < maxIterations) {
			Vector vec = velocities[iterator];
			float spread = 6;
			for (int i = 0; i < 20; i++) {
				Arrow a = shootLoc.getWorld().spawnArrow(shootLoc, vec, 3.0F, spread);
				spread = spread * 0.9f;
				a.setShooter(shooter);
				a.setCritical(true);
				a.setFireTicks(80);
				ARROWMAP.put(a.hashCode(), shooter);
				arrows.add(a);
				a.setCustomName("Arrow Rain");
				if (iterator % 10 == 0) {
					shooter.playSound(shooter.getLocation(), Sound.ITEM_FIRECHARGE_USE, 0.005F, 1.0F);
				}
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

	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<Integer, Player> getArrowMap() {
		return (HashMap<Integer, Player>) ARROWMAP.clone();
	}

}
