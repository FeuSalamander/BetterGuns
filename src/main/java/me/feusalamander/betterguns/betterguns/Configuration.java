package me.feusalamander.betterguns.betterguns;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Configuration {
    private final BetterGuns main;
    private final File f;
    public YamlConfiguration config;
    public Configuration(BetterGuns main){
        this.main = main;
        f = new File(main.getDataFolder(), "guns.yml");
        if(!f.exists()) create();
        config = YamlConfiguration.loadConfiguration(f);

    }
    private void create(){
        try {f.createNewFile();}catch (IOException e) {throw new RuntimeException(e);}
    }
}
