package com.example.rub.functionalities.locations;

import java.util.ArrayList;

public class Region extends Locality {
    public Region(String region){
        super(region);
        subLocalities = new ArrayList<>();
    }
    public void addCity(City city){
        for (Locality i : subLocalities){
            if (!i.getLocalityName().equals(city.getLocalityName())){
                subLocalities.add(city);
            }
        }
    }
    @Override
    public String toString(){
        return getLocalityName();
    }
}
