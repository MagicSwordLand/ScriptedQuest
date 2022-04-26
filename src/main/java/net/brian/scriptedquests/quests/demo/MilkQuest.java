package net.brian.scriptedquests.quests.demo;

import net.brian.scriptedquests.quests.Quest;
import org.bukkit.entity.Player;

public class MilkQuest extends Quest {

    public static final String ID = "milk";

    public MilkQuest() {
        super(ID);
    }

    @Override
    public void onStart(Player player) {
        player.sendMessage("你開始了milk任務");
    }

    @Override
    public void onEnd(Player player) {
        player.sendMessage("你完成了milk任務");
    }
}
