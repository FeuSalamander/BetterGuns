package me.feusalamander.betterguns.betterguns;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

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
        }
    }
}
