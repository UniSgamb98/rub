package com.example.rub.objects.filter.location;

import com.example.rub.functionalities.locations.Locality;
import com.example.rub.functionalities.locations.Region;
import com.example.rub.functionalities.locations.State;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class RegionFilter extends Filter implements AutoRemoving{
    private final ArrayList<Choice> regionsSelection;
    private final State stateAssigned;
    private CitiesFilter citiesFilter;


    public RegionFilter(State stateUsedInFilterTree){
        citiesFilter = new CitiesFilter(null);      //TODO NON DOVREBBE ESSERE ASSEGNATO AL COSTRUTTORE MA ASSEGNATO AL CHANGEVALUE();
        this.stateAssigned = stateUsedInFilterTree;
        regionsSelection = new ArrayList<>();
        Button addRegionButton = new Button("Aggiungi Regione");
        this.getChildren().add(addRegionButton);
        addRegionButton.setOnAction(actionEvent -> addRegion());
    }

    private void addRegion(){
        try {
            if (regionsSelection.isEmpty() || !regionsSelection.get(regionsSelection.size() - 1).getSelectLocalityName().isEmpty()) {
                Choice regionChoice = new Choice(this, citiesFilter, stateAssigned.getRegions());
                regionsSelection.add(regionChoice);
                this.getChildren().add(regionsSelection.size() - 1, regionChoice);
            }
        }catch (Exception e) {
            System.out.println("Nessuna voce selezionata");
        }
    }

    @Override
    public void removeChoice(Choice choice){
        regionsSelection.remove(choice);
    }

    @Override
    public void setAssigned(Locality locality) {

    }

    @Override
    public String toString(){
        return "RegionFilter dello stato " + stateAssigned;
    }

}
