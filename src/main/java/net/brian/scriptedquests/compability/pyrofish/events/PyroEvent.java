package net.brian.scriptedquests.compability.pyrofish.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class PyroEvent extends Event {

    private static final HandlerList handlerList = new HandlerList();

    private final Player player;
    private final String tier;
    private final int id;


    public PyroEvent(Player player, String tier, int id){
        this.player = player;
        this.tier = tier;
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public int getId() {
        return id;
    }

    public String getTier() {
        return tier;
    }

    public static HandlerList getHandlerList(){
        return handlerList;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
