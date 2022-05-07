package net.brian.scriptedquests.demo.quests;

import net.brian.scriptedquests.objectives.BreakBlockObjective;
import net.brian.scriptedquests.api.quests.Quest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BreakDiasyQuest extends Quest {

    public static String ID = "daisy";

    public BreakDiasyQuest() {
        super(ID);
        pushObjective(
                new BreakBlockObjective(this,"break",Material.OXEYE_DAISY,5)
                        .setInstruction(data -> "破壞 "+data.getAmount()+"/5 個daisy")
                        .setLocation("world",100,200,100)
                        .setEndProcess(player -> player.sendMessage("恭喜你完成了階段一"))

        );
    }

    @Override
    public void onEnd(Player player){
        player.sendMessage("你完成了 daisy 任務 超棒!");
    }

    @Override
    public void onStart(Player player) {
        player.sendMessage("你開始了daisy任務");
    }

}
