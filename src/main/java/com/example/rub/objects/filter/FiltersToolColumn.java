package com.example.rub.objects.filter;

import com.example.rub.enums.TagCategories;
import com.example.rub.functionalities.DBManager;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public class FiltersToolColumn extends VBox {
    private final ArrayList<FilterGroup> FilterGroupsList;
    public FiltersToolColumn (){
        FilterGroupsList = new ArrayList<>();
        for(int i = 0; i < TagCategories.values().length; i++){
            addFilterGroup(TagCategories.values()[i]);
        }
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
        ArrayList<String> activeFilters = new ArrayList<>();

        for (FilterGroup filterGroup : FilterGroupsList) {
            if (!filterGroup.getActiveFilters().isEmpty()) {
                activeFilters.addAll(filterGroup.getActiveFilters());
                toBeFilteredList.retainAll(filterGroup.getPartialFilteredList());
            }
        }
        System.out.println("   Trovati i filtri " + activeFilters + " attivi per la ricerca.");
        return toBeFilteredList;
    }
}
