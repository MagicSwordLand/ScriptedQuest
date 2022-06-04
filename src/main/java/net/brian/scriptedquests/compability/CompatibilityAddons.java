package net.brian.scriptedquests.compability;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.compability.placeholderapi.QuestPlaceholder;
import net.brian.scriptedquests.compability.pyrofish.FishingCommand;
import net.brian.scriptedquests.compability.vault.EconomyProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class CompatibilityAddons {

    private static boolean MMOItems = false;
    private static boolean MythicMobs = false;
    private static boolean PlaceholderAPI = false;
    private static boolean GPS = false;
    private static boolean PyroFish = false;

    public static void load(){
        PluginManager pluginManager = Bukkit.getPluginManager();
        if(pluginManager.getPlugin("MMOItems") != null){
            MMOItems = true;
        }
        if(pluginManager.getPlugin("MythicMobs") != null){
            MythicMobs = true;

        }
        if(pluginManager.getPlugin("PlaceholderAPI") != null){
            PlaceholderAPI = true;
            new QuestPlaceholder().register();
        }
        if(pluginManager.getPlugin("GPS") != null){
            GPS = true;
        }
        if(pluginManager.getPlugin("PyroFishingPro") != null){
            new FishingCommand(ScriptedQuests.getInstance());
            PyroFish = true;
        }
        EconomyProvider.load();
    }

    public static boolean hasGPS() {
        return GPS;
    }

    public static boolean hasMMOItems() {
        return MMOItems;
    }

    public static boolean hasMythicMobs() {
        return MythicMobs;
    }

    public static boolean hasPlaceholderAPI() {
        return PlaceholderAPI;
    }

    public static boolean hasPyroFish() {
        return PyroFish;
    }
}
