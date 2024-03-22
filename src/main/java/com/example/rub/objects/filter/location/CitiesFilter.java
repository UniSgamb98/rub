package com.example.rub.objects.filter.location;

import com.example.rub.functionalities.locations.City;
import com.example.rub.functionalities.locations.Locality;
import com.example.rub.functionalities.locations.Region;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CitiesFilter extends Filter implements AutoRemoving {
    private final ArrayList<Choice> citiesSelection;
    private Region regionAssigned;

    public CitiesFilter(Region regionAssigned){
        this.regionAssigned = regionAssigned;
        citiesSelection = new ArrayList<>();
        Button addCityButton = new Button("Aggiungi Centro Abitato");
        this.getChildren().add(addCityButton);
        addCityButton.setOnAction(actionEvent -> addCity());
    }

    private void addCity(){
        try {
            if (citiesSelection.isEmpty() || !citiesSelection.get(citiesSelection.size() - 1).getSelectLocalityName().isEmpty()) {
                Choice cityChoice = new Choice(this, new Filter(), regionAssigned.getCities());
                citiesSelection.add(cityChoice);
                this.getChildren().add(citiesSelection.size() - 1, cityChoice);
            }
        }catch (Exception e){
            System.out.println("Nessuna voce selezionata");
        }
    }
    private void addAllCity(){
        Choice citySelected = new Choice(this, null, regionAssigned.getCities());
        citiesSelection.add(citySelected);
        this.getChildren().add(citiesSelection.size()-1, citySelected);
    }

    public void setAssigned(Locality region){
        regionAssigned = (Region) region;
    }

    @Override
    public void removeChoice(Choice choice){
        citiesSelection.remove(choice);
    }
    @Override
    public String toString(){
        return "CityFilter della regione " + regionAssigned;
    }
}
