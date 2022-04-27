package net.brian.scriptedquests.demo.quests;

import net.brian.scriptedquests.objectives.KillMobsObjectives;
import net.brian.scriptedquests.api.quests.Quest;
import org.bukkit.entity.Player;

public class KillZombieQuest extends Quest {

    public static final String ID = "zombie";
    public KillZombieQuest() {
        super(ID);
        KillMobsObjectives killMobsObjectives = new KillMobsObjectives("kill",this,"SkeletonKing",5);
        pushObjective(killMobsObjectives);
        killMobsObjectives.setMobName("超op怪物");
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