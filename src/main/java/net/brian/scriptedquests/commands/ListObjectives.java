package net.brian.scriptedquests.commands;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import net.brian.scriptedquests.api.QuestManager;
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
            player.sendMessage("當前任務目標");
            PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(data->{
                data.getOnGoingQuests().forEach((questID,objID)->{
                   questManager.getQuest(questID).flatMap(quest -> quest.getObjective(objID))
                           .ifPresent(objective -> sender.sendMessage(objective.getInstruction(player)));
                });
            });
            player.sendMessage("");
        }
    }

    @Override
    public boolean needAdmin() {
        return false;
    }

}
