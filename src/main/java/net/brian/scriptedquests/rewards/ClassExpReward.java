package net.brian.scriptedquests.rewards;

import net.brian.classcore.players.PlayerClassProfile;
import org.bukkit.entity.Player;

import java.util.Random;

public class ClassExpReward implements Reward {

    RandomAmount randomAmount;
    public ClassExpReward(RandomAmount randomAmount){
        this.randomAmount = randomAmount;
    }

    public ClassExpReward(int amount){
        this(new RandomAmount(amount,amount));
    }


    @Override
    public void give(Player player) {
        PlayerClassProfile.get(player.getUniqueId()).ifPresent(profile -> {
            profile.addExp(randomAmount.getInt());
        });
    }

}
