package net.brian.scriptedquests.quests;

import net.brian.scriptedquests.conversation.NPCResponse;
import net.brian.scriptedquests.conversation.PlayerOption;
import net.brian.scriptedquests.objectives.ConversationObjective;
import net.brian.scriptedquests.objectives.KillMobsObjectives;
import net.brian.scriptedquests.objectives.ListenObjective;
import net.brian.scriptedquests.starter.NPCQuestStarter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TestQuest extends Quest{


    public TestQuest(){
        super("test");
        NPCQuestStarter.register(5,this);
        registerObjective(new ListenObjective(this,"startmsg",5,"最近天下不太平啊","若有人能來拯救世界就好了"));
        registerObjective(new ConversationObjective(this,"ask",getConversation()));
        registerObjective(new KillMobsObjectives("KillSkeleton",this,"SkeletonKing",2));
    }


    @Override
    public void onEnd(Player player) {
        player.sendMessage("Congratulation");
    }

    public void onStart(Player player){
        player.sendMessage("Started Quest");
    }


    ConversationObjective.Conversation getConversation(){
        NPCResponse thankMsg = new NPCResponse("太感謝了",5);
        PlayerOption endOption = new PlayerOption("沒問題~",((player, npc) ->
                thankMsg.send(player)));

        NPCResponse angryMsg = new NPCResponse("幹 有夠小氣",5);
        PlayerOption reject = new PlayerOption("不要",((player,npc)->{
            angryMsg.send(player);
        }));
        NPCResponse askMsg = new NPCResponse("少年阿 可以幫我殺個2隻怪物嗎",5,endOption,reject);
        return new ConversationObjective.Conversation(askMsg,endOption);
    }

}
