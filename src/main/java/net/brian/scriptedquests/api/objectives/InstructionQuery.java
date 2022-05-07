package net.brian.scriptedquests.api.objectives;

import net.brian.scriptedquests.api.objectives.data.ObjectiveData;
import org.bukkit.entity.Player;

public interface InstructionQuery<T extends ObjectiveData> {

    String get(T data);

}
