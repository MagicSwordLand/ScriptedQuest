package net.brian.scriptedquests.api.objectives;

import java.util.Optional;

public class SerializedQuestData {

    private String objectiveID = null;
    private String objectiveData = null;

    public SerializedQuestData(String objectiveID,String data){
            this.objectiveID = objectiveID;
            this.objectiveData = data;
    }

    public SerializedQuestData(String objectiveID){
            this.objectiveID = objectiveID;
    }
    public Optional<String> getObjectiveData() {
            return Optional.ofNullable(objectiveData);
        }
    public String getObjectiveID() {
            return objectiveID;
        }
}