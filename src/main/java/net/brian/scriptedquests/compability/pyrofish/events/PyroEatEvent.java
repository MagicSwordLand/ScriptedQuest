package net.brian.scriptedquests.compability.pyrofish.events;

import org.bukkit.entity.Player;

public class PyroEatEvent extends PyroEvent {

    public PyroEatEvent(Player player, String tier, int id) {
        super(player, tier, id);
    }

}
