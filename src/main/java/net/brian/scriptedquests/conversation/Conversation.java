package net.brian.scriptedquests.conversation;


import io.lumine.mythic.utils.metadata.Pair;

import me.clip.placeholderapi.PlaceholderAPI;
import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.quests.QuestManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;

import java.util.List;

public class Conversation{

        NPCQuestion npcQuestion;
        List<PlayerOption> playerOptions;

        public Conversation(NPCQuestion npcQuestion,List<PlayerOption> playerOptions){
            this.npcQuestion = npcQuestion;
            this.playerOptions = playerOptions;
        }
        
        public void send(int senderID, Player player){
            ScriptedQuests.getInstance().getConversationManager().cachePendingResponses(player, Pair.of(senderID,playerOptions));
            NPC npc = CitizensAPI.getNPCRegistry().getById(senderID);
            npcQuestion.messages.forEach(message->player.sendMessage(npc.getName()+": "+ PlaceholderAPI.setPlaceholders(player,message)));

            player.sendMessage("================================");
            player.sendMessage("");
            int a = 0;
            for (PlayerOption playerOption : playerOptions) {
                TextComponent component = Component.text(playerOption.message);
                component = component.clickEvent(ClickEvent.runCommand("/squestpickoption "+a));
                player.sendMessage(component);
                a++;
            }
            player.sendMessage("");
            player.sendMessage("================================");
        }
    }