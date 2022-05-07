package net.brian.scriptedquests.objectives;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.objectives.QuestObjective;

import net.brian.scriptedquests.api.quests.Quest;
import net.brian.scriptedquests.conversation.NPCQuestion;
import net.brian.scriptedquests.conversation.PlayerOption;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.awt.geom.RectangularShape;

public class ConversationObjective extends QuestObjective {


    private NPCQuestion startQuestion;
    private int npcID;
    private boolean startInstantly;

    public ConversationObjective(Quest quest,String objID,int npcID,boolean startInstantly,NPCQuestion startQuestion,PlayerOption... endOptions){
        super(quest,objID);
        this.startQuestion =startQuestion;
        this.npcID = npcID;
        for (PlayerOption endOption : endOptions) {
            PlayerOption.Result oldResult = endOption.getResult();
            endOption.setResult((player, id) -> {
                if(oldResult != null){
                    oldResult.process(player,id);
                }
                finish(player);
            });
        }
        NPC npc = CitizensAPI.getNPCRegistry().getById(npcID);
        if(npc != null){
           setLocation(npc.getEntity().getLocation());
        }
    }

    public ConversationObjective(Quest quest,String objID,int npcID,NPCQuestion startQuestion,PlayerOption... endOptions){
        this(quest,objID,npcID,true,startQuestion,endOptions);
    }

    public void start(Player player){
       super.start(player);
       if(startInstantly){
           startQuestion.send(npcID,player);
       }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onClickNPC(NPCRightClickEvent event){
        if(event.getNPC().getId() == npcID && playerIsDoing(event.getClicker())){
            if(!ScriptedQuests.getInstance().getConversationManager().inConversation(event.getClicker())){
                event.setCancelled(true);
                startQuestion.send(npcID,event.getClicker());
            }
        }
    }


}
