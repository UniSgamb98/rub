package com.example.rub.objects.filter;

import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.MyUtils;
import com.example.rub.functionalities.locations.LocationManager;
import com.example.rub.objects.filter.location.StateFilter;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public class FiltersToolColumn extends VBox {
    StateFilter locationFilter;
    public FiltersToolColumn (){
        LocationManager manager = null;
        try {
            manager = (LocationManager) MyUtils.read("mondo");
        } catch (Exception e) {
            DBManager.reconstruct();
        }

        locationFilter = new StateFilter(manager);
        this.getChildren().addAll(new Separator(), locationFilter);
    }
    public LinkedList<UUID> getFilteredList(){
        LinkedList<UUID> toBeFilteredList = new LinkedList<>(DBManager.getAllEntries());
        ArrayList<String> activeFilters = new ArrayList<>(locationFilter.getActiveFilters());
        for (String i : activeFilters){
            toBeFilteredList.retainAll(DBManager.getEntryFromFilter(i));
        }
        System.out.println("   Trovati i filtri " + activeFilters + " attivi per la ricerca.");
        return toBeFilteredList;
    }
}
