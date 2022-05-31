package net.brian.scriptedquests.compability.mythicmobs;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.api.objectives.PersistentObjective;
import net.brian.scriptedquests.api.objectives.data.MapData;
import net.brian.scriptedquests.api.quests.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class KillMultiMobsObjective extends PersistentObjective<MapData> {

    Map<String,Integer> mobsToKill;
    public KillMultiMobsObjective(Quest quest, String objectiveID, Map<String,Integer> mobsToKill, Condition... conditions) {
        super(quest, objectiveID, conditions);
        this.mobsToKill = mobsToKill;
    }

    @EventHandler
    public void onMobDeath(MythicMobDeathEvent event){
        if(!mobsToKill.containsKey(event.getMobType().getInternalName())){
            return;
        }
        if(event.getKiller() instanceof Player player){
            if(!playerIsDoing(player)) return;

            getData(player.getUniqueId()).ifPresent(data->{
                String mobName = event.getMobType().getInternalName();
                int require = mobsToKill.get(mobName);
                if(data.get(mobName) < require){
                    data.add(mobName,1);
                    if(checkFinish(data)){
                        finish(player);
                    };
                }
            });
        }
    }

    private boolean checkFinish(MapData mapData){
        for (Map.Entry<String, Integer> entry : mobsToKill.entrySet()) {
            if(mapData.get(entry.getKey()) < entry.getValue()) return false;
        }
        return true;
    }

    @Override
    public Class<MapData> getDataClass() {
        return MapData.class;
    }

    @Override
    public MapData newObjectiveData() {
        return new MapData();
    }
}
