package net.brian.scriptedquests.objectives;

import net.brian.scriptedquests.api.objectives.QuestObjective;
import net.brian.scriptedquests.api.quests.Quest;
import net.brian.scriptedquests.logger.QuestLogger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveToObjective extends QuestObjective {

    private Location targetLocation;
    private double radius;

    public MoveToObjective(Quest quest, String objectiveID, Location targetLocation, double radius) {
        super(quest, objectiveID);
        this.targetLocation = targetLocation;
        this.radius = radius;
        setLocation(targetLocation);
    }

    public MoveToObjective(Quest quest, String objectiveID, Location targetLocation) {
        this(quest, objectiveID,targetLocation,5);
    }

    public MoveToObjective(Quest quest, String objectiveID,String world,double x,double y,double z,double radius) {
        super(quest, objectiveID);
        World bukkitWorld = Bukkit.getWorld(world);
        if(bukkitWorld == null){
            QuestLogger.warn("Can't find world:"+world+" in quest:"+quest.getQuestID()+" obj:"+objectiveID);
            return;
        }
        targetLocation = new Location(bukkitWorld,x,y,z);
        this.radius = radius;
        setLocation(targetLocation);
    }

    @EventHandler
    public void onWalk(PlayerMoveEvent event){
        if(playerIsDoing(event.getPlayer())){
            Location loc = event.getPlayer().getLocation();
            if(loc.getWorld().equals(targetLocation.getWorld())){
                if(loc.distance(targetLocation) < radius){
                    finish(event.getPlayer());
                }
            }
        }
    }

}
