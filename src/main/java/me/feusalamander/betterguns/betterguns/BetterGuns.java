package me.feusalamander.betterguns.betterguns;

import org.bukkit.plugin.java.JavaPlugin;

public final class BetterGuns extends JavaPlugin {
    public Configuration config;

    @Override
    public void onEnable() {
        logger.info("BetterGuns by FeuSalamander is loading !");
        config = new Configuration(this);
    }

    @Override
    public void onDisable() {
        logger.info("BetterGuns by FeuSalamander is disabled !");
    }
}
