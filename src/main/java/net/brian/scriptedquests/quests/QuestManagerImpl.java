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

    private final Map<String , Quest> quests = new HashMap<>();


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
        Quest quest1 = quests.get(quest.getQuestID());
        if(quest1 != null){
            quest1.unregister();
        }
        quests.put(quest.getQuestID(),quest);
    }

}
