package net.brian.scriptedquests.api.conditions;

import org.bukkit.entity.Player;

public class DayTimeCondition implements Condition{

    @Override
    public boolean test(Player player) {
        long time = player.getWorld().getTime();
        return time < 12300 || time > 23850;
    }

}
