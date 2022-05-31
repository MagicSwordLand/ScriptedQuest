package net.brian.scriptedquests;

import net.brian.playerdatasync.PlayerDataSync;
import net.brian.scriptedquests.api.quests.QuestManager;
import net.brian.scriptedquests.commands.CommandManager;
import net.brian.scriptedquests.compability.CompatibilityAddons;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import net.brian.scriptedquests.conversation.ConversationManager;
import net.brian.scriptedquests.quests.QuestManagerImpl;
import org.bukkit.plugin.java.JavaPlugin;

public final class ScriptedQuests extends JavaPlugin {


    private static ScriptedQuests plugin;
    private QuestManager questManager;
    private ConversationManager conversationManager;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        PlayerDataSync.getInstance().register("quests", PlayerQuestDataImpl.class);
        questManager = new QuestManagerImpl(this);
        commandManager = new CommandManager(this);
        conversationManager = new ConversationManager(questManager);
        CompatibilityAddons.load();
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

    public CommandManager getCommandManager() {
        return commandManager;
    }

}
