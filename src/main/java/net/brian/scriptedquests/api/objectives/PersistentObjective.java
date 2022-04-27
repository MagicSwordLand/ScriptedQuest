package net.brian.scriptedquests.api.objectives;

import com.google.gson.Gson;
import net.brian.playerdatasync.events.PlayerDataFetchComplete;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.quests.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public abstract class PersistentObjective<T extends ObjectiveData> extends QuestObjective{

    private static final Gson gson = new Gson();

    private final Map<UUID,T> cachedData = new HashMap<>();

    public PersistentObjective(Quest quest, String objectiveID, Condition... conditions) {
        super(quest, objectiveID,conditions);
    }

    public abstract Class<T> getDataClass();
    public abstract T newObjectiveData();

    @Override
    public void start(Player player){
        T data = newObjectiveData();
        cachedData.put(player.getUniqueId(),data);
        PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(playerData->{
            playerData.setQuestData(quest.getQuestID(), new SerializedQuestData(quest.getQuestID(),data.toString()));
        });
    }

    @Override
    public void cancel(Player player){
        super.cancel(player);
        cachedData.remove(player.getUniqueId());
    }

    @Override
    public void finish(Player player){
        super.finish(player);
        cachedData.remove(player.getUniqueId());
    }

    public void loadPlayer(Player player){
        PlayerQuestDataImpl.get(player.getUniqueId())
                .flatMap(data-> data.getObjectiveData(quest.getQuestID(), objectiveID))
                .ifPresent(dataString-> cachedData.put(player.getUniqueId(),gson.fromJson(dataString,getDataClass())));
    }

    public Optional<T> getData(UUID uuid){
        return Optional.ofNullable(cachedData.get(uuid));
    }

    @EventHandler
    public void onDataLoadComplete(PlayerDataFetchComplete event){
        loadPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event){
        T data = cachedData.get(event.getPlayer().getUniqueId());
        if(data != null){
            PlayerQuestDataImpl.get(event.getPlayer().getUniqueId()).ifPresent(playerData->{
                playerData.setQuestData(quest.getQuestID(), new SerializedQuestData(quest.getQuestID(),data.toString()));
            });
        }
    }

}
