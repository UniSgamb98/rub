package com.example.rub;

import com.example.rub.beans.Contatto;
import com.example.rub.functionalities.DBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class NewEntryController {
    @FXML
    public TextField emailReferente;
    @FXML
    public TextField via;
    @FXML
    public TextField regione;
    @FXML
    public TextField partitaIva;
    @FXML
    public TextField codiceFiscale;
    @FXML
    public TextField numeroCivico;
    @FXML
    public TextField cap;
    @FXML
    public TextField provincia;
    @FXML
    public TextField sitoWeb;
    @FXML
    public TextField titolare;
    @FXML
    public TextField pec;
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

    public void sendNewEntry(ActionEvent event){
        Contatto newEntry = getContatto();
        if (newEntry.getPaese().isBlank()||newEntry.getRegione().isBlank()||newEntry.getCitta().isBlank()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Dati mancanti!");
            alert.setContentText("Prego assicurarsi di riempire i campi: Paese, Regione, Città");
            alert.showAndWait();
        }
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
        newEntry.setEmailReferente(emailReferente.getText());
        newEntry.setPaese(paese.getText());
        newEntry.setPersonaRiferimento(personaRiferimento.getText());
        newEntry.setTelefono(telefono.getText());
        newEntry.setCap(cap.getText());
        newEntry.setCodiceFiscale(codiceFiscale.getText());
        newEntry.setEmailCertificata(pec.getText());
        newEntry.setEmailGenereica(email.getText());
        newEntry.setSitoWeb(sitoWeb.getText());
        newEntry.setTitolare(titolare.getText());
        newEntry.setNumeroCivico(numeroCivico.getText());
        newEntry.setPartitaIva(partitaIva.getText());
        newEntry.setRegione(regione.getText());
        newEntry.setIndirizzo(via.getText());
        newEntry.setProvincia(provincia.getText());
        return newEntry;
    }
}
