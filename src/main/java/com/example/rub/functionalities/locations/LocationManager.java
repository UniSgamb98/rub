package com.example.rub.functionalities.locations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class LocationManager extends Locality implements Serializable {
    public LocationManager(String manager){
        super(manager);
        subLocalities = new ArrayList<>();
    }

    public ArrayList<Locality> getStates(){
        return subLocalities;
    }
    public void addState(State state){
        subLocalities.add(state);
    }
    public void addAllStates(State... states){
        subLocalities.addAll(Arrays.asList(states));
    }
    public void removeState(State state){
        subLocalities.remove(state);
    }

   /* public void addState(State state){
        states.add(state);
        states.sort(new StateComp());
    }

    public State getState(String state){
        boolean found = false;
        State ret = null;
        int i = 0;
        int j = states.size();
        while (!found){
            if(state.compareTo(states.get((i + j)/2).getLocalityName()) < 0){
                j = (i + j)/2;
            } else if (state.compareTo(states.get((i + j)/2).getLocalityName()) > 0) {
                i = (i + j)/2;
            } else if (i == j) {
                found = true;
            } else {
                found = true;
                ret = states.get(i);
            }
        }
        return ret;
    }*/

    @Override
    public String toString(){
        return getLocalityName();
    }
}


