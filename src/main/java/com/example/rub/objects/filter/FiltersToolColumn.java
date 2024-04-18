package com.example.rub.objects.filter;

import com.example.rub.enums.Interessamento.InteressamentoStatus;
import com.example.rub.enums.Operatori;
import com.example.rub.enums.TipoCliente;
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
    AllPurposeFilter tipologia;
    public FiltersToolColumn (){
        LocationManager manager = null;
        try {
            manager = (LocationManager) MyUtils.read("mondo");
        } catch (Exception e) {
            DBManager.reconstruct();
        }
        locationFilter = new StateFilter(manager);

        operatori = new AllPurposeFilter(Operatori.HUGO.name(), Operatori.SANTOLO.name(), Operatori.VICTORIA.name());
        interesse = new AllPurposeFilter(InteressamentoStatus.BLANK.name(), InteressamentoStatus.NON_TROVATO.name(), InteressamentoStatus.NON_INERENTE.name(), InteressamentoStatus.NULLO.name(), InteressamentoStatus.RICHIAMARE.name(), InteressamentoStatus.INFO.name(), InteressamentoStatus.LISTINO.name(), InteressamentoStatus.CAMPIONE.name(), InteressamentoStatus.CLIENTE.name());
        tipologia = new AllPurposeFilter(TipoCliente.LABORATORIO.name(), TipoCliente.RIVENDITORE.name(), TipoCliente.CENTROFRESAGGIO.name());
        this.getChildren().addAll(new Separator(), locationFilter, new Separator(), operatori, new Separator(), interesse, new Separator(), tipologia, new Separator());
        this.setSpacing(10.0);
    }
    public LinkedList<UUID> getFilteredList(){
        LinkedList<UUID> filteredList = new LinkedList<>();
        ArrayList<String> activeFilters = new ArrayList<>(locationFilter.getActiveFilters());
        for (String i : activeFilters){
            LinkedList<UUID> addedElementFromSingleFilter = new LinkedList<>(DBManager.getEntriesFromFilter(i));
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
        a = tipologia.getFilteredList();
        if (a != null){
            filteredList.retainAll(a);
        }


        System.out.println("   Trovati i filtri " + activeFilters + " attivi per la ricerca.");
        return filteredList;
    }
}
