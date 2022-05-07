package net.brian.scriptedquests.commands.subcommands;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.quests.QuestManager;
import net.brian.scriptedquests.commands.SubCommand;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkipCommand extends SubCommand {

    QuestManager questManager;
    public SkipCommand(ScriptedQuests plugin) {
        super(plugin, "skip");
        questManager = plugin.getQuestManager();
    }

    // /squests skip player quest

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(args.length>=3){
            Player player = Bukkit.getPlayer(args[1]);
            if(player != null){
                PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(data->{
                    String questID = args[2];
                    String objID = data.getOnGoingQuests().get(questID);
                    if(objID != null){
                        questManager.getQuest(questID)
                                .flatMap(quest -> quest.getObjective(objID))
                                .ifPresent(objective -> objective.finish(player));
                    }
                });
            }
        }
    }

    @Override
    public boolean needAdmin() {
        return true;
    }
}
