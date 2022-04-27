package net.brian.scriptedquests.data;

import java.util.Optional;

public class SerializedQuestData {

    private final String objectiveID;
    private String objectiveData = "";

    public SerializedQuestData(String objectiveID,String data){
            this.objectiveID = objectiveID;
            this.objectiveData = data;
    }

    public SerializedQuestData(String objectiveID){
            this.objectiveID = objectiveID;
    }
    public String getObjectiveData() {
            return objectiveData;
        }
    public String getObjectiveID() {
            return objectiveID;
        }
}