package net.brian.scriptedquests.rewards;

public class RandomAmount {

    double lowBound,upperBound;

    public RandomAmount(double lowBound,double upperBound){
        this.lowBound = Math.min(lowBound,upperBound);
        this.upperBound = Math.max(lowBound,upperBound);
    }

    public int getInt(){
        return (int) (lowBound+Math.random()*(upperBound-lowBound));
    }

    public double getDouble(){
        return lowBound+Math.random()*(upperBound-lowBound);
    }

    public float getFloat(){
        return (float) (lowBound+Math.random() * (upperBound-lowBound));
    }

}
