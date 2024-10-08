package com.example.rub;

import com.example.rub.beans.Contatto;
import com.example.rub.enums.LogType;
import com.example.rub.enums.comparator.EntryComp;
import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.MyUtils;
import com.example.rub.objects.note.DisplayableEntry;
import com.example.rub.objects.filter.FiltersToolColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;

public class SearchEntryController implements Initializable {
    @FXML
    public FiltersToolColumn filterTool;    //oggetto in indice 0 è una scritta.
    @FXML
    public TextField rapidSearchBar;
    @FXML
    public ListView<DisplayableEntry> resultsView;
    ObservableList<DisplayableEntry> results;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        results = FXCollections.observableArrayList();
        resultsView.setItems(results);
        try {
            DBManager.update();
        } catch (Exception e) {
            System.out.println("Problemi durante l'update dal Database persistente");
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
        }
        displayResults(DBManager.getAllEntries());
    }
    public void savageOldList(ObservableList<DisplayableEntry> oldList){
        results.clear();
        try {
            for (DisplayableEntry i : oldList) {
                results.add(new DisplayableEntry(i.getEntry().getId()));
            }
        } catch (NullPointerException ignored){}
        if (results.isEmpty()){
            for (UUID i : DBManager.getAllEntries())
                results.add(new DisplayableEntry(i));
        }
    }

    public void doFilteredSearch() {
        System.out.println("Eseguo ricerca filtrata");
        LinkedList<UUID> listToDisplay = new LinkedList<>(filterTool.getFilteredList());

        displayResults(listToDisplay);
    }
    private void displayResults(LinkedList<UUID> resultToDisplay){
        results.clear();
        resultToDisplay.sort(new EntryComp());

        for (UUID uuid : resultToDisplay) {
            results.add(DBManager.getDisplayableEntry(uuid));
        }
    }
    public void doRapidSearch() {
        System.out.println("Eseguo ricerca rapida");
        String input = rapidSearchBar.getText();
        if (input.isEmpty())    {
            displayResults(DBManager.getAllEntries());
            System.out.println("   Input nullo, mostro elenco completo");
        }
        else    {
            displayResults(DBManager.rapidSearch(input));
            System.out.println("   Trovati " + DBManager.rapidSearch(input).size() + " Risultati");
        }
        rapidSearchBar.clear();
    }

    public void doRequestEntryDetails(MouseEvent event){
        DisplayableEntry displayableEntry = resultsView.getSelectionModel().getSelectedItem();
        System.out.println("Apertura della scheda " + displayableEntry);
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("entry-details-page.fxml")));
        try {
            Parent root = loader.load();       //cambio scena
            EntryDetailsPageController controller = loader.getController();
            controller.preserveOldList(results);
            controller.setEntryProperty(displayableEntry.getEntry());
            controller.init(true);
            controller.setNoteDocument();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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

    public void doSwitchToFirstPage(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("firstPage.fxml")));       //cambio scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
            System.out.println("Errore durante la transizione in firstPage con doSwitchToFirstPage in SearchEntryController");
        }
    }

    public void copyList() {
        try {
            File file = new File("List.txt");
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for(DisplayableEntry i : results){
                Contatto j = i.getEntry();
                bw.write(j.getRagioneSociale()+ " : " + j.getTelefono());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}