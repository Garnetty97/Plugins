package com.gmail.garnetyeates.pvpplugin;

import java.util.Random;

import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.garnetyeates.pvpplugin.antibowspam.AntiBowSpam;
import com.gmail.garnetyeates.pvpplugin.arrowrain.ArrowRain;
import com.gmail.garnetyeates.pvpplugin.bulletarrows.BulletArrows;
import com.gmail.garnetyeates.pvpplugin.chatutilities.ChatUtilities;

import net.md_5.bungee.api.ChatColor;

public class PvpPlugin extends JavaPlugin implements CommandExecutor {
	
	public static Random random = new Random();
	
	public static String arrowStormName = ChatColor.GOLD + "" + ChatColor.BOLD + "Arrow Storm";
	
	@Override
	public void onEnable() {
		ChatUtilities.init();
		ArrowRain.init();
		AntiBowSpam.init();
		BulletArrows.init();
	}
	
	public static boolean isMyInventoryFull(Player p) {
		if (p.getInventory().firstEmpty() == -1) return true;
		else return false;
	}
}
