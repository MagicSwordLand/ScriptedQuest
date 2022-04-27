package net.brian.scriptedquests.logger;

import org.bukkit.Bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

public class QuestLogger {

    private static final Logger logger = Logger.getLogger("ScriptedQuest");

    public static void warn(String string){
        logger.log(Level.INFO,string);
    }

}
