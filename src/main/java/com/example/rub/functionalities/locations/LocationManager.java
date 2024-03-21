package com.example.rub.functionalities.locations;

import com.example.rub.functionalities.locations.comparators.StateComp;

import java.io.Serializable;
import java.util.ArrayList;

public class LocationManager implements Serializable {
    ArrayList<State> states;

    public LocationManager(){
        states = new ArrayList<>();
    }

    public ArrayList<State> getStates(){
        return states;
    }

    public void addState(State state){
        states.add(state);
        states.sort(new StateComp());
    }

    public State getState(String state){
        boolean found = false;
        State ret = null;
        int i = 0;
        int j = states.size();
        while (!found){
            if(state.compareTo(states.get((i + j)/2).getState()) < 0){
                j = (i + j)/2;
            } else if (state.compareTo(states.get((i + j)/2).getState()) > 0) {
                i = (i + j)/2;
            } else if (i == j) {
                found = true;
            } else {
                found = true;
                ret = states.get(i);
            }
        }
        return ret;
    }




}


