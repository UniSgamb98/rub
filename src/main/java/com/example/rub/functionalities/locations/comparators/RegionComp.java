package com.example.rub.functionalities.locations.comparators;

import com.example.rub.functionalities.locations.Region;

import java.util.Comparator;

public class RegionComp implements Comparator<Region> {

    @Override
    public int compare(Region o1, Region o2) {
        return o1.getStringRegion().compareTo(o2.getStringRegion());
    }
}
