package com.example.rub;

import com.example.rub.beans.Contatto;
import com.example.rub.enums.Interessamento;
import com.example.rub.enums.TipoCliente;
import com.example.rub.functionalities.DBManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class FirstPageController implements Initializable {
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
        } catch (Exception e) { System.out.println("Errore durante la transizione in new-entry con switchToNewEntry in FirstPage"); }
    }
    public void switchToSearchEntry(ActionEvent event){
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("search-entry.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) { System.out.println("Errore durante la transizione in search-entry con switchToSearchEntry in FirstPage"); }
    }

    public void doImportFromExcels() {
        System.out.println("Importazione contatti da file...");
        String in;
        Contatto newEntryFromFile;
        try{
            File file = new File("Importa.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            in = br.readLine();
            do{
                newEntryFromFile = new Contatto();
                int subStringStart = 0;
                int subStringEnd = in.indexOf(";");
                for(int i = 0; i < 8; i++){
                    String subString = in.substring(subStringStart,subStringEnd);
                    fillAttribute(i,newEntryFromFile, subString);
                    subStringStart = subStringEnd+1;
                    subStringEnd = in.indexOf(";", subStringStart+1);
                }
                System.out.println("   Inserimento di " + newEntryFromFile);
                DBManager.saveEntry(newEntryFromFile);

            }while((in = br.readLine()) != null );
            br.close();
        } catch (IOException e){
            System.out.println("Errore nell'importazione da file txt");
        }
    }
    private void fillAttribute (int index, Contatto bean, String attribute){
        switch (index){
            case 0:
                bean.setRagioneSociale(attribute);
                break;
            case 1:
                bean.setPersonaRiferimento(attribute);
                break;
            case 2:
                bean.setTelefono(attribute);
                break;
            case 3:
                bean.setEmail(attribute);
                break;
            case 4:
                bean.setInteressamento(Interessamento.valueOf(attribute));
                break;
            case 5:
                bean.setTipoCliente(TipoCliente.valueOf(attribute));
                break;
            case 6:
                bean.setPaese(attribute);
                break;
            case 7:
                bean.setCitta(attribute);
                break;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        DBManager.init();
    }
}