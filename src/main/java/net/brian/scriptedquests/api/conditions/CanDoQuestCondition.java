package net.brian.scriptedquests.api.conditions;

import net.brian.scriptedquests.api.player.PlayerQuestData;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import org.bukkit.entity.Player;

public class CanDoQuestCondition implements Condition{

    private String questID;
    boolean canRedo;

    public  CanDoQuestCondition(String questID,boolean canRedo){
        this.questID = questID;
        this.canRedo = canRedo;
    }

    @Override
    public boolean test(Player player) {
        return PlayerQuestData.get(player.getUniqueId())
                .map(data->{
                    if(data.isDoing(questID)){
                        return false;
                    }
                    return canRedo || !data.hasFinished(questID);
                }).orElse(false);
    }

}
