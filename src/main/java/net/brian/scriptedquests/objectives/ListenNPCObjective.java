package net.brian.scriptedquests.objectives;

import me.clip.placeholderapi.PlaceholderAPI;
import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.api.objectives.QuestObjective;
import net.brian.scriptedquests.api.quests.Quest;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ListenNPCObjective extends NPCObjective {

    String[] text;
    int delayTick;
    List<Player> listeningPlayer = new ArrayList<>();
    private boolean disableWalk = true;

    public ListenNPCObjective(Quest quest, String objectiveID, int npcID, int delayTick, String... text) {
        super(quest, objectiveID,npcID);
        this.delayTick = delayTick;
        this.text = text;
    }

    public ListenNPCObjective(Quest quest, String objectiveID, int npcID, String... text) {
        this(quest,objectiveID,npcID,40,text);
    }

    public ListenNPCObjective disableWalk(boolean enable){
        disableWalk = enable;
        return this;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(NPCRightClickEvent event){
        Player player = event.getClicker();
        if(event.getNPC().getId() == npcID && playerIsDoing(player) && Condition.test(player,conditions)){
            if(playerIsDoing(player)){
                event.setCancelled(true);
            }
            if(listeningPlayer.contains(player)){
                return;
            }
            if(ScriptedQuests.getInstance().getConversationManager().inConversation(player)){
                player.sendMessage("請結束上一個對話才可繼續");
                return;
            }
            listeningPlayer.add(player);
            final int[] index = new int[]{0};
            NPC npc = CitizensAPI.getNPCRegistry().getById(npcID);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(text.length > index[0]){
                        if(npc != null && player.isOnline()){
                            player.playSound(player.getLocation(),Sound.ENTITY_VILLAGER_AMBIENT,1,1);

                            player.sendMessage(PlaceholderAPI.setPlaceholders(player,text[index[0]]));
                            index[0]++;
                        }
                        else{
                            listeningPlayer.remove(player);
                            cancel();
                        }
                    }
                    else {
                        finish(player);
                        listeningPlayer.remove(player);
                        cancel();
                    }
                }
            }.runTaskTimer(ScriptedQuests.getInstance(),0,delayTick);
        }
    }

    @EventHandler
    public void onWalk(PlayerMoveEvent event){
        if(disableWalk){
            if(listeningPlayer.contains(event.getPlayer())){
                event.setCancelled(true);
            }
        }
    }

}
