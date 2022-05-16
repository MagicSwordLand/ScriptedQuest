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


public class ConversationObjective extends NPCObjective {


    private NPCQuestion startQuestion = null;
    private boolean startInstantly = true;

    public ConversationObjective(Quest quest,String objID,int npcID){
        super(quest,objID,npcID);

    }

    public ConversationObjective(Quest quest,String objID,int npcID,boolean startInstantly,NPCQuestion startQuestion,PlayerOption... endOptions){
        super(quest,objID,npcID);
        this.startQuestion =startQuestion;
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
        this.startInstantly = startInstantly;
    }

    public ConversationObjective(Quest quest,String objID,int npcID,NPCQuestion startQuestion,PlayerOption... endOptions){
        this(quest,objID,npcID,true,startQuestion,endOptions);
    }

    public void start(Player player){
       super.start(player);
       if(startInstantly){
           if(startQuestion != null){
               startQuestion.send(npcID,player);
           }
       }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onClickNPC(NPCRightClickEvent event){
        if(event.getNPC().getId() == npcID && playerIsDoing(event.getClicker())){
            if(!ScriptedQuests.getInstance().getConversationManager().inConversation(event.getClicker())){
                event.setCancelled(true);
                if(startQuestion != null){
                    startQuestion.send(npcID,event.getClicker());
                }
                else finish(event.getClicker());
            }
        }
    }

    public ConversationObjective setStartQuestion(NPCQuestion npcQuestion){
        startQuestion = npcQuestion;
        return  this;
    }

    public ConversationObjective setStartInstantly(boolean startInstantly) {
        this.startInstantly = startInstantly;
        return this;
    }
}
