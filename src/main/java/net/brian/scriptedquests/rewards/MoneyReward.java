package net.brian.scriptedquests.rewards;

import net.brian.scriptedquests.compability.vault.EconomyProvider;
import org.bukkit.entity.Player;

import java.util.Random;

public class MoneyReward implements Reward{


    RandomAmount randomAmount;
    public MoneyReward(RandomAmount randomAmount){
        this.randomAmount = randomAmount;
    }
    public MoneyReward(float amount){
        randomAmount = new RandomAmount(amount,amount);
    }

    @Override
    public void give(Player player) {
        EconomyProvider.give(player, randomAmount.getFloat());
    }

}
