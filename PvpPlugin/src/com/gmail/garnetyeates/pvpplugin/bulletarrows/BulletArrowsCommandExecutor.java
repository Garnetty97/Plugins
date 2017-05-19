package com.gmail.garnetyeates.pvpplugin.bulletarrows;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.gmail.garnetyeates.pvpplugin.PvpPlugin;

import net.md_5.bungee.api.ChatColor;

public class BulletArrowsCommandExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String name, String[] args) {
		if (cmd.getName().equalsIgnoreCase("bulletarrow") && sender instanceof Player) {
			if (args.length == 5 ) {
				if (args[0].equalsIgnoreCase("specific")) {
					// barrow specific [potioneffect] [level] [duration] [amount]
					Player p = (Player) sender;
					try {
						String potionEffect = args[1];
						int level = Integer.parseInt(args[2]);
						int duration = Integer.parseInt(args[3]);
						int amount = Integer.parseInt(args[4]);
						ItemStack barrow = BulletArrows.createSpecificBarrowItem(potionEffect, level, duration, amount);
						if (barrow != null) {
							if (PvpPlugin.isMyInventoryFull(p)) p.getWorld().dropItem(p.getLocation(), barrow);
							else p.getInventory().addItem(barrow);
						} else p.sendMessage(BulletArrows.prefix + "Unknown PotionEffectType");
					} catch (Exception e) {
						p.sendMessage(BulletArrows.prefix + "/barrow specific [potioneffect] [level] [duration] [amount]");
						p.sendMessage(BulletArrows.prefix + ChatColor.BOLD + "EVERYTHING IN BRACKETS MUST BE AN INTEGER");
						p.sendMessage(BulletArrows.prefix + ChatColor.BOLD + "[level] max = 15 | [duration] is in ticks (20 ticks = 1 second)");
					}
					//ItemStack barrow = BulletArrows.createSpecificBarrowItem(potionEffect, level, duration, amount)
				}
			} else {
				sender.sendMessage(BulletArrows.prefix + "Invalid arguments");
				sender.sendMessage(BulletArrows.prefix + "/barrow specific [potioneffect] [level] [duration] [amount]");
				sender.sendMessage(BulletArrows.prefix + "/barrow random [amount]");
			}
			
			
		}
		return true;
	}
	
	

}
