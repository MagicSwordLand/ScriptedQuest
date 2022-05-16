package net.brian.scriptedquests.api.conditions;

import org.bukkit.entity.Player;

public class DayTimeCondition implements Condition{

    boolean day;

    public DayTimeCondition(boolean day){
        this.day = day;
    }

    @Override
    public boolean test(Player player) {
        long time = player.getWorld().getTime();
        boolean isDay = time < 12300 || time > 23850;
        if(day){
            return isDay;
        }
        return !isDay;
    }
}
