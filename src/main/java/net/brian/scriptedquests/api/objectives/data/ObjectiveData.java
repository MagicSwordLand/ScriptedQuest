package net.brian.scriptedquests.api.objectives.data;

import com.google.gson.Gson;

public abstract class ObjectiveData {

    private static final Gson gson = new Gson();


    @Override
    public String toString(){
        return gson.toJson(this);
    }

}
