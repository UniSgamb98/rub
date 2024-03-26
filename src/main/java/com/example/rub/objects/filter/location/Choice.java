package com.example.rub.objects.filter.location;

import com.example.rub.functionalities.locations.Locality;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Choice extends HBox {
    protected Button removeButton;
    private final ChoiceBox<Locality> itemChoice;
    private final VBox parent;
    private final Filter child;

    public Choice(VBox parent, Filter child,  ArrayList<Locality> items){
        this.child = child;
        this.parent = parent;
        removeButton = new Button("-");
        itemChoice = new ChoiceBox<>();
        itemChoice.getItems().addAll(items);
        removeButton.setOnAction(actionEvent -> removeSelf());
        itemChoice.setOnAction(actionEvent -> valueHasChanged());
        this.getChildren().addAll(removeButton, itemChoice, child);
    }
    public String getSelectLocalityName(){
        String ret;
        if (itemChoice.getValue() == null){
            ret = ((Filter)parent).getAssigned().getLocalityName();
        } else {
            ret = itemChoice.getValue().getLocalityName();
        }
        return ret;
    }

    private void valueHasChanged() {
        if (!(parent instanceof CitiesFilter)){
            child.setAssigned(itemChoice.getValue());
            child.becomeVisible();
        }
    }
    protected Filter getChild(){
        return child;
    }
    private void removeSelf(){
            parent.getChildren().remove(this);
            ((AutoRemoving) parent).removeChoice(this);
    }
}
