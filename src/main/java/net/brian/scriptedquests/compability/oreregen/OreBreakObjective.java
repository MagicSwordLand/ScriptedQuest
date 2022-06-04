package net.brian.scriptedquests.compability.oreregen;

import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.api.objectives.PersistentObjective;
import net.brian.scriptedquests.api.objectives.data.IntegerData;
import net.brian.scriptedquests.api.quests.Quest;

public class OreBreakObjective extends PersistentObjective<IntegerData> {

    String oreID;
    int amount;

    public OreBreakObjective(Quest quest, String objectiveID,String oreID,int amount, Condition... conditions) {
        super(quest, objectiveID, conditions);
        this.oreID = oreID;
        this.amount = amount;
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
