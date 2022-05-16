package net.brian.scriptedquests.api.quests;

import org.bukkit.entity.Player;

import java.util.Optional;

public interface QuestManager {

    Optional<Quest> getQuest(String id);

    void register(Quest quest);

    void unregister(String questID);

    void unregisterAll();

}
