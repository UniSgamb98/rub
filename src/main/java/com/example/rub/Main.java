package com.example.rub;

import com.example.rub.functionalities.locations.City;
import com.example.rub.functionalities.locations.Region;
import com.example.rub.functionalities.locations.State;
import com.example.rub.objects.filter.location.RegionFilter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
       // Scene scene = new Scene(fxmlLoader.load(), 380, 285);
        Region veneto = new Region("Veneto");
        Region lombardia = new Region("Lombardia");
        Region lazio = new Region("Lazio");
        veneto.addAllCities(new City("Peschiera"), new City("Castelnuovo"),  new City("Pacengo"));
        State italia = new State("Italia");
        italia.addAllRegion(veneto, lombardia, lazio);
        Scene scene = new Scene(new RegionFilter(italia));
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}