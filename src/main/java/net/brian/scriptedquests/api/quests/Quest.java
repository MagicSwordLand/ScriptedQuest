package net.brian.scriptedquests.api.quests;


import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.conversations.PlayerOption;
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


    public void pushObjective(QuestObjective objective){
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


    public PlayerOption getStartOption(String message, Runnable action, boolean canRedo, Condition... conditions){
        return new PlayerOption(message,((player, npc) -> {
            this.startQuest(player);
            if(action != null){
                action.run();
            }
        }),true,player -> {
            Optional<PlayerQuestDataImpl> optData = PlayerQuestDataImpl.get(player.getUniqueId());
            if(optData.isPresent()){
                PlayerQuestDataImpl data = optData.get();
                if(data.isDoing(questID)) return false;
                if(!canRedo && data.hasFinished(questID)) return false;
                return Condition.test(player,conditions);
            }
            return false;
        });
    }


    public PlayerOption getStartOption(String message,boolean canRedo,Condition... conditions){
        return getStartOption(message,null,canRedo,conditions);
    }


    public String getQuestID() {
        return questID;
    }

}
