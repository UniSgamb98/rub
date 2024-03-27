package com.example.rub.objects.filter.location;

import com.example.rub.functionalities.locations.Locality;
import com.example.rub.functionalities.locations.State;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class RegionFilter extends Filter {
    private final ArrayList<Choice> regionsSelection;
    private State stateAssigned;
    private final Button addRegionButton;


    public RegionFilter(State stateUsedInFilterTree){
        this.stateAssigned = stateUsedInFilterTree;
        regionsSelection = new ArrayList<>();
        addRegionButton = new Button("Aggiungi Regione");
        addRegionButton.setOnAction(actionEvent -> addRegion());
    }

    private void addRegion(){
        try {
            if (regionsSelection.isEmpty() || !regionsSelection.get(regionsSelection.size() - 1).getSelectLocalityName(false).isEmpty()) {
                CitiesFilter citiesFilter = new CitiesFilter(null);
                Choice regionChoice = new Choice(this, citiesFilter, stateAssigned.getSubLocalities());
                regionsSelection.add(regionChoice);
                this.getChildren().add(regionsSelection.size() - 1, regionChoice);
            }
        }catch (Exception e) {
            System.out.println("Nessuna voce selezionata");
        }
    }
    public ArrayList<String> getActiveFilters(){
        ArrayList<String> ret = new ArrayList<>();
        for (Choice i : regionsSelection){
            if (((CitiesFilter)i.getChild()).getSelectionSize() == 0){
                ret.add(i.getSelectLocalityName(true));
            } else{
                ret.addAll(((CitiesFilter) i.getChild()).getActiveFilters());
            }
        }
        return ret;
    }

    public int getSelectionSize(){
        return regionsSelection.size();
    }
    @Override
    public void removeChoice(Choice choice){
        regionsSelection.remove(choice);
    }

    @Override
    public void setAssigned(Locality state){
        stateAssigned = (State) state;
        for (Choice i : regionsSelection){
            this.getChildren().remove(i);
        }
        regionsSelection.clear();
    }
    @Override
    public Locality getAssigned(){
        return stateAssigned;
    }
    @Override
    public String toString(){
        return "RegionFilter dello stato " + stateAssigned;
    }
    @Override
    protected void becomeVisible(){
        if(!this.getChildren().contains(addRegionButton)){
            this.getChildren().add(addRegionButton);
        }
    }
}
