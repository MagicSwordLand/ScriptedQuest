package net.brian.scriptedquests.data;

import java.util.Optional;

public class SerializedQuestData {

    private final long startTime;
    private String objectiveID;
    private String objectiveData = "";

    public SerializedQuestData(String objectiveID,String data){
            this.objectiveID = objectiveID;
            this.objectiveData = data;
            startTime = System.currentTimeMillis();
    }

    public SerializedQuestData(String objectiveID){
        this.objectiveID = objectiveID;
        startTime = System.currentTimeMillis();
    }

    public String getObjectiveData() {
            return objectiveData;
    }

    public void setObjectiveData(String objectiveData) {
        this.objectiveData = objectiveData;
    }

    public String getObjectiveID() {
            return objectiveID;
    }

    public void setObjectiveID(String objectiveID) {
        this.objectiveID = objectiveID;
    }

    public long getStartTime() {
        return startTime;
    }

}