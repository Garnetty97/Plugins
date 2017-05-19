package com.gmail.garnetyeates.pvpplugin.bulletarrows;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import com.gmail.garnetyeates.pvpplugin.PvpPlugin;

import net.md_5.bungee.api.ChatColor;

public class BulletArrows {

	private static JavaPlugin plugin = JavaPlugin.getPlugin(PvpPlugin.class);
	
	public static String prefix = ChatColor.GRAY + "[" + ChatColor.GREEN + "Barrow" + ChatColor.GRAY + "] ";
	
	public static void init() {
		Bukkit.getServer().getPluginManager().registerEvents(new BulletArrowsListener(), plugin);
		plugin.getCommand("bulletarrow").setExecutor(new BulletArrowsCommandExecutor());
	}
	
	public static ItemStack createSpecificBarrowItem(String potionEffect, int level, int duration, int amount) {
		PotionEffectType type = PotionEffectType.getByName(potionEffect);
		if (type == null || level < 1 || level > 15) return null;
		else {
			ItemStack barrow = new ItemStack(Material.FLINT, amount);
			ArrayList<String> lore = new ArrayList<>();
			RomanNumeral numeral = RomanNumeral.fromNumber(level);
			potionEffect = potionEffect.substring(0, 1).toUpperCase() + potionEffect.substring(1, potionEffect.length());
			lore.add(ChatColor.YELLOW + potionEffect + " " + numeral + ", " + (duration / 20) + " seconds");
			ItemMeta meta = barrow.getItemMeta();
			meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.LIGHT_PURPLE + "" + ChatColor.MAGIC + "O"
			+ ChatColor.RESET + "" + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Barrow" + ChatColor.LIGHT_PURPLE
			+ ChatColor.MAGIC + "O");
			meta.setLore(lore);
			barrow.setItemMeta(meta);
			return barrow;
		}
	}
	
	public static ItemStack createRandomBarrowItem() {
		return null;
	}

}
