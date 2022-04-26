package net.brian.scriptedquests.starter;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.quests.Quest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class QuestStarter{

    protected final Condition[] conditions;
    protected final Quest quest;

    public QuestStarter(Quest quest,Condition... condition){
        this.quest = quest;
        this.conditions = condition;
    }

    public boolean valid(Player player){
        for (Condition condition : conditions) {
            if(!condition.test(player)){
                return false;
            }
        }
        return true;
    }


}
