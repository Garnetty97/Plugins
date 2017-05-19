package com.gmail.garnetyeates.pvpplugin.arrowrain;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.garnetyeates.pvpplugin.PvpPlugin;

import net.md_5.bungee.api.ChatColor;

public class ArrowRain {

	private static JavaPlugin plugin = JavaPlugin.getPlugin(PvpPlugin.class);
	
	public static void init() {
		Bukkit.getPluginManager().registerEvents(new ArrowRainListener(), plugin);
		plugin.getCommand("arrowrain").setExecutor(new ArrowRainCommandExecutor());

	}
	
	public static void giveArrowStormItem(Player p) {
		ItemStack arrowRain = new ItemStack(Material.SPECTRAL_ARROW, 1);
		ItemMeta meta = arrowRain.getItemMeta();
		meta.setDisplayName(PvpPlugin.arrowStormName);
		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.YELLOW + "Left or right click with this in your hand to unleash an storm of arrows");
		meta.setLore(lore);
		arrowRain.setItemMeta(meta);
		if (PvpPlugin.isMyInventoryFull(p)) p.getWorld().dropItem(p.getLocation(), arrowRain);
		else p.getInventory().addItem(arrowRain);
	}
}
