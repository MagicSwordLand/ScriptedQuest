package net.brian.scriptedquests.compability.vault;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyProvider {

    private static Economy economy;

    public static void load(){
        RegisteredServiceProvider<Economy> provider = Bukkit.getServicesManager().getRegistration(Economy.class);
        if(provider != null){
            economy = provider.getProvider();
        }
    }

    public static boolean withdraw(Player player,float amount){
       EconomyResponse r = economy.withdrawPlayer(player,amount);
       if(r.transactionSuccess()){
           player.sendMessage("你沒有足夠的錢");
           return true;
       }
       return false;
    }

    public static void give(Player player,float amount){
        economy.depositPlayer(player,amount);
    }


}
