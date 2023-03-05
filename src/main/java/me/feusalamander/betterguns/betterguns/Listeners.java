package me.feusalamander.betterguns.betterguns;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Listeners implements Listener {
    private final BetterGuns main;
    public Listeners(BetterGuns main){
        this.main = main;
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
        if(item.getType().equals(Material.AIR)||!item.getItemMeta().hasDisplayName())return;
        final String name = ChatColor.stripColor(Objects.requireNonNull(item).getItemMeta().getDisplayName());
        for(String s : name.split(" ")){
            if(main.names.contains(s))e.setCancelled(true);
        }
    }
    @EventHandler
    private void onDamage(final EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player))return;
        final Player p = (Player) e.getDamager();
        final ItemStack item = p.getItemInHand();
        if(item.getType().equals(Material.AIR)||!item.getItemMeta().hasDisplayName())return;
        final String name = ChatColor.stripColor(Objects.requireNonNull(item).getItemMeta().getDisplayName());
        for(String s : name.split(" ")){
            if(main.names.contains(s))e.setCancelled(true);
        }
    }
}
