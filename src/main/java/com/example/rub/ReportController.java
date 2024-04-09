package com.example.rub;

import com.example.rub.enums.Operatori;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Objects;

public class ReportController {

    @FXML
    public ChoiceBox<Operatori> operator;
    @FXML
    public LineChart chart;
    @FXML
    public DatePicker startDate;
    @FXML
    public DatePicker stopDate;
    @FXML
    public CheckBox includeMessages;
    @FXML
    public TextField durata;
    @FXML
    public ListView History;
    @FXML
    public ListView contacted;

    public void doGoBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("firstPage.fxml")));       //cambio scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) { System.out.println("Errore durante la transizione in firstPage con doGoBack in ReportController");   }
    }

    public void doShowReport(ActionEvent event) {
    }
}
