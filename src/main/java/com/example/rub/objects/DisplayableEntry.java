package com.example.rub.objects;

import com.example.rub.beans.Contatto;
import com.example.rub.functionalities.DBManager;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.UUID;

public class DisplayableEntry extends HBox {
    private final Contatto entry;

    public DisplayableEntry(UUID entryRequested){
        entry = DBManager.retriveEntry(entryRequested);
        Color backgroundPaint = null;
        switch ((int) entry.getCoinvolgimento()){
            case 3:
                backgroundPaint = Color.YELLOW;
                break;
            case 4:
                backgroundPaint = Color.AQUA;
                break;
            case 5:
                backgroundPaint = Color.GREEN;
                break;

        }
        this.setBackground(new Background(new BackgroundFill(backgroundPaint, CornerRadii.EMPTY, Insets.EMPTY)));
        this.getChildren().addAll(new Label(entry.getRagioneSociale()), new Label(" in "));
        this.getChildren().add(new Label(entry.getPaese()));
    }
    public Contatto getEntry(){
        return DBManager.retriveEntry(entry.getId());
    }
    @Override
    public String toString(){
        return entry.getRagioneSociale();
    }

}
