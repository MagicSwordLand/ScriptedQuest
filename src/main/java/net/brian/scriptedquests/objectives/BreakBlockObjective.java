package net.brian.scriptedquests.objectives;

import net.brian.scriptedquests.api.objectives.ObjectiveData;
import net.brian.scriptedquests.api.objectives.PersistentObjective;
import net.brian.scriptedquests.api.quests.Quest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlockObjective extends PersistentObjective<BreakBlockObjective.BreakProfile> {

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
                data.amount++;
                if(data.amount >= amount){
                    finish(event.getPlayer());
                }
            });
        }
    }

    @Override
    public Class<BreakProfile> getDataClass() {
        return BreakProfile.class;
    }

    @Override
    public BreakProfile newObjectiveData() {
        return new BreakProfile(0);
    }

    @Override
    public String getInstruction(Player player) {
        return getData(player.getUniqueId()).map(data->{
            return "請破壞"+data.amount+"/"+amount+"個"+type.name();
        }).orElse("資料載入中");
    }

    static class BreakProfile extends ObjectiveData {
        int amount;
        BreakProfile(int amount){
            this.amount = amount;
        }
    }


}
