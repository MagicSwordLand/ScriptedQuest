package net.brian.scriptedquests.commands;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor {

    private final ScriptedQuests plugin;
    private final List<SubCommand> subCommands = new ArrayList<>();

    public CommandManager(ScriptedQuests plugin){
        this.plugin = plugin;
        plugin.getCommand("sQuest").setExecutor(this);
        subCommands.add(new ListObjectives(plugin));
        subCommands.add(new TrackCommand(plugin));
        subCommands.add(new SkipCommand(plugin));
        subCommands.add(new ResetCommand(plugin));
        subCommands.add(new StartCommand(plugin));
    }



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length >= 1){
            SubCommand subCommand = getCommand(args[0]);
            if(subCommand != null ){
                if(subCommand.needAdmin()){
                    if(sender.hasPermission("sQuest.admin")){
                        subCommand.onCommand(sender,args);
                    }
                }
                else subCommand.onCommand(sender,args);
                return true;
            }
        }
        sender.sendMessage("找不到該指令");
        return true;
    }

    private SubCommand getCommand(String name){
        for (SubCommand subCommand : subCommands) {
            if(subCommand.getName().equalsIgnoreCase(name)){
                return subCommand;
            }
        }
        return null;
    }

}
