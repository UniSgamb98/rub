package com.example.rub.enums;

import java.util.LinkedList;

public class Interessamento {
    private InteressamentoStatus status;

    public Interessamento(){}
    public Interessamento(InteressamentoStatus interessamentoStatus){this.setStatus(interessamentoStatus);}
    public enum InteressamentoStatus {
        BLANK,
        NON_TROVATO,
        NON_INERENTE,
        NULLO,
        RICHIAMARE,
        INFO,
        LISTINO,
        CAMPIONE,
        CLIENTE
    }

    public String getQuestionForm(){
        String ret = null;
        switch (getStatus()){
            case BLANK:
                ret = "Nessuna novità";
                break;
            case NON_TROVATO:
                ret = "Trovato nessuno che mi ha risposto";
                break;
            case NON_INERENTE:
                ret = "Capito che non ha un utilizzo per il nostro prodotto";
                break;
            case NULLO:
                ret = "Capito che non è interessato al nostro prodotto";
                break;
            case RICHIAMARE:
                ret = "Capito che devo richiamare per successivi riscontri";
                break;
            case INFO:
                ret = "Mandato informazioni sulla nostra azienda";
                break;
            case LISTINO:
                ret = "Mandato il listino/catalogo";
                break;
            case CAMPIONE:
                ret = "Mandato un campione al cliente";
                break;
            case CLIENTE:
                ret = "Ottenuto un ordine del nostro prodotto";
                break;
        }
        return ret;
    }

    public static InteressamentoStatus fromQuestionForm(String s){
        InteressamentoStatus ret = InteressamentoStatus.BLANK;
        if (s != null) {
            switch (s) {
                case "Trovato nessuno che mi ha risposto":
                    ret = InteressamentoStatus.NON_TROVATO;
                    break;

                case "Capito che non ha un utilizzo per il nostro prodotto":
                    ret = InteressamentoStatus.NON_INERENTE;
                    break;

                case "Capito che non è interessato al nostro prodotto":
                    ret = InteressamentoStatus.NULLO;
                    break;

                case "Capito che devo richiamare per successivi riscontri":
                    ret = InteressamentoStatus.RICHIAMARE;
                    break;

                case "Mandato informazioni sulla nostra azienda":
                    ret = InteressamentoStatus.INFO;
                    break;

                case "Mandato il listino/catalogo":
                    ret = InteressamentoStatus.LISTINO;
                    break;

                case "Mandato un campione al cliente":
                    ret = InteressamentoStatus.CAMPIONE;
                    break;

                case "Ottenuto un ordine del nostro prodotto":
                    ret = InteressamentoStatus.CLIENTE;
                    break;
            }
        }
        return ret;
    }

    public InteressamentoStatus getStatus() {
        return status;
    }

    public void setStatus(InteressamentoStatus status) {
        this.status = status;
    }

    public static LinkedList<Interessamento> getSet(){
        LinkedList<Interessamento> ret = new LinkedList<>();
        ret.add(new Interessamento(InteressamentoStatus.BLANK));
        ret.add(new Interessamento(InteressamentoStatus.NON_TROVATO));
        ret.add(new Interessamento(InteressamentoStatus.NON_INERENTE));
        ret.add(new Interessamento(InteressamentoStatus.NULLO));
        ret.add(new Interessamento(InteressamentoStatus.RICHIAMARE));
        ret.add(new Interessamento(InteressamentoStatus.INFO));
        ret.add(new Interessamento(InteressamentoStatus.LISTINO));
        ret.add(new Interessamento(InteressamentoStatus.CAMPIONE));
        ret.add(new Interessamento(InteressamentoStatus.CLIENTE));
        return ret;
    }
    @Override
    public String toString(){
        return getQuestionForm();
    }
}