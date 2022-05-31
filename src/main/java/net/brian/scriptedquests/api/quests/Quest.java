package net.brian.scriptedquests.api.quests;


import net.brian.playerdatasync.PlayerDataSync;
import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import net.brian.scriptedquests.api.objectives.QuestObjective;
import net.brian.scriptedquests.conversation.PlayerOption;
import net.brian.scriptedquests.data.SerializedQuestData;
import net.brian.scriptedquests.rewards.Reward;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class Quest {

    protected final String questID;
    protected boolean cancelAble = true;
    protected String displayName;

    protected final List<Reward> rewards = new ArrayList<>();
    protected final LinkedHashMap<String,QuestObjective> objectives = new LinkedHashMap<>();

    public Quest(String questID){
        this(questID,questID);
    }

    public Quest(String questID,String displayName){
        this.questID = questID;
        this.displayName = displayName;

    }



    public Quest addRewards(Reward... rewards){
        this.rewards.addAll(Arrays.stream(rewards).toList());
        return this;
    }

    public Quest pushObjective(QuestObjective... objectives){
        for (QuestObjective objective : objectives) {
            this.objectives.put(objective.getObjectiveID(),objective);
        }
        cachePlayers();
        return this;
    }

    public void startQuest(Player player){
        PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(data->{
            if(data.getMaxQuestAmount() <= data.getOnGoingQuests().size()) {
                player.sendMessage("你已經到達可接的任務上限，請提高冒險者等級或是放棄部分任務。");
                return;
            }
            if(!data.isDoing(questID)){
                Iterator<Map.Entry<String ,QuestObjective>> it = objectives.entrySet().iterator();
                if(it.hasNext()){
                    onStart(player);
                    it.next().getValue().start(player);
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,2);
                }
                else {
                    onEnd(player);
                    data.addFinishQuest(questID);
                    data.removeQuestData(questID);
                };
            }
        });
    }


    public void finish(Player player,String objectiveID){
        PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(data->{
            Iterator<Map.Entry<String, QuestObjective>> it = objectives.entrySet().iterator();
            while (it.hasNext()){
                Map.Entry<String, QuestObjective> entry = it.next();
                if(entry.getKey().equals(objectiveID)){
                    if(it.hasNext()){
                        it.next().getValue().start(player);
                    }
                    else{
                        onEnd(player);
                        rewards.forEach(reward -> reward.give(player));
                        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE,1,1);
                        data.addFinishQuest(questID);
                        player.sendTitle("","完成任務 ["+displayName+"]");
                        data.removeQuestData(questID);
                    }
                    break;
                }
            }
        });
    }

    public void cancel(Player player){
        PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(data->{
            String objID = data.getOnGoingQuests().get(questID);
            if(objID != null){
                getObjective(objID).ifPresent(objective -> {
                    objective.cancel(player);
                });
            }
            data.removeQuestData(questID);
            onCancel(player);
        });
    }

    public void cancelAll(){
        objectives.forEach((s, objective) -> {
            objective.cancelAll();
        });
    }

    public void onEnd(Player player){

    }

    public void onStart(Player player){

    }

    public void onCancel(Player player){

    }

    public boolean isCancelAble() {
        return cancelAble;
    }

    public Optional<QuestObjective> getObjective(String id){
        return Optional.ofNullable(objectives.get(id));
    }

    public PlayerOption getStartOption(String message, PlayerOption.Result result, boolean canRedo, Condition... conditions){
        return new PlayerOption(message,conditions)
                .addCondition(player -> PlayerQuestDataImpl.get(player.getUniqueId()).map(data->{
                    if(!canRedo && data.hasFinished(questID)) return false;
                    if(data.isDoing(questID)) return false;
                    return true;
                }).orElse(false))
                .setResult((player, npcID) -> {
                    startQuest(player);
                    if(result != null){
                        result.process(player,npcID);
                    }
                });
    }


    public PlayerOption getStartOption(String message,boolean canRedo,Condition... conditions){
        return getStartOption(message,null,canRedo,conditions);
    }


    public String getQuestID() {
        return questID;
    }

    public void unregister(){
        for (QuestObjective objective : objectives.values()) {
            objective.unregister();
        }
    }

    public String getDisplay(){
        return displayName;
    }


    /**
     * 當伺服器運行中 重載Script的時候 會跑一遍
     */
    private void cachePlayers(){
        if(PlayerDataSync.getInstance() == null) return;

        for (PlayerQuestDataImpl data : PlayerDataSync.getInstance().getPlayerDatas().getTable(PlayerQuestDataImpl.class).cacheData.values()) {
            SerializedQuestData questData = data.getQuestData(questID);
            if(questData != null){
                getObjective(questData.getObjectiveID()).ifPresent(objective -> {
                    objective.cachePlayer(Bukkit.getPlayer(data.getUuid()),questData.getObjectiveData());
                });
            }
        }
    }

    /**
     * 當玩家登入的時候 PlayerData onDeserialize 時  會把資料傳進來
     */
    public void cachePlayer(Player player,PlayerQuestDataImpl playerData,SerializedQuestData serializedQuestData){
        QuestObjective questObjective =objectives.get(serializedQuestData.getObjectiveID());
        if(questObjective != null){
            questObjective.cachePlayer(player, serializedQuestData.getObjectiveData());
        }
    }

}
