package net.brian.scriptedquests.quests;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.QuestManager;
import net.brian.scriptedquests.quests.demo.BreakDiasyQuest;
import net.brian.scriptedquests.quests.demo.KillZombieQuest;
import net.brian.scriptedquests.quests.demo.MilkQuest;

import java.util.*;
import java.util.Optional;

public class QuestManagerImpl implements QuestManager {

    Map<String ,Quest> quests = new HashMap<>();


    public QuestManagerImpl(ScriptedQuests plugin){
        register(new TestQuest());
        register(new KillZombieQuest());
        register(new BreakDiasyQuest());
        register(new MilkQuest());
    }



    @Override
    public Optional<Quest> getQuest(String id) {
        return Optional.ofNullable(quests.get(id));
    }

    @Override
    public void register(Quest quest) {
        quests.put(quest.questID,quest);
    }

}
