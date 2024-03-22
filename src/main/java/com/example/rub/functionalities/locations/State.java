package com.example.rub.functionalities.locations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class State extends Locality implements Serializable {
    public State(String state){
        super(state);
        subLocalities = new ArrayList<>();
    }

    public ArrayList<Locality> getRegions() {
        return subLocalities;
    }
    public void addRegion(Region region){
        subLocalities.add(region);
    }
    public void addAllRegion(Region... region){
        subLocalities.addAll(Arrays.asList(region));
    }
    public void removeRegion(Region region){
        subLocalities.remove(region);
    }
    @Override
    public String toString(){
        return getLocalityName();
    }
}
