package net.brian.scriptedquests.rewards;

import net.Indyuce.mmoitems.MMOItems;
import net.brian.itemleveling.ItemLeveling;
import net.brian.playerdatasync.util.ReturnItems;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MMOReward extends Reward{

    private final String type,id;
    private final int tier,level;


    public MMOReward(String type, String id, Number amount, int tier, int level){
        super(amount);
        this.type =type;
        this.id = id;
        this.tier = tier;
        this.level = level;
    }

    public MMOReward(String type,String id,Number amount){
        this(type,id,amount,1,0);
    }

    public MMOReward(String type,String id){
        this(type,id,1,1,0);
    }


    @Override
    public void give(Player player,Number number) {
        ItemStack itemStack = MMOItems.plugin.getItem(type,id);
        if(itemStack != null){
            itemStack.setAmount(number.intValue());
            ReturnItems.process(player,
                    ItemLeveling.getInstance().getItemService().getItem(itemStack,tier,level));
        }
    }

    @Override
    protected String getMessage(Number amount) {
        ItemStack itemStack = MMOItems.plugin.getItem(type,id);
        if(itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()){
            String name = itemStack.getItemMeta().getDisplayName();
            return "&a➯ "+name+" x"+amount;
        }
        return "§a➯ 未知名子物品 x"+amount;
    }


}
