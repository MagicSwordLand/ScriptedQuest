package net.brian.scriptedquests.demo.quests;

import net.brian.scriptedquests.api.quests.Quest;
import net.brian.scriptedquests.conversations.NPCQuestion;
import net.brian.scriptedquests.conversations.PlayerOption;
import net.brian.scriptedquests.objectives.ConversationObjective;
import net.brian.scriptedquests.objectives.KillMobsObjectives;
import net.brian.scriptedquests.objectives.ListenObjective;
import net.brian.scriptedquests.starter.NPCQuestStarter;
import org.bukkit.entity.Player;

public class TestQuest extends Quest {


    public TestQuest(){
        super("test");
        NPCQuestStarter.register(5,this);
        pushObjective(new ListenObjective(this,"startmsg",5,"最近天下不太平啊","若有人能來拯救世界就好了"));
        pushObjective(new ConversationObjective(this,"ask",getConversation()));
        pushObjective(new KillMobsObjectives("KillSkeleton",this,"SkeletonKing",2));
    }


    @Override
    public void onEnd(Player player) {
        player.sendMessage("Congratulation");
    }

    public void onStart(Player player){
        player.sendMessage("Started Quest");
    }


    ConversationObjective.Conversation getConversation(){
        NPCQuestion thankMsg = new NPCQuestion("太感謝了",5);
        PlayerOption endOption = new PlayerOption("沒問題~",((player, npc) ->
                thankMsg.send(player)));

        NPCQuestion angryMsg = new NPCQuestion("幹 有夠小氣",5);
        PlayerOption reject = new PlayerOption("不要",((player,npc)->{
            angryMsg.send(player);
        }));
        NPCQuestion askMsg = new NPCQuestion("少年阿 可以幫我殺個2隻怪物嗎",5,endOption,reject);
        return new ConversationObjective.Conversation(askMsg,endOption);
    }

}
