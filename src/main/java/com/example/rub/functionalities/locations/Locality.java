package com.example.rub.functionalities.locations;

import java.util.ArrayList;

public abstract class Locality {
    private final String name;
    protected ArrayList<Locality> subLocalities;

    public Locality(String name) {
        this.name = name;
    }
    public String getLocalityName(){
        return name;
    }
}
