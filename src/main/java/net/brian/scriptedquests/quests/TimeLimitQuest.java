package net.brian.scriptedquests.quests;

import net.brian.playerdatasync.util.time.TimeUnit;
import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.objectives.QuestObjective;
import net.brian.scriptedquests.api.objectives.data.ObjectiveData;
import net.brian.scriptedquests.api.quests.Quest;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import net.brian.scriptedquests.data.SerializedQuestData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public abstract class TimeLimitQuest extends Quest {

    public static final long SECONDS = 1000L;
    public static final long MINUTE = 60000L;
    public static final long HOUR = 3600000L;
    public static final long DAY = 86400000L;

    public static final long T20220515 = 1652544000000L;

    long interval;
    long startTime;
    BukkitTask bukkitTask;

    public TimeLimitQuest(String questID,String displayName,long interval,long startTime) {
        super(questID,displayName);
        this.interval = interval;
        this.startTime = startTime;
        long millSecDelay = interval-((System.currentTimeMillis()-startTime)%interval);
        cancelTask(milliSecToTick(millSecDelay));
    }

    public TimeLimitQuest(String questID,String displayName){
        this(questID,displayName, DAY,T20220515);
    }

    public void onStart(Player player){
        long remainTime = interval-((System.currentTimeMillis()-startTime)%interval);
        player.sendMessage("§7[§a任務§7] §f已開始限時任務，你還有 "+TimeUnit.getDisplayTime(remainTime)+" 可完成此任務");
    }


    void cancelTask(long delayTick){
        bukkitTask = Bukkit.getScheduler().runTaskLater(ScriptedQuests.getInstance(),()->{
            objectives.values().forEach(objective -> {
                objective.getOnlinePlayers().forEach(player -> {
                    player.sendMessage("§7[§a任務§7]§f "+displayName+" 已超時，判定為失敗");
                });
                objective.cancelAll();
            });
            cancelTask(milliSecToTick(interval));
        },delayTick);
    }

    @Override
    public void unregister(){
        bukkitTask.cancel();
    }

    @Override
    public void cachePlayer(Player player, SerializedQuestData data){
        long a = (data.getStartTime()-startTime)/interval;
        long b = (System.currentTimeMillis()-startTime)/interval;
        if(a != b){
            player.sendMessage("任務 ["+displayName+"] 已超時，判定為失敗");
            PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(playerData->{
                playerData.removeQuestData(questID);
            });
            return;
        }
        super.cachePlayer(player,data);
    }


    private static long milliSecToTick(long milliSec){
        return milliSec/50;
    }

}
