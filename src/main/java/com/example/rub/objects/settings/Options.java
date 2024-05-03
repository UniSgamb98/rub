package com.example.rub.objects.settings;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Options extends HBox {
    private final CheckBox isManager;
    private final TextField field;

    public Options(String checkboxLabel){
        this.setAlignment(Pos.CENTER);
        this.setSpacing(5);
        Button remove = new Button("Rimuovi");
        isManager = new CheckBox(checkboxLabel);
        field = new TextField();
        remove.setOnAction(event1 -> ((VBox) this.getParent()).getChildren().remove(this));
        this.getChildren().addAll(field, isManager, remove);
    }

    public boolean isSelected(){
        return isManager.isSelected();
    }

    public String getField(){
        return field.getText();
    }
    public void setField(String s){
        field.setText(s);
    }

    public void setIsManager(boolean b){
        isManager.setSelected(b);
    }
}
