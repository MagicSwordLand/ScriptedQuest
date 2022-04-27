package net.brian.scriptedquests.quests;


import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import net.brian.scriptedquests.api.objectives.QuestObjective;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class Quest {

    protected final String questID;

    private final LinkedHashMap<String,QuestObjective> objectives = new LinkedHashMap<>();

    public Quest(String questID){
        this.questID = questID;
    }


    public void registerObjective(QuestObjective objective){
        objectives.put(objective.getObjectiveID(),objective);
    }

    public void startQuest(Player player){
        PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(data->{
            if(!data.isDoing(questID)){
                Iterator<Map.Entry<String ,QuestObjective>> it = objectives.entrySet().iterator();
                if(it.hasNext()){
                    onStart(player);
                    it.next().getValue().start(player);
                }
            }
        });
    }


    public void finish(Player player,String objectiveID){
        PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(data->{
            Iterator<Map.Entry<String, QuestObjective>> it = objectives.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry<String, QuestObjective> entry = it.next();
                if(entry.getKey().equals(objectiveID)){
                    if(it.hasNext()){
                        it.next().getValue().start(player);
                    }
                    else{
                        onEnd(player);
                        data.addFinishQuest(questID);
                        data.removeQuest(questID);
                    }
                    break;
                }
            }
        });
    }

    public void cancel(Player player){
        PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(data->{
            data.removeQuest(questID);
            objectives.values().forEach(objective -> objective.cancel(player));
        });
    }

    public void onEnd(Player player){

    }

    public void onStart(Player player){

    }


    public Optional<QuestObjective> getObjective(String id){
        return Optional.ofNullable(objectives.get(id));
    }





    public String getQuestID() {
        return questID;
    }

}
