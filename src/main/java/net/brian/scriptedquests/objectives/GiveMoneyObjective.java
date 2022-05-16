package net.brian.scriptedquests.objectives;

import net.brian.scriptedquests.api.objectives.QuestObjective;
import net.brian.scriptedquests.api.quests.Quest;
import net.brian.scriptedquests.utils.VaultAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class GiveMoneyObjective extends NPCObjective {

    float amount;
    String message = "支付20塊";

    public GiveMoneyObjective(Quest quest, String objectiveID,int npcID,float amount) {
        super(quest, objectiveID,npcID);
        this.amount = amount;
    }

    public GiveMoneyObjective setMessage(String message){
        this.message = message;
        return this;
    }



    @EventHandler
    public void onClick(NPCRightClickEvent event){
        if(event.getNPC().getId() == npcID && playerIsDoing(event.getClicker())){
            TextComponent text = Component.text(message)
                    .hoverEvent(HoverEvent.showText(Component.text("點擊支付"+amount)))
                    .clickEvent(ClickEvent.runCommand("/givenpcmoney "+quest.getQuestID()+" "+objectiveID));
            event.getClicker().sendMessage(text);
        }
    }

    // /givenpcmoney questID objectiveID

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        if (playerIsDoing(event.getPlayer())) {
            String message = event.getMessage();
            if(message.startsWith("/givenpcmoney ")){
                String[] args = message.split(" ");
                if(args.length == 3){
                    String questID = args[1];
                    String objID = args[2];
                    if(questID.equals(quest.getQuestID()) && objectiveID.equals(objID)){
                        if(VaultAPI.withdraw(event.getPlayer(),amount)){
                            finish(event.getPlayer());
                        }
                    }
                }
            }
        }
    }


}
