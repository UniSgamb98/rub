package com.example.rub.objects.filter.location;

import com.example.rub.functionalities.locations.Locality;
import com.example.rub.functionalities.locations.Region;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class Choice extends HBox {
    protected Button removeButton;
    protected ChoiceBox<Locality> items;
    private final Node parent;  //un filtro piu ampio
    private VBox child; //il filtro piu stretto

    public Choice(HBox parent, VBox child, ArrayList<Locality> item){
        this.child = child;
        this.parent = parent;
        constructorChoice(item);
    }
    public Choice(VBox parent, VBox child,  ArrayList<Locality> item){
        this.child = child;
        this.parent = parent;
        constructorChoice(item);
    }

    private void constructorChoice(ArrayList<Locality> item){
        removeButton = new Button("-");
        items = new ChoiceBox<>();
        items.getItems().addAll(item);
        removeButton.setOnAction(actionEvent -> removeSelf());
        items.setOnAction(actionEvent -> valueHasChanged());
        this.getChildren().addAll(removeButton, items, child);
    }

    private void valueHasChanged() {
        if (parent instanceof CitiesFilter){
            //do nothing
        } else if (parent instanceof RegionFilter) {
            ((CitiesFilter)child).setLocality(items.getValue());
            //setChild(new CitiesFilter((Region) ((RegionFilter) parent).getMyLocality()));           //TODO: devo passare una regione non una string
        } else if (parent instanceof  Node) {

        }

    }

    private void removeSelf(){
        if (this.getParent() instanceof VBox){
            ((VBox) parent).getChildren().remove(this);
            ((AutoRemoving) parent).removeChoice(this);
        }else{
            ((HBox) parent).getChildren().remove(this);
            ((AutoRemoving) parent).removeChoice(this);
        }
    }

    public void setChild(VBox child){
        this.child = child;
    }

    public String getSelectedItem(){
        return items.getValue().toString();
    }
}
