package net.brian.scriptedquests.objectives;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.objectives.QuestObjective;
import net.brian.scriptedquests.quests.Quest;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ListenObjective extends QuestObjective {

    String[] text;
    int npcID;
    int delayTick;
    List<Player> players = new ArrayList<>();

    public ListenObjective(Quest quest, String objectiveID,int npcID,int delayTick,String... text) {
        super(quest, objectiveID);
        this.npcID = npcID;
        this.delayTick = delayTick;
        this.text = text;
    }

    public ListenObjective(Quest quest, String objectiveID,int npcID,String... text) {
        this(quest,objectiveID,npcID,40,text);
    }

    @EventHandler
    public void onClick(NPCRightClickEvent event){
        Player player = event.getClicker();
        if(event.getNPC().getId() == npcID && playerIsDoing(player)){
            if(players.contains(player)){
                return;
            }
            players.add(player);
            final int[] index = new int[]{0};
            NPC npc = CitizensAPI.getNPCRegistry().getById(npcID);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(text.length > index[0]){
                        if(npc != null && player.isOnline()){
                            player.sendMessage(npc.getName()+": "+text[index[0]]);
                            index[0]++;
                        }
                        else{
                            cancel();
                        }
                    }
                    else {
                        finish(player);
                        cancel();
                    }
                }
            }.runTaskTimer(ScriptedQuests.getInstance(),0,delayTick);
        }
    }




    @Override
    public String getInstruction(Player player) {
        NPC npc = CitizensAPI.getNPCRegistry().getById(npcID);
        if(npc != null){
            return "跟"+npc.getName() +" 說話";
        }
        return "找不到NPC, Quest:"+quest.getQuestID()+" obj:"+objectiveID;
    }
}
