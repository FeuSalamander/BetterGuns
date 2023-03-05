package me.feusalamander.betterguns.betterguns;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tab implements TabCompleter {
    private List<String> guns;
    public Tab(List<String> guns){
        this.guns = guns;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1){
            return Arrays.asList("list", "get");
        }
        if(args[0].equalsIgnoreCase("get")){
            return guns;
        }
        return Collections.singletonList("");
    }
}
