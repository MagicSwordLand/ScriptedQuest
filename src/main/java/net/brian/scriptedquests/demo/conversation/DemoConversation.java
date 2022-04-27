package net.brian.scriptedquests.demo.conversation;

import net.brian.scriptedquests.api.quests.QuestManager;
import net.brian.scriptedquests.conversations.NPCQuestion;
import net.brian.scriptedquests.conversations.PlayerOption;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import net.brian.scriptedquests.demo.quests.BreakDiasyQuest;
import net.brian.scriptedquests.demo.quests.KillZombieQuest;

public class DemoConversation {

    QuestManager questManager;
    public DemoConversation(QuestManager questManager){
        this.questManager=questManager;
    }

    public NPCQuestion get(){

        PlayerOption zombie = questManager.getQuest("zombie").get().getStartOption("殺殭屍",false,player -> {
            return PlayerQuestDataImpl.get(player.getUniqueId()).filter(data->!data.hasFinished("daisy")).isPresent();
        });

        PlayerOption milk = questManager.getQuest("milk").get().getStartOption("擠牛奶",false,player -> {
            return PlayerQuestDataImpl.get(player.getUniqueId()).filter(data-> data.hasFinished(BreakDiasyQuest.ID) && data.hasFinished(KillZombieQuest.ID)).isPresent();
        });

        PlayerOption daisy = questManager.getQuest("daisy").get().getStartOption("採花",false);

        return new NPCQuestion("哈囉 你想做甚麼任務", 6,zombie,milk,daisy);

    }


}
