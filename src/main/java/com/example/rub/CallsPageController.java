package com.example.rub;

import com.example.rub.enums.LogType;
import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.MyUtils;
import com.example.rub.objects.note.DisplayableEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;

public class CallsPageController implements Initializable {
    @FXML
    public ListView<DisplayableEntry> callList;
    public DatePicker date;
    private ObservableList<DisplayableEntry> list;
    private Stage stage;

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

    public void doRequestEntryDetails(MouseEvent mouseEvent) {
        DisplayableEntry displayableEntry = callList.getSelectionModel().getSelectedItem();
        System.out.println("Apertura della scheda " + displayableEntry);
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("entry-details-page.fxml")));
        try {
            Parent root = loader.load();       //cambio scena
            EntryDetailsPageController controller = loader.getController();
            controller.preserveOldList(list);
            controller.setEntryProperty(displayableEntry.getEntry());
            controller.init(true);
            controller.setNoteDocument();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setOnHidden(e -> controller.shutdown());
            stage.show();
        } catch (Exception e) {
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
            System.out.println("Errore durante la transizione in firstPage con doRequestEntryDetails in SearchEntryController");
        }
    }

    public void setProperties(Stage stage){
        this.stage = stage;
    }
}
