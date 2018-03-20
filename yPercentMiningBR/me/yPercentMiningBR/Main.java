package me.yPercentMiningBR;

import org.bukkit.plugin.java.*;
import java.io.*;
import org.bukkit.plugin.*;
import org.bukkit.event.block.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.inventory.*;
import org.bukkit.enchantments.*;
import java.util.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;

public class Main extends JavaPlugin implements Listener
{
    public void onEnable() {
        final ConsoleCommandSender bc = Bukkit.getConsoleSender();
        bc.sendMessage("§3-=-=-=-=-=-=-=-=-=-=-=-=-");
        bc.sendMessage("§aPlugin: §7" + this.getDescription().getName());
        bc.sendMessage("§aVersao: §7" + this.getDescription().getVersion());
        bc.sendMessage("§aStats: §7Ativado");
        final File file = new File(this.getDataFolder(), "config.yml");
        if (!file.exists()) {
            bc.sendMessage("§aConfig: §7Criando...");
            this.saveDefaultConfig();
        }
        else {
            bc.sendMessage("§aConfig: §7Recarregando...");
            this.reloadConfig();
        }
        bc.sendMessage("§aDev: §7ySmokeBR");
        bc.sendMessage("§3-=-=-=-=-=-=-=-=-=-=-=-=-");
        Bukkit.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
    }
    
    public void onDisable() {
        final ConsoleCommandSender bc = Bukkit.getConsoleSender();
        bc.sendMessage("§3-=-=-=-=-=-=-=-=-=-=-=-=-");
        bc.sendMessage("§4Plugin: §7" + this.getDescription().getName());
        bc.sendMessage("§4Versao: §7" + this.getDescription().getVersion());
        bc.sendMessage("§4Stats: §7Desativado");
        bc.sendMessage("§4Config: §7Salvada!");
        bc.sendMessage("§4Dev: §7ySmokeBR");
        bc.sendMessage("§3-=-=-=-=-=-=-=-=-=-=-=-=-");
    }
    
    @EventHandler
    public void onBreakBlockPercent(final BlockBreakEvent e) {
        final Random gen = new Random();
        final Random gen2 = new Random();
        for (final String i : this.getConfig().getStringList("Minerios")) {
            if (e.getBlock().getType().equals((Object)Material.getMaterial(Integer.parseInt(i)))) {
                final double s = this.getConfig().getDouble("Chance", 0.0);
                final double s2 = this.getConfig().getDouble("Comandos.Chance", 0.0);
                if (100.0 * gen.nextDouble() >= s) {
                    continue;
                }
                if (this.getConfig().getBoolean("Ativar_Comandos") && 100.0 * gen2.nextDouble() < s2) {
                    for (final String comandos : this.getConfig().getStringList("Comandos.Executar")) {
                        Bukkit.dispatchCommand((CommandSender)Bukkit.getServer().getConsoleSender(), comandos.replace("&", "§").replace("{player}", e.getPlayer().getName()));
                    }
                }
                if (this.getConfig().getBoolean("Ativar_Mensagem_global")) {
                    Bukkit.broadcastMessage(this.getConfig().getString("Mensagem_global").replace("&", "§").replace("{player}", e.getPlayer().getName()));
                    if (this.getConfig().getBoolean("Ativar_Mensagem")) {
                        e.getPlayer().sendMessage(this.getConfig().getString("Mensagem").replace("&", "§"));
                    }
                    else {
                        this.getConfig().getBoolean("Ativar_Mensagem");
                    }
                }
                else if (!this.getConfig().getBoolean("Ativar_Mensagem_global")) {
                    if (this.getConfig().getBoolean("Ativar_Mensagem")) {
                        e.getPlayer().sendMessage(this.getConfig().getString("Mensagem").replace("&", "§"));
                    }
                    else {
                        this.getConfig().getBoolean("Ativar_Mensagem");
                    }
                }
                for (final String itens : this.getConfig().getStringList("Dar_Itens")) {
                    final String[] args = itens.split("-");
                    if (args[0].contains(":")) {
                        final String[] argsd = args[0].split(":");
                        final int id = Integer.valueOf(argsd[0]);
                        final int data = Integer.valueOf(argsd[1]);
                        final int qnt = Integer.valueOf(args[1]);
                        final String nome = args[2];
                        final String encantamentos = args[3];
                        final int porcent = Integer.valueOf(args[4]);
                        final Random r = new Random();
                        final int num = r.nextInt(100);
                        if (num > porcent) {
                            continue;
                        }
                        final ItemStack item = new ItemStack(Material.getMaterial(id), qnt, (short)data);
                        if (!nome.equalsIgnoreCase("0") || !nome.equalsIgnoreCase("0")) {
                            final ItemMeta itemM;
                            (itemM = item.getItemMeta()).setDisplayName(nome.replaceAll("&", "§"));
                            item.setItemMeta(itemM);
                        }
                        if (!encantamentos.equalsIgnoreCase("0") || !encantamentos.equalsIgnoreCase("0")) {
                            final String[] enchants = encantamentos.split(",");
                            String[] array;
                            for (int length = (array = enchants).length, i2 = 0; i2 < length; ++i2) {
                                final String enchant = array[i2];
                                final String[] leveis = enchant.split(":");
                                item.addUnsafeEnchantment(Enchantment.getByName(leveis[0]), (int)Integer.valueOf(leveis[1]));
                            }
                        }
                        if (this.getConfig().getBoolean("Dropar_Itens")) {
                            e.getBlock().getDrops().clear();
                            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), item);
                        }
                        else {
                            if (this.getConfig().getBoolean("Dropar_Itens")) {
                                continue;
                            }
                            e.getPlayer().getInventory().addItem(new ItemStack[] { item });
                            e.getPlayer().updateInventory();
                        }
                    }
                    else {
                        if (args[0].contains(":")) {
                            continue;
                        }
                        final int id2 = Integer.valueOf(args[0]);
                        final int qnt2 = Integer.valueOf(args[1]);
                        final String nome2 = args[2];
                        final String encantamentos2 = args[3];
                        final int porcent2 = Integer.valueOf(args[4]);
                        final Random r2 = new Random();
                        final int num2 = r2.nextInt(100);
                        if (num2 > porcent2) {
                            continue;
                        }
                        final ItemStack item2 = new ItemStack(Material.getMaterial(id2), qnt2);
                        if (!nome2.equalsIgnoreCase("0") || !nome2.equalsIgnoreCase("0")) {
                            final ItemMeta itemM2;
                            (itemM2 = item2.getItemMeta()).setDisplayName(nome2.replaceAll("&", "§"));
                            item2.setItemMeta(itemM2);
                        }
                        if (!encantamentos2.equalsIgnoreCase("0") || !encantamentos2.equalsIgnoreCase("0")) {
                            final String[] enchants2 = encantamentos2.split(",");
                            String[] array2;
                            for (int length2 = (array2 = enchants2).length, i3 = 0; i3 < length2; ++i3) {
                                final String enchant2 = array2[i3];
                                final String[] leveis2 = enchant2.split(":");
                                item2.addUnsafeEnchantment(Enchantment.getByName(leveis2[0]), (int)Integer.valueOf(leveis2[1]));
                            }
                        }
                        if (this.getConfig().getBoolean("Dropar_Itens")) {
                            e.getBlock().getDrops().clear();
                            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), item2);
                        }
                        else {
                            if (this.getConfig().getBoolean("Dropar_Itens")) {
                                continue;
                            }
                            e.getPlayer().getInventory().addItem(new ItemStack[] { item2 });
                            e.getPlayer().updateInventory();
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onJoinDev(final PlayerJoinEvent e) {
        if (e.getPlayer().getName().equalsIgnoreCase("ySmokeBR") || e.getPlayer().getName().equalsIgnoreCase("zLifeHacker")) {
            e.getPlayer().sendMessage("§aEsse servidor usa seu plugin: §f" + this.getDescription().getName());
        }
    }
}
