package net.brian.scriptedquests.conversation;

import org.bukkit.entity.Player;

import java.util.*;

public class NPCRespondProfile {

    private final SortedSet<NPCQuestion> npcQuestions = new TreeSet<>(Comparator.comparingInt(q -> q.priority));

    int npcID;
    public NPCRespondProfile(int npcID){
        this.npcID = npcID;
    }

    public NPCRespondProfile add(NPCQuestion npcQuestion){
        npcQuestions.add(npcQuestion);
        return this;
    }

    public Conversation getConversation(Player player){
        for (NPCQuestion npcQuestion : npcQuestions) {
            if(npcQuestion.valid(player)){
                List<PlayerOption> options = npcQuestion.getPlayerOptions(player);
                if(!options.isEmpty()){
                    return new Conversation(npcQuestion,options);
                }
            }
        }
        return null;
    }



}
