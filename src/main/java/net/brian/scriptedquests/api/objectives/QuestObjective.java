package net.brian.scriptedquests.api.objectives;


import me.clip.placeholderapi.PlaceholderAPI;
import net.brian.playerdatasync.PlayerDataSync;
import net.brian.playerdatasync.events.PlayerDataFetchComplete;
import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.data.SerializedQuestData;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.api.quests.Quest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.swing.plaf.metal.MetalBorders;
import java.util.*;
import java.util.function.Function;
import java.util.logging.Level;

public abstract class QuestObjective implements Listener {

    protected final String objectiveID;
    protected final Quest quest;
    protected final Condition[] conditions;
    protected final List<Player> onlinePlayers = new ArrayList<>();
    protected Location location;
    protected Process endProcess;
    protected String instruction;

    public QuestObjective(Quest quest,String objectiveID){
        this(quest,objectiveID,player -> true);
    }


    public QuestObjective(Quest quest,String objectiveID, Condition... condition){
        this.quest = quest;
        this.conditions = condition;
        this.objectiveID = objectiveID;
        Bukkit.getServer().getPluginManager().registerEvents(this, ScriptedQuests.getInstance());
    }




    public void start(Player player){
        PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(data->{
            data.setQuestData(quest.getQuestID(),objectiveID,null);
        });
        onlinePlayers.add(player);
        String instruction = getInstruction(player);

        if(instruction != null && !instruction.equals("")){
            player.sendTitle("","[任務目標] "+getInstruction(player));
        }
    }

    public void cancel(Player player){
        if(onlinePlayers.contains(player)){
            quest.onCancel(player);
            onlinePlayers.remove(player);
        }
    }

    public void cancelAll(){
        onlinePlayers.forEach(player -> {
            quest.onCancel(player);
            PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(data->{
                data.removeQuestData(quest.getQuestID());
            });
        });
        onlinePlayers.clear();
    }


    public void finish(Player player){
        UUID uuid = player.getUniqueId();
        PlayerQuestDataImpl.get(uuid).ifPresent(data->{
            quest.finish(player,objectiveID);
            onlinePlayers.remove(player);
            if(data.getTrackingObjective().filter(objective -> objective.equals(this)).isPresent()){
                data.endTracking();
            }

        });
        if(endProcess != null){
            endProcess.run(player);
        }
    }


    public void cachePlayer(Player player,String args){
        onlinePlayers.add(player);
    }





    public String getObjectiveID() {
        return objectiveID;
    }


    public Quest getParent() {
        return quest;
    }




    protected boolean valid(Player player){
        for (Condition condition : conditions) {
            if(!condition.test(player)){
                return false;
            }
        }
        return true;
    }

    public boolean playerIsDoing(Player player){
        return onlinePlayers.contains(player);
    }

    public Collection<Player> getOnlinePlayers(){
        return onlinePlayers;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        onlinePlayers.remove(event.getPlayer());
    }


    public String getInstruction(Player player){
        if(instruction != null){
            return PlaceholderAPI.setPlaceholders(player,instruction);
        }
        return "instruction not set";
    }

    public QuestObjective setInstruction(String instruction){
        this.instruction = instruction;
        return this;
    }


    public QuestObjective setLocation(Location location){
        this.location = location;
        return this;
    }

    public QuestObjective setLocation(String world,double x,double y,double z){
        return setLocation(new Location(Bukkit.getWorld(world),x,y,z));
    }

    public Location getLocation() {
        return location;
    }

    public interface Process{
        void run(Player player);
    }

    public QuestObjective setEndProcess(Process endProcess) {
        this.endProcess = endProcess;
        return this;
    }

    public Process getEndProcess() {
        return endProcess;
    }

    public void unregister(){
        HandlerList.unregisterAll(this);
        onlinePlayers.clear();
    }

}
