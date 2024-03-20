package com.example.rub.functionalities;


import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;

public class LocationManager implements Serializable {
    private ArrayList<Pair<String, ArrayList<Pair<String, ArrayList<Pair<String, ArrayList<String>>>>>>> location;   //stato->regione->provincia->citta

    public LocationManager(){
        try {
            location =  (ArrayList<Pair<String, ArrayList<Pair<String, ArrayList<Pair<String, ArrayList<String>>>>>>>) MyUtils.read("mappaMondo");
        } catch (Exception e){
            System.out.println("Errore nel recupero delle localita");
        }
    }

    public String getState(){

    }

    public String getRegion(){

    }

    public String getProvince(){

    }

    public String getCity(){

    }

}


