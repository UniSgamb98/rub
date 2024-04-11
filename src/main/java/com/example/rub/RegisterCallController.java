package com.example.rub;

import com.example.rub.beans.Contatto;
import com.example.rub.enums.Interessamento;
import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.NoteManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.w3c.dom.Document;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterCallController implements Initializable {
    private final ObjectProperty<Contatto> entryProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<EntryDetailsPageController> controllerProperty = new SimpleObjectProperty<>();
    @FXML
    public ChoiceBox<String> feedback;
    @FXML
    public DatePicker prossimaChiamata;
    @FXML
    public Spinner<Integer> durata;
    @FXML
    public TextArea note;
    public CheckBox isMessage;

    public void doCancelRegistration(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    public void doRegisterCall(ActionEvent event) {
        Contatto bean = entryProperty.get();
        try {
            NoteManager nm = new NoteManager();
            Document doc;
            try {   //RECUPERO xml NOTE SE INESISTENTE CREAZIONE DI UNO NUOVO
                doc = nm.readXml("" + bean.getNoteId());
            }catch (Exception e) {
                System.out.println("Creazione nuovo documento di nota");
                doc = nm.createDocument(bean.getRagioneSociale());
            }
            nm.addCallNote(doc, note.getText(), durata.getValue(), isMessage.isSelected());
            Interessamento fedback = null;
            try {
                fedback = Interessamento.valueOf(feedback.getValue());
            } catch (Exception ignored) {}
            DBManager.setNextCall(bean.getId(), prossimaChiamata.getValue(), fedback);
            nm.writeXml(doc, ""+bean.getNoteId());
        } catch (Exception e){
            System.out.println("Errore durante la scrittura del file Xml delle note");
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        controllerProperty.get().refresh();
    }
    public void setEntryProperty(Contatto entry){
        entryProperty.set(entry);
    }
    public void setControllerProperty(EntryDetailsPageController controller) {
        controllerProperty.set(controller);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        feedback.getItems().addAll(Interessamento.NON_TROVATO.name(), Interessamento.NON_INERENTE.name(), Interessamento.NULLO.name(), Interessamento.RICHIAMARE.name(), Interessamento.INFO.name(), Interessamento.LISTINO.name(), Interessamento.CAMPIONE.name(), Interessamento.CLIENTE.name());
        durata.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,300));
    }
}
