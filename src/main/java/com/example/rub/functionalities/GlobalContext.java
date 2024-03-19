package com.example.rub.functionalities;

import com.example.rub.enums.Operatori;

import java.util.ArrayList;
import java.util.UUID;

public abstract class GlobalContext {
    public static Operatori operator;
    public static ArrayList<UUID> openedEntries = new ArrayList<>();
}
