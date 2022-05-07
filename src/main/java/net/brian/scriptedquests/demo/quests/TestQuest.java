package net.brian.scriptedquests.demo.quests;

import net.brian.scriptedquests.api.quests.Quest;
import net.brian.scriptedquests.conversation.NPCMessage;
import net.brian.scriptedquests.conversation.NPCQuestion;
import net.brian.scriptedquests.conversation.PlayerOption;
import net.brian.scriptedquests.objectives.ConversationObjective;
import net.brian.scriptedquests.compability.mythicmobs.KillMobsObjective;
import net.brian.scriptedquests.objectives.ListenNPCObjective;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TestQuest extends Quest {

    Location location = new Location(Bukkit.getWorld("world"),100,100,100);

    public TestQuest(){
        super("test");

        pushObjective(
                new ListenNPCObjective(this,"startmsg",5,
                        "最近天下不太平",
                        "若是有人能拯救世界就好了啊..."
                        )
                        .setLocation(location)
                        .setInstruction("找到npcID = 5 並跟他對話"),

                getConversationObj().setInstruction("接受npc5的任務"),

                new KillMobsObjective(this,"KillSkeleton","SkeletonKing",2)
                        .setInstruction(data -> "擊殺 "+data.getAmount()+"/2 隻怪物")
                        .setLocation("world",280,64,170)
        );

    }


    @Override
    public void onEnd(Player player) {
        player.sendMessage("Congratulation");
    }

    public void onStart(Player player){
        player.sendMessage("Started Quest");
    }


    ConversationObjective getConversationObj(){
        PlayerOption endOption = new PlayerOption("當然～　我馬上出發");
        NPCQuestion npcQuestion = new NPCQuestion("能幫我沙幾隻怪物嗎")
                .addPlayerOptions(
                        new PlayerOption("不要",new NPCMessage("幹小氣鬼")),
                        new PlayerOption("好啊!")
                                .setNpcResponse(new NPCQuestion("很好! 這隻怪物在遙遠的天邊","而且很強，即便如此你也要去嗎").addPlayerOptions(
                                        endOption.setNpcResponse("太好了 我果然沒看錯人"),
                                        new PlayerOption("我有點怕，還是算了")
                                                .setNpcResponse("媽的臭俗辣，浪費我時間")
                                        )
                                )
                );
        return new ConversationObjective(this,"ask",5,npcQuestion,endOption);
    }

}
