package net.brian.scriptedquests.api.conditions;

import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotFinishCondition implements Condition{

    private final List<String> questIDs;

    public NotFinishCondition(String... questIDs){
        this.questIDs = new ArrayList<>();
        this.questIDs.addAll(Arrays.stream(questIDs).toList());

    }

    @Override
    public boolean test(Player player) {
        return PlayerQuestDataImpl.get(player.getUniqueId())
                .map(data->{
                    for (String questID : questIDs) {
                        if(data.hasFinished(questID)){
                            return false;
                        }
                    }
                    return true;
                })
                .orElse(false);
    }
}
