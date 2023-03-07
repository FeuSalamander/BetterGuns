package me.feusalamander.betterguns.betterguns;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public final class BetterGuns extends JavaPlugin {
    public GunList gunlist;
    public List<Gun> guns = new ArrayList<>();
    public List<String> names = new ArrayList<>();
    public static List<Player> playerdrop = new ArrayList<>();
    @Override
    public void onEnable() {
        logger.info("BetterGuns by FeuSalamander is loading !");
        gunlist = new GunList(this);
        Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
        Objects.requireNonNull(getCommand("bg")).setTabCompleter(new Tab(names));
        createguns();
        getnames();
        for(Gun gun : guns)Bukkit.getPluginManager().registerEvents(gun, this);
    }
    @Override
    public void onDisable() {
        logger.info("BetterGuns by FeuSalamander is disabled !");
        for(Gun gun : guns)gun.clear();
    }
    private void createguns(){
        for(String gun : gunlist.config.getKeys(false)){
            final ConfigurationSection section = gunlist.config.getConfigurationSection(gun);
            assert section != null;
            final Gun newgun = new Gun(
                    section.getString("name"),
                    section.getString("id"),
                    Objects.requireNonNull(section.getString("click")),
                    section.getDouble("damage"),
                    section.getString("kill-message"),
                    section.getInt("max-ammo"),
                    section.getInt("reload-time"),
                    section.getInt("cooldown"),
                    section.getString("sound.reload"),
                    section.getString("sound.shoot"));
            guns.add(newgun);
        }
    }
    private void getnames(){
        for(Gun gun : guns){
            names.add(gun.name);
        }
    }
}
