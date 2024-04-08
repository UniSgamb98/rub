package com.example.rub;

import com.example.rub.enums.Interessamento;
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

public class ModifyNoteController implements Initializable {
    @FXML
    public TextField durata;
    @FXML
    public ChoiceBox<String> feedback;
    @FXML
    public TextArea note;
    String fileName;
    Element element;

    public void setDocument(String path, Element element){
        this.fileName = path;
        this.element = element;
        note.setText(element.getTextContent());
        durata.setText(element.getAttribute("durata"));
        System.out.println(element.getNodeName());
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
        feedback.getItems().addAll(Interessamento.NON_TROVATO.name(), Interessamento.NON_INERENTE.name(), Interessamento.NULLO.name(), Interessamento.RICHIAMARE.name(), Interessamento.INFO.name(), Interessamento.LISTINO.name(), Interessamento.CAMPIONE.name(), Interessamento.CLIENTE.name());
    }
}
