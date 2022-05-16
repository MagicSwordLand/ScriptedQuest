package net.brian.scriptedquests.conversation;


import io.lumine.mythic.utils.metadata.Pair;

import me.clip.placeholderapi.PlaceholderAPI;
import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.quests.QuestManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import org.bukkit.Bukkit;
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
            List<String> messages = npcQuestion.messages;

            player.sendMessage("","");

            player.sendMessage("§7================================");
            for (String message : messages) {
                player.sendMessage(PlaceholderAPI.setPlaceholders(player,message));
            }
            player.sendMessage("");
            int a = 0;
            for (PlayerOption playerOption : playerOptions) {
                TextComponent component = Component.text("§c["+(a+1)+"]§f  "+ PlaceholderAPI.setPlaceholders(player,playerOption.message))
                        .clickEvent(ClickEvent.runCommand("/squestpickoption "+a))
                        .hoverEvent(HoverEvent.showText(Component.text("點擊回答此選項")));
                player.sendMessage(component);
                player.sendMessage("");
                a++;
            }

            player.sendMessage("§7================================");
        }

    }