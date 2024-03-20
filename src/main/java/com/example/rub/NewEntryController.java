package com.example.rub;

import com.example.rub.beans.Contatto;
import com.example.rub.enums.Interessamento;
import com.example.rub.enums.TipoCliente;
import com.example.rub.functionalities.DBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class NewEntryController implements Initializable {

    @FXML
    private TextField citta;
    @FXML
    private TextField telefono;
    @FXML
    private TextField personaRiferimento;
    @FXML
    private TextField ragioneSociale;
    @FXML
    private TextField paese;
    @FXML
    private TextField email;
    @FXML
    private ChoiceBox<TipoCliente> tipoCliente;
    @FXML
    private ChoiceBox<Interessamento> interesse;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        tipoCliente.getItems().addAll(TipoCliente.LABORATORIO, TipoCliente.RIVENDITORE, TipoCliente.CENTROFRESAGGIO);
        interesse.getItems().addAll(Interessamento.IMMEDIATO, Interessamento.PROSSIMAMENTE, Interessamento.NULLO);
    }

    public void sendNewEntry(ActionEvent event){
        Contatto newEntry = getContatto();
        DBManager.saveEntry(newEntry);
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("firstPage.fxml")));       //cambio scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) { System.out.println("Errore durante la transizione in firstPage con sendNewEntry");   }
    }

    public void abortNewEntry(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("firstPage.fxml")));       //cambio scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) { System.out.println("Errore durante la transizione in firstPage con abortNewEntry");   }
    }

    private Contatto getContatto() {    //TODO: Aggiornare con nuovi dati
        Contatto newEntry = new Contatto();                         //creazione Bean contatto
        newEntry.setRagioneSociale(ragioneSociale.getText());
        newEntry.setCitta(citta.getText());
        newEntry.setEmailReferente(email.getText());
        newEntry.setPaese(paese.getText());
        newEntry.setPersonaRiferimento(personaRiferimento.getText());
        newEntry.setTelefono(telefono.getText());
        newEntry.setInteressamento(interesse.getValue());
        newEntry.setTipoCliente(tipoCliente.getValue());
        return newEntry;
    }
}
