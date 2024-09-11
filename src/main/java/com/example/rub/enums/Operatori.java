package com.example.rub.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Operatori {
    SANTOLO,
    HUGO,
    TERESA,
    VICTORIA,
    GAETANO,
    TOMMASO,
    BLANK;

    public static List<Operatori> getOperators(){
        ArrayList<Operatori> operators = new ArrayList<>(Arrays.asList(Operatori.values()));
        operators.remove(Operatori.BLANK);
        return operators;
    }

    public static String[] getOperatorNames(){
        String[] operators = new String[Operatori.values().length - 1];
        for(int i = 0; i < operators.length; i++){
            operators[i] = Operatori.values()[i].toString();
        }

        return operators;
    }
}

