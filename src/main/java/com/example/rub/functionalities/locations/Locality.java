package com.example.rub.functionalities.locations;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Locality implements Serializable {    //TODO: USARE DI PIù LA EREDITARIETA ES. ADDCITY
    private final String name;
    protected ArrayList<Locality> subLocalities;

    public Locality(String name) {
        this.name = name;
    }
    public Locality getSubLocality(String subLocality){
        Locality ret = null;
        for (Locality i : subLocalities){
            if (i.getLocalityName().equals(subLocality)){
                ret = i;
            }
        }
        return ret;
    }
    public ArrayList<Locality> getSubLocalities () {
        return subLocalities;
    }
    public String getLocalityName(){
        return name;
    }
    public boolean contains(String locality){
        boolean ret = false;
        for (Locality i : subLocalities){
            if (i.getLocalityName().equals(locality)) {
                ret = true;
                break;
            }
        }
        return ret;
    }
}
