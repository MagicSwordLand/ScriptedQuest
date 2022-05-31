package net.brian.scriptedquests.objectives;

import io.lumine.mythic.lib.api.item.NBTItem;
import net.brian.scriptedquests.api.objectives.data.MapData;
import net.brian.scriptedquests.api.quests.Quest;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GiveMultiItemObjective extends PersistentNPCObjective<MapData> {

    private Map<String,Integer> requireMap;

    public GiveMultiItemObjective(Quest quest, String objectiveID, int npcID, Map<String,Integer> requireMap) {
        super(quest, objectiveID,npcID);
        this.requireMap = requireMap;
    }

    public GiveMultiItemObjective(Quest quest, String objectiveID, int npcID) {
        this(quest,objectiveID,npcID,new HashMap<>());
    }

    public GiveMultiItemObjective setAmount(String id, int amount){
        requireMap.put(id,amount);
        return this;
    }


    @EventHandler
    public void onClick(NPCRightClickEvent event){
        if(event.getNPC().getId() == npcID){
            Player player = event.getClicker();
            if(playerIsDoing(player) && valid(player)){
                ItemStack mainHand = player.getInventory().getItemInMainHand();
                ItemStack offHand = player.getInventory().getItemInOffHand();
                String mainHandKey = getKey(mainHand);
                String offHandKey = getKey(offHand);
                if(requireMap.containsKey(mainHandKey) || requireMap.containsKey(offHandKey)){
                    event.setCancelled(true);
                }

                takeItem(player, mainHand,mainHandKey);
                if(playerIsDoing(player)){
                    takeItem(player, offHand,offHandKey);
                }
            }
        }
    }

    private void takeItem(Player player, ItemStack itemStack,String key) {

        if(key != null && requireMap.containsKey(key)){
            getData(player.getUniqueId()).ifPresent(data->{
                int dataOldAmount = data.get(key);
                int requireAmount = requireMap.get(key);
                if(dataOldAmount < requireAmount){

                    if(dataOldAmount + itemStack.getAmount() >= requireAmount){
                        itemStack.setAmount(itemStack.getAmount()-(requireAmount-dataOldAmount));
                        data.set(key,requireAmount);
                    }
                    else {
                        data.add(key,itemStack.getAmount());
                        itemStack.setAmount(0);
                    }
                }
                if(checkFinished(data)){
                    finish(player);
                }
            });
        }
    }

    public String getKey(ItemStack itemStack){
        if(itemStack != null){
            NBTItem nbtItem = NBTItem.get(itemStack);
            if(nbtItem.hasType()){
                return nbtItem.getType()+":"+nbtItem.getString("MMOITEMS_ITEM_ID");
            }
        }
        return "";
    }

    private boolean checkFinished(MapData mapData){
        for (Map.Entry<String, Integer> entry : requireMap.entrySet()) {
            if(mapData.get(entry.getKey()) < entry.getValue()){
                return false;
            }
        }
        return true;
    }

    @Override
    public Class<MapData> getDataClass() {
        return MapData.class;
    }

    @Override
    public MapData newObjectiveData() {
        return new MapData();
    }



}
