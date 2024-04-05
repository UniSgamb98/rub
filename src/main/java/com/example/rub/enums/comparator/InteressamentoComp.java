package com.example.rub.enums.comparator;

import com.example.rub.enums.Interessamento;

import java.util.Comparator;

public class InteressamentoComp implements Comparator<Interessamento> {
    @Override
    public int compare(Interessamento i1, Interessamento i2){
        int ret = 0;
        if (!(i1 == null || i2 == null)) {
            switch (i1) {
                case BLANK:
                    if (i2 != Interessamento.BLANK) {
                        ret = 1;
                    }
                    break;
                case NON_TROVATO:
                    if (i2 == Interessamento.BLANK) {
                        ret = 1;
                    } else {
                        ret = -1;
                    }
                    break;
                case NON_INERENTE:
                    if (i2 == Interessamento.BLANK || i2 == Interessamento.NON_TROVATO) {
                        ret = 1;
                    } else {
                        ret = -1;
                    }
                    break;
                case NULLO:
                    if (i2 == Interessamento.BLANK || i2 == Interessamento.NON_TROVATO || i2 == Interessamento.NON_INERENTE) {
                        ret = 1;
                    } else {
                        ret = -1;
                    }
                    break;
                case RICHIAMARE:
                    if (i2 == Interessamento.BLANK || i2 == Interessamento.NON_TROVATO || i2 == Interessamento.NON_INERENTE || i2 == Interessamento.NULLO) {
                        ret = 1;
                    } else {
                        ret = -1;
                    }
                    break;
                case INFO:
                    if (i2 == Interessamento.CLIENTE || i2 == Interessamento.CAMPIONE || i2 == Interessamento.LISTINO) {
                        ret = -1;
                    } else {
                        ret = 1;
                    }
                    break;
                case LISTINO:
                    if (i2 == Interessamento.CLIENTE || i2 == Interessamento.CAMPIONE) {
                        ret = -1;
                    } else {
                        ret = 1;
                    }
                    break;
                case CAMPIONE:
                    if (i2 == Interessamento.CLIENTE) {
                        ret = -1;
                    } else {
                        ret = 1;
                    }
                    break;
                case CLIENTE:
                    if (i2 != Interessamento.CLIENTE) {
                        ret = -1;
                    }
                    break;
            }
        }
        return ret;
    }
}
