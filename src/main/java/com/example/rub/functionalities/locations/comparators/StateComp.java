package com.example.rub.functionalities.locations.comparators;

import com.example.rub.functionalities.locations.State;

import java.util.Comparator;

public class StateComp implements Comparator<State> {
    @Override
    public int compare(State o1, State o2) {
        return o1.getLocalityName().compareTo(o2.getLocalityName());
    }
}
