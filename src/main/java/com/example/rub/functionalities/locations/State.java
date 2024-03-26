package com.example.rub.functionalities.locations;

import java.util.ArrayList;

public class State extends Locality {
    public State(String state){
        super(state);
        subLocalities = new ArrayList<>();
    }
    public void addRegion(Region region){
        subLocalities.add(region);
    }
    @Override
    public String toString(){
        return getLocalityName();
    }
}
