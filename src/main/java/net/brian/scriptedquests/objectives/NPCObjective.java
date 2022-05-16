package net.brian.scriptedquests.objectives;

import net.brian.scriptedquests.api.objectives.QuestObjective;
import net.brian.scriptedquests.api.quests.Quest;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;

public abstract class NPCObjective extends QuestObjective {
    protected final int npcID;
    private boolean useNPCLocation = true;

    public NPCObjective(Quest quest, String objectiveID,int npcID) {
        super(quest, objectiveID);
        this.npcID = npcID;
    }

    public NPCObjective useNPCLocation(boolean enable){
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
