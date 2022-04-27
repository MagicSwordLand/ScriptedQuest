package net.brian.scriptedquests.logger;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class QuestLogger {

    public static void warn(String string){
        Bukkit.getLogger().log(Level.INFO,string);
    }

}
