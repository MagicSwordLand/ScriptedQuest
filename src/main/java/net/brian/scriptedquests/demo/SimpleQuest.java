package net.brian.scriptedquests.demo;

import net.brian.scriptedquests.api.objectives.QuestObjective;
import net.brian.scriptedquests.api.quests.Quest;
import net.brian.scriptedquests.compability.mythicmobs.KillMobsObjective;
import net.brian.scriptedquests.objectives.GiveItemObjective;
import net.brian.scriptedquests.objectives.ListenNPCObjective;
import net.brian.scriptedquests.objectives.ListenTalkObjective;
import net.brian.scriptedquests.rewards.MMOReward;
import net.brian.scriptedquests.rewards.MoneyReward;
import net.brian.scriptedquests.rewards.RandomAmount;
import org.bukkit.entity.Player;

public class SimpleQuest extends Quest {

    public SimpleQuest() {
        super("simple-quest-1");

        final int npcID = 10;

        //聽完對話
        QuestObjective obj1 = new ListenTalkObjective(this,"obj1",
                "%player_display_name% 你今天好嗎",
                "有任務要請你幫忙做",
                "去山上幫我打妖怪吧!")
                .setInstruction("聽完跟npc的對話");

        //擊殺 5 隻 SkeletonKing
        QuestObjective obj2 = new KillMobsObjective(this,"obj2","SkeletonKing",5)
                .setInstruction(data-> "擊殺 "+data.getAmount()+"/5 隻怪物")
                .setEndProcess(player -> player.sendMessage("殺夠了 回去找npc好了"));

        //右鍵npc對話
        QuestObjective obj3 = new ListenNPCObjective(this,"obj3",npcID,
                "太感謝你了",
                "再幫我去帶回xxx好不好")
                .setInstruction("回去找npc吧!");

        //交物品給npc
        QuestObjective obj4 = new GiveItemObjective(this,"obj4",npcID,"MATERIAL:END",10)
                .setInstruction(data->"帶回 "+data.getAmount()+"/10 個xxx");

        //聽完對話
        QuestObjective obj5 = new ListenTalkObjective(this,"obj5",
                "Nice 太贊了",
                "這是你的獎勵");

        pushObjective(obj1,obj2,obj3,obj4,obj5);

        addRewards(
                new MoneyReward(new RandomAmount(10,20)),
                new MMOReward("type","id",new RandomAmount(10,20))
        );

    }


    @Override
    public void onEnd(Player player) {
        player.sendMessage("完成任務");
    }
}
