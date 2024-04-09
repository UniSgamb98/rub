package com.example.rub;

import com.example.rub.beans.Contatto;
import com.example.rub.enums.Interessamento;
import com.example.rub.enums.TipoCliente;
import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.GlobalContext;
import com.example.rub.functionalities.MyUtils;
import com.example.rub.objects.NoteDisplayer;
import javafx.animation.AnimationTimer;
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
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class EntryDetailsPageController implements Initializable, Runnable {
    private final ObjectProperty<Contatto> entryProperty = new SimpleObjectProperty<>();
    @FXML
    public DatePicker prossimaChiamata;
    @FXML
    public DatePicker ultimaChiamata;
    @FXML
    public Label volteContattati;
    @FXML
    public ChoiceBox<TipoCliente> tipoCliente;
    @FXML
    public ChoiceBox<Interessamento> interessamento;
    @FXML
    public TextField emailReferente;
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
    @FXML
    public TextField regione;
    @FXML
    public TextField indirizzo;
    @FXML
    public TextField civico;
    @FXML
    public TextField provincia;
    @FXML
    public TextField cap;
    @FXML
    public TextField titolare;
    @FXML
    public TextField sito;
    @FXML
    public TextField pec;
    @FXML
    public TextField emailGenerica;
    @FXML
    public TextField codiceFiscale;
    @FXML
    public TextField partitaIva;
    @FXML
    public NoteDisplayer noteDisplayer;
    @FXML
    public Label savedText;
    @FXML
    public GridPane gridData;
    private Contatto entryToDisplayDetails;

    public void switchToSearchEntry(ActionEvent event) {
        shutdown();
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
            try {
                GlobalContext.openedEntries = (ArrayList<UUID>) MyUtils.read("fileAperti");
                GlobalContext.openedEntries.remove(entryToDisplayDetails.getId());
                MyUtils.write(GlobalContext.openedEntries, "fileAperti");
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void doRegisterCall() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("register-call.fxml"));
            Parent root = loader.load();
            RegisterCallController controller = loader.getController();
            controller.setEntryProperty(entryToDisplayDetails);
            controller.setControllerProperty(this);
            Stage callStage = new Stage();
            callStage.setTitle("Chiamata");
            Scene scene = new Scene(root, 380, 285);
            callStage.setScene(scene);
            callStage.getIcons().add(new Image("AppIcon.png"));
            callStage.show();
        } catch (IOException e) {
            System.out.println("Errore durante la transizione in register-call con doRegisterCall in EntryDetailsPageController");
        }
    }
    public void doDelete(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminazione permanente");
        alert.setContentText("Sei sicuro di voler eliminare questo contatto?");
        alert.setHeaderText("Eliminazione di " + entryToDisplayDetails.getRagioneSociale());
        alert.showAndWait();
        boolean result = alert.getResult().getText().equals("OK");
        if  (result) {
            DBManager.deleteEntry(entryToDisplayDetails.getId());
            try {
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("search-entry.fxml")));       //cambio scena
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                System.out.println("Errore durante la transizione in search-entry con doDelete in EntryDetailsPageController");
            }
        }
    }
    public void doSaveChanges() {
        if(DBManager.modifyEntry(entryToDisplayDetails.getId(),getContatto())){
            isModifiable.setSelected(false);
            ActionEvent event = new ActionEvent(null, isModifiable);
            allowChangesPressed(event);
            Thread thread = new Thread(this);
            thread.start();
        }
    }

    public void init(boolean fromScratch) {
        if (fromScratch) {  //true entro da search entry / false ho chiuso la finestra di registra chiamata
            entryToDisplayDetails = entryProperty.get();
        } else {
            entryToDisplayDetails = DBManager.retriveEntry(entryToDisplayDetails.getId());
        }
        ragioneSociale.setText(entryToDisplayDetails.getRagioneSociale());
        personaDiRiferimento.setText(entryToDisplayDetails.getPersonaRiferimento());
        paese.setText(entryToDisplayDetails.getPaese());
        citta.setText(entryToDisplayDetails.getCitta());
        telefono.setText(entryToDisplayDetails.getTelefono());
        emailReferente.setText(entryToDisplayDetails.getEmailReferente());
        interessamento.setValue(entryToDisplayDetails.getInteressamento());
        tipoCliente.setValue(entryToDisplayDetails.getTipoCliente());
        volteContattati.setText("" + entryToDisplayDetails.getVolteContattati());
        ultimaChiamata.setValue(entryToDisplayDetails.getUltimaChiamata());
        prossimaChiamata.setValue(entryToDisplayDetails.getProssimaChiamata());
        regione.setText(entryToDisplayDetails.getRegione());
        cap.setText(entryToDisplayDetails.getCap());
        pec.setText(entryToDisplayDetails.getEmailCertificata());
        emailGenerica.setText(entryToDisplayDetails.getEmailGenereica());
        titolare.setText(entryToDisplayDetails.getTitolare());
        partitaIva.setText(entryToDisplayDetails.getPartitaIva());
        civico.setText(entryToDisplayDetails.getNumeroCivico());
        sito.setText(entryToDisplayDetails.getSitoWeb());
        codiceFiscale.setText(entryToDisplayDetails.getCodiceFiscale());
        provincia.setText(entryToDisplayDetails.getProvincia());
        indirizzo.setText(entryToDisplayDetails.getIndirizzo());
    }
    private Contatto getContatto(){
        Contatto newEntry = new Contatto();                         //creazione Bean contatto
        newEntry.setRagioneSociale(ragioneSociale.getText());
        newEntry.setCitta(citta.getText());
        newEntry.setEmailReferente(emailReferente.getText());
        newEntry.setPaese(paese.getText());
        newEntry.setPersonaRiferimento(personaDiRiferimento.getText());
        newEntry.setTelefono(telefono.getText());
        newEntry.setInteressamento(interessamento.getValue());
        newEntry.setTipoCliente(tipoCliente.getValue());
        newEntry.setVolteContattati(Integer.parseInt(volteContattati.getText()));
        newEntry.setUltimaChiamata(ultimaChiamata.getValue());
        newEntry.setProssimaChiamata(prossimaChiamata.getValue());
        newEntry.setRegione(regione.getText());
        newEntry.setCap(cap.getText());
        newEntry.setEmailCertificata(pec.getText());
        newEntry.setEmailGenereica(emailGenerica.getText());
        newEntry.setTitolare(titolare.getText());
        newEntry.setPartitaIva(partitaIva.getText());
        newEntry.setNumeroCivico(civico.getText());
        newEntry.setSitoWeb(sito.getText());
        newEntry.setCodiceFiscale(codiceFiscale.getText());
        newEntry.setProvincia(provincia.getText());
        newEntry.setIndirizzo(indirizzo.getText());
        return newEntry;
    }
    public void setEntryProperty(Contatto entry){
        entryProperty.set(entry);
    }
    public void setNoteDocument(){
        noteDisplayer.setDocument(entryToDisplayDetails.getId());
    }
    private void setFieldDisability(boolean state){
        for (TextField textField : Arrays.asList(ragioneSociale, personaDiRiferimento, citta, paese, emailReferente, telefono, regione, indirizzo, provincia, cap, civico, partitaIva, codiceFiscale, emailGenerica, sito, pec, titolare)) {
            textField.setDisable(state);
        }
        interessamento.setDisable(state);
        tipoCliente.setDisable(state);
        ultimaChiamata.setDisable(state);
        prossimaChiamata.setDisable(state);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tipoCliente.getItems().addAll(TipoCliente.LABORATORIO, TipoCliente.RIVENDITORE, TipoCliente.CENTROFRESAGGIO);
        interessamento.getItems().addAll(Interessamento.NON_TROVATO, Interessamento.NON_INERENTE, Interessamento.NULLO, Interessamento.RICHIAMARE, Interessamento.INFO,Interessamento.LISTINO,Interessamento.CAMPIONE, Interessamento.CLIENTE);
        gridData.addEventFilter(ActionEvent.ACTION, event -> {
            if (event.getTarget() instanceof NoteDisplayer) {
                refresh();
            }
        });
    }

    public void shutdown(){
            try {
                GlobalContext.openedEntries = (ArrayList<UUID>) MyUtils.read("fileAperti");
            } catch (Exception e){
                GlobalContext.openedEntries = new ArrayList<>();
            }
            GlobalContext.openedEntries.remove(entryToDisplayDetails.getId());
            MyUtils.write(GlobalContext.openedEntries,"fileAperti");
    }
    public void refresh(){
        init(false);
        noteDisplayer.refresh();
    }

    @Override
    public void run() {
        savedText.setVisible(true);
        AnimationTimer timer = new AnimationTimer() {
            double opacity = 1.0;
            long startTime = -1;
            @Override
            public void handle(long now) {
                if (startTime == -1){
                    startTime = now;
                }
                if ((now - startTime) > 1000000000) {
                    opacity = opacity - 0.02;
                    savedText.setOpacity(opacity);
                    if (opacity < 0) {
                        savedText.setVisible(false);
                        savedText.setOpacity(1.0);
                        this.stop();
                    }
                }
            }
        };
        timer.start();
    }
}
