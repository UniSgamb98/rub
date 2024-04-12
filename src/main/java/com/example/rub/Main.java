package com.example.rub;

import com.example.rub.functionalities.GlobalContext;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 380, 285);
        stage.setTitle("Orodent");
        stage.getIcons().add(new Image("AppIcon.png"));
        stage.setScene(scene);
        stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            if(!GlobalContext.notProgrammedCalls.isEmpty()) {
                try {
                    Stage theStage = (Stage) event.getSource();
                    Scene oldScene = theStage.getScene();
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("logout.fxml")));
                    Parent root = loader.load();
                    Scene newScene = new Scene(root);
                    LogoutController controller = loader.getController();
                    controller.setProperties(oldScene);
                    theStage.setScene(newScene);
                    theStage.show();
                    event.consume();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}