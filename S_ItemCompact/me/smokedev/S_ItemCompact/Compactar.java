package me.smokedev.S_ItemCompact;

import org.bukkit.event.player.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.inventory.*;

public class Compactar implements Listener
{
    @EventHandler
    public void onJoinedSmoke(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        if (p.getName().equalsIgnoreCase("ySmokeBR")) {
            p.sendMessage("§4§l");
            p.sendMessage("§4§l");
            p.sendMessage("§4§l");
            p.sendMessage("§4§l");
            p.sendMessage("§4§l");
            p.sendMessage("§4§l");
            p.sendMessage("§4§l");
            p.sendMessage("§4§l[!] Esse servidor utiliza o seu plugin S_ItemCompact");
        }
    }
    
    @EventHandler
    public void onClickedEvent(final InventoryClickEvent e) {
        final Player p = (Player)e.getWhoClicked();
        try {
            if (e.getInventory().getTitle().equalsIgnoreCase(Main.getInst().getConfig().getString("Menu.Nome").replace("&", "§"))) {
                if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
                    e.setCancelled(true);
                    return;
                }
                for (final String itens : Main.getInst().getConfig().getConfigurationSection("Menu.Itens").getKeys(false)) {
                    if (e.getCurrentItem().getItemMeta().getDisplayName().contains(Main.getInst().getConfig().getString("Menu.Itens." + itens + ".ItemGUI.Nome").replace("&", "§"))) {
                        try {
                            if (p.hasPermission(Main.getInst().getConfig().getString("Menu.Itens." + itens + ".compactar.item-config.PERMISSAO"))) {
                                final String arg = Main.getInst().getConfig().getString("Menu.Itens." + itens + ".compactar.item-config.IDFROM");
                                final String arg2 = Main.getInst().getConfig().getString("Menu.Itens." + itens + ".compactar.item-config.IDTO");
                                final String[] args = String.valueOf(arg).split(":");
                                final String[] args2 = String.valueOf(arg2).split(":");
                                final ItemStack it = new ItemStack(Material.getMaterial(Integer.parseInt(args[0])), p.getInventory().getMaxStackSize(), (short)Integer.parseInt(args[1]));
                                if (arg.contains(":")) {
                                    final int min = 9;
                                    final ItemStack[] getContents = p.getInventory().getContents();
                                    final boolean itensContains = p.getInventory().containsAtLeast(it, min);
                                    final PlayerInventory pi = p.getInventory();
                                    if (itensContains) {
                                        int cuantity = 0;
                                        final Material m = Material.getMaterial(Main.getInst().getConfig().getString("Menu.Itens." + itens + ".compactar.item-config.MATERIAL"));
                                        for (int i = 0; i < getContents.length; ++i) {
                                            if (getContents[i] != null && getContents[i].getType().equals((Object)m)) {
                                                final int cant = getContents[i].getAmount();
                                                cuantity += cant;
                                            }
                                        }
                                        final float cat = Math.nextUp(cuantity / 9);
                                        final float cat2 = Math.nextUp(cuantity % 9);
                                        final ItemStack resto = new ItemStack(Material.getMaterial(Integer.parseInt(args[0])), (int)cat2, (short)Integer.parseInt(args[1]));
                                        final ItemStack divisao = new ItemStack(Material.getMaterial(Integer.parseInt(args2[0])), (int)cat, (short)Integer.parseInt(args2[1]));
                                        for (int j = 0; j < getContents.length; ++j) {
                                            pi.removeItem(new ItemStack[] { it });
                                        }
                                        if ((int)cat2 > 0) {
                                            pi.addItem(new ItemStack[] { resto });
                                        }
                                        pi.addItem(new ItemStack[] { divisao });
                                        p.sendMessage(Main.getInst().getConfig().getString("Menu.Mensagens.compactado").replaceAll("&", "§"));
                                        p.updateInventory();
                                        p.closeInventory();
                                    }
                                    else if (!itensContains) {
                                        p.sendMessage(Main.getInst().getConfig().getString("Menu.Mensagens.erro").replaceAll("&", "§"));
                                        p.updateInventory();
                                        p.closeInventory();
                                    }
                                }
                            }
                            else {
                                p.sendMessage("§cvoc\u00ea n\u00e3o tem permiss\u00e3o para compactar " + Main.getInst().getConfig().getString("Menu.Itens." + itens + ".ItemGUI.Nome").replace("&", "§"));
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException eae) {
                            p.closeInventory();
                        }
                        e.setCancelled(true);
                    }
                    else {
                        e.setCancelled(true);
                    }
                }
            }
        }
        catch (NullPointerException ex) {
            e.setCancelled(true);
        }
    }
}
