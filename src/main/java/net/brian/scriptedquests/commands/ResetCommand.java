package net.brian.scriptedquests.commands;

import net.brian.scriptedquests.ScriptedQuests;
import org.bukkit.command.CommandSender;

public class ResetCommand extends SubCommand{


    public ResetCommand(ScriptedQuests plugin) {
        super(plugin, "reset");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {

    }

    @Override
    public boolean needAdmin() {
        return false;
    }
}
