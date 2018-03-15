package me.smokedev.speedynetwork;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Guis {
	/*
	 * public Inventory createInventory(int size, String name) { Inventory inv =
	 * Bukkit.createInventory(null, size, name); return inv;
	 * 
	 * 
	 * }
	 */

	@SuppressWarnings("deprecation")
	public void clickOpen(Player p, String gui) {
			Inventory inv = Bukkit.createInventory(null, Main.getMain().getConfigYML().getInt("Guis." + gui + ".size"),
					Main.getMain().getConfigYML().getString("Guis." + gui + ".name").replace("&", "§"));
			if(Main.getMain().getConfigYML().getBoolean("Guis."+gui+".background") == true) {
				Random r = new Random();
				int random = r.nextInt(15);
				for (int i = 0; i < inv.getSize(); i++) {
					ItemStack fundo = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) random);
					ItemMeta fundoMeta = fundo.getItemMeta();
					fundoMeta.setDisplayName("§c");
					Glow glow = new Glow(0);
					fundoMeta.addEnchant(glow, 1, false);
					fundo.setItemMeta(fundoMeta);
					inv.setItem(i, fundo);
				}

			}
			for (String itens : Main.getMain().getConfigYML().getConfigurationSection("Guis." + gui + ".itens").getKeys(false)) {
				ItemStack item = new ItemStack(
						Material.getMaterial(
								Main.getMain().getConfigYML().getInt("Guis." + gui + ".itens." + itens + ".id")),
						1, (short) Main.getMain().getConfigYML().getInt("Guis." + gui + ".itens." + itens + ".data"));
				
				ItemMeta itemMeta = item.getItemMeta();
				itemMeta.setDisplayName(Main.getMain().getConfigYML().getString("Guis." + gui + ".itens." + itens + ".name").replace("&", "§"));
				
				ArrayList<String> lore = new ArrayList<>();
				for(String loreList : Main.getMain().getConfigYML().getStringList("Guis." + gui + ".itens." + itens + ".lore")) {
					lore.add(loreList.replace("&", "§"));
				}
				itemMeta.setLore(lore);
				if(Main.getMain().getConfigYML().getBoolean("Guis." + gui + ".itens." + itens + ".glow") == true) {
					Glow glow = new Glow(0);
					itemMeta.addEnchant(glow, 1, false);
				}
				
				item.setItemMeta(itemMeta);
				inv.setItem(Main.getMain().getConfigYML().getInt("Guis." + gui + ".itens." + itens + ".pos"), item);

			}

			p.openInventory(inv);
		
	}

}
