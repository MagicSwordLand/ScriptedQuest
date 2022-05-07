package net.brian.scriptedquests.demo.conversation;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.conditions.FinishedQuestsCondition;
import net.brian.scriptedquests.api.conditions.NotFinishCondition;
import net.brian.scriptedquests.api.quests.QuestManager;

import net.brian.scriptedquests.conversation.NPCQuestion;
import net.brian.scriptedquests.conversation.PlayerOption;

public class DemoConversation {

    QuestManager questManager;

    public DemoConversation(QuestManager questManager){
        this.questManager=questManager;
        ScriptedQuests.getInstance().getConversationManager().pushNPCQuestion(6,get());
    }

    public NPCQuestion get(){

        PlayerOption zombie = questManager.getQuest("zombie").get()
                .getStartOption("殺殭屍",
                        false,
                        new NotFinishCondition("daisy")
                );

        PlayerOption milk = questManager.getQuest("milk").get()
                .getStartOption("擠牛奶",
                        false,
                        new FinishedQuestsCondition("zombie","daisy")
                );

        PlayerOption daisy = questManager.getQuest("daisy").get().getStartOption("採花",false);

        return new NPCQuestion("哈囉 你想做甚麼任務").addPlayerOptions(zombie, milk, daisy);

    }


}
