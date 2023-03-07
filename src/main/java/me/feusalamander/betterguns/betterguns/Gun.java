package me.feusalamander.betterguns.betterguns;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@SuppressWarnings("deprecation")

public class Gun implements Listener {
    public final String name;
    private final String id;
    private final Double damage;
    private boolean click = false;
    private final String killmsg;
    private final int maxammo;
    private final int reloadtime;
    private final int cooldown;
    private final List<Arrow> arrows = new ArrayList<>();
    private final List<String> noshoot = new ArrayList<>();
    public Gun(final String name,
               final String id,
               final String click,
               final Double damage,
               final String killmsg,
               final int maxammo,
               final int reloadtime,
               final int cooldown){
        this.name = name;
        this.id = id;
        if(click.equalsIgnoreCase("right"))this.click = true;
        this.damage = damage;
        this.killmsg = killmsg;
        this.maxammo = maxammo;
        this.reloadtime = reloadtime;
        this.cooldown = cooldown;
    }
    public ItemStack getItem(){
        final Material material = Material.getMaterial(id);
        if(material == null)return null;
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§a"+name+" <"+maxammo+">");
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
                assert killer != null;
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
        if(BetterGuns.playerdrop.contains(e.getPlayer())){
            BetterGuns.playerdrop.remove(e.getPlayer());
            return;
        }
        if((e.getAction().isRightClick()&&click)||(e.getAction().isLeftClick()&&!click)){
            shoot(p);
        }else {
            other(p);
        }
    }
    private void shoot(final Player p){
        ItemMeta meta = p.getItemInHand().getItemMeta();
        String[] split = meta.getDisplayName().split(" ");
        final int ammo = Integer.parseInt(StringUtils.substringBetween(split[1],"<",">"));
        if(ammo<=0){
            reload(p.getItemInHand(), p);
        }else{
            if(cooldown > 0){
                if(!noshoot.contains(p.getName())){noshoot.add(p.getName());}else{return;}
                Bukkit.getScheduler().runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("BetterGuns")), () -> noshoot.remove(p.getName()), cooldown);
            }
            Arrow a = p.launchProjectile(Arrow.class);
            arrows.add(a);
            a.setVelocity(p.getLocation().getDirection().multiply(1.5));
            meta.setDisplayName(split[0]+" <"+(ammo-1)+">");
            p.getItemInHand().setItemMeta(meta);

        }

    }
    private void other(final Player p){

    }
    public void reload(ItemStack item, Player p){
        String name = item.getItemMeta().getDisplayName();
        if(name.endsWith("r"))return;
        String ammo = StringUtils.substringBetween(name, "<", ">");
        if(Integer.parseInt(ammo) == maxammo)return;
        String n = name.replaceAll(ammo, String.valueOf(maxammo)).replaceAll("r", "");
        ItemMeta meta = item.getItemMeta();
        if(reloadtime == 0){
            meta.setDisplayName(n);
            item.setItemMeta(meta);
            return;
        }
        meta.setDisplayName(name+"r");
        item.setItemMeta(meta);
        Bukkit.getScheduler().runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("BetterGuns")), () -> {
            if(p.getInventory().contains(item)){
                int slot = p.getInventory().first(item);
                meta.setDisplayName(n);
                item.setItemMeta(meta);
                p.getInventory().setItem(slot, item);
            }
        }, reloadtime);
    }
    public void clear(){
        for(Arrow arrow : arrows){
            arrow.remove();
        }
        arrows.clear();
    }
}
