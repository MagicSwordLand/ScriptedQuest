package net.brian.scriptedquests.rewards;

import org.bukkit.entity.Player;

public abstract class Reward {

    Number amount;

    public Reward(Number amount){
        this.amount = amount;
    }


    public abstract void give(Player player,Number number);

    protected abstract String getMessage(Number amount);

    public LootBag generateLootBag(){
        return new LootBag(this);
    }

    public static class LootBag{
        Reward reward;
        String message;
        Number amount;

        public LootBag(Reward reward){
            amount = reward.amount.doubleValue();
            this.reward = reward;
            message = reward.getMessage(amount);
        }

        public String getMessage() {
            return message;
        }

        public void give(Player player){
            reward.give(player,amount);
        }

    }

}
