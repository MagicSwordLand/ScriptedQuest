package net.brian.scriptedquests.compability.mmoitems;

import net.Indyuce.mmoitems.api.event.item.ConsumableConsumedEvent;
import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.api.objectives.data.IntegerData;
import net.brian.scriptedquests.api.objectives.PersistentObjective;
import net.brian.scriptedquests.api.quests.Quest;
import org.bukkit.event.EventHandler;

public class UseItemObjective extends PersistentObjective<IntegerData> {

    private final String type,id;
    private int amount;

    public UseItemObjective(Quest quest, String objectiveID, String type, String id, int amount) {
        super(quest, objectiveID);
        this.type = type;
        this.id = id;
        this.amount = amount;
    }

    public UseItemObjective(Quest quest, String objectiveID, String type, String id){
        this(quest,objectiveID,type,id,1);
    }

    @EventHandler
    public void onUse(ConsumableConsumedEvent event){
        String type = event.getMMOItem().getType().getId();
        String id = event.getMMOItem().getId();
        if(this.type.equalsIgnoreCase(type) && this.id.equalsIgnoreCase(id) && playerIsDoing(event.getPlayer())){
            if(Condition.test(event.getPlayer(),conditions)){
                getData(event.getPlayer().getUniqueId()).ifPresent(data->{
                    data.add();
                    if(data.getAmount() >= amount) {
                        finish(event.getPlayer());
                    }
                });
            }
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
