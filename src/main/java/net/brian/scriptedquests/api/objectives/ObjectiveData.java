package net.brian.scriptedquests.api.objectives;

import com.google.gson.Gson;

public class ObjectiveData {

    private static final Gson gson = new Gson();


    @Override
    public String toString(){
        return gson.toJson(this);
    }

}
