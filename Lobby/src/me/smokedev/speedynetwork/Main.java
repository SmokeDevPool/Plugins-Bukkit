package me.smokedev.speedynetwork;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private static ArrayList<Player> admin = new ArrayList<>();
	private static Main main;
	private static Guis guis;
	private File config;
	private YamlConfiguration config2;

	private File data;
	private YamlConfiguration data2;

	public YamlConfiguration getConfigYML() {
		return config2;
	}

	public YamlConfiguration getDataYML() {
		return data2;
	}

	public void registerGlow() {
		try {
			Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Glow glow = new Glow(70);
			Enchantment.registerEnchantment(glow);
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createFile() {
		File configF = new File(getDataFolder(), "config.yml");
		File dataF = new File(getDataFolder(), "data.yml");
		if (!configF.exists()) {
			saveResource("config.yml", false);
		}
		if (!dataF.exists()) {
			saveResource("data.yml", false);
		}
		config = new File(getDataFolder(), "config.yml");
		this.config2 = YamlConfiguration.loadConfiguration(config);

		data = new File(getDataFolder(), "data.yml");
		this.data2 = YamlConfiguration.loadConfiguration(data);
	}

	public void saveFile() {
		try {
			config2.save(config);
			data2.save(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadFile() {
		try {
			config2.load(config);
			data2.load(data);
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Main getMain() {
		return main;
	}

	public static Guis getGuis() {
		return guis;
	}

	public static ArrayList<Player> getAdmin() {
		return admin;
	}

	@Override
	public void onEnable() {
		main = this;
		guis = new Guis();
		createFile();
		loadFile();
		saveFile();
		registerGlow();
		System.out.println("[Lobby] Plugin ativado.");
		Bukkit.getPluginManager().registerEvents(new Events(), this);
	}

	@Override
	public void onDisable() {
		System.out.println("[Lobby] Plugin Desativado.");

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("Impossivel, voce nao é um jogador");
			return true;
		}
		Player p = (Player) sender;
		if (p.hasPermission("admin.mode")) {

			if (command.getName().equalsIgnoreCase("admin")) {

				if (args.length == 0) {
					p.sendMessage("§aUse:§7 /admin <§aon§7/§coff§7>");
					return true;
				}
				if (args.length == 1) {
					String cmd = args[0];

					if (cmd.equalsIgnoreCase("on")) {
						if (Main.getAdmin().contains(p)) {
							p.sendMessage("§7Você já esta no modo admin!");
							return true;
						}
						p.setMaxHealth(20.0);
						p.setHealth(20.0); 
						Main.getAdmin().add(p);
						p.sendMessage("§aVocê entrou no modo admin!");
					} else if (cmd.equalsIgnoreCase("off")) {
						if (!Main.getAdmin().contains(p)) {
							p.sendMessage("§7Você não esta no modo admin!");
							return true;
						}
						p.setMaxHealth(2.0);
						p.setHealth(2.0); 
						Main.getAdmin().remove(p);
						p.sendMessage("§cVocê saiu do modo admin!");
					}

				}

			}

			if (command.getName().equalsIgnoreCase("setlobby")) {

				Main.getMain().getDataYML().set("Lobby.X", p.getLocation().getX());
				Main.getMain().getDataYML().set("Lobby.Y", p.getLocation().getY());
				Main.getMain().getDataYML().set("Lobby.Z", p.getLocation().getZ());
				Main.getMain().getDataYML().set("Lobby.Yaw", p.getLocation().getYaw());
				Main.getMain().getDataYML().set("Lobby.Pitch", p.getLocation().getPitch());
				Main.getMain().getDataYML().set("Lobby.World", p.getLocation().getWorld().getName());
				saveFile();
				p.sendMessage("§a[SMOKEDEV] Lobby setado com sucesso!");

			}
		} else {
			p.sendMessage("§cvocê não tem permissao!");
			return true;
		}

		return false;
	}

}
