package net.brian.scriptedquests.api.conditions;

import org.bukkit.entity.Player;

public class HasPermission implements Condition{

    private final String perm;
    public HasPermission(String perm){
        this.perm = perm;
    }

    @Override
    public boolean test(Player player) {
        return player.hasPermission(perm);
    }

}
