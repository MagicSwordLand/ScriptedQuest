package net.brian.scriptedquests.objectives;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.api.objectives.ObjectiveData;
import net.brian.scriptedquests.api.objectives.PersistentObjective;
import net.brian.scriptedquests.api.objectives.QuestObjective;
import net.brian.scriptedquests.quests.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.Optional;
import java.util.UUID;

public class KillMobsObjectives extends PersistentObjective<KillMobsObjectives.KillCount> {


    private final String mobType;
    private final int amount;

    private String mobName = "";

    public KillMobsObjectives(String objectiveID, Quest quest, String mobType, int amount, Condition... conditions) {
        super(quest,objectiveID,conditions);
        this.mobType = mobType;
        this.amount = amount;

    }

    @EventHandler
    public void onKill(MythicMobDeathEvent event){
        if(event.getMobType().getInternalName().equalsIgnoreCase(mobType)){
            getData(event.getKiller().getUniqueId()).ifPresent(data->{
                if(valid((Player) event.getKiller())){
                    data.amount ++;
                    if(data.amount >= amount){
                        finish((Player) event.getKiller());
                    }
                }
            });
        }
    }


    @Override
    public Class<KillCount> getDataClass() {
        return KillCount.class;
    }

    @Override
    public KillCount newObjectiveData() {
        return new KillCount(0);
    }

    @Override
    public String getInstruction(Player player) {
        return mobName +getData(player.getUniqueId()).map(data->data.amount).orElse(-1) +"/"+amount;
    }

    static class KillCount extends ObjectiveData {
        int amount;
        public KillCount(int amount){
            this.amount = amount;
        }
    };



}
