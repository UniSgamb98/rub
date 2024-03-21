package com.example.rub.objects.filter.location;

import com.example.rub.functionalities.locations.Locality;
import com.example.rub.functionalities.locations.Region;
import com.example.rub.functionalities.locations.State;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class RegionFilter extends VBox implements AutoRemoving{
    private final ArrayList<Choice> regionsSelection;
    private final State stateUsedInFilterTree;
    private CitiesFilter citiesFilter;
    private Region myRegion;


    public RegionFilter(State stateUsedInFilterTree){
        myRegion = null;
        citiesFilter = new CitiesFilter(myRegion);
        this.stateUsedInFilterTree = stateUsedInFilterTree;
        regionsSelection = new ArrayList<>();
        Button addRegionButton = new Button("Aggiungi Regione");
        this.getChildren().add(addRegionButton);
        addRegionButton.setOnAction(actionEvent -> addRegion());
    }

    private void addRegion(){
        if (regionsSelection.isEmpty() || regionsSelection.get(regionsSelection.size() - 1) == null) {
            Choice regionSelected = new Choice(this, citiesFilter, stateUsedInFilterTree.getRegions());
            regionsSelection.add(regionSelected);
            this.getChildren().add(regionsSelection.size() - 1, regionSelected);
        }
    }

    public ArrayList<String> getSelectedRegions(){
        ArrayList<String> ret = new ArrayList<>();
        for (Choice i : regionsSelection){
            ret.add(i.getSelectedItem());
        }
        return ret;
    }

    @Override
    public void removeChoice(Choice choice){
        regionsSelection.remove(choice);
    }

    @Override
    public Locality getMyLocality() {
        return myRegion;
    }

}
