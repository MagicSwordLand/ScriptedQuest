package net.brian.scriptedquests.conversation.conversations;

import net.brian.scriptedquests.api.QuestManager;
import net.brian.scriptedquests.conversation.NPCResponse;
import net.brian.scriptedquests.conversation.PlayerOption;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import org.bukkit.entity.Player;

public class DemoConversation {

    QuestManager questManager;
    public DemoConversation(QuestManager questManager){
        this.questManager=questManager;
    }

    public NPCResponse get(){
        PlayerOption zombie = new PlayerOption("擊殺殭屍",((player, npc) -> {
            questManager.getQuest("zombie").ifPresent(quest -> quest.startQuest(player));
        }),player -> PlayerQuestDataImpl.get(player.getUniqueId()).map(
                data-> !data.hasFinished("daisy") && !data.hasFinished("zombie")
        ).orElse(false));

        PlayerOption milk = new PlayerOption("擠牛奶",((player, npc) -> {
            questManager.getQuest("milk").ifPresent(quest -> quest.startQuest(player));
        }),player -> PlayerQuestDataImpl.get(player.getUniqueId()).map(
                data-> {
                    return data.hasFinished("zombie") && data.hasFinished("daisy") && !data.hasFinished("milk");
                }
        ).orElse(false));

        PlayerOption daisy = new PlayerOption("採花",((player, npc) -> {
            questManager.getQuest("daisy").ifPresent(quest -> quest.startQuest(player));
        }),player -> PlayerQuestDataImpl.get(player.getUniqueId()).map(data->{
            return  !data.hasFinished("daisy");
        }).orElse(false));

        return new NPCResponse("哈囉 你想做甚麼任務", 6,zombie,milk,daisy);
    }


}
