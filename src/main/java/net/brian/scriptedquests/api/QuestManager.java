package net.brian.scriptedquests.api;

import net.brian.scriptedquests.quests.Quest;

import java.util.Optional;

public interface QuestManager {

    Optional<Quest> getQuest(String id);

    void register(Quest quest);
}
