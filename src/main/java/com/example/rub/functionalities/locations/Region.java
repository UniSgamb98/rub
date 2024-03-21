package com.example.rub.functionalities.locations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Region extends Locality implements Serializable {
    private final ArrayList<Locality> cities;
    private String region;

    public Region(String region){
        cities = new ArrayList<>();
        this.region = region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStringRegion() {
        return region;
    }

    public Region getRegion(String region){
        return this;
    }


    public ArrayList<Locality> getCities() {
        return cities;
    }

    public void addCities(Locality city){
        cities.add(city);
    }
    public void addAllCities(Locality... city){
        cities.addAll(Arrays.asList(city));
    }
    public void removeCities(String city){
        cities.remove(city);
    }

    @Override
    public String toString(){
        return region;
    }
}
