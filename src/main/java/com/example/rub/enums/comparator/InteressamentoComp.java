package com.example.rub.enums.comparator;

import com.example.rub.enums.Interessamento.InteressamentoStatus;

import java.util.Comparator;

public class InteressamentoComp implements Comparator<InteressamentoStatus> {
    @Override
    public int compare(InteressamentoStatus i1, InteressamentoStatus i2){
        int ret = 0;
        if (!(i1 == null || i2 == null)) {
            switch (i1) {
                case BLANK:
                    if (i2 != InteressamentoStatus.BLANK) {
                        ret = 1;
                    }
                    break;
                case NON_TROVATO:
                    if (i2 == InteressamentoStatus.BLANK) {
                        ret = 1;
                    } else {
                        ret = -1;
                    }
                    break;
                case NON_INERENTE:
                    if (i2 == InteressamentoStatus.BLANK || i2 == InteressamentoStatus.NON_TROVATO) {
                        ret = 1;
                    } else {
                        ret = -1;
                    }
                    break;
                case NULLO:
                    if (i2 == InteressamentoStatus.BLANK || i2 == InteressamentoStatus.NON_TROVATO || i2 == InteressamentoStatus.NON_INERENTE) {
                        ret = 1;
                    } else {
                        ret = -1;
                    }
                    break;
                case RICHIAMARE:
                    if (i2 == InteressamentoStatus.BLANK || i2 == InteressamentoStatus.NON_TROVATO || i2 == InteressamentoStatus.NON_INERENTE || i2 == InteressamentoStatus.NULLO) {
                        ret = 1;
                    } else {
                        ret = -1;
                    }
                    break;
                case INFO:
                    if (i2 == InteressamentoStatus.CLIENTE || i2 == InteressamentoStatus.CAMPIONE || i2 == InteressamentoStatus.LISTINO) {
                        ret = -1;
                    } else {
                        ret = 1;
                    }
                    break;
                case LISTINO:
                    if (i2 == InteressamentoStatus.CLIENTE || i2 == InteressamentoStatus.CAMPIONE) {
                        ret = -1;
                    } else {
                        ret = 1;
                    }
                    break;
                case CAMPIONE:
                    if (i2 == InteressamentoStatus.CLIENTE) {
                        ret = -1;
                    } else {
                        ret = 1;
                    }
                    break;
                case CLIENTE:
                    if (i2 != InteressamentoStatus.CLIENTE) {
                        ret = 1;
                    }
                    break;
            }
        }
        return ret;
    }
}
