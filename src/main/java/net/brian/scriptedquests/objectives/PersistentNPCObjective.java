package net.brian.scriptedquests.objectives;

import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.api.objectives.PersistentObjective;
import net.brian.scriptedquests.api.objectives.data.ObjectiveData;
import net.brian.scriptedquests.api.quests.Quest;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;

public abstract class PersistentNPCObjective<T extends ObjectiveData> extends PersistentObjective<T> {

    protected final int npcID;
    private boolean useNPCLocation = true;

    public PersistentNPCObjective(Quest quest, String objectiveID,int npcID, Condition... conditions) {
        super(quest, objectiveID, conditions);
        this.npcID = npcID;
    }





    public PersistentNPCObjective<T> useNPCLocation(boolean enable){
        useNPCLocation = enable;
        return this;
    }


    @Override
    public Location getLocation(){
        if(useNPCLocation){
            NPC npc = CitizensAPI.getNPCRegistry().getById(npcID);
            if(npc != null){
                return npc.getEntity().getLocation();
            }
        }
        return location;
    }

}
