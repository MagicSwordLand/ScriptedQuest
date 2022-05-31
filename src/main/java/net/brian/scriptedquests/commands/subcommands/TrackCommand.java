package net.brian.scriptedquests.commands.subcommands;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.commands.SubCommand;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class TrackCommand extends SubCommand {

    public TrackCommand(ScriptedQuests plugin) {
        super(plugin, "tracking");
    }

    // /squest tracking <track/end> questID

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(args.length<2) return;
        if(sender instanceof Player player){
            String action = args[1];
            PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(data->{
                if(action.equalsIgnoreCase("track")){
                    if(args.length >= 3){
                        String questID = args[2];
                        data.setTrackingQuest(questID);
                    }
                }
                else if(action.equalsIgnoreCase("end")){
                    sender.sendMessage("已停止追蹤任務");
                    data.endTracking();
                }
                else if(action.equalsIgnoreCase("start")){
                    data.startTracking();
                    sender.sendMessage("開始追蹤任務");
                }
            });

        }
    }


    @Override
    public boolean needAdmin() {
        return false;
    }
}
