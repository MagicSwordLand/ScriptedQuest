package net.brian.scriptedquests.data;

import net.brian.playerdatasync.PlayerDataSync;
import net.brian.playerdatasync.data.PlayerData;
import net.brian.playerdatasync.data.gson.PostProcessable;
import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.data.PlayerQuestData;
import net.brian.scriptedquests.api.quests.QuestManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;


public class PlayerQuestDataImpl extends PlayerData implements PlayerQuestData, PostProcessable {

    private final HashMap<String,SerializedQuestData> serializedQuestData = new HashMap<>();
    private final Set<String> finishedQuestIDs = new HashSet<>();

    public PlayerQuestDataImpl(UUID uuid) {
        super(uuid);
    }

    public String getObjectiveData(String quest, String objective){
        SerializedQuestData data = serializedQuestData.get(quest);
        if(data != null){
            return data.getObjectiveData();
        }
        return null;
    }





    @Override
    public void removeQuest(String questID) {
        serializedQuestData.remove(questID);
    }

    @Override
    public void setQuestData(String questID, SerializedQuestData data) {
        serializedQuestData.put(questID,data);
    }

    @Override
    public boolean isDoing(String questID) {
        return serializedQuestData.containsKey(questID);
    }

    @Override
    public void addFinishQuest(String questID) {
        finishedQuestIDs.add(questID);
    }

    @Override
    public boolean hasFinished(String questID){
        return finishedQuestIDs.contains(questID);
    }

    @Override
    public Map<String, String> getOnGoingQuests() {
        Map<String,String> map = new HashMap<>();
        serializedQuestData.forEach((id,questData)-> map.put(id,questData.getObjectiveID()));
        return map;
    }

    public static Optional<PlayerQuestDataImpl> get(UUID uuid){
        return PlayerDataSync.getInstance().getData(uuid,PlayerQuestDataImpl.class);
    }


    @Override
    public void gsonPostDeserialize() {
        QuestManager questManager = ScriptedQuests.getInstance().getQuestManager();
        Player player = Bukkit.getPlayer(uuid);
        serializedQuestData.forEach((questID,serializedData)->{
            questManager.getQuest(questID).flatMap(quest -> quest.getObjective(serializedData.getObjectiveID()))
                    .ifPresent(objective -> objective.cachePlayer(player,serializedData.getObjectiveData()));
        });

    }

    @Override
    public void gsonPostSerialize() {

    }


}
