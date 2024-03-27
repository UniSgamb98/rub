package com.example.rub.objects.filter.location;

import com.example.rub.functionalities.locations.Locality;
import com.example.rub.functionalities.locations.Region;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class CitiesFilter extends Filter {
    private final ArrayList<Choice> citiesSelection;
    private Region regionAssigned;
    private final Button addCityButton;

    public CitiesFilter(Region regionAssigned){
        this.regionAssigned = regionAssigned;
        citiesSelection = new ArrayList<>();
        addCityButton = new Button("Aggiungi Centro Abitato");
        addCityButton.setOnAction(actionEvent -> addCity());
    }

    private void addCity(){
        try {
            if (citiesSelection.isEmpty() || !citiesSelection.get(citiesSelection.size() - 1).getSelectLocalityName(false).isEmpty()) {
                Choice cityChoice = new Choice(this, new Filter(), regionAssigned.getSubLocalities());
                citiesSelection.add(cityChoice);
                this.getChildren().add(citiesSelection.size() - 1, cityChoice);
            }
        }catch (Exception e){
            System.out.println("Nessuna voce selezionata");
        }
    }
    @Override
    public void setAssigned(Locality region){
        regionAssigned = (Region) region;
        for (Choice i : citiesSelection){
            this.getChildren().remove(i);
        }
        citiesSelection.clear();
    }
    @Override
    public Locality getAssigned(){
        return regionAssigned;
    }
    public ArrayList<String> getActiveFilters(){
        ArrayList<String> ret = new ArrayList<>();
        for (Choice i : citiesSelection){
            ret.add(i.getSelectLocalityName(true));
        }
        return ret;
    }

    public int getSelectionSize(){
        return citiesSelection.size();
    }
    @Override
    public void removeChoice(Choice choice){
        citiesSelection.remove(choice);
    }
    @Override
    public String toString(){
        return "CityFilter della regione " + regionAssigned;
    }
    @Override
    protected void becomeVisible(){
        if (!this.getChildren().contains(addCityButton)) {
            this.getChildren().add(addCityButton);
        }
    }
}
