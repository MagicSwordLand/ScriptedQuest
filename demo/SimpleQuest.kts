import net.brian.scriptedquests.api.objectives.QuestObjective
import net.brian.scriptedquests.api.objectives.data.IntegerData
import net.brian.scriptedquests.api.quests.Quest
import net.brian.scriptedquests.compability.mythicmobs.KillMobsObjective
import net.brian.scriptedquests.objectives.GiveItemObjective
import net.brian.scriptedquests.objectives.ListenNPCObjective
import net.brian.scriptedquests.objectives.ListenTalkObjective
import net.brian.scriptedquests.rewards.MMOReward
import net.brian.scriptedquests.rewards.MoneyReward
import net.brian.scriptedquests.rewards.RandomAmount
import org.bukkit.entity.Player

class SimpleQuest : Quest("simple-quest-1") {
    init {
        val npcID = 10

        //聽完對話
        val obj1 = ListenTalkObjective(this, "obj1",
            "%player_display_name% 你今天好嗎",
            "有任務要請你幫忙做",
            "去山上幫我打妖怪吧!"
        )
            .setInstruction("聽完跟npc的對話")

        //擊殺 5 隻 SkeletonKing
        val obj2 = KillMobsObjective(this, "obj2", "SkeletonKing", 5)
            .setInstruction { data: IntegerData -> "擊殺 ${data.amount}/5 隻怪物" }
            .setEndProcess { player: Player -> player.sendMessage("殺夠了 回去找npc好了") }

        //右鍵npc對話
        val obj3 = ListenNPCObjective(this, "obj3", npcID,
            "太感謝你了",
            "再幫我去帶回xxx好不好"
        )
            .setInstruction("回去找npc吧!")

        //交物品給npc
        val obj4: QuestObjective = GiveItemObjective(this, "obj4", npcID, "MATERIAL:END", 10)
            .setInstruction { data: IntegerData -> "帶回 ${data.amount}/10 個xxx" }

        //聽完對話
        val obj5: QuestObjective = ListenTalkObjective( this, "obj5",
            "Nice 太贊了",
            "這是你的獎勵"
        )

        //註冊任務目標
        //他會按照順序排給玩家
        //當玩家完成當前的 objective 就會派發下一個 objective 給他
        pushObjective(obj1, obj2, obj3, obj4, obj5)
        addRewards(
            MoneyReward(RandomAmount(10, 20)),
            MMOReward("type", "id", RandomAmount(10, 20))
        )
    }

    override fun onEnd(player: Player) {
        player.sendMessage("完成任務")
    }
}
