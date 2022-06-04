package net.brian.scriptedquests.rewards.amounts;

public class RandomAmount extends Number{

    double lowBound,upperBound;

    public RandomAmount(double lowBound,double upperBound){
        this.lowBound = Math.min(lowBound,upperBound);
        this.upperBound = Math.max(lowBound,upperBound);
    }

    public RandomAmount(int lowBound,int upperBound){
        this.lowBound = lowBound;
        this.upperBound = upperBound;
    }



    @Override
    public int intValue() {
        return (int) (lowBound+Math.random()*(upperBound-lowBound));
    }

    @Override
    public long longValue() {
        return (long) (lowBound+Math.random()*(upperBound-lowBound));
    }

    @Override
    public float floatValue() {
        return (float) (lowBound+Math.random()*(upperBound-lowBound));
    }

    @Override
    public double doubleValue() {
        return (lowBound+Math.random()*(upperBound-lowBound));
    }
}
