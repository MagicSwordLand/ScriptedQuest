package net.brian.scriptedquests.compability.pyrofish.events;

import org.bukkit.entity.Player;

public class PyroFishingEvent extends PyroEvent {

    public PyroFishingEvent(Player player, String tier, int id) {
        super(player, tier, id);
    }

}
