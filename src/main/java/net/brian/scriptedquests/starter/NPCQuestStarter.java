package net.brian.scriptedquests.starter;

import net.brian.scriptedquests.ScriptedQuests;
import net.brian.scriptedquests.api.conditions.Condition;
import net.brian.scriptedquests.quests.Quest;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.data.type.Scaffolding;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.awt.desktop.QuitResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NPCQuestStarter implements Listener {



    private static final HashMap<Integer, List<QuestStarter>> quests = new HashMap<>();


    public NPCQuestStarter() {
        Bukkit.getServer().getPluginManager().registerEvents(this, ScriptedQuests.getInstance());
    }


    @EventHandler
    public void onClick(NPCRightClickEvent event){
        List<QuestStarter> list = quests.get(event.getNPC().getId());
        Player player = event.getClicker();
        if(list != null){
            list.forEach(starter -> {
                for (Condition condition : starter.conditions) {
                    if(!condition.test(player)){
                        return;
                    }
                }
                starter.quest.startQuest(player);
            });
        }
    }

    public static void register(int npcID,Quest quest,Condition... conditions){
        QuestStarter starter = new QuestStarter(quest,conditions);
        List<QuestStarter> list = quests.get(npcID);
        if (list != null) {
            list.add(starter);
        }
        else{
            list = new ArrayList<>();
            quests.put(npcID,list);
            list.add(starter);
        }
    }




}
