package com.example.rub;

import com.example.rub.beans.Contatto;
import com.example.rub.enums.LogType;
import com.example.rub.enums.Operatori;
import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.GlobalContext;
import com.example.rub.functionalities.MyUtils;
import com.example.rub.functionalities.NoteManager;
import com.example.rub.objects.note.DisplayableEntry;
import com.example.rub.objects.note.NoteDisplayer;
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
    @FXML
    public Label blanks;
    public Label infos;
    @FXML
    public Label notFounds;
    @FXML
    public Label notRelateds;
    @FXML
    public Label nullInterests;
    @FXML
    public Label priceLists;
    @FXML
    public Label samples;
    @FXML
    public Label recalls;
    @FXML
    public Label clients;
    ObservableList<DisplayableEntry> contactedList;
    ArrayList<Pair<UUID, String>> timeLine;
    int[] reportInfo = {0,0,0,0,0};   //tot.aziende - tot comunicazioni - nuove listino - nuove campionature - nuovi clienti
    int[] workInfo = {0,0,0,0,0,0,0,0,0};
    String[] workTips = {"","","","","","","","",""};
    String start;
    String stop;

    public void doGoBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("firstPage.fxml")));       //cambio scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
            System.out.println("Errore durante la transizione in firstPage con doGoBack in ReportController");
        }
    }

    public void doShowReport() {
        try {
            start = startDate.getValue().toString();
            stop = stopDate.getValue().toString();
            createTimeLine();
            displayFilterOptions();
            totalUniqueContacts.setText(": " + reportInfo[0]);
            totalCommunications.setText(": " + reportInfo[1]);
            totalNewPriceList.setText(": " + reportInfo[2]);
            totalNewSampling.setText(": " + reportInfo[3]);
            totalNewClients.setText(": " + reportInfo[4]);
            blanks.setText(": " + workInfo[0]);
            notFounds.setText(": " + workInfo[1]);
            notRelateds.setText(": " + workInfo[2]);
            nullInterests.setText(": " + workInfo[3]);
            recalls.setText(": " + workInfo[4]);
            infos.setText(": " + workInfo[5]);
            priceLists.setText(": " + workInfo[6]);
            samples.setText(": " + workInfo[7]);
            clients.setText(": " + workInfo[8]);

            blanks.setTooltip(new Tooltip(workTips[0]));
            notFounds.setTooltip(new Tooltip(workTips[1]));
            notRelateds.setTooltip(new Tooltip(workTips[2]));
            nullInterests.setTooltip(new Tooltip(workTips[3]));
            recalls.setTooltip(new Tooltip(workTips[4]));
            infos.setTooltip(new Tooltip(workTips[5]));
            priceLists.setTooltip(new Tooltip(workTips[6]));
            samples.setTooltip(new Tooltip(workTips[7]));
            clients.setTooltip(new Tooltip(workTips[8]));

            filtro.getItems().retainAll("Periodo analizzato");
            if (!filtro.getItems().contains("Periodo analizzato"))  filtro.getItems().add("Periodo analizzato");    //si verifica solo la prima volta
            filtro.getItems().addAll(getIntermediateDate(startDate.getValue(), stopDate.getValue()));
            filtro.setValue("Periodo analizzato");
            setChart();
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
            System.out.println(filtro.getValue());
            if (filtro.getValue().equals("Periodo analizzato")) {
                for (Pair<UUID, String> p : timeLine) {
                    if (!toDisplay.contains(p.getKey())) {
                        toDisplay.add(p.getKey());
                    }
                }
                subUniqueContacts.setText(": " + reportInfo[0]);
                subCommunications.setText(": " + reportInfo[1]);
                subNewPriceList.setText(": " + reportInfo[2]);
                subNewSampling.setText(": " + reportInfo[3]);
                subNewClients.setText(": " + reportInfo[4]);
            } else {
                int communications = 0;
                int prices = 0;
                int samples = 0;
                int clients = 0;
                for (Pair<UUID, String> p : timeLine) {
                    if (p.getValue().substring(0, p.getValue().length()-2).equals(filtro.getValue())) {
                        communications++;
                        if (!toDisplay.contains(p.getKey())) {
                            toDisplay.add(p.getKey());
                            switch (p.getValue().substring(p.getValue().length()-2,p.getValue().length()-1)){
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
        reportInfo[0] = 0;
        reportInfo[1] = 0;
        reportInfo[2] = 0;
        reportInfo[3] = 0;
        reportInfo[4] = 0;

        for (UUID i : DBManager.getAllEntries()){
            Contatto j = DBManager.retriveEntry(i);
            boolean firstTime = true;
            for (String k : nm.getAnnotationDates(j.getNoteId(), durata.getValue(), operator.getValue(), includeMessages.isSelected())){
                String dateOfAnnotation = k.substring(0, k.length() - 2);
                if (dateOfAnnotation.compareTo(start) >= 0 && dateOfAnnotation.compareTo(stop) <= 0) {
                    switch (k.substring(k.length()-2,k.length()-1)){
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
                    timeLine.add(new Pair<>(i, k));
                    if(firstTime) {
                        reportInfo[0]++;
                        firstTime = false;
                    }
                    switch (k.substring(k.length()-1)){
                        case "A":
                            workInfo[0]++;
                            workTips[0] = workTips[0] + j.getRagioneSociale() + "\n";
                            break;
                        case "B":
                            workInfo[1]++;
                            workTips[1] = workTips[1] + j.getRagioneSociale() + "\n";
                            break;
                        case "C":
                            workInfo[2]++;
                            workTips[2] = workTips[2] + j.getRagioneSociale() + "\n";
                            break;
                        case "D":
                            workInfo[3]++;
                            workTips[3] = workTips[3] + j.getRagioneSociale() + "\n";
                            break;
                        case "E":
                            workInfo[4]++;
                            workTips[4] = workTips[4] + j.getRagioneSociale() + "\n";
                            break;
                        case "F":
                            workInfo[5]++;
                            workTips[5] = workTips[5] + j.getRagioneSociale() + "\n";
                            break;
                        case "G":
                            workInfo[6]++;
                            workTips[6] = workTips[6] + j.getRagioneSociale() + "\n";
                            break;
                        case "H":
                            workInfo[7]++;
                            workTips[7] = workTips[7] + j.getRagioneSociale() + "\n";
                            break;
                        case "I":
                            workInfo[8]++;
                            workTips[8] = workTips[8] + j.getRagioneSociale() + "\n";
                            break;
                    }
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

        for (XYChart.Series<String, Integer> s : chart.getData()) {
            for (XYChart.Data<String, Integer> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip("" + d.getYValue().doubleValue()));
            }
        }
    }

    private int getFrequency(String date){
        int ret = 0;
        for (Pair<UUID, String> i : timeLine){
            if (i.getValue().substring(0, i.getValue().length() - 2).equals(date))  ret++;
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
