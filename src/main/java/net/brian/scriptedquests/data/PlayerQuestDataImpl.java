package net.brian.scriptedquests.data;

import com.live.bemmamin.gps.logic.Point;
import net.brian.playerdatasync.PlayerDataSync;
import net.brian.playerdatasync.data.PlayerData;
import net.brian.playerdatasync.data.gson.PostProcessable;
import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.player.PlayerQuestData;
import net.brian.scriptedquests.api.quests.QuestManager;
import net.brian.scriptedquests.compability.CompatibilityAddons;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;


public class PlayerQuestDataImpl extends PlayerData implements PlayerQuestData, PostProcessable {

    private final HashMap<String,SerializedQuestData> serializedQuestDatas = new HashMap<>();

    private final HashMap<String,Long> finishedQuestIDs = new HashMap<>();

    private String trackingQuestID = "";

    private int maxQuestAmount = 5;


    public PlayerQuestDataImpl(UUID uuid) {
        super(uuid);
    }


    @Override
    public String getObjectiveData(String quest, String objective){
        SerializedQuestData data = serializedQuestDatas.get(quest);
        if(data != null){
            if(objective.equals(data.getObjectiveID())){
                return data.getObjectiveData();
            }
        }
        return null;
    }



    @Override
    public void removeQuestData(String questID) {
        serializedQuestDatas.remove(questID);
    }

    @Override
    public SerializedQuestData getQuestData(String quest) {
        return serializedQuestDatas.get(quest);
    }

    @Override
    public void setQuestData(String questID, String objID,String objData) {
        SerializedQuestData questData = serializedQuestDatas.get(questID);
        if(questData != null){
            questData.setObjectiveID(objID);
            questData.setObjectiveData(objData);
        }
        if(questData == null){
            serializedQuestDatas.put(questID,new SerializedQuestData(objID,objData));
        }
    }

    @Override
    public boolean isDoing(String questID) {
        return serializedQuestDatas.containsKey(questID);
    }

    @Override
    public boolean isDoing(String questID, String objectiveID) {
        if(serializedQuestDatas.containsKey(questID)){
            return serializedQuestDatas.get(questID).getObjectiveID().equals(objectiveID);
        }
        return false;
    }

    @Override
    public void addFinishQuest(String questID) {
        finishedQuestIDs.put(questID,System.currentTimeMillis());
    }

    @Override
    public boolean hasFinished(String questID){
        return finishedQuestIDs.containsKey(questID);
    }

    @Override
    public long getLastFinishTimeStamp(String questID){
        return finishedQuestIDs.getOrDefault(questID,0L);
    }

    @Override
    public Map<String, Long> getFinishedQuests() {
        return (HashMap<String,Long>) finishedQuestIDs.clone();
    }


    @Override
    public int getMaxQuestAmount() {
        return maxQuestAmount;
    }

    @Override
    public void setMaxQuestAmount(int amount){
        this.maxQuestAmount = amount;
    }

    @Override
    public String getTrackingQuest() {
        return trackingQuestID;
    }

    @Override
    public void setTrackingQuest(String questID) {
        trackingQuestID = questID;
        startTracking();
    }

    @Override
    public void startTracking() {
        SerializedQuestData serializedQuestData = serializedQuestDatas.get(trackingQuestID);
        if(serializedQuestData != null){
            ScriptedQuests.getInstance().getQuestManager().getQuest(trackingQuestID)
                    .flatMap(quest -> quest.getObjective(serializedQuestData.getObjectiveID()))
                    .ifPresent(objective -> {
                        if(CompatibilityAddons.hasGPS()){
                            Location location = objective.getLocation();
                            Point point = new Point("quest",location);
                            if(location != null){
                                com.live.bemmamin.gps.playerdata.PlayerData.getPlayerData(uuid)
                                        .startNavigation(point,true,"開始追蹤任務進度");
                            }
                            else{
                                Bukkit.getPlayer(uuid).sendMessage("此階段無法指路");
                            }
                        }
                    });
        }
    }

    @Override
    public void endTracking() {
        trackingQuestID = "";
        if(CompatibilityAddons.hasGPS()){
            com.live.bemmamin.gps.playerdata.PlayerData.getPlayerData(uuid).exitNavigation();
        }
    }

    @Override
    public Map<String, String> getOnGoingQuests() {
        Map<String,String> map = new HashMap<>();
        serializedQuestDatas.forEach((id, questData)-> map.put(id,questData.getObjectiveID()));
        return map;
    }

    public static Optional<PlayerQuestDataImpl> get(UUID uuid){
        return PlayerDataSync.getInstance().getData(uuid,PlayerQuestDataImpl.class);
    }

    @Override
    public void gsonPostDeserialize() {
        QuestManager questManager = ScriptedQuests.getInstance().getQuestManager();
        Player player = Bukkit.getPlayer(uuid);
        serializedQuestDatas.forEach((questID, serializedData)-> {
            questManager.getQuest(questID).ifPresent(quest -> {
                quest.cachePlayer(player,this,serializedData);
            });
        });

        startTracking();
    }

    public void clearAll(){
        serializedQuestDatas.clear();
        finishedQuestIDs.clear();
    }

}
