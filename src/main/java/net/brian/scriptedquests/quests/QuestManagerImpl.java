package net.brian.scriptedquests.quests;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.quests.Quest;
import net.brian.scriptedquests.api.quests.QuestManager;
import net.brian.scriptedquests.demo.quests.BreakDiasyQuest;
import net.brian.scriptedquests.demo.quests.KillZombieQuest;
import net.brian.scriptedquests.demo.quests.MilkQuest;
import net.brian.scriptedquests.demo.quests.TestQuest;

import java.util.*;
import java.util.Optional;

public class QuestManagerImpl implements QuestManager {

    Map<String , Quest> quests = new HashMap<>();


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
        quests.put(quest.getQuestID(),quest);
    }

}
