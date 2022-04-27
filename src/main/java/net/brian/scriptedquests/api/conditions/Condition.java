package net.brian.scriptedquests.api.conditions;

import org.bukkit.entity.Player;

public interface Condition {

    boolean test(Player player);


    public static boolean test(Player player,Condition[] conditions){
        for (Condition condition : conditions) {
            if(!condition.test(player)){
                return false;
            }
        }
        return true;
    }

}
