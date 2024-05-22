package com.example.rub.enums.comparator;

import com.example.rub.functionalities.DBManager;

import java.util.Comparator;
import java.util.UUID;

public class EntryComp implements Comparator<UUID> {
    @Override
    public int compare(UUID o1, UUID o2) {
        return  (DBManager.retriveEntry(o1).getRagioneSociale().compareTo(DBManager.retriveEntry(o2).getRagioneSociale()));
    }
}
