package net.brian.scriptedquests.rewards;

import net.brian.classcore.players.PlayerClassProfile;
import org.bukkit.entity.Player;

public class ClassExpReward extends Reward {


    public ClassExpReward(Number amount){
        super(amount);
    }



    @Override
    public void give(Player player,Number number) {
        PlayerClassProfile.get(player.getUniqueId()).ifPresent(profile -> {
            profile.addExp(number.intValue());
        });

    }

    @Override
    public String getMessage(Number amount) {
        return "§a➯ §7經驗: §f"+amount;
    }

}
