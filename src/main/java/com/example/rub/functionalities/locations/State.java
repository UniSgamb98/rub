package com.example.rub.functionalities.locations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class State extends Locality implements Serializable {
    private final ArrayList<Locality> regions;
    private String state;

    public State(String state){
        regions = new ArrayList<>();
        this.state = state;
    }

    public String getState(){
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<Locality> getRegions() {
        return new ArrayList<>(regions);
    }













    public void addRegion(Region region){
        regions.add(region);
    }
    public void addAllRegion(Region... region){
        regions.addAll(Arrays.asList(region));
    }
    public void removeRegion(Region region){
        regions.remove(region);
    }
}
