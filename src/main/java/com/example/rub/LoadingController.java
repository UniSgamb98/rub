package com.example.rub;

import com.example.rub.beans.Contatto;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LoadingController {
    private final ObjectProperty<Integer> entrySaved;
    @FXML
    public Label savedContacts;
    @FXML
    public VBox vbox;

    public LoadingController(){
        entrySaved = new SimpleObjectProperty<>();
        entrySaved.set(0);
        entrySaved.addListener((observable, oldValue, newValue) -> savedContacts.setText(newValue+""));
    }
    public void increment(){
        entrySaved.set(entrySaved.get()+1);
    }
    public void addFailure(Contatto entry){
        vbox.getChildren().add(new Text(entry.getRagioneSociale()));
    }
}