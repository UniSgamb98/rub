package com.example.rub;

import com.example.rub.enums.LogType;
import com.example.rub.enums.Operatori;
import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.GlobalContext;
import com.example.rub.functionalities.MyUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;

public class FirstPageController implements Initializable {
    @FXML
    public Button importButton;
    @FXML
    public Button exportButton;
    @FXML
    public Button notesButton;
    @FXML
    public Button settingsButton;
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void switchToNewEntry (ActionEvent event) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("new-entry.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
            System.out.println("Errore durante la transizione in new-entry con switchToNewEntry in FirstPage");
        }
    }
    public void switchToSearchEntry(ActionEvent event){
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("search-entry.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
            System.out.println("Errore durante la transizione in search-entry con switchToSearchEntry in FirstPage");
        }
    }

    public void doImportFromExcels() {
        System.out.println("Importazione contatti da file...");
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("loading.fxml")));
        Stage stage1 = new Stage();
        LoadingController controller;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Importazione");
        alert.setContentText("Importazione avvenuta con successo");
        try {
            Scene scene1 = new Scene(loader.load());
            controller = loader.getController();
            stage1.setScene(scene1);
            stage1.show();
            try {
                controller.addFailure(DBManager.importa("Importa.txt"));
            } catch (IOException e) {
                MyUtils.log(LogType.ERROR);
                MyUtils.log(LogType.MESSAGE, e);
                System.out.println("Errore nell'importazione da file txt");
                alert.setContentText("Importazione fallita:" + e.getCause().toString());
            }
        } catch (IOException e) {
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
        }
        DBManager.saveData();
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        if (DBManager.isNull()) {
            DBManager.init();
            try {
                GlobalContext.notProgrammedCalls.removeIf(i -> DBManager.retriveEntry(i).getProssimaChiamata() != null);
                if (!GlobalContext.notProgrammedCalls.isEmpty()) {
                    FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("login-reminder.fxml")));
                    Parent root = loader.load();
                    LogoutController controller = loader.getController();
                    controller.setProperties(scene);
                    Scene newScene = new Scene(root);
                    Stage newStage = new Stage();
                    newStage.getIcons().add(new Image("AppIcon.png"));
                    newStage.setTitle("Promemoria amichevole");

                    newStage.setScene(newScene);
                    newStage.show();
                } else {
                    MyUtils.log(LogType.MESSAGE, "Non ha promemoria dall'ultimo log out");
                }
            } catch (IOException e) {
                System.out.println("NIENTE DA RICORDARE");
                MyUtils.log(LogType.MESSAGE, "Non ha promemoria dall'ultimo log out");
            }
        }
        if (GlobalContext.operator == Operatori.TOMMASO){
            importButton.setVisible(true);
            importButton.setPrefSize(130.0,130.0);
            importButton.setMinHeight(Region.USE_COMPUTED_SIZE);
            exportButton.setVisible(true);
            exportButton.setPrefSize(130.0,130.0);
            exportButton.setMinHeight(Region.USE_COMPUTED_SIZE);
            notesButton.setPrefSize(130.0, 130.0);
            notesButton.setVisible(true);
            notesButton.setMinHeight(Region.USE_COMPUTED_SIZE);
        /*    settingsButton.setPrefSize(130.0, 130.0);
            settingsButton.setVisible(true);
            settingsButton.setMinHeight(Region.USE_COMPUTED_SIZE);*/
        }
    }

    public void doShowCallsToMake(ActionEvent event) {
        boolean alreadyOpen = false;
        for (Window i : Window.getWindows()){
            if (Objects.equals(i.getScene().getRoot().getId(), "calls-page")){
                i.requestFocus();
                alreadyOpen = true;
            }
        }
        if(!alreadyOpen) {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("calls-page.fxml"));
                Parent root = loader.load();
                CallsPageController controller = loader.getController();
                controller.setProperties((Stage) ((Node) event.getSource()).getScene().getWindow());
                Stage callStage = new Stage();
                callStage.setTitle("Elenco Chiamate");
                Scene scene = new Scene(root);
                callStage.setScene(scene);
                callStage.getIcons().add(new Image("AppIcon.png"));
                callStage.show();
            } catch (Exception e) {
                MyUtils.log(LogType.ERROR);
                MyUtils.log(LogType.MESSAGE, e);
                System.out.println("Errore durante la visualizzazione di callList con doShowCallList in FirstPageController");
            }
        }
    }

    public void doShowReport(ActionEvent event) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("report.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
            System.out.println("Errore durante la transizione in showReport con switchToNewEntry in FirstPage");
        }
    }

    public void doExportForExcels() {
        DBManager.export(false);
    }

    public void doNumberNotes() {
        DBManager.notes();
    }

    public void openSettings(ActionEvent event) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("settings.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            MyUtils.log(LogType.ERROR);
            MyUtils.log(LogType.MESSAGE, e);
            System.out.println("Errore durante la transizione in settings con openSettings in FirstPage");
        }
    }
}