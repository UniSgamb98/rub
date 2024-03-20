package com.example.rub;

import com.example.rub.beans.Contatto;
import com.example.rub.enums.Interessamento;
import com.example.rub.enums.TipoCliente;
import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.GlobalContext;
import com.example.rub.functionalities.MyUtils;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;

public class EntryDetailsPageController implements Initializable {
    private final ObjectProperty<Contatto> entryProperty = new SimpleObjectProperty<>();
    @FXML
    public Label prossimaChiamata;
    @FXML
    public Label ultimaChiamata;
    @FXML
    public Label volteContattati;
    @FXML
    public ChoiceBox<TipoCliente> tipoCliente;
    @FXML
    public ChoiceBox<Interessamento> interessamento;
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
    public CheckBox isModifiable;
    @FXML
    public Button saveButton;
    private Contatto entryToDisplayDetails;
    public void switchToSearchEntry(ActionEvent event) {
        GlobalContext.openedEntries.remove(entryToDisplayDetails.getId());
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
        if (isModifiable.isSelected()) {  //Checkbox = true
            try {
                GlobalContext.openedEntries = (ArrayList<UUID>) MyUtils.read("fileAperti");
            } catch (Exception e) {
                GlobalContext.openedEntries = new ArrayList<>();
            }
            if (!GlobalContext.openedEntries.contains(entryToDisplayDetails.getId())) {
                GlobalContext.openedEntries.add(entryToDisplayDetails.getId());
                MyUtils.write(GlobalContext.openedEntries, "fileAperti");
                saveButton.setVisible(true);
                setFieldDisability(false);
            } else {    //Apertura fallita
                ((CheckBox) event.getTarget()).setSelected(false);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Impossibile salvare cambiamenti");
                alert.setContentText("Qualcun'altro ha aperto la scheda che cerchi di modificare");
                alert.showAndWait();
            }
        } else {    //Checkbox = false
            saveButton.setVisible(false);
            setFieldDisability(true);
            GlobalContext.openedEntries.remove(entryToDisplayDetails.getId());
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
        DBManager.modifyEntry(entryToDisplayDetails.getId(),getContatto());
    }

    public void init() {
        entryToDisplayDetails = entryProperty.get();
        ragioneSociale.setText(entryToDisplayDetails.getRagioneSociale());
        personaDiRiferimento.setText(entryToDisplayDetails.getPersonaRiferimento());
        paese.setText(entryToDisplayDetails.getPaese());
        citta.setText(entryToDisplayDetails.getCitta());
        telefono.setText(entryToDisplayDetails.getTelefono());
        email.setText(entryToDisplayDetails.getEmail());
        interessamento.setValue(entryToDisplayDetails.getInteressamento());
        tipoCliente.setValue(entryToDisplayDetails.getTipoCliente());
        volteContattati.setText("" + entryToDisplayDetails.getVolteContattati());
        ultimaChiamata.setText(entryToDisplayDetails.getUltimaChiamata());
        prossimaChiamata.setText(entryToDisplayDetails.getProssimaChiamata());
    }
    private Contatto getContatto(){
        Contatto newEntry = new Contatto();                         //creazione Bean contatto
        newEntry.setRagioneSociale(ragioneSociale.getText());
        newEntry.setCitta(citta.getText());
        newEntry.setEmail(email.getText());
        newEntry.setPaese(paese.getText());
        newEntry.setPersonaRiferimento(personaDiRiferimento.getText());
        newEntry.setTelefono(telefono.getText());
        newEntry.setInteressamento(interessamento.getValue());
        newEntry.setTipoCliente(tipoCliente.getValue());
        return newEntry;
    }
    public void setEntryProperty(Contatto entry){
        entryProperty.set(entry);
    }
    private void setFieldDisability(boolean state){
        ragioneSociale.setDisable(state);
        personaDiRiferimento.setDisable(state);
        citta.setDisable(state);
        paese.setDisable(state);
        email.setDisable(state);
        telefono.setDisable(state);
        ragioneSociale.setDisable(state);
        ragioneSociale.setDisable(state);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tipoCliente.getItems().addAll(TipoCliente.LABORATORIO, TipoCliente.RIVENDITORE, TipoCliente.CENTROFRESAGGIO);
        interessamento.getItems().addAll(Interessamento.IMMEDIATO, Interessamento.PROSSIMAMENTE, Interessamento.NULLO);
    }
}
