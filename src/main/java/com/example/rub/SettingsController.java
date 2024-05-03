package com.example.rub;

import com.example.rub.objects.settings.Options;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.LinkedList;
import java.util.Objects;

public class SettingsController {
    @FXML
    public VBox operatorBox;

    public void addOperator() {
        Options temp = new Options("Manager");
        operatorBox.getChildren().add(temp);
    }

    public void doGoBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("firstPage.fxml")));       //cambio scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.out.println("Errore durante la transizione in firstPage con doLogin");
        }
    }

    public LinkedList<String> getOperators(){
        LinkedList<String> ret = new LinkedList<>();
        for (Node i : operatorBox.getChildren()){
            ret.add(((Options) i).getField());
        }
        return ret;
    }

    public void saveSettings() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("Settings");
        doc.appendChild(rootElement);




    }
}
