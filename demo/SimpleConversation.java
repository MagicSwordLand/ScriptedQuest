package net.brian.scriptedquests.utils;

import net.brian.scriptedquests.conversation.NPCQuestion;
import net.brian.scriptedquests.conversation.PlayerOption;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

public class SimpleConversation {


    public static void sendPlayer(Player reciever){
        int npcID = 5;
        new NPCQuestion("你今天過得好嗎").addPlayerOptions(
                new PlayerOption("還不錯")
                        .setNpcResponse(new NPCQuestion("那要不要來點可樂").addPlayerOptions(
                                new PlayerOption("好啊")
                                        .setResult((player, npcID1) -> player.sendMessage("獲得可樂")),
                                new PlayerOption("不要")
                                        .setNpcResponse("遺憾")
                                        .setResult((player, npcID1) -> player.damage(10000))
                        )),

                new PlayerOption("不太好")
                        .setNpcResponse(new NPCQuestion("那要不要來點巧克力").addPlayerOptions(
                                new PlayerOption("好啊")
                                        .setResult((player, npcID1) -> player.sendMessage("獲得巧克力")),
                                new PlayerOption("不要")
                                        .setNpcResponse("竟然有人不喜歡巧克力...")
                        ))
        ).send(5,reciever);
    }

}
