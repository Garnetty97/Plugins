package com.gmail.garnetyeates.pvpplugin.arrowrain;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.garnetyeates.pvpplugin.PvpPlugin;

import net.md_5.bungee.api.ChatColor;

public class ArrowRainCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String name, String[] arg3) {
		if (cmd.getName().equalsIgnoreCase("ArrowRain") && sender instanceof Player) {
			Player p = (Player) sender;
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
		if (cmd.getName().equalsIgnoreCase("Nuke") && sender instanceof Player) {
			Player player = (Player) sender;
			for (int i = 0; i < 10; i++) {
				player.getWorld().createExplosion(player.getLocation(), 75f);
			}
			
		}	
		return true;
	}
	
	

}
