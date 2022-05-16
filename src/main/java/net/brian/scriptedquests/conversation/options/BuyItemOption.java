package net.brian.scriptedquests.conversation.options;

import net.Indyuce.mmoitems.MMOItems;
import net.brian.playerdatasync.util.ReturnItems;
import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.conversation.PlayerOption;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BuyItemOption extends PlayerOption {

    Economy economy = Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider();

    ItemStack itemStack;
    float price;

    public BuyItemOption(String message, String type,String id,int amount,float price) {
        super(message);
        this.price = price;
        itemStack= MMOItems.plugin.getItem(type,id);
        if(itemStack != null){
            itemStack.setAmount(amount);
        }

        result = (player, npcID) -> {
            EconomyResponse r = economy.withdrawPlayer(player,price);
            if(r.transactionSuccess()){
                ReturnItems.process(player,itemStack);
            }
            else{
                player.sendMessage("§c你的錢不夠");
            }
        };
    }

    @Override
    public BuyItemOption setResult(Result addResult){
        Result newResult = (player, npcID) -> {
            result.process(player,npcID);
            addResult.process(player,npcID);
        };
        result = newResult;
        return this;
    }

    public BuyItemOption setSuccessResult(Result result){
        Result newResult = (player, npcID) -> {
            EconomyResponse r = economy.withdrawPlayer(player,price);
            if(r.transactionSuccess()){
                ReturnItems.process(player,itemStack);
                result.process(player,npcID);
            }
        };
        super.setResult(newResult);
        return this;
    }



}
