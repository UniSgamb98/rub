package com.example.rub;

import com.example.rub.beans.Contatto;
import com.example.rub.enums.Operatori;
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
    @FXML
    public ChoiceBox<String> filtro;
    public Label assignedContactsToOperator;
    @FXML
    public Label totalUniqueContacts;
    @FXML
    public Label totalNewSampling;
    @FXML
    public Label totalNewClients;
    @FXML
    public Label totalNewInfo;
    @FXML
    public Label totalCommunications;
    ObservableList<HBox> contactedList;
    ArrayList<Pair<UUID, String>> timeLine;
    int[] reportInfo = {0,0,0,0,0};   //tot.aziende - tot comunicazioni - nuove info - nuove campionature - nuovi clienti
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
            filtro.getItems().clear();      //chissa perchè questa linea lancio un InvocationTargetException ma funziona tutto correttamente
            filtro.getItems().add("Periodo analizzato");
            filtro.getItems().addAll(getIntermediateDate(startDate.getValue(), stopDate.getValue()));
            filtro.setValue("Periodo analizzato");
            displayFilterOptions();
            setChart();
            totalUniqueContacts.setText(": " + reportInfo[0]);
            totalCommunications.setText(": " + reportInfo[1]);
            totalNewInfo.setText(": " + reportInfo[2]);
            totalNewSampling.setText(": " + reportInfo[3]);
            totalNewClients.setText(": " + reportInfo[4]);
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Campi Mancanti");
            alert.setContentText("Inserisci data di inizio e fine");
            alert.setHeaderText("Dati mancanti!");
            alert.show();
        } catch (ParserConfigurationException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setContentText("Errore durante la lettura della nota");
            alert.setHeaderText("Errore");
            alert.show();
        }
    }

    public void displayFilterOptions(){
        if (filtro.getValue()!= null) {
            LinkedList<UUID> toDisplay = new LinkedList<>();
            for (Pair<UUID, String> p : timeLine) {
                if (filtro.getValue().equals("Periodo analizzato")) {
                    if (!toDisplay.contains(p.getKey())) {
                        toDisplay.add(p.getKey());
                    }
                } else {
                    if (!toDisplay.contains(p.getKey()) && p.getValue().equals(filtro.getValue())) {
                        toDisplay.add(p.getKey());
                    }
                }
            }
            reportInfo[0] = toDisplay.size();
            displayResults(toDisplay);
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
        NoteManager nm = new NoteManager();
        for (UUID i : DBManager.getAllEntries()){
            Contatto j = DBManager.retriveEntry(i);
            for (String k : nm.getAnnotationDates(j.getNoteId(), durata.getValue(), operator.getValue(), includeMessages.isSelected())){
                switch (k.substring(k.length()-1)){
                    case "A":
                        reportInfo[2]++;
                        break;
                    case "B":
                        reportInfo[3]++;
                        break;
                    case "C":
                        reportInfo[4]++;
                        break;
                }
                k = k.substring(0, k.length()-1);
                if (k.compareTo(start) >= 0 && k.compareTo(stop) <= 0) {
                    timeLine.add(new Pair<>(i, k));
                }
            }
        }
        reportInfo[1]=timeLine.size();
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
        while (start.toString().compareTo(stop.toString()) <= 0) {
            ret.add(start.toString());
            start = start.plusDays(1);
        }
        return ret;
    }

    public void doShowNotes() {
        try {
            DisplayableEntry displayableEntry = (DisplayableEntry) contacted.getSelectionModel().getSelectedItem();
            history.setDocument(displayableEntry.getEntry().getId());
        } catch (RuntimeException ignored) {}
    }

    public void doFillWithToday() {
        stopDate.setValue(LocalDate.now());
        startDate.setValue(LocalDate.now());
        doShowReport();
    }

    public void doFillWithMonth() {
        startDate.setValue(LocalDate.now().minusMonths(1));
        stopDate.setValue(LocalDate.now());
        doShowReport();
    }

    public void doFillWithWeek() {
        startDate.setValue(LocalDate.now().minusWeeks(1));
        stopDate.setValue(LocalDate.now());
        doShowReport();
    }

    public void operatorHasChanged( ) {
        assignedContactsToOperator.setText(": "+DBManager.getOperatorTotalContacts(operator.getValue()));
    }
}
