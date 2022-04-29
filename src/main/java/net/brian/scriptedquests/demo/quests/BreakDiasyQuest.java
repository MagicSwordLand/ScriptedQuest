package net.brian.scriptedquests.demo.quests;

import net.brian.scriptedquests.objectives.BreakBlockObjective;
import net.brian.scriptedquests.api.quests.Quest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BreakDiasyQuest extends Quest {

    public static String ID = "daisy";

    public BreakDiasyQuest() {
        super(ID);
        BreakBlockObjective breakBlockObjective = new BreakBlockObjective(this,"break",Material.OXEYE_DAISY,5);
        breakBlockObjective.setInstruction("破壞 %d/5 個daisy",player -> {
            return breakBlockObjective.getData(player.getUniqueId())
                    .map(BreakBlockObjective.BreakProfile::getAmount).orElse(0);
        });

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
