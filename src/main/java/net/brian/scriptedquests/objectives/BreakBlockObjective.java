package net.brian.scriptedquests.objectives;

import net.brian.scriptedquests.api.objectives.QuestObjective;
import net.brian.scriptedquests.quests.Quest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;
import java.util.UUID;

public class BreakBlockObjective extends QuestObjective {

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
    public Class<?> getDataClass() {
        return BreakProfile.class;
    }

    @Override
    public Object newObjectiveData() {
        return new BreakProfile(0);
    }

    @Override
    public String getInstruction(Player player) {
        return getData(player.getUniqueId()).map(data->{
            return "請破壞"+data.amount+"/"+amount+"個"+type.name();
        }).orElse("資料載入中");
    }

    static class BreakProfile{
        int amount;
        BreakProfile(int amount){
            this.amount = amount;
        }
    }

    public Optional<BreakProfile> getData(UUID uuid){
        Object data = cachedPlayerData.get(uuid);
        if(data != null){
            return Optional.of((BreakProfile) data);
        }
        return Optional.empty();
    }

}
