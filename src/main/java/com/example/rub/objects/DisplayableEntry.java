package com.example.rub.objects;

import com.example.rub.beans.Contatto;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class DisplayableEntry extends HBox {
    private final Contatto entry;

    public DisplayableEntry(Contatto entryRequested){
        entry = entryRequested;
        this.getChildren().addAll(new Label(entry.getRagioneSociale()), new Label(" in "));
        this.getChildren().add(new Label(entry.getPaese()));
    }
    public Contatto getEntry(){
        return entry;
    }
    @Override
    public String toString(){
        return entry.getRagioneSociale();
    }

}
