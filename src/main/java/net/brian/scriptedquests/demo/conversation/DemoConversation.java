package net.brian.scriptedquests.demo.conversation;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.quests.QuestManager;

import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import net.brian.scriptedquests.demo.quests.BreakDiasyQuest;
import net.brian.scriptedquests.demo.quests.KillZombieQuest;
import net.brian.scriptedquests.conversation.NPCQuestion;
import net.brian.scriptedquests.conversation.PlayerOption;

public class DemoConversation {

    QuestManager questManager;

    public DemoConversation(QuestManager questManager){
        this.questManager=questManager;
        ScriptedQuests.getInstance().getConversationManager().pushNPCQuestion(6,get());
    }

    public NPCQuestion get(){

        PlayerOption zombie = questManager.getQuest("zombie").get().getStartOption("殺殭屍",false, player -> {
            return PlayerQuestDataImpl.get(player.getUniqueId()).filter(data->!data.hasFinished("daisy")).isPresent();
        });

        PlayerOption milk = questManager.getQuest("milk").get().getStartOption("擠牛奶",false,player -> {
            return PlayerQuestDataImpl.get(player.getUniqueId()).filter(data-> data.hasFinished(BreakDiasyQuest.ID) && data.hasFinished(KillZombieQuest.ID)).isPresent();
        });

        PlayerOption daisy = questManager.getQuest("daisy").get().getStartOption("採花",false);

        return new NPCQuestion("哈囉 你想做甚麼任務")
                .addPlayerOption(zombie)
                .addPlayerOption(milk)
                .addPlayerOption(daisy);
    }


}
