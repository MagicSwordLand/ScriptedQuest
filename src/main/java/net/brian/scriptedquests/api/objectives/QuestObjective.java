package net.brian.scriptedquests.api.objectives;


import com.google.gson.Gson;
import net.brian.playerdatasync.PlayerDataSync;
import net.brian.playerdatasync.events.PlayerDataFetchComplete;
import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import net.brian.scriptedquests.quests.Quest;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public abstract class QuestObjective implements Listener {


    protected final static Gson gson = new Gson();

    protected final String objectiveID;
    protected final Quest quest;
    protected final Condition[] conditions;
    protected final Map<UUID,Object> cachedPlayerData = new HashMap<>();

    public QuestObjective(Quest quest,String objectiveID){
        this(quest,objectiveID,player -> true);
    }

    public QuestObjective(Quest quest,String objectiveID, Condition... condition){
        this.quest = quest;
        this.conditions = condition;
        this.objectiveID = objectiveID;
        Bukkit.getServer().getPluginManager().registerEvents(this, ScriptedQuests.getInstance());
    }


    public abstract Class<?> getDataClass();
    public abstract Object newObjectiveData();


    public void start(Player player){
        PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(data->{
            if(getDataClass() != null){
                Object newData = newObjectiveData();
                cachedPlayerData.put(player.getUniqueId(),newData);
                data.setObjectiveData(quest.getQuestID(), objectiveID,gson.toJson(newData));
            }
            else {
                cachedPlayerData.put(player.getUniqueId(), null);
            }
        });
    }

    public void cancel(Player player){
        cachedPlayerData.remove(player.getUniqueId());
    }


    public void finish(Player player){
        UUID uuid = player.getUniqueId();
        PlayerQuestDataImpl.get(uuid).ifPresent(data->{
            quest.finish(player,objectiveID);
            data.removeObjective(quest.getQuestID(), objectiveID);
            cachedPlayerData.remove(uuid);
        });
    }

    @EventHandler
    public void onDataLoadComplete(PlayerDataFetchComplete event){
        UUID uuid = event.getPlayer().getUniqueId();
        PlayerQuestDataImpl.get(uuid)
                .ifPresent(data->{
                    if(getDataClass() != null){
                        data.getObjectiveData(quest.getQuestID(), objectiveID).ifPresent(dataString->{
                            cachedPlayerData.put(uuid,gson.fromJson(dataString,getDataClass()));
                        });
                    }
                    else if(data.isOnGoingObj(quest.getQuestID(), objectiveID)){
                        cachedPlayerData.put(uuid,null);
                    }
                });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event){
        if(getDataClass() != null){
            Object obj = cachedPlayerData.get(event.getPlayer().getUniqueId());
            if(obj != null){
                PlayerQuestDataImpl.get(event.getPlayer().getUniqueId()).ifPresent(data->{
                    String dataString = PlayerDataSync.getGson().toJson(obj);
                    data.setObjectiveData(quest.getQuestID(),objectiveID,dataString);
                });
            }
        }
    }

    public String getObjectiveID() {
        return objectiveID;
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

    public boolean playerIsOngoing(Player player){
        return cachedPlayerData.containsKey(player.getUniqueId());
    }

}
