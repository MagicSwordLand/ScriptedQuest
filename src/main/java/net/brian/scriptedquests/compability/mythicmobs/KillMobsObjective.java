package net.brian.scriptedquests.compability.mythicmobs;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.api.objectives.data.IntegerData;
import net.brian.scriptedquests.api.objectives.PersistentObjective;
import net.brian.scriptedquests.api.quests.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class KillMobsObjective extends PersistentObjective<IntegerData> {


    private final String mobType;
    private final int amount;

    private String mobName = "";

    public KillMobsObjective(Quest quest,String objectiveID , String mobType, int amount, Condition... conditions) {
        super(quest,objectiveID,conditions);
        this.mobType = mobType;
        this.amount = amount;
    }

    @EventHandler
    public void onKill(MythicMobDeathEvent event){
        if(event.getMobType().getInternalName().equalsIgnoreCase(mobType)){
            getData(event.getKiller().getUniqueId()).ifPresent(data->{
                if(valid((Player) event.getKiller())){
                    data.add(1);
                    if(data.getAmount() >= amount){
                        finish((Player) event.getKiller());
                    }
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


    public void setMobName(String name){
        this.mobName = name;
    }


}
