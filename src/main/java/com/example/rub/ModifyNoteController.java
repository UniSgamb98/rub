package com.example.rub;

import com.example.rub.enums.Interessamento.InteressamentoStatus;
import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.NoteManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class ModifyNoteController implements Initializable {
    @FXML
    public TextField durata;
    @FXML
    public ChoiceBox<String> feedback;
    @FXML
    public TextArea note;
    String fileName;
    Element element;
    UUID entryID;

    public void setDocument(String path, Element element, UUID entryID){
        this.fileName = path;
        this.element = element;
        this.entryID = entryID;
        note.setText(element.getTextContent());
        durata.setText(element.getAttribute("durata"));
    }

    public void doGoBack(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void doSend(ActionEvent event) {
        try {
            NoteManager nm = new NoteManager();
            Document doc = nm.readXml(fileName);
            nm.modifyNote(doc, element.getAttribute("number"), note.getText(), feedback.getValue(), durata.getText());
            nm.writeXml(doc, fileName);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception ignored) {}
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        feedback.getItems().addAll(InteressamentoStatus.NON_TROVATO.name(), InteressamentoStatus.NON_INERENTE.name(), InteressamentoStatus.NULLO.name(), InteressamentoStatus.RICHIAMARE.name(), InteressamentoStatus.INFO.name(), InteressamentoStatus.LISTINO.name(), InteressamentoStatus.CAMPIONE.name(), InteressamentoStatus.CLIENTE.name());
    }

    public void doDeleteNote(ActionEvent event) {
        try {
            NoteManager nm = new NoteManager();
            Document doc = nm.readXml(fileName);
            if (nm.deleteNote(doc, element.getAttribute("number"))){
                nm.writeXml(doc, fileName);
                DBManager.reduceVolteContattati(entryID);
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception ignored) {}
    }
}
