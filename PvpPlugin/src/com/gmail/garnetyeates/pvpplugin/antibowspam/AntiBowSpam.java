package com.gmail.garnetyeates.pvpplugin.antibowspam;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.gmail.garnetyeates.pvpplugin.PvpPlugin;

import net.md_5.bungee.api.ChatColor;

public class AntiBowSpam implements Listener {

	String prefix = ChatColor.GRAY + "[" + ChatColor.DARK_GREEN + "AntiBowSpam" + ChatColor.GRAY + "] ";
	
	public static void init() {
		Bukkit.getServer().getPluginManager().registerEvents(new AntiBowSpam(), JavaPlugin.getPlugin(PvpPlugin.class));
	}
	
	@EventHandler
	public void ON_SHOOT(ProjectileLaunchEvent event) {
		if (event.getEntity() instanceof Arrow && event.getEntity().getShooter() instanceof Player) {
			Player shooter = (Player) event.getEntity().getShooter();
			Vector velocity = event.getEntity().getVelocity();
			double x = Math.abs(velocity.getX());
			double y = Math.abs(velocity.getY());
			double z = Math.abs(velocity.getZ());
			double speed = x + y + z;
			if (speed < 3 && shooter.isOp() == false) {
				shooter.sendMessage(prefix + "That aint gonna happen dawg");
				event.setCancelled(true);
			}
		}
	}
}
