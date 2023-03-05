package me.feusalamander.betterguns.betterguns;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class BetterGuns extends JavaPlugin {
    public GunList gunlist;
    public List<Gun> guns = new ArrayList<>();
    public List<String> names = new ArrayList<>();
    @Override
    public void onEnable() {
        logger.info("BetterGuns by FeuSalamander is loading !");
        gunlist = new GunList(this);
        Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
        Objects.requireNonNull(getCommand("bg")).setTabCompleter(new Tab());
        createguns();
        getnames();
    }

    @Override
    public void onDisable() {
        logger.info("BetterGuns by FeuSalamander is disabled !");
    }
    private void createguns(){
        for(String gun : gunlist.config.getKeys(false)){
            final ConfigurationSection section = gunlist.config.getConfigurationSection(gun);
            final Gun newgun = new Gun(section.getString("name"));
            guns.add(newgun);
        }
    }
    private void getnames(){
        for(Gun gun : guns){
            names.add(gun.name);
        }
    }
}
