package net.brian.scriptedquests.api.objectives.data;

import java.util.HashMap;

public class MapData extends ObjectiveData{

    private HashMap<String,Integer> map = new HashMap<>();



    public int get(String key){
        return map.getOrDefault(key,0);
    }


    public void add(String key,int amount){
        map.put(key,map.getOrDefault(key,0)+amount);
    }

    public void set(String key,int amount){
        map .put(key,amount);
    }
}
