package net.brian.scriptedquests.api;

import net.brian.playerdatasync.PlayerDataSync;
import net.brian.scriptedquests.api.objectives.SerializedQuestData;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface PlayerQuestData {


    void removeQuest(String questID);

    void setQuestData(String questID, SerializedQuestData data);

    boolean isDoing(String questID);

    void addFinishQuest(String questID);
    boolean hasFinished(String questID);

    Map<String,String> getOnGoingQuests();

    static Optional<PlayerQuestDataImpl> get(UUID uuid){
        return PlayerDataSync.getInstance().getData(uuid,PlayerQuestDataImpl.class);
    }
}
