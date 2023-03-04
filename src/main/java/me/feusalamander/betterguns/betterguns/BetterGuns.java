package me.feusalamander.betterguns.betterguns;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class BetterGuns extends JavaPlugin {
    public GunList gunlist;
    public List<Gun> guns;
    @Override
    public void onEnable() {
        logger.info("BetterGuns by FeuSalamander is loading !");
        gunlist = new GunList(this);
        for(String gun : gunlist.config.getKeys(false)){
            final ConfigurationSection section = gunlist.config.getConfigurationSection(gun);
            final Gun newgun = new Gun(section.getString("name"));
            guns.add(newgun);
        }
    }

    @Override
    public void onDisable() {
        logger.info("BetterGuns by FeuSalamander is disabled !");
    }
}
