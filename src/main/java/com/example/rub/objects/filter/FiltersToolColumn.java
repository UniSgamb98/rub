package com.example.rub.objects.filter;

import com.example.rub.enums.TagCategories;
import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.locations.City;
import com.example.rub.functionalities.locations.LocationManager;
import com.example.rub.functionalities.locations.Region;
import com.example.rub.functionalities.locations.State;
import com.example.rub.objects.filter.location.StateFilter;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public class FiltersToolColumn extends VBox {
    private final ArrayList<FilterGroup> FilterGroupsList;
    StateFilter locationFilter;
    public FiltersToolColumn (){
        FilterGroupsList = new ArrayList<>();
        for(int i = 0; i < TagCategories.values().length; i++){
            addFilterGroup(TagCategories.values()[i]);
        }

        City peschiera = new City("Peschiera");
        City castelnuovo = new City("Castelnuovo");
        City pacengo = new City("Pacengo");
        Region veneto = new Region("Veneto");
        Region lombardia = new Region("Lombardia");
        Region lazio = new Region("Lazio");
        State italia = new State("Italia");
        State uk = new State("Inghilterra");
        LocationManager manager = new LocationManager("Manager");
        veneto.addAllCities(peschiera, castelnuovo, pacengo);
        italia.addAllRegion(veneto, lombardia, lazio);
        manager.addAllStates(italia, uk);


        locationFilter = new StateFilter(manager);
        this.getChildren().addAll(new Separator(), locationFilter);
    }
    private void addFilterGroup(TagCategories category){
        FilterGroup filterGroup = new FilterGroup(category);
        FilterGroupsList.add(filterGroup);
        this.getChildren().add(filterGroup);
    }
    public void updateAllToggleButtons(){
        for (FilterGroup i : FilterGroupsList){
            i.updateToggleButtons();
        }
    }
    public LinkedList<UUID> getFilteredList(){
        LinkedList<UUID> toBeFilteredList = new LinkedList<>(DBManager.getAllEntries());
        ArrayList<String> activeFilters = new ArrayList<>(locationFilter.getActiveFilters());
        for (String i : activeFilters){
            toBeFilteredList.retainAll(DBManager.getEntryFromFilter(i));
        }


        /*  //todo VECCHIO METODO
        for (FilterGroup filterGroup : FilterGroupsList) {
            if (!filterGroup.getActiveFilters().isEmpty()) {
                activeFilters.addAll(filterGroup.getActiveFilters());
                toBeFilteredList.retainAll(filterGroup.getPartialFilteredList());
            }
        }*/
        System.out.println("   Trovati i filtri " + activeFilters + " attivi per la ricerca.");
        return toBeFilteredList;
    }
}
