package com.example.rub;

import com.example.rub.beans.Contatto;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class EntryDetailsPageController{
    private final ObjectProperty<Contatto> entryProperty = new SimpleObjectProperty<>();
    @FXML
    public Label prossimaChiamata;
    @FXML
    public Label ultimaChiamata;
    @FXML
    public Label volteContattati;
    @FXML
    public TextField tipoCliente;
    @FXML
    public TextField interessamento;
    @FXML
    public TextField email;
    @FXML
    public TextField telefono;
    @FXML
    public TextField citta;
    @FXML
    public TextField paese;
    @FXML
    public TextField personaDiRiferimento;
    @FXML
    public TextField ragioneSociale;
    @FXML
    private Contatto entryToDisplay;
    public void switchToSearchEntry(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("search-entry.fxml")));       //cambio scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) { System.out.println("Errore durante la transizione in search-entry con switchToSearchEntry in EntryDetailsPageController");   }
    }


    public void init() {
        entryToDisplay = entryProperty.get();
        ragioneSociale.setText(entryToDisplay.getRagioneSociale());
        personaDiRiferimento.setText(entryToDisplay.getPersonaRiferimento());
        paese.setText(entryToDisplay.getPaese());
        citta.setText(entryToDisplay.getCitta());
        telefono.setText(entryToDisplay.getTelefono());
        email.setText(entryToDisplay.getEmail());
        interessamento.setText(entryToDisplay.getInteressamento());
        tipoCliente.setText(entryToDisplay.getTipoCliente());
        volteContattati.setText("" + entryToDisplay.getVolteContattati());
        System.out.println(entryToDisplay.getUltimaChiamata());
        ultimaChiamata.setText(entryToDisplay.getUltimaChiamata());
        prossimaChiamata.setText(entryToDisplay.getProssimaChiamata());
    }
    public void setEntryProperty(Contatto entry){
        entryProperty.set(entry);
    }
}
