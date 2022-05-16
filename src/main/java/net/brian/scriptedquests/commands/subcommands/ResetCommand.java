package net.brian.scriptedquests.commands.subcommands;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.quests.QuestManager;
import net.brian.scriptedquests.commands.SubCommand;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetCommand extends SubCommand {

    QuestManager questManager;
    public ResetCommand(ScriptedQuests plugin) {
        super(plugin, "reset");
        questManager = plugin.getQuestManager();
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        // /squest reset player <questID>
        if(sender.hasPermission("squest.admin") && args.length >= 3){
            Player player = Bukkit.getPlayer(args[1]);
            if(player != null){
                PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(data->{
                    if(args[2].equals("all_all")){
                        data.clearAll();
                        return;
                    }
                    String objID= data.getOnGoingQuests().get(args[2]);
                    if(objID != null){
                        questManager.getQuest(args[2]).flatMap(quest -> quest.getObjective(objID))
                                .ifPresent(objective -> objective.cancel(player));
                    }
                    data.removeQuestData(args[2]);
                });
            }
        }

    }

    @Override
    public boolean needAdmin() {
        return false;
    }
}