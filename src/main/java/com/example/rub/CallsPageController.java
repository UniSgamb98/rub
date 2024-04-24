package com.example.rub;

import com.example.rub.functionalities.DBManager;
import com.example.rub.objects.note.DisplayableEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.UUID;

public class CallsPageController implements Initializable {
    @FXML
    public ListView<DisplayableEntry> callList;
    public DatePicker date;
    private ObservableList<DisplayableEntry> list;
    public void setCallList(LinkedList<UUID> idList){
        list.clear();
        for (UUID i : idList){
          list.add(new DisplayableEntry(i));
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        list = FXCollections.observableArrayList();
        callList.setItems(list);
        date.setValue(LocalDate.now());
        setCallList(DBManager.getCallList(LocalDate.now()));
    }

    public void exit(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void dateChange(){
        setCallList(DBManager.getCallList(date.getValue()));
    }
}
