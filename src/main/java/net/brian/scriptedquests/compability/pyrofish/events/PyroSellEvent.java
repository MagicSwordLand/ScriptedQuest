package net.brian.scriptedquests.compability.pyrofish.events;

import org.bukkit.entity.Player;

public class PyroSellEvent extends PyroEvent {

    public PyroSellEvent(Player player, String tier, int id) {
        super(player, tier, id);
    }
}
