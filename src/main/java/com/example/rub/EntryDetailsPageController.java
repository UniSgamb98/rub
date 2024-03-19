package com.example.rub;

import com.example.rub.beans.Contatto;
import com.example.rub.functionalities.GlobalContext;
import com.example.rub.functionalities.MyUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

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
        GlobalContext.openedEntries.remove(entryToDisplay.getId());
        MyUtils.write(GlobalContext.openedEntries,"fileAperti");
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("search-entry.fxml")));       //cambio scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) { System.out.println("Errore durante la transizione in search-entry con switchToSearchEntry in EntryDetailsPageController");   }
    }

    public void allowChangesPressed(ActionEvent event){
        if (((CheckBox) event.getTarget()).isSelected()) {
            try {
                GlobalContext.openedEntries = (ArrayList<UUID>) MyUtils.read("fileAperti");
            } catch (Exception e) {
                GlobalContext.openedEntries = new ArrayList<>();
            }
            if (!GlobalContext.openedEntries.contains(entryToDisplay.getId())) {
                GlobalContext.openedEntries.add(entryToDisplay.getId());
                MyUtils.write(GlobalContext.openedEntries, "fileAperti");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Impossibile salvare cambiamenti");
                alert.setContentText("Qualcun'altro ha aperto la scheda che cerchi di modificare");
                alert.showAndWait();
            }
        } else {
            GlobalContext.openedEntries.remove(entryToDisplay.getId());
            if (GlobalContext.openedEntries.isEmpty()){     //ELIMINIZAIONE SE VUOTI DI FILE APERTI
                MyUtils.delete("fileAperti");
            } else {    //ALTRIMENTI SOVRASCRIVO FILEAPERTI
                MyUtils.write(GlobalContext.openedEntries, "fileAperti");
            }
        }
    }


    public void doRegisterCall() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Lavoro per il futuro");
        alert.setContentText("Questa funzionalità non è ancora disponibile");
        alert.showAndWait();
    }
    public void doSaveChanges() {
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
        ultimaChiamata.setText(entryToDisplay.getUltimaChiamata());
        prossimaChiamata.setText(entryToDisplay.getProssimaChiamata());
    }

    public void setEntryProperty(Contatto entry){
        entryProperty.set(entry);
    }
}
