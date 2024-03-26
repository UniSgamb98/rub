package com.example.rub.functionalities.locations;

import java.util.ArrayList;
import java.util.Arrays;

public class Region extends Locality {
    public Region(String region){
        super(region);
        subLocalities = new ArrayList<>();
    }
    public void addCity(City city){
        subLocalities.add(city);
    }
    @Override
    public String toString(){
        return getLocalityName();
    }
}
