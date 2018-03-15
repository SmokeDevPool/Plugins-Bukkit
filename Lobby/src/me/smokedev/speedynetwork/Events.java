package me.smokedev.speedynetwork;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Events implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.setMaxHealth(2.0);
		p.setHealth(2.0); 
		
		if (p.getName().equalsIgnoreCase("SmokeDev")) {
			p.sendMessage("§a§lVocê desenvolveu esse Plugin");
		}

		if (!Main.getAdmin().contains(p)) {
			Location loc = new Location(Bukkit.getWorld(Main.getMain().getDataYML().getString("Lobby.World")),
					Main.getMain().getDataYML().getDouble("Lobby.X"), Main.getMain().getDataYML().getDouble("Lobby.Y"),
					Main.getMain().getDataYML().getDouble("Lobby.Z"), Main.getMain().getDataYML().getLong("Lobby.Yaw"),
					Main.getMain().getDataYML().getLong("Lobby.Pitch"));
			p.teleport(loc);
			if (Main.getMain().getConfigYML().getBoolean("Configuration.JoinMenssage") == false) {
				e.setJoinMessage(null);

			} else {
				e.setJoinMessage(Main.getMain().getConfigYML().getString("Mensagens.Join").replace("&", "§")
						.replaceAll("%player%", p.getName()));
			}

			if (Main.getMain().getConfigYML().getBoolean("Configuration.clearInventory") == true) {
				p.getInventory().clear();
			}
			// ItemStack item = new ItemStack(Material.COMPASS,1);
			if (Main.getMain().getConfigYML().getBoolean("Configuration.setBackground") == true) {
				Random r = new Random();
				int random = r.nextInt(15);
				for (int i = 0; i < p.getInventory().getSize(); i++) {
					ItemStack fundo = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) random);

					ItemMeta fundoMeta = fundo.getItemMeta();
					fundoMeta.setDisplayName("§c");
					Glow glow = new Glow(0);
					fundoMeta.addEnchant(glow, 1, false);
					fundo.setItemMeta(fundoMeta);
					p.getInventory().setItem(i, fundo);

				}
			}
			for (String itens : Main.getMain().getConfigYML().getConfigurationSection("Itens").getKeys(false)) {
				ItemStack compass = new ItemStack(Material.getMaterial(Main.getMain().getConfigYML().getInt("Itens." + itens + ".id")), 1);
				
				ItemMeta compassMeta = compass.getItemMeta();
				
				compassMeta.setDisplayName(Main.getMain().getConfigYML().getString("Itens." + itens + ".name").replaceAll("&", "§"));
				
				ArrayList<String> loreList = new ArrayList<>();
				
				for (String c : Main.getMain().getConfigYML().getStringList("Itens." + itens + ".lore")) {
					loreList.add(c.replace("&", "§"));
				}
				compassMeta.setLore(loreList);
				
				compass.setItemMeta(compassMeta);

				p.getInventory().setItem(Main.getMain().getConfigYML().getInt("Itens." + itens + ".pos"), compass);

			}

		}

	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (!Main.getAdmin().contains(p)) {
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				for (String itens : Main.getMain().getConfigYML().getConfigurationSection("Itens").getKeys(false)) {
					if (p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(
							Main.getMain().getConfigYML().getString("Itens." + itens + ".name").replaceAll("&", "§"))) {
						if (!Main.getMain().getConfigYML().getString("Itens." + itens + ".gui")
								.equalsIgnoreCase("null")) {
							Main.getGuis().clickOpen(p,
									Main.getMain().getConfigYML().getString("Itens." + itens + ".gui"));
						}
					}
					for (String comandos : Main.getMain().getConfig().getStringList("Itens." + itens + ".comando")) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), comandos);
					}
				}
			}

		}

	}

	@EventHandler
	public void onInventory(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (!Main.getAdmin().contains(p)) {
			for (String gui : Main.getMain().getConfigYML().getConfigurationSection("Guis").getKeys(false)) {
				Inventory inv = Bukkit.createInventory(null,
						Main.getMain().getConfigYML().getInt("Guis." + gui + ".size"),
						Main.getMain().getConfigYML().getString("Guis." + gui + ".name").replace("&", "§"));
				if (e.getInventory().getName().equalsIgnoreCase(inv.getName())) {
					for (String itens : Main.getMain().getConfigYML().getConfigurationSection("Guis." + gui + ".itens")
							.getKeys(false)) {
						try {
							if (e.getCurrentItem().getItemMeta().getDisplayName()
									.equalsIgnoreCase(Main.getMain().getConfigYML()
											.getString("Guis." + gui + ".itens." + itens + ".name")
											.replace("&", "§"))) {
								for (String command : Main.getMain().getConfigYML()
										.getStringList("Guis." + gui + ".itens." + itens + ".comando")) {
									Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);

								}
								for (String menssagem : Main.getMain().getConfigYML()
										.getStringList("Guis." + gui + ".itens." + itens + ".mensagem")) {
									Bukkit.broadcastMessage(menssagem.replace("&", "§").replace("%player%", p.getName())
											.replace("%server%",
													Main.getMain().getConfigYML()
															.getString("Guis." + gui + ".itens." + itens + ".name")
															.replace("&", "§")));
								}

							}
						} catch (NullPointerException e2) {
							// TODO: handle exception
							e.setCancelled(true);
						}

					}
				}
			}
			e.setCancelled(true);
			e.getWhoClicked().closeInventory();
		}
	}

	@EventHandler
	public void onBuilder(BlockPlaceEvent e) {
		if (!Main.getAdmin().contains((Player) e.getPlayer())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBeak(BlockBreakEvent e) {
		if (!Main.getAdmin().contains((Player) e.getPlayer())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (Main.getMain().getConfigYML().getBoolean("Configuration.QuitMenssage") == false) {
			e.setQuitMessage(null);

		} else {
			e.setQuitMessage(Main.getMain().getConfigYML().getString("Mensagens.Quit").replace("&", "§")
					.replaceAll("%player%", p.getName()));
		}
	}

	@EventHandler
	public void onQuit(PlayerKickEvent e) {
		Player p = e.getPlayer();
		if (Main.getMain().getConfigYML().getBoolean("Configuration.QuitMenssage") == false) {
			e.setLeaveMessage(null);

		} else {
			e.setLeaveMessage(Main.getMain().getConfigYML().getString("Mensagens.Quit").replace("&", "§")
					.replaceAll("%player%", p.getName()));
		}
	}
	@EventHandler
	public void onDamager(EntityDamageEvent e) {
		Player p = (Player) e.getEntity();
		if(!Main.getAdmin().contains(p)) {
			e.setCancelled(true);
		}
	}

}
