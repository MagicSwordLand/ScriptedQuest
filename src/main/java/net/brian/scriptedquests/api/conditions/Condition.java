package net.brian.scriptedquests.api.conditions;

import org.bukkit.entity.Player;

import java.util.Collection;

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

    public static boolean test(Player player, Collection<Condition> conditions){
        for (Condition condition : conditions) {
            if(!condition.test(player)){
                return false;
            }
        }
        return true;
    }
}
