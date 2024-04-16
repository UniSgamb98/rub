package com.example.rub;

import com.example.rub.enums.Interessamento.InteressamentoStatus;
import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.GlobalContext;
import com.example.rub.functionalities.MyUtils;
import com.example.rub.objects.DisplayableEntry;
import com.example.rub.objects.NoteDisplayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.UUID;

public class LogoutController implements Initializable {
    public ListView<HBox> contacted;
    ObservableList<HBox> contactedList;
    public NoteDisplayer history;
    public Label ragioneSociale;
    public Label paese;
    public DatePicker reminderDate;
    private Scene oldScene;

    public void ignoreReminder(ActionEvent event) {
        try {
            DisplayableEntry displayableEntry = (DisplayableEntry) contacted.getSelectionModel().getSelectedItem();
            GlobalContext.notProgrammedCalls.remove(displayableEntry.getEntry().getId());
            contactedList.remove(displayableEntry);
            MyUtils.write(GlobalContext.notProgrammedCalls, GlobalContext.operator.name());
            if (contactedList.isEmpty()) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        } catch (Exception e)   { System.out.println("non è stato selezionato un contatto");    }
    }

    public void setReminder(ActionEvent event) {
        try {
            if (reminderDate.getValue() == null){
                throw new Exception();
            }
            DisplayableEntry displayableEntry = (DisplayableEntry) contacted.getSelectionModel().getSelectedItem();
            DBManager.setNextCall(displayableEntry.getEntry().getId(), reminderDate.getValue(), InteressamentoStatus.BLANK, -1, false);
            contactedList.remove(displayableEntry);
            MyUtils.write(GlobalContext.notProgrammedCalls, GlobalContext.operator.name());
            if (contactedList.isEmpty()) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        } catch (Exception e)   { System.out.println("non è stato selezionato un contatto");   }
    }

    public void doGoBack(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(oldScene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Errore durante la transizione in firstPage con doLogin");
        }
    }

    public void doRemindMeLater(ActionEvent event) {
        MyUtils.write(GlobalContext.notProgrammedCalls, GlobalContext.operator.name());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setProperties(Scene oldScene){
        this.oldScene = oldScene;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contactedList = FXCollections.observableArrayList();
        contacted.setItems(contactedList);
        displayResults(GlobalContext.notProgrammedCalls);
    }

    private void displayResults(LinkedList<UUID> resultToDisplay){
        contactedList.clear();
        for (UUID uuid : resultToDisplay) {
            contactedList.add(DBManager.getDisplayableEntry(uuid));
        }
    }

    public void doShowNotes() {
        try {
            DisplayableEntry displayableEntry = (DisplayableEntry) contacted.getSelectionModel().getSelectedItem();
            history.setDocument(displayableEntry.getEntry().getId());
            ragioneSociale.setText(displayableEntry.getEntry().getRagioneSociale());
            paese.setText(displayableEntry.getEntry().getPaese());
        } catch (RuntimeException ignored) {}
    }
}
