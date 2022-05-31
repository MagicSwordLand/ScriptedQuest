package net.brian.scriptedquests.objectives;

import io.lumine.mythic.lib.api.item.NBTItem;
import net.brian.scriptedquests.api.objectives.data.IntegerData;
import net.brian.scriptedquests.api.quests.Quest;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class GiveItemObjective extends PersistentNPCObjective<IntegerData> {

    private final String itemKey;
    private final int requireAmount;

    public GiveItemObjective(Quest quest, String objectiveID, int npcID,String itemKey,int requireAmount) {
        super(quest, objectiveID,npcID);
        this.itemKey = itemKey;
        this.requireAmount = requireAmount;
    }

    @EventHandler
    public void onClickNPC(NPCRightClickEvent event){
        if(event.getNPC().getId() == npcID && playerIsDoing(event.getClicker())){
            Player player = event.getClicker();

            if(takeItem(player,player.getInventory().getItemInMainHand())){
                event.setCancelled(true);
            }

            if(playerIsDoing(player) && takeItem(player,player.getInventory().getItemInOffHand())){
                event.setCancelled(true);
            }

        }
    }

    private boolean takeItem(Player player,ItemStack itemStack){
        if(!getKey(itemStack).equals(itemKey)) return false;
        getData(player.getUniqueId()).ifPresent(data->{
            if(itemStack.getAmount() >= requireAmount){
                itemStack.setAmount(itemStack.getAmount()-requireAmount);
                finish(player);
            }
            else{
                data.add(itemStack.getAmount());
                itemStack.setAmount(0);
            }
        });
        return true;
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


    @Override
    public Class<IntegerData> getDataClass() {
        return IntegerData.class;
    }

    @Override
    public IntegerData newObjectiveData() {
        return new IntegerData();
    }
}
