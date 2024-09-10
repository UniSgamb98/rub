package com.example.rub;

import com.example.rub.beans.Contatto;
import com.example.rub.enums.Interessamento;
import com.example.rub.enums.Interessamento.InteressamentoStatus;
import com.example.rub.enums.LogType;
import com.example.rub.enums.Outcome;
import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.GlobalContext;
import com.example.rub.functionalities.MyUtils;
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
    @FXML
    public CheckBox isMessage;
    @FXML
    public Label label3;
    @FXML
    public Label label2;
    @FXML
    public Label label1;
    @FXML
    public Slider coinvolgimento;
    @FXML
    public Label label4;

    public void doCancelRegistration(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    public void doRegisterCall(ActionEvent event) {
        MyUtils.log(LogType.ADDNOTE, note.getText());
        Contatto bean = entryProperty.get();
        Outcome outcome = Outcome.FAILURE;
        try {
            NoteManager nm = new NoteManager();
            Document doc;
            try {   //RECUPERO xml NOTE SE INESISTENTE CREAZIONE DI UNO NUOVO
                doc = nm.readXml("" + bean.getNoteId());
            }catch (Exception e) {
                System.out.println("Creazione nuovo documento di nota");
                doc = nm.createDocument(bean.getRagioneSociale());
            }
            InteressamentoStatus fedback = Interessamento.fromQuestionForm(feedback.getValue());
            int checkpoint = getCheckpoint(fedback, bean.getCheckpoint());
            nm.addCallNote(doc, note.getText(), durata.getValue(), isMessage.isSelected(), bean.getInteressamento(), fedback, checkpoint);
            double involvement = (isMessage.isSelected()? -1 : coinvolgimento.getValue());
            outcome = DBManager.setNextCall(bean.getId(), prossimaChiamata.getValue(), fedback, involvement, !isMessage.isSelected(), isMessage.isSelected(), bean.getNoteId());
            nm.writeXml(doc, ""+bean.getNoteId());
        } catch (Exception e){
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
            System.out.println("Errore durante la scrittura del file Xml delle note");
        }
        if ((prossimaChiamata.getValue()==null && (!Interessamento.fromQuestionForm(feedback.getValue()).equals(InteressamentoStatus.NULLO) && !(Interessamento.fromQuestionForm(feedback.getValue()).equals(InteressamentoStatus.NON_INERENTE) )) && !GlobalContext.notProgrammedCalls.contains(entryProperty.get().getId()))){
            if (outcome.equals(Outcome.RECOVERED_SUCCESS))  entryProperty.set(DBManager.retriveEntry(DBManager.recoverFromNoteId(entryProperty.get().getNoteId())));
            GlobalContext.notProgrammedCalls.add(entryProperty.get().getId());
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        if (outcome.equals(Outcome.RECOVERED_SUCCESS))  controllerProperty.get().recoverIdOfDisplayedEntry();
        controllerProperty.get().refresh();
    }

    private static int getCheckpoint(InteressamentoStatus fedback, int beanCheckpoint) {
        int checkpoint = 0;
        if (fedback != null){
            if (fedback.equals(InteressamentoStatus.LISTINO) && beanCheckpoint < 1){
                checkpoint = 1;
            }else if (fedback.equals(InteressamentoStatus.CAMPIONE) && beanCheckpoint < 2){
                checkpoint = 2;
            }else if (fedback.equals(InteressamentoStatus.CLIENTE) && beanCheckpoint < 3){
                checkpoint = 3;
            }
        }
        return checkpoint;
    }

    public void setEntryProperty(Contatto entry, EntryDetailsPageController controller){
        entryProperty.set(entry);
        coinvolgimento.setValue(entryProperty.get().getCoinvolgimento());
        controllerProperty.set(controller);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (Interessamento i : Interessamento.getSet()){
            feedback.getItems().add(i.getQuestionForm());
        }
        feedback.setValue("Nessuna novità");
        durata.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,300));
    }

    public void hideCallScheduling() {
        feedback.setVisible(!isMessage.isSelected());
        prossimaChiamata.setVisible(!isMessage.isSelected());
        coinvolgimento.setVisible(!isMessage.isSelected());
        durata.setVisible(!isMessage.isSelected());
        label1.setVisible(!isMessage.isSelected());
        label2.setVisible(!isMessage.isSelected());
        label3.setVisible(!isMessage.isSelected());
        label4.setVisible(!isMessage.isSelected());
    }
}
