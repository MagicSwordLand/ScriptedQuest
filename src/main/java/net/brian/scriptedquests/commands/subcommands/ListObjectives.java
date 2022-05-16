package net.brian.scriptedquests.commands.subcommands;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.commands.SubCommand;
import net.brian.scriptedquests.data.PlayerQuestDataImpl;
import net.brian.scriptedquests.api.quests.QuestManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import javax.swing.*;
import java.util.HashMap;


public class ListObjectives extends SubCommand implements Listener {

    HashMap<Player,String> pendingConfirm;

    QuestManager questManager;
    public ListObjectives(ScriptedQuests plugin) {
        super(plugin,"list");
        questManager = plugin.getQuestManager();
        pendingConfirm = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        if(sender instanceof Player player){
            player.sendMessage("當前任務目標");
            PlayerQuestDataImpl.get(player.getUniqueId()).ifPresent(data->{
                pendingConfirm.remove(player);
                data.getOnGoingQuests().forEach((questID,objID)->{
                   questManager.getQuest(questID).flatMap(quest -> quest.getObjective(objID)).ifPresent(objective -> {
                       String instruction =objective.getInstruction(player);
                       TextComponent deleteComp = Component.text(" §c[✘]")
                               .hoverEvent(HoverEvent.showText(Component.text("點擊兩次放棄該任務")))
                               .clickEvent(ClickEvent.runCommand("/cancelquest "+questID));
                       TextComponent trackComp = Component.text("["+objective.getParent().getDisplay()+"] "+instruction);
                       trackComp = trackComp.hoverEvent(HoverEvent.showText(Component.text("點擊開始追蹤任務")));
                       trackComp = trackComp.clickEvent(ClickEvent.runCommand("/squest tracking track "+objective.getParent().getQuestID()));
                       trackComp = trackComp.append(deleteComp);
                       player.sendMessage(trackComp);
                   });
                });
            });
            player.sendMessage("");
        }
    }

    @Override
    public boolean needAdmin() {
        return false;
    }


    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        String[] args = event.getMessage().split("cancelquest ");
        if(args.length == 2){
            event.setCancelled(true);
            String pending = pendingConfirm.get(event.getPlayer());
            if(pending != null){
                if(pending.equalsIgnoreCase(args[1])){
                    pendingConfirm.remove(event.getPlayer());
                    plugin.getQuestManager().getQuest(args[1]).ifPresent(quest -> {
                        if(quest.isCancelAble()){
                            quest.cancel(event.getPlayer());
                            event.getPlayer().sendMessage("§e任務"+quest.getDisplay()+"已取消");
                        }
                        else event.getPlayer().sendMessage("§c此任務無法取消");
                    });
                }
            }
            if(pending == null){
                event.getPlayer().sendMessage("§e你確定要放棄任務嗎？再次點擊放棄該讓務");
                pendingConfirm.put(event.getPlayer(),args[1]);
            }
        }
    }

}
