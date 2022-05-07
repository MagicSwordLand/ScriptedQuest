package net.brian.scriptedquests.commands.subcommands;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand extends SubCommand {

    public StartCommand(ScriptedQuests plugin) {
        super(plugin, "start");
    }

    // /squest start player questID

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission("squest.admin")){
            if(args.length >= 3){
                Player player = Bukkit.getPlayer(args[1]);
                if(player != null){
                    plugin.getQuestManager().getQuest(args[2]).ifPresent(quest -> {
                        quest.startQuest(player);
                    });
                }
            }
        }
    }

    @Override
    public boolean needAdmin() {
        return false;
    }
}
