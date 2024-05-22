package com.example.rub.functionalities;

import com.example.rub.beans.OpenedEntry;
import com.example.rub.enums.Operatori;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public abstract class GlobalContext {
    public static Operatori operator;
    public static ArrayList<OpenedEntry> openedEntries = new ArrayList<>();
    public static LinkedList<UUID> notProgrammedCalls = new LinkedList<>();

    public static boolean isEntryOpened(UUID id){
        boolean ret = false;
        for (OpenedEntry i : openedEntries){
            if (i.getEntry().equals(id)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    public static void closeOpenedEntry(UUID id){
        openedEntries.removeIf(i -> (i.getEntry().equals(id)) && i.getOperator().equals(operator));
    }
}
