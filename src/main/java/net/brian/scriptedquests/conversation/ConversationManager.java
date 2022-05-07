package net.brian.scriptedquests.conversation;

import io.lumine.mythic.utils.metadata.Pair;
import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.quests.QuestManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationManager implements Listener {

    private final Map<Integer, NPCRespondProfile> questions = new HashMap<>();
    private final Map<Player, Pair<Integer,List<PlayerOption>>> pendingOptions = new HashMap<>();

    public ConversationManager(QuestManager questManager){
        Bukkit.getServer().getPluginManager().registerEvents(this,ScriptedQuests.getInstance());
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
        if(!pendingOptions.containsKey(event.getClicker())){
            NPCRespondProfile npcRespondProfile = questions.get(event.getNPC().getId());
            if(npcRespondProfile != null){
                Player player = event.getClicker();
                Conversation conversation = npcRespondProfile.getConversation(player);
                if(conversation != null){
                    conversation.send(event.getNPC().getId(),player);
                }
            }
        }
        else event.getClicker().sendMessage("§e請先結束上當前對話，蹲下可取消當前對話");
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
                    if(option < playerOptions.getValue().size()){
                        pendingOptions.remove(player);
                        playerOptions.getValue().get(option).process(player,playerOptions.getKey());
                    }
                }catch (NumberFormatException ignore){
                }
            }
            else player.sendMessage("§c此對話已結束");
        }
    }

    @EventHandler
    public void onShift(PlayerToggleSneakEvent event){
        if(pendingOptions.containsKey(event.getPlayer())){
            pendingOptions.remove(event.getPlayer());
            event.getPlayer().sendMessage("§c已結束對話");
        }
    }

    @EventHandler
    public void onWalkAway(PlayerMoveEvent event){
        Pair<Integer,List<PlayerOption>> pair = pendingOptions.get(event.getPlayer());
        if(pair != null){
            Player player = event.getPlayer();
            NPC npc = CitizensAPI.getNPCRegistry().getById(pair.getKey());
            if(npc != null && player.getLocation().distance(npc.getEntity().getLocation()) > 5){
                pendingOptions.remove(player);
                player.sendMessage("§c距離對話者太遠 當前對話已取消");
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        pendingOptions.remove(event.getPlayer());
    }

    public void cachePendingResponses(Player player,Pair<Integer,List<PlayerOption>> options){
        pendingOptions.put(player,options);
    }

    public boolean inConversation(Player player){
        return pendingOptions.containsKey(player);
    }
}
