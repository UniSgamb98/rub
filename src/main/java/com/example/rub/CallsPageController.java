package com.example.rub;

import com.example.rub.functionalities.DBManager;
import com.example.rub.objects.DisplayableEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.UUID;

public class CallsPageController implements Initializable {
    @FXML
    public ListView<DisplayableEntry> callList;
    private ObservableList<DisplayableEntry> list;


    public void setCallList(LinkedList<UUID> idList){
        for (UUID i : idList){
          list.add(new DisplayableEntry(DBManager.retriveEntry(i)));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        list = FXCollections.observableArrayList();
        callList.setItems(list);
    }
}
