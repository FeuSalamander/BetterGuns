package me.feusalamander.betterguns.betterguns;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class GunList {
    private final BetterGuns main;
    private final File f;
    public YamlConfiguration config;
    public GunList(final BetterGuns main){
        this.main = main;
        f = new File(main.getDataFolder(), "guns.yml");
        if(!f.exists()) main.saveResource("guns.yml", false);
        config = YamlConfiguration.loadConfiguration(f);
    }
}
