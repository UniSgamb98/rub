package com.example.rub.beans;

import com.example.rub.enums.Operatori;

import java.io.Serializable;
import java.util.UUID;

public class OpenedEntry implements Serializable {
    UUID entry;
    Operatori operator;


    public OpenedEntry(UUID id, Operatori operator){
        entry = id;
        this.operator = operator;
    }
    public Operatori getOperator() {
        return operator;
    }
    public UUID getEntry() {
        return entry;
    }
    public void setOperator(Operatori operator) {
        this.operator = operator;
    }
}
