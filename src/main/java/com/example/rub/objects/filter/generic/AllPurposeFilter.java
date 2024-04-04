package com.example.rub.objects.filter.generic;

import com.example.rub.functionalities.DBManager;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public class AllPurposeFilter extends FlowPane {
    ArrayList<ToggleButton> buttons;
    public AllPurposeFilter(String... labels){
        buttons = new ArrayList<>();
        for (String i : labels){
            ToggleButton t = new ToggleButton(i);
            buttons.add(t);
            this.getChildren().add(t);
        }
    }

    public LinkedList<UUID> getFilteredList(){
        LinkedList<UUID> ret = new LinkedList<>();
        LinkedList<String> activeFilters = new LinkedList<>();  //Controllo i filtri attivi
        for(ToggleButton i : buttons){
            if(i.isSelected()){
                activeFilters.add(i.getText());
            }
        }
        if (activeFilters.isEmpty()){   //Nessun filtro attivo restituisco null che verrà ignorato
            ret = null;
        } else {    //Ci sono filtri attivi retituisco una lista con cui fare RetainAll
            for (String i : activeFilters){
                try {
                    ret.addAll(DBManager.getEntryFromFilter(i));
                } catch (NullPointerException e){
                    System.out.println("Index non contiene il filtro");
                }
            }
        }
        return ret;
    }
}
