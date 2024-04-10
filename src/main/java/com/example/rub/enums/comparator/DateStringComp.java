package com.example.rub.enums.comparator;

import javafx.util.Pair;

import java.util.Comparator;
import java.util.UUID;

public class DateStringComp implements Comparator<Pair<UUID, String>> {

    @Override
    public int compare(Pair<UUID, String> o1, Pair<UUID, String> o2) {
        return o1.getValue().compareTo(o2.getValue());
    }
}
