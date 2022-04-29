package net.brian.scriptedquests.demo.quests;

import net.brian.scriptedquests.api.quests.Quest;
import net.brian.scriptedquests.conversation.NPCQuestion;
import net.brian.scriptedquests.conversation.PlayerOption;
import net.brian.scriptedquests.objectives.ConversationObjective;
import net.brian.scriptedquests.objectives.KillMobsObjectives;
import net.brian.scriptedquests.objectives.ListenObjective;
import org.bukkit.entity.Player;

public class TestQuest extends Quest {


    public TestQuest(){
        super("test");
        ListenObjective listenObjective = new ListenObjective(this,"startmsg",5,"最近天下不太平啊","若有人能來拯救世界就好了");
        listenObjective.setInstruction("找到 npcID = 5 並跟他對話");
        pushObjective(listenObjective);
        ConversationObjective conversationObjective = getConversation();
        conversationObjective.setInstruction("接受npc5 的任務");
        pushObjective(conversationObjective);

        pushObjective(new KillMobsObjectives("KillSkeleton",this,"SkeletonKing",2)
                .setInstruction("殺死 2隻怪物"));
    }


    @Override
    public void onEnd(Player player) {
        player.sendMessage("Congratulation");
    }

    public void onStart(Player player){
        player.sendMessage("Started Quest");
    }


    ConversationObjective getConversation(){
        PlayerOption endOption = new PlayerOption("當然～　我馬上出發");
        NPCQuestion npcQuestion = new NPCQuestion("能幫我沙幾隻怪物嗎")
                .addPlayerOption(new PlayerOption("不要")
                        .setNpcResponse("幹小氣鬼"))
                .addPlayerOption(new PlayerOption("好啊")
                        .setNpcResponse(new NPCQuestion("很好! 這隻怪物在遙遠的天邊","而且很強，即便如此你也要去嗎")
                                .addPlayerOption(endOption
                                        .setNpcResponse("太好了! 我果然沒看錯人"))
                                .addPlayerOption(new PlayerOption("我有點怕 還是算了")
                                        .setNpcResponse("媽的臭俗辣，浪費我時間"))));
        return new ConversationObjective(this,"ask",5,npcQuestion,endOption);
    }

}
