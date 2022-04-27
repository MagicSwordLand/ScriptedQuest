package net.brian.scriptedquests.conversations;

import net.brian.scriptedquests.ScriptedQuests;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class NPCQuestion {

    int npcID;
    String message;
    List<PlayerOption> playerOptions;
    HashMap<Player,List<PlayerOption>> validOptions = new HashMap<>();

    public NPCQuestion(String message, int npcID, PlayerOption... playerOptions){
        this.message = message;
        this.playerOptions = List.of(playerOptions);
        this.npcID = npcID;
    }

    public void send(Player player){
        int i=0;
        List<PlayerOption> optionSet = playerOptions.stream().filter(playerOption -> playerOption.valid(player)).toList();
        validOptions.put(player,optionSet);
        if(optionSet.isEmpty()){
            player.sendMessage(getNPC().getName()+": "+message);
            ScriptedQuests.getInstance().getConversationManager().endConversation(player);
            return;
        }
        else{
            player.sendMessage("");
            player.sendMessage(getNPC().getName()+": "+message);
            player.sendMessage("================================");
            player.sendMessage("");
            for (PlayerOption playerOption : optionSet) {
                String line = playerOption.getMessage(player);
                TextComponent component = new TextComponent(TextComponent.fromLegacyText(line));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/squestpickoption "+i));
                player.sendMessage(component);
                player.sendMessage("");
                i++;
            }
        }
        player.sendMessage("================================");
        ScriptedQuests.getInstance().getConversationManager().cachePending(player,this);
    }

    public List<PlayerOption> getPlayerOptions(){
        return playerOptions;
    }

    public Optional<PlayerOption> getOption(Player player, int index) {
        List<PlayerOption> list = validOptions.get(player);
        if(list != null && list.size()>index){
            return Optional.ofNullable(list.get(index));
        }
        return Optional.empty();
    }

    public NPC getNPC() {
        return CitizensAPI.getNPCRegistry().getById(npcID);
    }

    public int getNpcID() {
        return npcID;
    }
}
