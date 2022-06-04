package net.brian.scriptedquests.rewards;

import net.brian.questlevel.data.PlayerQuestLevel;
import org.bukkit.entity.Player;

public class QuestExpReward extends Reward{

    public QuestExpReward(Number amount){
        super(amount);
    }

    @Override
    public void give(Player player,Number amount) {
        PlayerQuestLevel.get(player.getUniqueId()).ifPresent(data->{
            data.addPoints(amount.intValue());
        });
    }

    @Override
    protected String getMessage(Number amount) {
        return "§a➯ §7傭兵積分: §f"+amount;
    }

}
