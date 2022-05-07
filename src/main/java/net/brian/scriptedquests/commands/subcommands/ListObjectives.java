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


public class ListObjectives extends SubCommand {
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
                           .ifPresent(objective -> {
                               String instruction =objective.getInstruction(player);
                               TextComponent textComponent = Component.text(instruction);
                               textComponent = textComponent.hoverEvent(HoverEvent.showText(Component.text("點擊開始追蹤任務")));
                               textComponent = textComponent.clickEvent(ClickEvent.runCommand("/squest tracking track "+objective.getParent().getQuestID()));
                               player.sendMessage(textComponent);
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

}
