package com.example.rub;

import com.example.rub.functionalities.DBManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.UUID;

public class SearchEntryController implements Initializable {
    @FXML
    public VBox filtersVBox;    //oggetto in indice 0 è una scritta.
    @FXML
    public TextField rapidSearchBar;
    @FXML
    public ListView<HBox> resultsView;
    ObservableList<HBox> results;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        results = FXCollections.observableArrayList();
        ArrayList<String> filters = DBManager.getFilterOptions();
        int i = 0;
        for (String filter : filters){
            i++;
            filtersVBox.getChildren().add(new ToggleButton(filter));
        }
        System.out.println("Filtri trovati: " + i);
        resultsView.setItems(results);
        displayResults(DBManager.getAllEntries());
    }

    public void doFilteredSearch() {
        System.out.println("Eseguo ricerca filtrata");
        ArrayList<String> activeFilters = new ArrayList<>();

        for (Node nodeIn : filtersVBox.getChildren()) {
            if (nodeIn instanceof ToggleButton && ((ToggleButton) nodeIn).isSelected()) {
                activeFilters.add(((ToggleButton)nodeIn).getText());
            }
        }
        System.out.println("Trovati "+ activeFilters + " filtri attivi");

        if(activeFilters.isEmpty()) displayResults(DBManager.getAllEntries());
        else displayResults(DBManager.getEntryFromFilter(activeFilters.get(0)));
    }
    private void displayResults(LinkedList<UUID> resultToDisplay){
        results.clear();

        for (UUID uuid : resultToDisplay) {
            results.add(DBManager.getDisplayableEntry(uuid));
        }
    }
    public void doRapidSearch() {
        System.out.println("Eseguo ricerca rapida");
        String input = rapidSearchBar.getText();
        if (input.isEmpty())    {
            displayResults(DBManager.getAllEntries());
            System.out.println("Input nullo, mostro elenco completo");
        }
        else    {
            displayResults(DBManager.rapidSearch(input));
            System.out.println("Trovati " + DBManager.rapidSearch(input).size() + " Risultati");
        }
        rapidSearchBar.clear();
    }

    public void doRequestEntryDetails(){
        System.out.println("Apertura "+ resultsView.getSelectionModel().getSelectedItem());

    }
}
