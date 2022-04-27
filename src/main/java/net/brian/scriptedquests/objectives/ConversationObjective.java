package net.brian.scriptedquests.objectives;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.objectives.QuestObjective;
import net.brian.scriptedquests.conversations.NPCQuestion;
import net.brian.scriptedquests.conversations.PlayerOption;
import net.brian.scriptedquests.api.quests.Quest;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class ConversationObjective extends QuestObjective {

    private final NPCQuestion npcQuestion;
    private final PlayerOption endOption;

    public ConversationObjective(Quest quest,String objID, Conversation conversation){
        super(quest,objID);
        this.npcQuestion = conversation.startResponse;
        this.endOption = conversation.endOption;
        PlayerOption.Result originResult = endOption.getResult();
        endOption.setResult(((player, npc) -> {
            originResult.process(player,npc);
            finish(player);
        }));
    }


    @Override
    public String getInstruction(Player player) {
        return "與"+ npcQuestion.getNPC().getName()+"對話";
    }

    public record Conversation(NPCQuestion startResponse, PlayerOption endOption){};

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClickNPC(NPCRightClickEvent event){
        Player player = event.getClicker();
        if(event.getNPC().getId() == npcQuestion.getNpcID() && playerIsDoing(player)){
            if(!ScriptedQuests.getInstance().getConversationManager().inConversation(player)){
                npcQuestion.send(player);
                event.setCancelled(true);
            }
            else{
                player.sendMessage("正在對話中，請蹲下來取消");
            }
        }
    }


}
