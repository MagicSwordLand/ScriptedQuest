package net.brian.scriptedquests.conversations;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.quests.QuestManager;
import net.brian.scriptedquests.demo.conversation.DemoConversation;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;

public class ConversationManager implements Listener {

    private final QuestManager questManager;

    private double maxDistance = 5;
    private final HashMap<Player, NPCQuestion> pendingResponses = new HashMap<>();
    private final HashMap<Integer, NPCQuestion> responseMap = new HashMap<>();

    public ConversationManager(ScriptedQuests plugin){
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
        questManager = plugin.getQuestManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST,ignoreCancelled = true)
    public void onNPCClick(NPCRightClickEvent event){
        if(!inConversation(event.getClicker())){
            NPCQuestion response = responseMap.get(event.getNPC().getId());
            if(response != null){
                response.send(event.getClicker());
            }
        }
    }


    public void cachePending(Player player, NPCQuestion npcQuestion){
        pendingResponses.put(player, npcQuestion);
    }

    @EventHandler
    public void onInputOption(PlayerCommandPreprocessEvent event){
        if(event.getMessage().startsWith("/squestpickoption")){
            event.setCancelled(true);
            NPCQuestion npcQuestion = pendingResponses.get(event.getPlayer());
            if(npcQuestion == null || npcQuestion.getNPC() == null) {
                event.getPlayer().sendMessage("無法回覆此對話");
                return;
            }

            String[] cmd = event.getMessage().split(" ");
            if(cmd.length>=2){
                try {
                    int option = Integer.parseInt(cmd[1]);
                    if(npcQuestion.getPlayerOptions().size() > option){
                        npcQuestion.getOption(event.getPlayer(), option)
                                .ifPresent(playerOption->{
                                    pendingResponses.remove(event.getPlayer());
                                    playerOption.result.process(event.getPlayer(), npcQuestion.getNPC());
                                });
                    }
                }catch (NumberFormatException ignore){
                }
            }
        }
    }

    @EventHandler
    public void onWalkAway(PlayerMoveEvent event){
        NPCQuestion npcQuestion = pendingResponses.get(event.getPlayer());
        if(npcQuestion != null){
            NPC npc = npcQuestion.getNPC();
            if(npc != null){
                if(maxDistance <event.getPlayer().getLocation().distance(npc.getEntity().getLocation())){
                    event.getPlayer().sendMessage("距離對話者太遠，已停止對話");
                    cancelConversation(event.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        cancelConversation(event.getPlayer());
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event){
        if(!event.getPlayer().isSneaking()){
            cancelConversation(event.getPlayer());
        }
    }

    public void cancelConversation(Player player){
        if(pendingResponses.containsKey(player)){
            pendingResponses.remove(player);
            player.sendMessage("已取消當前對話");
        }
    }

    public void endConversation(Player player){
        pendingResponses.remove(player);
    }

    public boolean inConversation(Player player){
        return pendingResponses.containsKey(player);
    }


    public void registerConversation(int npcID,NPCQuestion question){
        responseMap.put(npcID,question);
    }



}
