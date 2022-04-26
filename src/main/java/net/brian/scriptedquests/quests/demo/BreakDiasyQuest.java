package net.brian.scriptedquests.quests.demo;

import net.brian.scriptedquests.objectives.BreakBlockObjective;
import net.brian.scriptedquests.quests.Quest;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BreakDiasyQuest extends Quest {

    public static String ID = "daisy";

    public BreakDiasyQuest() {
        super(ID);
        BreakBlockObjective obj = new BreakBlockObjective(this,"break", Material.OXEYE_DAISY,5);
        registerObjective(obj);
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
