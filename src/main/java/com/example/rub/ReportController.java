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
    public ListView<DisplayableEntry> contacted;
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
    public Label totalNewPriceList;
    @FXML
    public Label totalCommunications;
    @FXML
    public Label subCommunications;
    @FXML
    public Label subUniqueContacts;
    @FXML
    public Label subNewClients;
    @FXML
    public Label subNewPriceList;
    @FXML
    public Label subNewSampling;
    @FXML
    public HBox box;
    ObservableList<DisplayableEntry> contactedList;
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
            totalNewPriceList.setText(": " + reportInfo[2]);
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
            history.clear();
            LinkedList<UUID> toDisplay = new LinkedList<>();
            if (filtro.getValue().equals("Periodo analizzato")) {
                for (Pair<UUID, String> p : timeLine) {
                    if (!toDisplay.contains(p.getKey())) {
                        toDisplay.add(p.getKey());
                    }
                }
                subCommunications.setText(totalCommunications.getText());
                subUniqueContacts.setText(totalUniqueContacts.getText());
                subNewPriceList.setText(totalNewPriceList.getText());
                subNewSampling.setText(totalNewSampling.getText());
                subNewClients.setText(totalNewClients.getText());
            } else {
                int communications = 0;
                int prices = 0;
                int samples = 0;
                int clients = 0;
                for (Pair<UUID, String> p : timeLine) {
                    if (p.getValue().substring(0, p.getValue().length()-1).equals(filtro.getValue())) {
                        communications++;
                        if (!toDisplay.contains(p.getKey())) {
                            toDisplay.add(p.getKey());
                            switch (p.getValue().substring(p.getValue().length()-1)){
                                case "A":
                                    prices++;
                                    break;
                                case "B":
                                    samples++;
                                    break;
                                case "C":
                                    clients++;
                                    break;
                            }
                        }
                    }
                }
                subCommunications.setText(": " + communications);
                subUniqueContacts.setText(": " + toDisplay.size());
                subNewPriceList.setText(": " + prices);
                subNewSampling.setText(": " + samples);
                subNewClients.setText(": " + clients);
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
        box.addEventFilter(ActionEvent.ACTION, event -> {
            if (event.getTarget() instanceof  NoteDisplayer) {
                LinkedList<UUID> temp = new LinkedList<>();
                for (DisplayableEntry i : contactedList){
                    temp.add(i.getEntry().getId());
                }
                displayResults(temp);
            }
        });
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
                if (k.substring(0, k.length()-1).compareTo(start) >= 0 && k.substring(0, k.length()-1).compareTo(stop) <= 0) {
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
        XYChart.Series<String, Integer> chartData = new XYChart.Series<>();
        chartData.setName("N° Chiamate");
        for (String i : getIntermediateDate(startDate.getValue(), stopDate.getValue())){
            chartData.getData().add(new XYChart.Data<>(i, getFrequency(i)));
        }
        chart.getData().add(chartData);
    }

    private int getFrequency(String date){
        int ret = 0;
        for (Pair<UUID, String> i : timeLine){
            if (i.getValue().substring(0, i.getValue().length() - 1).equals(date))  ret++;
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
            DisplayableEntry displayableEntry = contacted.getSelectionModel().getSelectedItem();
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
