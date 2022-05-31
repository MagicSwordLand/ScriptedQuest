package net.brian.scriptedquests.rewards;

import net.Indyuce.mmoitems.MMOItems;
import net.brian.itemleveling.ItemLeveling;
import net.brian.playerdatasync.util.ReturnItems;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MMOReward implements Reward{

    private final String type,id;
    private final int tier,level;
    private final RandomAmount amount;


    public MMOReward(String type,String id,RandomAmount amount,int tier,int level){
        this.type =type;
        this.id = id;
        this.amount = amount;
        this.tier = tier;
        this.level = level;
    }

    public MMOReward(String type,String id,RandomAmount amount){
        this(type,id,amount,1,0);
    }

    public MMOReward(String type,String id){
        this(type,id,new RandomAmount(1,1),1,0);
    }


    @Override
    public void give(Player player) {
        ItemStack itemStack = MMOItems.plugin.getItem(type,id);
        if(itemStack != null){
            itemStack.setAmount(amount.getInt());
            ReturnItems.process(player,
                    ItemLeveling.getInstance().getItemService().getItem(itemStack,tier,level));
        }
    }

}
