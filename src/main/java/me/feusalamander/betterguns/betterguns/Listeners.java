package me.feusalamander.betterguns.betterguns;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
@SuppressWarnings("deprecation")
public class Listeners implements Listener {
    private final BetterGuns main;
    public Listeners(BetterGuns main){
        this.main = main;
    }
    private boolean isGun(final ItemStack item){
        if(item == null||!item.hasItemMeta()||!item.getItemMeta().hasDisplayName())return false;
        final String name = ChatColor.stripColor(Objects.requireNonNull(item).getItemMeta().getDisplayName());
        String[] split = name.split(" ");
        if(!main.names.contains(split[0]))return false;
        return true;
    }
    @EventHandler
    private void oncommand(final PlayerCommandPreprocessEvent e){
        final String m = e.getMessage();
        final Player p = e.getPlayer();
        if(m.equalsIgnoreCase("/bg list")){
            p.sendMessage("Gun List");
            for(String name : main.names){
                p.sendMessage("- "+name);
            }
        }else if(m.startsWith("/bg get ")){
            final String name = m.replace("/bg get ", "");
            if(main.names.contains(name)){
                for(Gun gun : main.guns){
                    if(gun.name.equalsIgnoreCase(name)){
                        final ItemStack item = gun.getItem();
                        if(item == null){
                            p.sendMessage("§cThere is a problem with the configuration of the gun");
                            return;
                        }
                        p.getInventory().addItem(gun.getItem());
                        return;
                    }
                }
            }
        }
    }
    @EventHandler
    private void onDestroy(final BlockBreakEvent e){
        final Player p = e.getPlayer();
        final ItemStack item = p.getItemInHand();
        if(isGun(item))e.setCancelled(true);
    }
    @EventHandler
    private void onDamage(final EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof final Player p))return;
        final ItemStack item = p.getItemInHand();
        if(isGun(item))e.setCancelled(true);
    }
    @EventHandler
    private void onDrop(final PlayerDropItemEvent e){
        final ItemStack item = e.getItemDrop().getItemStack();
        final ItemMeta meta = item.getItemMeta();
        if(!meta.hasDisplayName())return;
        final String name = ChatColor.stripColor(Objects.requireNonNull(meta).getDisplayName());
        for(String s : name.split(" ")){
            if(main.names.contains(s)){
                BetterGuns.playerdrop.add(e.getPlayer());
                e.setCancelled(true);
                for(Gun gun : main.guns){
                    if(s.equalsIgnoreCase(gun.name)){
                        gun.reload(item, e.getPlayer());
                        return;
                    }
                }
            }else return;
        }
    }
    @EventHandler
    private void onClick(final InventoryClickEvent e){
        final ItemStack item = e.getCurrentItem();
        final ItemStack item2 = e.getCursor();
        if(!(isGun(item)||isGun(item2)))return;
        final InventoryType inv = e.getView().getTopInventory().getType();
        if(!(inv.equals(InventoryType.CRAFTING)||
                inv.equals(InventoryType.CHEST)||
                inv.equals(InventoryType.ENDER_CHEST)||
                inv.equals(InventoryType.SHULKER_BOX)||
                inv.equals(InventoryType.BARREL))){
            e.setCancelled(true);
        }
    }
}
