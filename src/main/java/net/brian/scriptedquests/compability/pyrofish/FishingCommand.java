package net.brian.scriptedquests.compability.pyrofish;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.compability.pyrofish.events.PyroEatEvent;
import net.brian.scriptedquests.compability.pyrofish.events.PyroFishingEvent;
import net.brian.scriptedquests.compability.pyrofish.events.PyroSellEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

public class FishingCommand implements CommandExecutor {


    public FishingCommand(ScriptedQuests plugin){
        plugin.getCommand("fishevent").setExecutor(this);
    }

    // /fishevent player sell/eat/fish tier id
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("fishevent.admin")){
            if(args.length > 3){
                Player player = Bukkit.getPlayer(args[0]);
                String action = args[1];
                String tier = args[2];
                int id = Integer.parseInt(args[3]);

                switch (action) {
                    case "sell" -> new PyroSellEvent(player, tier, id).callEvent();
                    case "fish" -> new PyroFishingEvent(player, tier, id).callEvent();
                    case "eat" -> new PyroEatEvent(player, tier, id).callEvent();
                }

            }
        }
        return true;
    }
}
