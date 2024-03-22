package com.example.rub;

import com.example.rub.functionalities.locations.City;
import com.example.rub.functionalities.locations.LocationManager;
import com.example.rub.functionalities.locations.Region;
import com.example.rub.functionalities.locations.State;
import com.example.rub.objects.filter.location.RegionFilter;
import com.example.rub.objects.filter.location.StateFilter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        City peschiera = new City("Peschiera");
        City castelnuovo = new City("Castelnuovo");
        City pacengo = new City("Pacengo");
        Region veneto = new Region("Veneto");
        Region lombardia = new Region("Lombardia");
        Region lazio = new Region("Lazio");
        State italia = new State("Italia");
        State uk = new State("Inghilterra");
        LocationManager manager = new LocationManager("Manager");
        veneto.addAllCities(peschiera, castelnuovo, pacengo);
        italia.addAllRegion(veneto, lombardia, lazio);
        manager.addAllStates(italia, uk);
        Scene scene = new Scene(new StateFilter(manager),600,600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}