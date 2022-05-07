package net.brian.scriptedquests.objectives;

import me.clip.placeholderapi.PlaceholderAPI;
import net.brian.playerdatasync.events.PlayerDataFetchComplete;
import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.objectives.QuestObjective;
import net.brian.scriptedquests.api.quests.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.scheduler.BukkitRunnable;

public class ListenTalkObjective extends QuestObjective {

    private String[] texts;
    private long period = 40;

    public ListenTalkObjective(Quest quest, String objectiveID, String... texts) {
        super(quest, objectiveID);
        this.texts = texts;
    }

    public ListenTalkObjective setPeriod(long period) {
        this.period = period;
        return this;
    }

    @Override
    public void start(Player player){
        super.start(player);
        send(player);
    }

    private void send(Player player){
        int[] index = new int[]{0};
        new BukkitRunnable() {
            @Override
            public void run() {
                if(index[0] >= texts.length){
                    finish(player);
                    cancel();
                }
                else{
                    if(player.isOnline()){
                        player.sendMessage(PlaceholderAPI.setPlaceholders(player,texts[index[0]]));
                        index[0]++;
                    }
                    else{
                        cancel();
                    }
                }
            }
        }.runTaskTimer(ScriptedQuests.getInstance(),0,period);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLoaded(PlayerDataFetchComplete event){
        if(playerIsDoing(event.getPlayer())){
            send(event.getPlayer());
        }
    }

}
