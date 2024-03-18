package com.example.rub.objects;

import com.example.rub.beans.Contatto;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.UUID;

public class DisplayableEntry extends HBox {
    private final Contatto entry;

    public DisplayableEntry(Contatto entryRequested){
        entry = entryRequested;
        this.getChildren().add(new Label(entry.getRagioneSociale()));
        this.getChildren().add(new Label(entry.getPaese()));
        this.getChildren().add(new Label(entry.getCitta()));
    }
    public UUID getUuid(){
        return entry.getId();
    }

}
