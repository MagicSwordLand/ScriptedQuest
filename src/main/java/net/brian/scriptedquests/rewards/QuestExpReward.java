package net.brian.scriptedquests.rewards;

import net.brian.questlevel.data.PlayerQuestLevel;
import org.bukkit.entity.Player;

import java.util.Random;

public class QuestExpReward implements Reward{


    private final RandomAmount amount;
    public QuestExpReward(RandomAmount amount){
        this.amount = amount;
    }

    @Override
    public void give(Player player) {
        PlayerQuestLevel.get(player.getUniqueId()).ifPresent(data->{
            data.addPoints(amount.getInt());
        });
    }
}
