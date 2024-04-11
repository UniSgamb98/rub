package com.example.rub;

import com.example.rub.beans.Contatto;
import com.example.rub.enums.Operatori;
import com.example.rub.enums.comparator.DateStringComp;
import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.GlobalContext;
import com.example.rub.functionalities.NoteManager;
import com.example.rub.objects.DisplayableEntry;
import com.example.rub.objects.NoteDisplayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.xml.parsers.ParserConfigurationException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class ReportController implements Initializable {

    @FXML
    public ChoiceBox<Operatori> operator;
    @FXML
    public LineChart<String, Integer> chart;
    @FXML
    public DatePicker startDate;
    @FXML
    public DatePicker stopDate;
    @FXML
    public CheckBox includeMessages;
    @FXML
    public Spinner<Integer> durata;
    @FXML
    public NoteDisplayer history;
    @FXML
    public ListView<HBox> contacted;
    ObservableList<HBox> contactedList;
    ArrayList<Pair<UUID, String>> timeLine;
    String start;
    String stop;

    public void doGoBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("firstPage.fxml")));       //cambio scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) { System.out.println("Errore durante la transizione in firstPage con doGoBack in ReportController");   }
    }

    public void doShowReport() {
        try {
            start = startDate.getValue().toString();
            stop = stopDate.getValue().toString();
            createTimeLine();
            LinkedList<UUID> toDisplay = new LinkedList<>();
            for(Pair<UUID, String> p : timeLine){
                if (!toDisplay.contains(p.getKey())){
                    toDisplay.add(p.getKey());
                }
            }
            displayResults(toDisplay);
            setChart();

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Campi Mancanti");
            alert.setContentText("Inserisci data di inizio e fine");
            alert.setHeaderText("Dati mancanti!");
            alert.show();
        } catch (ParserConfigurationException e) {
            System.out.println("no");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        operator.getItems().addAll(Operatori.values());
        if (!(GlobalContext.operator == Operatori.TOMMASO || GlobalContext.operator == Operatori.GAETANO || GlobalContext.operator == Operatori.VICTORIA)){
            operator.setValue(GlobalContext.operator);
            operator.setDisable(true);
        }
        contactedList = FXCollections.observableArrayList();
        contacted.setItems(contactedList);
        durata.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,300));
    }

    private void createTimeLine () throws ParserConfigurationException {
        timeLine = new ArrayList<>();
        LinkedList<UUID> allEntries = DBManager.getAllEntries();
        NoteManager nm = new NoteManager();
        for (UUID i : allEntries){
            Contatto j = DBManager.retriveEntry(i);
            LinkedList<String> annotationDates = nm.getAnnotationDates(j.getNoteId(), durata.getValue(), operator.getValue());
            for (String k : annotationDates){
                if (k.compareTo(start) >= 0 && k.compareTo(stop) <= 0) {
                    timeLine.add(new Pair<>(j.getId(), k));
                }
            }
        }
        timeLine.sort(new DateStringComp());
    }

    private void displayResults(LinkedList<UUID> resultToDisplay){
        contactedList.clear();
        for (UUID uuid : resultToDisplay) {
            contactedList.add(DBManager.getDisplayableEntry(uuid));
        }
    }

    private void setChart(){
        chart.getData().clear();
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("N° Chiamate");
        for (String i : getIntermediateDate(startDate.getValue(), stopDate.getValue())){
            series.getData().add(new XYChart.Data<>(i, getFrequency(i)));
        }
        chart.getData().add(series);
    }

    private int getFrequency(String date){
        int ret = 0;
        for (Pair<UUID, String> i : timeLine){
            if (i.getValue().equals(date))  ret++;
        }
        return ret;
    }

    private ArrayList<String> getIntermediateDate (LocalDate start, LocalDate stop){
        ArrayList<String> ret = new ArrayList<>();
        while (start.toString().compareTo(stop.toString()) < 0) {
            ret.add(start.toString());
            start = start.plusDays(1);
        }
        return ret;
    }

    public void doShowNotes() {
        DisplayableEntry displayableEntry = (DisplayableEntry) contacted.getSelectionModel().getSelectedItem();
        history.setDocument(displayableEntry.getEntry().getId());
    }
}
