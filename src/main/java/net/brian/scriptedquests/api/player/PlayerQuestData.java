package net.brian.scriptedquests.api.player;

import net.brian.playerdatasync.PlayerDataSync;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import net.brian.scriptedquests.data.SerializedQuestData;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface PlayerQuestData {


    void removeQuestData(String questID);

    SerializedQuestData getQuestData(String quest);
    String getObjectiveData(String quest, String objective);
    void setQuestData(String questID, String objID, String objectiveData);

    boolean isDoing(String questID);
    boolean isDoing(String questID,String objectiveID);

    void addFinishQuest(String questID);
    boolean hasFinished(String questID);
    long getLastFinishTimeStamp(String questID);
    Map<String,Long> getFinishedQuests();

    int getMaxQuestAmount();
    void setMaxQuestAmount(int amount);

    /**
     * Tracking
     */
    String getTrackingQuest();
    void setTrackingQuest(String questID);
    void startTracking();
    void endTracking();

    Map<String,String> getOnGoingQuests();

    static Optional<PlayerQuestDataImpl> get(UUID uuid){
        return PlayerDataSync.getInstance().getData(uuid,PlayerQuestDataImpl.class);
    }
}
