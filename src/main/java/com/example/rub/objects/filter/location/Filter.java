package com.example.rub.objects.filter.location;

import com.example.rub.functionalities.locations.Locality;
import javafx.scene.layout.VBox;

public class Filter extends VBox implements AutoRemoving{
    @Override
    public void removeChoice(Choice choice) {}
    @Override
    public void setAssigned(Locality locality) {}
    public Locality getAssigned(){return null;}
    protected void becomeVisible(){}
}
