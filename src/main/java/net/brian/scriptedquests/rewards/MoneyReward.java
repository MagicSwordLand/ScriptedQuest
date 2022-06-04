package net.brian.scriptedquests.rewards;

import net.brian.scriptedquests.compability.vault.EconomyProvider;
import org.bukkit.entity.Player;

public class MoneyReward extends Reward {



    public MoneyReward(Number amount){
        super(amount);
    }

    @Override
    public void give(Player player,Number number) {
        EconomyProvider.give(player, number.floatValue());
    }

    @Override
    protected String getMessage(Number amount) {
        return "§a➯ §7錢幣: §f"+amount;
    }


}
