package net.brian.scriptedquests.commands;

import net.brian.scriptedquests.ScriptedQuests;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    protected final String name;
    protected final ScriptedQuests plugin;

    public SubCommand(ScriptedQuests plugin, String name){
        this.name = name;
        this.plugin = plugin;
    }

    public abstract void onCommand(CommandSender sender,String[] args);

    public abstract boolean needAdmin();

    public String getName() {
        return name;
    }

}
