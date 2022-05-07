package net.brian.scriptedquests.api.quests;


import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import net.brian.scriptedquests.api.objectives.QuestObjective;
import net.brian.scriptedquests.conversation.PlayerOption;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.*;

public abstract class Quest {

    protected final String questID;

    private final LinkedHashMap<String,QuestObjective> objectives = new LinkedHashMap<>();

    public Quest(String questID,QuestObjective... objectives){
        this.questID = questID;
        pushObjective(objectives);
    }


    public Quest pushObjective(QuestObjective... objectives){
        for (QuestObjective objective : objectives) {
            this.objectives.put(objective.getObjectiveID(),objective);
        }
        return this;
    }

    public void startQuest(Player player){
        PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(data->{
            if(!data.isDoing(questID)){
                Iterator<Map.Entry<String ,QuestObjective>> it = objectives.entrySet().iterator();
                if(it.hasNext()){
                    onStart(player);
                    it.next().getValue().start(player);
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,2);
                }
                else {
                    onEnd(player);
                    data.addFinishQuest(questID);
                    data.removeQuest(questID);
                };
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
                        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE,1,1);
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


    public PlayerOption getStartOption(String message, PlayerOption.Result result, boolean canRedo, Condition... conditions){
        return new PlayerOption(message,conditions)
                .addCondition(player -> PlayerQuestDataImpl.get(player.getUniqueId()).map(data->{
                    if(!canRedo && data.hasFinished(questID)) return false;
                    if(data.isDoing(questID)) return false;
                    return true;
                }).orElse(false))
                .setResult((player, npcID) -> {
                    startQuest(player);
                    if(result != null){
                        result.process(player,npcID);
                    }
                });
    }


    public PlayerOption getStartOption(String message,boolean canRedo,Condition... conditions){
        return getStartOption(message,null,canRedo,conditions);
    }


    public String getQuestID() {
        return questID;
    }

    public void unregister(){
        for (QuestObjective objective : objectives.values()) {
            HandlerList.unregisterAll(objective);
        }
    }

}
