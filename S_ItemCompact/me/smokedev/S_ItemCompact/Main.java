package me.smokedev.S_ItemCompact;

import org.bukkit.plugin.java.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.meta.*;
import java.util.*;
import java.lang.reflect.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

public class Main extends JavaPlugin
{
    public static Main instance;
    public Inventory inventory;
    
    public Main() {
        this.inventory = Bukkit.createInventory((InventoryHolder)null, this.getConfig().getInt("Menu.tamanho"), this.getConfig().getString("Menu.Nome").replace("&", "§"));
    }
    
    public static Main getInst() {
        return Main.instance;
    }
    
    public void onEnable() {
        Main.instance = this;
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("[ItemCompact] Ativando plugin...");
        System.out.println("[ItemCompact] Plugin Ativado.");
        System.out.println(" ");
        System.out.println(" ");
        this.registerGlow();
        Bukkit.getPluginManager().registerEvents((Listener)new Compactar(), (Plugin)this);
        this.getConfig().addDefault("config.yml", (Object)true);
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }
    
    public void onDisable() {
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("[ItemCompact] Desativando plugin...");
        System.out.println("[ItemCompact] Plugin Ativado.");
        System.out.println(" ");
        System.out.println(" ");
    }
    
    public void configurateInventory() {
        final Glow glow = new Glow(70);
        final ItemStack fundo = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)8);
        final ItemMeta fundom = fundo.getItemMeta();
        fundom.addEnchant((Enchantment)glow, 1, true);
        fundom.setDisplayName("§c ");
        fundo.setItemMeta(fundom);
        for (int i = 0; i < this.getConfig().getInt("Menu.tamanho"); ++i) {
            this.inventory.setItem(i, fundo);
        }
        for (final String itens : this.getConfig().getConfigurationSection("Menu.Itens").getKeys(false)) {
            final String lore = this.getConfig().getString("Menu.Itens." + itens + ".ItemGUI.Lore.descricao");
            final boolean activelore = this.getConfig().getBoolean("Menu.Itens." + itens + ".ItemGUI.Lore.ativar");
            final int slot = this.getConfig().getInt("Menu.Itens." + itens + ".ItemGUI.Slot");
            final ItemStack item = new ItemStack(Material.getMaterial(this.getConfig().getInt("Menu.Itens." + itens + ".ItemGUI.ID")), 1, (short)this.getConfig().getInt("Menu.Itens." + itens + ".ItemGUI.Data"));
            final ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(this.getConfig().getString("Menu.Itens." + itens + ".ItemGUI.Nome").replace("&", "§"));
            if (activelore) {
                if (lore.contains("\n")) {
                    itemMeta.setLore((List)Arrays.asList(lore.replace("&", "§").replaceAll("&([0-9a-f])", "§$1").split("\n")));
                }
                else if (!lore.contains("\n")) {
                    itemMeta.setLore((List)Arrays.asList(lore.replace("&", "§")));
                }
            }
            item.setItemMeta(itemMeta);
            this.inventory.setItem(slot, item);
        }
    }
    
    public void registerGlow() {
        try {
            final Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            final Glow glow = new Glow(70);
            Enchantment.registerEnchantment((Enchantment)glow);
        }
        catch (IllegalArgumentException ex) {}
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final Player p = (Player)sender;
        if (command.getName().equalsIgnoreCase("itemcompact")) {
            this.configurateInventory();
            p.openInventory(this.inventory);
            p.updateInventory();
            return true;
        }
        return false;
    }
}
