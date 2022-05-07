package net.brian.scriptedquests.api.objectives.data;

public class IntegerData extends ObjectiveData{

    private int amount = 0;

    public IntegerData(){
    }

    public int getAmount() {
        return amount;
    }

    public void add(int amount){
        this.amount += amount;
    }

    public void add(){
        amount++;
    }

}
