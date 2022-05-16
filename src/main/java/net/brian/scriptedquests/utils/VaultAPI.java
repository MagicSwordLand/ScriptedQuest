package net.brian.scriptedquests.utils;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class VaultAPI {

    static Economy economy = Bukkit.getServer().getServicesManager().getRegistration(Economy.class).getProvider();

    public static boolean withdraw(Player player,float amount){
        EconomyResponse r = economy.withdrawPlayer(player,amount);
        if(r.transactionSuccess()){
            return true;
        }
        player.sendMessage(ChatColor.RED+"你沒有足夠的錢");
        return false;
    }

    public static double getMoney(Player player){
        return economy.getBalance(player);
    }



}
