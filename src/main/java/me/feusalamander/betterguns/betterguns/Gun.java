package me.feusalamander.betterguns.betterguns;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Gun implements Listener {
    public final String name;
    private final String id;
    private final Double damage;
    private boolean click = false;
    private final String killmsg;
    private List<Arrow> arrows = new ArrayList<>();
    public Gun(final String name, final String id, final String click, final Double damage, final String killmsg){
        this.name = name;
        this.id = id;
        if(click.equalsIgnoreCase("right"))this.click = true;
        this.damage = damage;
        this.killmsg = killmsg;
    }
    public ItemStack getItem(){
        final Material material = Material.getMaterial(id);
        if(material == null)return null;
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a"+name);
        item.setItemMeta(meta);
        return item;
    }
    @EventHandler
    private void onDamage(ProjectileHitEvent e){
        final Entity entity = e.getEntity();
        if(!(entity instanceof Arrow a&&arrows.contains(a)))return;
        e.setCancelled(true);
        final Entity victim1 = e.getHitEntity();
        final Block block = e.getHitBlock();
        if(victim1 instanceof LivingEntity victim){
            final Player killer = (Player) a.getShooter();
            victim.setKiller(killer);
            victim.damage(damage);
            if(victim.getHealth()<damage&&victim instanceof Player){
                final String message = killmsg.replaceAll("#victim#", victim.getName()).replaceAll("#killer#", killer.getName());
                Bukkit.broadcastMessage(message);
            }
        }else if(block != null){

        }
        arrows.remove(a);
        a.remove();
    }
    @EventHandler
    private void onClick(final PlayerInteractEvent e){
        if(e.getItem() == null||!e.getItem().getItemMeta().hasDisplayName())return;
        final String name = Objects.requireNonNull(e.getItem()).getItemMeta().getDisplayName();
        if(!name.contains(this.name))return;
        final Player p = e.getPlayer();
        if((e.getAction().isRightClick()&&click)||(e.getAction().isLeftClick()&&!click)){
            shoot(p);
        }else {
            other(p);
        }
    }
    private void shoot(final Player p){
        Arrow a = p.launchProjectile(Arrow.class);
        arrows.add(a);
        a.setVelocity(p.getLocation().getDirection().multiply(1.5));
    }
    private void other(final Player p){

    }
    public void clear(){
        for(Arrow arrow : arrows){
            arrow.remove();
        }
        arrows.clear();
    }
}
