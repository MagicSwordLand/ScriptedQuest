package net.brian.scriptedquests.rewards;

import me.clip.placeholderapi.PlaceholderAPI;
import net.brian.playerdatasync.util.IridiumColorAPI;
import org.bukkit.entity.Player;

public class MessageReward extends Reward{

    String message;

    public MessageReward(String message) {
        super(0);
        this.message = message;
    }

    @Override
    public void give(Player player, Number number) {
    }

    @Override
    protected String getMessage(Number amount) {
        return IridiumColorAPI.process(message);
    }
}
