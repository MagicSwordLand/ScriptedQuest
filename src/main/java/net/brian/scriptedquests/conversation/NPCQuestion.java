package net.brian.scriptedquests.conversation;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NPCQuestion extends NPCMessage {

    int priority = 1;
    final List<PlayerOption> playerOptions = new ArrayList<>();

    public NPCQuestion(String... message){
        super(message);

    }

    public NPCQuestion addMessage(String... messages){
        this.messages.addAll(Arrays.stream(messages).toList());
        return this;
    }

    public NPCQuestion setMessage(String... messages){
        this.messages = Arrays.stream(messages).toList();
        return this;
    }


    public NPCQuestion addPlayerOption(PlayerOption playerOption){
        playerOptions.add(playerOption);
        return this;
    }

    public List<PlayerOption> getPlayerOptions(Player player) {
        return playerOptions.stream().filter(option-> option.shouldShow(player)).collect(Collectors.toList());
    }

    @Override
    public void send(int npcID,Player player){
        NPC npc = CitizensAPI.getNPCRegistry().getById(npcID);
        if(npc != null){
            new Conversation(this,getPlayerOptions(player)).send(npcID,player);
        }
    }

}
