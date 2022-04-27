package net.brian.scriptedquests.conversations;

import me.clip.placeholderapi.PlaceholderAPI;
import net.brian.scriptedquests.api.conditions.Condition;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;

public class PlayerOption {
    Condition[] conditions;
    String message;
    Result result;
    boolean end;

    public PlayerOption(String message, Result result,boolean end,Condition... conditions){
        this.message = message;
        this.result = result;
        this.conditions = conditions;
        this.end = end;
    }

    public PlayerOption(String message, Result result,Condition... conditions){
        this(message,result,false,conditions);
    }

    public PlayerOption(String message,boolean end,Condition... conditions){
        this(message,((player, npc) -> {}),end,conditions);
    }

    public PlayerOption(String message,Condition... conditions){
        this(message,((player, npc) -> {}),conditions);
    }


    public interface Result {
        void process(Player player, NPC npc);
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result){
        this.result= result;
    }

    public String getMessage(Player player){
        return PlaceholderAPI.setPlaceholders(player,message);
    }

    public boolean valid(Player player){
        for (Condition condition : conditions) {
            if(!condition.test(player)){
                return false;
            }
        }
        return true;
    }

    public boolean shouldEnd(){
        return end;
    }
}
