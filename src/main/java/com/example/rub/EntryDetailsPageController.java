package com.example.rub;

import com.example.rub.beans.Contatto;
import com.example.rub.beans.OpenedEntry;
import com.example.rub.enums.Interessamento.InteressamentoStatus;
import com.example.rub.enums.LogType;
import com.example.rub.enums.Operatori;
import com.example.rub.enums.Outcome;
import com.example.rub.enums.TipoCliente;
import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.GlobalContext;
import com.example.rub.functionalities.MyUtils;
import com.example.rub.objects.mail.EmailSenderShortcut;
import com.example.rub.objects.note.DisplayableEntry;
import com.example.rub.objects.note.NoteDisplayer;
import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    public ChoiceBox<InteressamentoStatus> interessamento;
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
    @FXML
    public Slider involvement;
    @FXML
    public EmailSenderShortcut emailSenderShortcut;
    @FXML
    public HBox emailBox;
    @FXML
    public CheckBox check1;
    @FXML
    public CheckBox check2;
    @FXML
    public CheckBox check3;
    @FXML
    public Button registerCallButton;
    @FXML
    public VBox adminExpandedInfos;
    @FXML
    public ChoiceBox<Operatori> operator;
    @FXML
    public TextField noteId;
    @FXML
    public DatePicker acquisition;
    @FXML
    public TextField telefono2;
    @FXML
    public TextField cellulare;
    private Contatto entryToDisplayDetails;
    ObservableList<DisplayableEntry> oldResults;
    final KeyCombination keyCombinationShiftC = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);

    public void doGoBack(ActionEvent event) {
        if (!entryToDisplayDetails.compare(getContatto())){
            ButtonType yes = new ButtonType("si");
            ButtonType no = new ButtonType("no");
            Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Salvare prima di continuare?", yes, no);
            a.setTitle("Salvataggio");
            a.setHeaderText("(╯°□°)╯︵ ┻━┻");

            a.showAndWait().ifPresent(response -> {
                if (response == yes) {
                    doSaveChanges();
                }
            });
        }
        shutdown();
        try {   //cambio scena
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("search-entry.fxml")));
            Parent root = loader.load();
            SearchEntryController controller = loader.getController();
            controller.savageOldList(oldResults);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Errore durante la transizione in search-entry con doGoBack in EntryDetailsPageController");
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e );
        }
    }

    public void preserveOldList(ObservableList<DisplayableEntry> oldList){
        oldResults = FXCollections.observableArrayList();
        oldResults.addAll(oldList);
    }

    public void allowChangesPressed(){

        if (isModifiable.isSelected()) {  //Checkbox = true
            try {
                GlobalContext.openedEntries = (ArrayList<OpenedEntry>) MyUtils.read("fileAperti");
            } catch (Exception e) {
                GlobalContext.openedEntries = new ArrayList<>();
            }
            if (!GlobalContext.isEntryOpened(entryToDisplayDetails.getId())) {
                GlobalContext.openedEntries.add(new OpenedEntry(entryToDisplayDetails.getId(), GlobalContext.operator));
                MyUtils.write(GlobalContext.openedEntries, "fileAperti");
                setFieldDisability(false);
            } else {    //Apertura fallita
                isModifiable.setSelected(false);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Impossibile salvare cambiamenti");
                alert.setContentText("Qualcun'altro ha aperto la scheda che cerchi di modificare");
                alert.showAndWait();
            }
        } else {    //Checkbox = false
            if (HasBeenChanged()){
                try {
                    GlobalContext.openedEntries = (ArrayList<OpenedEntry>) MyUtils.read("fileAperti");
                    GlobalContext.closeOpenedEntry(entryToDisplayDetails.getId());
                    MyUtils.write(GlobalContext.openedEntries, "fileAperti");
                    setFieldDisability(true);
                } catch (IOException | ClassNotFoundException e) {
                    MyUtils.log(LogType.ERROR);
                    MyUtils.log(LogType.MESSAGE, e);
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private boolean HasBeenChanged() {
        boolean ret = true;
        if (!entryToDisplayDetails.compare(getContatto())){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Salvataggio");
            alert.setContentText("Salvare le modifiche prima di continuare?");
            alert.showAndWait();
            boolean result = alert.getResult().getText().equals("OK");
            if (result){
                doSaveChanges();
            } else {
                ret = false;
                isModifiable.setSelected(true);
            }
        }
        return ret;
    }

    public void doRegisterCall() {
        try {
            if (!entryToDisplayDetails.compare(getContatto())){
                doSaveChanges();
            }
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("register-call.fxml"));
            Parent root = loader.load();
            RegisterCallController controller = loader.getController();
            controller.setEntryProperty(entryToDisplayDetails, this);
            Stage callStage = new Stage();
            callStage.setTitle("Chiamata");
            Scene scene = new Scene(root);
            callStage.setScene(scene);
            callStage.getIcons().add(new Image("AppIcon.png"));
            callStage.show();
            MyUtils.log(LogType.WINDOW, scene.getRoot().getId());
        } catch (IOException e) {
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
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
                MyUtils.log(LogType.ERROR);
                MyUtils.log(LogType.MESSAGE, e);
                System.out.println("Errore durante la transizione in search-entry con doDelete in EntryDetailsPageController");
            }
        }
    }
    public void doSaveChanges() {
        Outcome outcome = DBManager.modifyEntry(entryToDisplayDetails.getId(), getContatto());
        Thread thread;
        switch (outcome){
            case SUCCESS:
                thread = new Thread(this);
                thread.start();
                entryToDisplayDetails = DBManager.retriveEntry(entryToDisplayDetails.getId());
                break;
            case FAILURE:
                //do nothing
                break;
            case RECOVERED_SUCCESS:
                thread = new Thread(this);
                thread.start();
                recoverIdOfDisplayedEntry();
                break;
        }
    }
    public void recoverIdOfDisplayedEntry(){
        entryToDisplayDetails = DBManager.retriveEntry(DBManager.recoverFromNoteId(entryToDisplayDetails.getNoteId()));
    }

    public void init(boolean fromScratch) {
        adminExpandedInfos.setVisible(false);
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
        involvement.setValue(entryToDisplayDetails.getCoinvolgimento());

        if (entryToDisplayDetails.getCheckpoint() >= 1) {check1.setSelected(true);}
        if (entryToDisplayDetails.getCheckpoint() >= 2) {check2.setSelected(true);}
        if (entryToDisplayDetails.getCheckpoint() >= 3) {check3.setSelected(true);}

        emailSenderShortcut.setDestinatario(entryToDisplayDetails.getEmailReferente());

        operator.setValue(entryToDisplayDetails.getOperator());
        noteId.setText(entryToDisplayDetails.getNoteId().toString());
        acquisition.setValue(entryToDisplayDetails.getAcquisizione());

        try {
            GlobalContext.openedEntries = (ArrayList<OpenedEntry>) MyUtils.read("fileAperti");
        } catch (Exception e) {
            GlobalContext.openedEntries = new ArrayList<>();
        }
        if (!GlobalContext.isEntryOpened(entryToDisplayDetails.getId())) {
            GlobalContext.openedEntries.add(new OpenedEntry(entryToDisplayDetails.getId(), GlobalContext.operator));
            MyUtils.write(GlobalContext.openedEntries, "fileAperti");
            setFieldDisability(false);
            isModifiable.setSelected(true);
        }
    }
    private Contatto getContatto(){
        Contatto newEntry = new Contatto();                         //creazione Bean contatto
        newEntry.setRagioneSociale(ragioneSociale.getText());
        newEntry.setPersonaRiferimento(personaDiRiferimento.getText());
        newEntry.setEmailReferente(emailReferente.getText());
        newEntry.setTelefono(telefono.getText());
        newEntry.setPaese(paese.getText());
        newEntry.setRegione(regione.getText());
        newEntry.setCitta(citta.getText());
        newEntry.setIndirizzo(indirizzo.getText());
        newEntry.setNumeroCivico(civico.getText());
        newEntry.setProvincia(provincia.getText());
        newEntry.setCap(cap.getText());
        newEntry.setInteressamento(interessamento.getValue());
        newEntry.setTipoCliente(tipoCliente.getValue());
        newEntry.setPartitaIva(partitaIva.getText());
        newEntry.setCodiceFiscale(codiceFiscale.getText());
        newEntry.setTitolare(titolare.getText());
        newEntry.setEmailGenereica(emailGenerica.getText());
        newEntry.setEmailCertificata(pec.getText());
        newEntry.setVolteContattati(Integer.parseInt(volteContattati.getText()));
        newEntry.setUltimaChiamata(ultimaChiamata.getValue());
        newEntry.setProssimaChiamata(prossimaChiamata.getValue());
        newEntry.setSitoWeb(sito.getText());
        newEntry.setCoinvolgimento(involvement.getValue());
        newEntry.setCellulare(cellulare.getText());
        newEntry.setTelefono2(telefono2.getText());

        if (check3.isSelected()){
            newEntry.setCheckpoint(3);
        }else {
            if (check2.isSelected()){
                newEntry.setCheckpoint(2);
            }else {
                if (check1.isSelected()){
                    newEntry.setCheckpoint(1);
                } else {
                    newEntry.setCheckpoint(0);
                }
            }
        }

        // questi servono per far funzionare il metodo Contatto.equals(Contatto)
        newEntry.setNoteId(UUID.fromString(noteId.getText()));
        newEntry.setOperator(operator.getValue());
        newEntry.setAcquisizione(acquisition.getValue());
        return newEntry;
    }
    public void setEntryProperty(Contatto entry){
        entryProperty.set(entry);
    }
    public void setNoteDocument(){
        noteDisplayer.setDocument(entryToDisplayDetails.getId());
    }
    private void setFieldDisability(boolean state){
        for (TextField textField : Arrays.asList(ragioneSociale, personaDiRiferimento, citta, paese, emailReferente, telefono, telefono2, cellulare, regione, indirizzo, provincia, cap, civico, partitaIva, codiceFiscale, emailGenerica, sito, pec, titolare)) {
            textField.setDisable(state);
        }
        involvement.setDisable(state);
        for (CheckBox checkpoint : Arrays.asList(check1, check2, check3)){
            checkpoint.setDisable(state);
        }
        interessamento.setDisable(state);
        tipoCliente.setDisable(state);
        ultimaChiamata.setDisable(state);
        prossimaChiamata.setDisable(state);
        saveButton.setVisible(!state);
        registerCallButton.setVisible(!state);

        operator.setDisable(state);
        noteId.setDisable(state);
        acquisition.setDisable(state);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tipoCliente.getItems().addAll(TipoCliente.LABORATORIO, TipoCliente.RIVENDITORE, TipoCliente.CENTROFRESAGGIO);
        operator.getItems().addAll(Operatori.HUGO, Operatori.SANTOLO, Operatori.VICTORIA, Operatori.TOMMASO, Operatori.GAETANO);
        interessamento.getItems().addAll(InteressamentoStatus.NON_TROVATO, InteressamentoStatus.NON_INERENTE, InteressamentoStatus.NULLO, InteressamentoStatus.RICHIAMARE, InteressamentoStatus.INFO, InteressamentoStatus.LISTINO, InteressamentoStatus.CAMPIONE, InteressamentoStatus.CLIENTE);
        gridData.addEventFilter(ActionEvent.ACTION, event -> {
            if (event.getTarget() instanceof NoteDisplayer) {
                refresh();
            }
        });
        ragioneSociale.setOnKeyPressed(event -> {
            if (keyCombinationShiftC.match(event)) {
                adminExpandedInfos.setVisible(true);
            }
        });
    }

    public void shutdown(){
            try {
                GlobalContext.openedEntries = (ArrayList<OpenedEntry>) MyUtils.read("fileAperti");
            } catch (Exception e){
                GlobalContext.openedEntries = new ArrayList<>();
            }
            GlobalContext.closeOpenedEntry(entryToDisplayDetails.getId());
            MyUtils.write(GlobalContext.openedEntries,"fileAperti");
    }
    public void refresh(){
        init(false);
        noteDisplayer.refresh();
        emailSenderShortcut.refresh();
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

    public void openMailPreferences(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("mail-preferences.fxml"));
            Parent root = loader.load();
            MailPreferencesController controller = loader.getController();
            controller.loadPreferences(this);
            controller.setProperties(((Node) event.getSource()).getScene());
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
            System.out.println("Errore durante la transizione openMailPreferences in EntryDetailsPageController");
        }
    }

    public void manageCheckpoint(ActionEvent event) {
        String name = ((CheckBox) event.getTarget()).getId();
        boolean state = ((CheckBox) event.getTarget()).isSelected();
        switch (name){
            case "check1":
                if (!state) {
                    check2.setSelected(false);
                    check3.setSelected(false);
                }
                break;
            case "check2":
                if (state){
                    check1.setSelected(true);
                }else {
                    check3.setSelected(false);
                }
                break;
            case "check3":
                if (state) {
                    check1.setSelected(true);
                    check2.setSelected(true);
                }
                break;
        }

    }

    public void notifyChange() {
    }

}
