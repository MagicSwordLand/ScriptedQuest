package net.brian.scriptedquests.objectives;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.objectives.QuestObjective;

import net.brian.scriptedquests.api.quests.Quest;
import net.brian.scriptedquests.conversation.NPCQuestion;
import net.brian.scriptedquests.conversation.PlayerOption;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.awt.geom.RectangularShape;

public class ConversationObjective extends QuestObjective {


    private NPCQuestion startQuestion;
    private PlayerOption endOption;
    private int npcID;

    public ConversationObjective(Quest quest,String objID,int npcID,NPCQuestion startQuestion,PlayerOption endOption){
        super(quest,objID);
        this.startQuestion =startQuestion;
        this.endOption = endOption;
        this.npcID = npcID;
        PlayerOption.Result oldResult = endOption.getResult();
        endOption.setResult((player, id) -> {
            if(oldResult != null){
                oldResult.process(player,id);
            }
            finish(player);
        });
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
