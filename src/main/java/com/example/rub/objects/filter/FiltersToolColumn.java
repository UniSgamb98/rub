package com.example.rub.objects.filter;

import com.example.rub.enums.Interessamento;
import com.example.rub.enums.Operatori;
import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.MyUtils;
import com.example.rub.functionalities.locations.LocationManager;
import com.example.rub.objects.filter.generic.AllPurposeFilter;
import com.example.rub.objects.filter.location.StateFilter;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public class FiltersToolColumn extends VBox {
    StateFilter locationFilter;
    AllPurposeFilter operatori;
    AllPurposeFilter interesse;
    public FiltersToolColumn (){
        LocationManager manager = null;         //TODO si può scrivere meglio
        try {
            manager = (LocationManager) MyUtils.read("mondo");
        } catch (Exception e) {
            DBManager.reconstruct();
        }
        locationFilter = new StateFilter(manager);

        operatori = new AllPurposeFilter(Operatori.HUGO.name(), Operatori.SANTOLO.name(), Operatori.VICTORIA.name());
        interesse = new AllPurposeFilter(Interessamento.NON_INERENTE.name(), Interessamento.NON_INERENTE.name(), Interessamento.NULLO.name(), Interessamento.RICHIAMARE.name(), Interessamento.INFO.name(), Interessamento.LISTINO.name(), Interessamento.CAMPIONE.name(), Interessamento.CLIENTE.name());

        this.getChildren().addAll(new Separator(), locationFilter, operatori, new Separator(), interesse, new Separator());
    }
    public LinkedList<UUID> getFilteredList(){
        LinkedList<UUID> filteredList = new LinkedList<>();
        ArrayList<String> activeFilters = new ArrayList<>(locationFilter.getActiveFilters());
        for (String i : activeFilters){
            LinkedList<UUID> addedElementFromSingleFilter = new LinkedList<>(DBManager.getEntryFromFilter(i));
            for (UUID j : addedElementFromSingleFilter){
                if(!filteredList.contains(j)){
                    filteredList.add(j);
                }
            }
        }
        if (filteredList.isEmpty()) filteredList.addAll(DBManager.getAllEntries());

        LinkedList<UUID> a = operatori.getFilteredList();
        if (a != null){
            filteredList.retainAll(a);
        }
        a = interesse.getFilteredList();
        if (a != null){
            filteredList.retainAll(a);
        }


        System.out.println("   Trovati i filtri " + activeFilters + " attivi per la ricerca.");
        return filteredList;
    }
}
