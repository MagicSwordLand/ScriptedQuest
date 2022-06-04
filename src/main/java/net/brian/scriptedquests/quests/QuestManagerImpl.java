package net.brian.scriptedquests.quests;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.quests.Quest;
import net.brian.scriptedquests.api.quests.QuestManager;


import java.util.*;
import java.util.Optional;

public class QuestManagerImpl implements QuestManager {

    private final Map<String , Quest> quests = new HashMap<>();


    ScriptedQuests plugin;
    public QuestManagerImpl(ScriptedQuests plugin){
        this.plugin = plugin;
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

    public Collection<Quest> getQuests(){
        return quests.values();
    }

    @Override
    public void unregister(String questID) {
        Quest quest = quests.get(questID);
        if(quest != null){
            quest.unregister();
            quests.remove(questID);
        }
    }

    @Override
    public void unregisterAll() {
        quests.values().forEach(Quest::unregister);
        quests.clear();
    }


}
