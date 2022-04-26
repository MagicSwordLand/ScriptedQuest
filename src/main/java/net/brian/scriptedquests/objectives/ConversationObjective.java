package net.brian.scriptedquests.objectives;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.objectives.QuestObjective;
import net.brian.scriptedquests.conversation.NPCResponse;
import net.brian.scriptedquests.conversation.PlayerOption;
import net.brian.scriptedquests.quests.Quest;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class ConversationObjective extends QuestObjective {

    private final NPCResponse npcResponse;
    private final PlayerOption endOption;

    public ConversationObjective(Quest quest,String objID, Conversation conversation){
        super(quest,objID);
        this.npcResponse = conversation.startResponse;
        this.endOption = conversation.endOption;
        PlayerOption.Result originResult = endOption.getResult();
        endOption.setResult(((player, npc) -> {
            originResult.process(player,npc);
            finish(player);
        }));
    }

    @Override
    public Class<?> getDataClass() {
        return Object.class;
    }

    @Override
    public Object newObjectiveData() {
        return new Object();
    }

    @Override
    public String getInstruction(Player player) {
        return "與"+npcResponse.getNPC().getName()+"對話";
    }

    public record Conversation(NPCResponse startResponse,PlayerOption endOption){};

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClickNPC(NPCRightClickEvent event){
        Player player = event.getClicker();
        if(event.getNPC().getId() == npcResponse.getNpcID() && playerIsOngoing(player)){
            if(!ScriptedQuests.getInstance().getConversationManager().inConversation(player)){
                npcResponse.send(player);
                event.setCancelled(true);
            }
            else{
                player.sendMessage("正在對話中，請蹲下來取消");
            }
        }
    }


}
