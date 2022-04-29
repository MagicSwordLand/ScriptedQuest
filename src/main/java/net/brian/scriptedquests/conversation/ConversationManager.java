package net.brian.scriptedquests.conversation;

import io.lumine.mythic.utils.metadata.Pair;
import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.quests.QuestManager;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationManager implements Listener {

    private final Map<Integer, NPCRespondProfile> questions = new HashMap<>();
    private final Map<Player, Pair<Integer,List<PlayerOption>>> pendingOptions = new HashMap<>();

    public ConversationManager(QuestManager questManager){
        Bukkit.getServer().getPluginManager().registerEvents(this,ScriptedQuests.getInstance());
        pushNPCQuestion(5,new NPCQuestion("甚麼事?")
                .addPlayerOption(questManager.getQuest("test")
                        .map(quest -> quest.getStartOption("最近治安如何",false))
                        .orElse(new PlayerOption("天氣真好").setNpcResponse("對阿")))
                .addPlayerOption(new PlayerOption("沒什麼").setNpcResponse("沒事就滾開")));
    }

    public void pushNPCQuestion(int npcID,NPCQuestion npcQuestion){
        NPCRespondProfile responses = questions.get(npcID);
        if (responses != null) {
            responses.add(npcQuestion);
        }
        else{
            responses = new NPCRespondProfile(npcID);
            responses.add(npcQuestion);
            questions.put(npcID,responses);
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
    public void onRightClick(NPCRightClickEvent event){
        NPCRespondProfile npcRespondProfile = questions.get(event.getNPC().getId());
        if(npcRespondProfile != null){
            Player player = event.getClicker();
            Conversation conversation = npcRespondProfile.getConversation(player);
            if(conversation != null){
                conversation.send(event.getNPC().getId(),player);
            }
        }
    }


    @EventHandler
    public void onInputOption(PlayerCommandPreprocessEvent event){
        if(event.getMessage().startsWith("/squestpickoption")){
            event.setCancelled(true);
            Player player = event.getPlayer();
            Pair<Integer,List<PlayerOption>> playerOptions = pendingOptions.get(player);
            String[] cmd = event.getMessage().split(" ");

            if(cmd.length>=2 && playerOptions != null){
                try {
                    int option = Integer.parseInt(cmd[1]);
                    pendingOptions.remove(player);
                    playerOptions.getValue().get(option).process(player,playerOptions.getKey());
                }catch (NumberFormatException ignore){
                }
            }
            else player.sendMessage("你無法回覆此對話");
        }
    }

    public void cachePendingResponses(Player player,Pair<Integer,List<PlayerOption>> options){
        pendingOptions.put(player,options);
    }

    public boolean inConversation(Player player){
        return pendingOptions.containsKey(player);
    }
}
