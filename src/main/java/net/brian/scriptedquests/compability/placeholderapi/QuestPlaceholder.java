package net.brian.scriptedquests.compability.placeholderapi;

import me.clip.placeholderapi.PlaceholderHook;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.brian.scriptedquests.api.objectives.QuestObjective;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class QuestPlaceholder extends PlaceholderExpansion {

    public QuestPlaceholder(){

    }

    @Override
    public @NotNull String getIdentifier() {
        return "squests";
    }

    @Override
    public @NotNull String getAuthor() {
        return "SleepAllDay";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    /*
    %squests_track%
     */
    public String onRequest(OfflinePlayer offPlayer, @NotNull String args){
        Player player = offPlayer.getPlayer();
        if(player  == null) return "";
        return PlayerQuestDataImpl.get(player.getUniqueId())
                .map(data->{
                    if(args.equals("track")){
                        Optional<QuestObjective> objective = data.getTrackingObjective();
                        if(objective.isPresent()){
                            return objective.get().getInstruction(player);
                        }
                    }
                    return "";
                }).orElse("");
    }

}
