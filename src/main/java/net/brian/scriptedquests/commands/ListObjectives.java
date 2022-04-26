package net.brian.scriptedquests.commands;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.QuestManager;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import net.brian.scriptedquests.quests.Quest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ListObjectives extends SubCommand{
    QuestManager questManager;
    public ListObjectives(ScriptedQuests plugin) {
        super(plugin,"list");
        questManager = plugin.getQuestManager();
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender instanceof Player player){
            PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(data->{
                data.getOnGoingQuestDatas().forEach((key,map)->{
                    questManager.getQuest(key).ifPresent(quest -> {
                        for (String objID : map.keySet()) {
                            player.sendMessage(quest.getObjective(objID).getInstruction(player));
                        }
                    });
                });
            });
        }
    }

    @Override
    public boolean needAdmin() {
        return false;
    }

}
