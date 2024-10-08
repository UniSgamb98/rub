package com.example.rub;

import com.example.rub.enums.LogType;
import com.example.rub.enums.Operatori;
import com.example.rub.functionalities.GlobalContext;
import com.example.rub.functionalities.MyUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private ChoiceBox<Operatori> operator;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        operator.getItems().addAll(Operatori.getOperators());
        operator.getItems().remove(Operatori.SANTOLO);
        operator.getItems().remove(Operatori.HUGO);
        //operator.setValue(Operatori.TOMMASO);
    }

    public void doLogin(ActionEvent event) {
        if (Operatori.getOperators().contains(operator.getValue())) {
            GlobalContext.operator = operator.getValue();
            try {
                MyUtils.log(LogType.ACCESS);
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("firstPage.fxml")));       //cambio scena
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                MyUtils.log(LogType.ERROR);
                MyUtils.log(LogType.MESSAGE, e);
                System.out.println("Errore durante la transizione in firstPage con doLogin");
            }
        }
    }
}
