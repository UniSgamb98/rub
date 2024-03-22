package com.example.rub.objects.filter.location;

import com.example.rub.functionalities.locations.Locality;
import javafx.scene.layout.VBox;

public class Filter extends VBox implements AutoRemoving{

    @Override
    public void removeChoice(Choice choice) {}
    @Override
    public void setAssigned(Locality locality) {}
    protected void setVisibility(boolean visibility){}      //TODO Questo forse è meglio metterlo sempre true boH
}
