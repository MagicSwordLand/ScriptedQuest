package net.brian.scriptedquests.api.conditions;

import net.brian.questlevel.data.PlayerQuestLevel;
import org.bukkit.entity.Player;

import java.util.Comparator;

public class QuestLevelCondition implements Condition{

    int targetLevel;


    public QuestLevelCondition(int targetLevel){
        this.targetLevel = targetLevel;
    }

    @Override
    public boolean test(Player player) {
        return PlayerQuestLevel.get(player.getUniqueId())
                .map(data-> data.getLevel() >= targetLevel)
                .orElse(false);
    }

}
