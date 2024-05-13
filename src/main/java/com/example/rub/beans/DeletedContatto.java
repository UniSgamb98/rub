package com.example.rub.beans;

import com.example.rub.functionalities.GlobalContext;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class DeletedContatto implements Serializable {
    private final Contatto contatto;
    private final String fromWho;
    private final Date when;
    public DeletedContatto(Contatto contatto){
        this.contatto = contatto;
        Calendar now = Calendar.getInstance();
        when = now.getTime();
        fromWho = GlobalContext.operator.name();
    }
    public Contatto getContatto() {
        return contatto;
    }
    public Date getWhen() {
        return when;
    }
    public String getFromWho() {
        return fromWho;
    }
    public UUID getNoteID(){
        return contatto.getNoteId();
    }
}
