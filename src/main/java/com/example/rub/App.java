package com.example.rub;

import com.example.rub.enums.LogType;
import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.GlobalContext;
import com.example.rub.functionalities.MyUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 380, 285);
        stage.setTitle("Orodent");
        stage.getIcons().add(new Image("AppIcon.png"));
        stage.setScene(scene);
        stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            if(!GlobalContext.notProgrammedCalls.isEmpty()) {
                MyUtils.log(LogType.WINDOW, "logout");
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
                    MyUtils.log(LogType.ERROR);
                    MyUtils.log(LogType.MESSAGE, e);
                    System.out.println("errore con logout Main.java");
                }
            } else {
                MyUtils.log(LogType.EXIT);
                DBManager.export(true);
            }
        });
        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            double direction;
            if (stage.xProperty().get() < 200)    direction = 0;
            else if (stage.xProperty().get() > 900)     direction = 1;
            else direction = 0.5;
            stage.setX(stage.getX()+((oldVal.doubleValue() - newVal.doubleValue())*direction));
        });
        stage.sceneProperty().addListener((obs, oldVal, newVal) -> MyUtils.log(LogType.WINDOW, newVal.getRoot().getId()));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}