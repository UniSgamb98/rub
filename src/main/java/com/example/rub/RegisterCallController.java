package com.example.rub;

import com.example.rub.beans.Contatto;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterCallController {
    private final ObjectProperty<Contatto> entryProperty = new SimpleObjectProperty<>();
    private Contatto bean;
    @FXML
    public ChoiceBox<String> feedback;
    @FXML
    public DatePicker prossimaChiamata;
    @FXML
    public TextField durata;
    @FXML
    public TextArea note;

    public void doCancelRegistration(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    public void doRegisterCall(ActionEvent event){
        bean = entryProperty.get();
        bean.incrementVolteContattati();
        //TODO: NoteManager Stuff
    }
    public void setEntryProperty(Contatto entry){
        entryProperty.set(entry);
    }
}
