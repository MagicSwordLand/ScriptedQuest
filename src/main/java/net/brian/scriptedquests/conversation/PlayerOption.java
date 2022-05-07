package net.brian.scriptedquests.conversation;

import net.brian.scriptedquests.api.conditions.Condition;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerOption {


    private Result result;
    String message;
    List<Condition> conditions = new ArrayList<>();
    NPCMessage npcMessage;


    public PlayerOption(String message,Condition... conditions){
        this.message = message;
        this.conditions.addAll(Arrays.asList(conditions));
    }

    public PlayerOption(String message,NPCMessage npcMessage,Condition... conditions){
        this.message = message;
        this.conditions.addAll(Arrays.asList(conditions));
        this.npcMessage = npcMessage;
    }



    public boolean shouldShow(Player player){
        return Condition.test(player,conditions);
    }

    public PlayerOption addCondition(Condition condition){
        conditions.add(condition);
        return this;
    }


    public PlayerOption setResult(Result result){
        this.result = result;
        return this;
    }


    public PlayerOption setNpcResponse(NPCMessage npcQuestion){
        this.npcMessage = npcQuestion;
        return this;
    }

    public PlayerOption setNpcResponse(String... message){
        return setNpcResponse(new NPCMessage(message));
    }

    public Result getResult() {
        return result;
    }

    public void process(Player player, int npcID){
        if(result != null){
            result.process(player,npcID);
        }
        if(npcMessage != null){
            npcMessage.send(npcID,player);
        }
    }


    public static interface Result{
        void process(Player player,int npcID);
    }


}
