package net.brian.scriptedquests.quests.demo;

import net.brian.scriptedquests.objectives.KillMobsObjectives;
import net.brian.scriptedquests.quests.Quest;
import org.bukkit.entity.Player;

public class KillZombieQuest extends Quest {

    public static final String ID = "zombie";
    public KillZombieQuest() {
        super(ID);
        KillMobsObjectives killMobsObjectives = new KillMobsObjectives("kill",this,"SkeletonKing",5);
        registerObjective(killMobsObjectives);
    }

    @Override
    public void onEnd(Player player) {
        player.sendMessage("你完成了殺殭屍任務");
    }

    @Override
    public void onStart(Player player) {
        player.sendMessage("你開始了zombie任務");
    }
}
