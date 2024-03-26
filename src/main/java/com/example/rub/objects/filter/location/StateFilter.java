package com.example.rub.objects.filter.location;

import com.example.rub.functionalities.locations.Locality;
import com.example.rub.functionalities.locations.LocationManager;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.function.Predicate;

public class StateFilter extends Filter {
    private final ArrayList<Choice> statesSelection;
    private LocationManager managerUsed;
    private final Button addStateButton;

    public StateFilter(LocationManager managerUsed){
        this.managerUsed = managerUsed;
        statesSelection = new ArrayList<>();
        addStateButton = new Button("Aggiungi Paese");
        this.getChildren().add(addStateButton);
        addStateButton.setOnAction(actionEvent -> addState());
    }

    private void addState(){
        try {
            if (statesSelection.isEmpty() || !statesSelection.get(statesSelection.size() - 1).getSelectLocalityName().isEmpty()) {
                RegionFilter regionsFilter = new RegionFilter(null);
                Choice stateChoice = new Choice(this, regionsFilter, managerUsed.getSubLocalities());
                statesSelection.add(stateChoice);
                this.getChildren().add(statesSelection.size() - 1, stateChoice);
            }
        }catch (Exception e) {
            System.out.println("Nessuna voce selezionata");
        }
    }
    public ArrayList<String> getActiveFilters(){
        ArrayList<String> ret = new ArrayList<>();
        for (Choice i : statesSelection){
            if (((RegionFilter)i.getChild()).getSelectionSize() == 0){
                ret.add(i.getSelectLocalityName());
            } else{
                ret.addAll(((RegionFilter) i.getChild()).getActiveFilters());
            }
        }
        Predicate<String> condition = n -> n.equals("manager"); //QUESTO SUCCEDE SE SI LASCIA UN CHOICE SENZA VALORE
        ret.removeIf(condition);
        return ret;
    }
    @Override
    public void removeChoice(Choice choice) {
        statesSelection.remove(choice);
    }
    @Override
    public void setAssigned(Locality locality) {
        managerUsed = (LocationManager) locality;
    }
    @Override
    public Locality getAssigned(){
        return managerUsed;
    }
    @Override
    public String toString(){
        return "StateFilter del mondo " + managerUsed;
    }
    @Override
    protected void becomeVisible(){
        this.getChildren().add(addStateButton);
    }
}
