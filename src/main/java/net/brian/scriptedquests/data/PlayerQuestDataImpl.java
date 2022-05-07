package net.brian.scriptedquests.data;

import com.live.bemmamin.gps.logic.Point;
import net.brian.playerdatasync.PlayerDataSync;
import net.brian.playerdatasync.data.PlayerData;
import net.brian.playerdatasync.data.gson.PostProcessable;
import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.data.PlayerQuestData;
import net.brian.scriptedquests.api.objectives.QuestObjective;
import net.brian.scriptedquests.api.quests.QuestManager;
import net.brian.scriptedquests.compability.CompatibilityAddons;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;


public class PlayerQuestDataImpl extends PlayerData implements PlayerQuestData, PostProcessable {

    private final HashMap<String,SerializedQuestData> serializedQuestDatas = new HashMap<>();
    private final Set<String> finishedQuestIDs = new HashSet<>();

    private String trackingQuestID = "";


    public PlayerQuestDataImpl(UUID uuid) {
        super(uuid);
    }

    public String getObjectiveData(String quest, String objective){
        SerializedQuestData data = serializedQuestDatas.get(quest);
        if(data != null){
            return data.getObjectiveData();
        }
        return null;
    }



    @Override
    public void removeQuest(String questID) {
        serializedQuestDatas.remove(questID);
    }

    @Override
    public void setQuestData(String questID, SerializedQuestData data) {
        serializedQuestDatas.put(questID,data);
    }

    @Override
    public boolean isDoing(String questID) {
        return serializedQuestDatas.containsKey(questID);
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
    public Optional<QuestObjective> getTrackingObjective() {
        SerializedQuestData serializedQuestData = serializedQuestDatas.get(trackingQuestID);
        if(serializedQuestData != null){
            return ScriptedQuests.getInstance().getQuestManager()
                    .getQuest(trackingQuestID)
                    .flatMap(quest -> quest.getObjective(serializedQuestData.getObjectiveID()));
        }
        return Optional.empty();
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
        serializedQuestDatas.forEach((questID, serializedData)->{
            questManager.getQuest(questID).flatMap(quest -> quest.getObjective(serializedData.getObjectiveID()))
                    .ifPresent(objective -> objective.cachePlayer(player,serializedData.getObjectiveData()));
        });
        startTracking();
    }

    @Override
    public void gsonPostSerialize() {

    }


}
