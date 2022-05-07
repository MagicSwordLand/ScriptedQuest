package net.brian.scriptedquests.conversation;

import me.clip.placeholderapi.PlaceholderAPI;
import net.brian.scriptedquests.ScriptedQuests;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NPCMessage {

    List<String> messages = new ArrayList<>();

    public NPCMessage(String... messages){
        this.messages.addAll(Arrays.stream(messages).toList());
    }

    public void send(int npcID, Player player){
        NPC npc = CitizensAPI.getNPCRegistry().getById(npcID);
        if(npc != null){
            int[] index = {0};
            Bukkit.getScheduler().runTaskTimer(ScriptedQuests.getInstance(),()->{
                if(index[0] < messages.size()){
                    player.sendMessage(PlaceholderAPI.setPlaceholders(player,messages.get(index[0])));
                    index[0]++;
                }
            },0,35);
        }
    }
}
