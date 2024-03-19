package com.example.rub.objects.filter;

import com.example.rub.enums.TagCategories;
import com.example.rub.functionalities.DBManager;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public class FilterGroup extends VBox {
    private final TagCategories category;
    private final FlowPane toggleableFilters;

    public FilterGroup(TagCategories category){
        toggleableFilters = new FlowPane();
        this.category = category;
        this.getChildren().addAll(new Label(category.name() + ":"),toggleableFilters);
        updateToggleButtons();
    }
    
    public ArrayList<String> getActiveFilters(){
        ArrayList<String> activeFilters = new ArrayList<>();
        for (Node nodeIn : toggleableFilters.getChildren()) {
            if (nodeIn instanceof ToggleButton && ((ToggleButton) nodeIn).isSelected()) {
                activeFilters.add(((ToggleButton)nodeIn).getText());
            }
        }
        return activeFilters;
    }

    protected LinkedList<UUID> getPartialFilteredList(){
        ArrayList<String> activeFilters = getActiveFilters();

        LinkedList<UUID> partialFilteredList = new LinkedList<>();  //RECUPERO UUID DAI FILTRI ATTIVI
        for (String filter : activeFilters){
            partialFilteredList.addAll(DBManager.getEntryFromFilter(filter));
        }
        return partialFilteredList;
    }
    public void updateToggleButtons(){
        LinkedList<String> filters = DBManager.getFilterOptionsFromCategory(category);  //RECUPERO TUTTI I TAG DELLA CATEGORIA
        toggleableFilters.getChildren().clear();
        for (String filter : filters){
            toggleableFilters.getChildren().add(new ToggleButton(filter));
        }
        System.out.println("Trovati " + filters.size() + " nella categoria " + category);
    }
}
