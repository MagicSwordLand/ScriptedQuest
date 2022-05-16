package net.brian.scriptedquests.conversation;

import net.brian.scriptedquests.api.conditions.Condition;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NPCQuestion extends NPCMessage {

    int priority = 1;
    final List<PlayerOption> playerOptions = new ArrayList<>();
    List<Condition> conditions = new ArrayList<>();

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


    public NPCQuestion addPlayerOptions(PlayerOption playerOption, PlayerOption... options){
        playerOptions.add(playerOption);
        playerOptions.addAll(Arrays.stream(options).toList());
        return this;
    }

    public List<PlayerOption> getPlayerOptions(Player player) {
        return playerOptions.stream().filter(option-> option.shouldShow(player)).collect(Collectors.toList());
    }


    public boolean valid(Player player){
        return Condition.test(player,conditions);
    }

    public NPCQuestion setCondition(Condition... conditions){
        this.conditions.addAll(Arrays.stream(conditions).toList());
        return this;
    }

    public NPCQuestion setPriority(int priority){
        this.priority = priority;
        return this;
    }

    @Override
    public void send(int npcID,Player player){
        if(Condition.test(player,conditions)){
            NPC npc = CitizensAPI.getNPCRegistry().getById(npcID);
            if(npc != null){
                new Conversation(this,getPlayerOptions(player)).send(npcID,player);
            }
        }
    }

}
