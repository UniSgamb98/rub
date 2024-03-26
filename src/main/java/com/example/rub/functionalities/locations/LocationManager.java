package com.example.rub.functionalities.locations;

import java.util.ArrayList;
import java.util.Arrays;

public class LocationManager extends Locality {
    public LocationManager(){
        super("manager");
        subLocalities = new ArrayList<>();
    }
    public void addState(State state){
        subLocalities.add(state);
    }
    @Override
    public String toString(){
        return getLocalityName();
    }
}
