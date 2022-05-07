package net.brian.scriptedquests.objectives;

import net.brian.scriptedquests.api.objectives.data.IntegerData;
import net.brian.scriptedquests.api.objectives.data.ObjectiveData;
import net.brian.scriptedquests.api.objectives.PersistentObjective;
import net.brian.scriptedquests.api.quests.Quest;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlockObjective extends PersistentObjective<IntegerData> {

    final int amount;
    Material type;

    public BreakBlockObjective(Quest quest, String objectiveID,Material type,int amount) {
        super(quest, objectiveID);
        this.amount = amount;
        this.type = type;
    }


    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if(event.getBlock().getType().equals(type)){
            getData(event.getPlayer().getUniqueId()).ifPresent(data->{
                data.add(1);
                if(data.getAmount() >= amount){
                    finish(event.getPlayer());
                }
            });
        }
    }

    @Override
    public Class<IntegerData> getDataClass() {
        return IntegerData.class;
    }

    @Override
    public IntegerData newObjectiveData() {
        return new IntegerData();
    }



}
