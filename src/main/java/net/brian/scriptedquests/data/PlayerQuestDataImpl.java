package net.brian.scriptedquests.data;

import net.brian.playerdatasync.PlayerDataSync;

import java.util.*;

public class PlayerQuestDataImpl {

    Map<String,HashMap<String,String>> onGoingQuestDatas = new HashMap<>();

    List<String> finishedQuest = new ArrayList<>();

    List<String> tags = new ArrayList<>();




    public void removeQuestData(String questID){
        onGoingQuestDatas.remove(questID);
    }

    public void finishQuest(String questID){
        finishedQuest.add(questID);
        onGoingQuestDatas.remove(questID);
    }

    public void addTag(String tag){
        tags.add(tag);
    }

    public void removeTag(String tag){
        tags.remove(tag);
    }

    public boolean hasTag(String tag){
        return tags.contains(tag);
    }



    public Optional<String> getObjectiveData(String questID, String objectiveID){
        HashMap<String,String> objectiveDatas = this.onGoingQuestDatas.get(questID);
        if(objectiveDatas != null){
            return Optional.ofNullable(objectiveDatas.get(objectiveID));
        }
        return Optional.empty();
    }

    public void setObjectiveData(String questID,String objectiveID,String objData){
        HashMap<String, String> objMap = onGoingQuestDatas.computeIfAbsent(questID, k -> new HashMap<>());
        objMap.put(objectiveID,objData);
    }

    public void addOnGoingQuest(String questID){
        onGoingQuestDatas.put(questID,new HashMap<>());
    }

    public static Optional<PlayerQuestDataImpl> get(UUID uuid){
        return PlayerDataSync.getInstance().getData(uuid,PlayerQuestDataImpl.class);
    }

    public Set<String> getOnGoingQuests(){
        return onGoingQuestDatas.keySet();
    }

    public boolean hasFinished(String questID){
        return finishedQuest.contains(questID);
    }


    public Map<String, HashMap<String, String>> getOnGoingQuestDatas() {
        return onGoingQuestDatas;
    }
    public boolean isOnGoingObj(String questID,String objID){
        HashMap<String,String> objData = onGoingQuestDatas.get(questID);
        if(objData != null){
            return objData.containsKey(objID);
        }
        else return false;
    }

    public void removeObjective(String questID,String objectiveID){
        HashMap<String,String> objMap = onGoingQuestDatas.get(questID);
        if(objMap  != null){
            objMap.remove(objectiveID);
        }
    }
}
