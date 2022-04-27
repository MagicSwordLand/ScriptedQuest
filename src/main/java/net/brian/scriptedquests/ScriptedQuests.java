package net.brian.scriptedquests;

import net.brian.playerdatasync.PlayerDataSync;
import net.brian.scriptedquests.api.quests.QuestManager;
import net.brian.scriptedquests.commands.CommandManager;
import net.brian.scriptedquests.conversations.ConversationManager;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import net.brian.scriptedquests.demo.conversation.DemoConversation;
import net.brian.scriptedquests.quests.QuestManagerImpl;
import net.brian.scriptedquests.starter.NPCQuestStarter;
import org.bukkit.plugin.java.JavaPlugin;

public final class ScriptedQuests extends JavaPlugin {


    private static ScriptedQuests plugin;
    private QuestManager questManager;
    private ConversationManager conversationManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        PlayerDataSync.getInstance().register("quests", PlayerQuestDataImpl.class);
        questManager = new QuestManagerImpl(this);
        new CommandManager(this);
        conversationManager = new ConversationManager(this);
        conversationManager.registerConversation(6,new DemoConversation(questManager).get());
        new NPCQuestStarter();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ScriptedQuests getInstance() {
        return plugin;
    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    public ConversationManager getConversationManager() {
        return conversationManager;
    }
}
