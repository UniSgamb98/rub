package com.example.rub.objects.filter.location;

import com.example.rub.functionalities.locations.Locality;
import com.example.rub.functionalities.locations.Region;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class CitiesFilter extends VBox implements AutoRemoving {
    private final ArrayList<Choice> citiesSelection;
    private Region regionUsedInFilterTree;

    public CitiesFilter(Region regionUsedInFilterTree){
        this.regionUsedInFilterTree = regionUsedInFilterTree;
        citiesSelection = new ArrayList<>();
        Button addCityButton = new Button("Aggiungi Centro Abitato");
        this.getChildren().add(addCityButton);
        addCityButton.setOnAction(actionEvent -> addCity());
    }

    private void addCity(){
        try {
            Choice citySelected = new Choice(this, null, regionUsedInFilterTree.getCities());
            citiesSelection.add(citySelected);
            this.getChildren().add(citiesSelection.size() - 1, citySelected);
        } catch (Exception e){
            System.out.println("Nessuna voce selezionata");
        }
    }
    private void addAllCity(){
        Choice citySelected = new Choice(this, null, regionUsedInFilterTree.getCities());
        citiesSelection.add(citySelected);
        this.getChildren().add(citiesSelection.size()-1, citySelected);
    }

    public ArrayList<String> getSelectedCities(){
        ArrayList<String> ret = new ArrayList<>();
        for (Choice i : citiesSelection){
            ret.add(i.getSelectedItem());
        }
        return ret;
    }

    public void setRegionUsedInFilterTree(Region region){
        regionUsedInFilterTree = region;
    }
    @Override
    public void removeChoice(Choice choice){
        citiesSelection.remove(choice);
    }

    @Override
    public Locality getMyLocality() {
        return regionUsedInFilterTree;
    }

    public void setLocality(Locality locality) {
        regionUsedInFilterTree = (Region) locality;
    }

    @Override
    public String toString(){
        return "CityFilter della regione " + regionUsedInFilterTree ;
    }
}
