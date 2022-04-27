package net.brian.scriptedquests.api.objectives;


import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.data.SerializedQuestData;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.api.quests.Quest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public abstract class QuestObjective implements Listener {

    protected final String objectiveID;
    protected final Quest quest;
    protected final Condition[] conditions;
    protected final List<Player> onlinePlayers = new ArrayList<>();

    public QuestObjective(Quest quest,String objectiveID){
        this(quest,objectiveID,player -> true);
    }

    public QuestObjective(Quest quest,String objectiveID, Condition... condition){
        this.quest = quest;
        this.conditions = condition;
        this.objectiveID = objectiveID;
        Bukkit.getServer().getPluginManager().registerEvents(this, ScriptedQuests.getInstance());
    }




    public void start(Player player){
        PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(data->{
            data.setQuestData(quest.getQuestID(),new SerializedQuestData(objectiveID));
        });
    }

    public void cancel(Player player){
        onlinePlayers.remove(player);
    }


    public void finish(Player player){
        UUID uuid = player.getUniqueId();
        PlayerQuestDataImpl.get(uuid).ifPresent(data->{
            quest.finish(player,objectiveID);
            onlinePlayers.remove(player);
        });
    }



    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        onlinePlayers.remove(event.getPlayer());
    }


    public String getObjectiveID() {
        return objectiveID;
    }

    public Quest getParent() {
        return quest;
    }

    public abstract String getInstruction(Player player);


    protected boolean valid(Player player){
        for (Condition condition : conditions) {
            if(!condition.test(player)){
                return false;
            }
        }
        return true;
    }

    public boolean playerIsDoing(Player player){
        return onlinePlayers.contains(player);
    }

}
