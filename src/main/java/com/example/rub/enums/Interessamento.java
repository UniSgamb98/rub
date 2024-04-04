package com.example.rub.enums;

import java.util.LinkedList;

public enum Interessamento {
    BLANK,
    NON_TROVATO,
    NON_INERENTE,
    NULLO,
    RICHIAMARE,
    INFO,
    LISTINO,
    CAMPIONE,
    CLIENTE;

    public LinkedList<String> getAllNames(){
        LinkedList<String> ret = new LinkedList<>();
        for (Interessamento i : Interessamento.values()){
            ret.add(i.name());
        }
        return ret;
    }
}
