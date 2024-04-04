package com.example.rub;

import com.example.rub.beans.Contatto;
import com.example.rub.enums.Interessamento;
import com.example.rub.enums.Operatori;
import com.example.rub.enums.TipoCliente;
import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.GlobalContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;

public class FirstPageController implements Initializable {
    @FXML
    public Button importButton;
    @FXML
    public Button exportButton;
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Importazione");
        alert.setContentText("Importazione avvenuta con successo");
        try{
            File file = new File("Importa.txt");
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            in = br.readLine();
            do{
                newEntryFromFile = new Contatto();
                int subStringStart = 0;
                int subStringEnd = in.indexOf(";");
                for(int i = 0; i <= 20; i++){
                    String subString = in.substring(subStringStart,subStringEnd);
                    if(subString.equals("%")) {
                        fillAttribute(i, newEntryFromFile, "");
                    } else {
                        fillAttribute(i, newEntryFromFile, subString);
                    }
                    subStringStart = subStringEnd+1;
                    subStringEnd = in.indexOf(";", subStringStart+1);
                }
                System.out.println("   Inserimento di " + newEntryFromFile);
                DBManager.saveEntry(newEntryFromFile, false);
            }while((in = br.readLine()) != null );
            br.close();
        } catch (IOException e){
            System.out.println("Errore nell'importazione da file txt");
            alert.setContentText("Importazione fallita:" + e.getCause().toString());
        }
            alert.showAndWait();
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
                bean.setEmailReferente(attribute);
                break;
            case 3:
                bean.setTelefono(attribute);
                break;
            case 4:
                bean.setPaese(attribute);
                break;
            case 5:
                bean.setRegione(attribute);
                break;
            case 6:
                bean.setCitta(attribute);
                break;
            case 7:
                bean.setIndirizzo(attribute);
                break;
            case 8:
                bean.setNumeroCivico(attribute);
                break;
            case 9:
                bean.setProvincia(attribute);
                break;
            case 10:
                bean.setCap(attribute);
                break;
            case 11:
                if(attribute.isEmpty()) {
                    bean.setInteressamento(Interessamento.BLANK);
                } else{
                    bean.setInteressamento(Interessamento.valueOf(attribute));
                }
                break;
            case 12:
                if (attribute.isEmpty()){
                    bean.setTipoCliente(TipoCliente.BLANK);
                } else {
                    bean.setTipoCliente(TipoCliente.valueOf(attribute));
                }
                break;
            case 13:
                bean.setPartitaIva(attribute);
                break;
            case 14:
                bean.setCodiceFiscale(attribute);
                break;
            case 15:
                bean.setTitolare(attribute);
                break;
            case 16:
                bean.setEmailGenereica(attribute);
                break;
            case 17:
                bean.setEmailCertificata(attribute);
                break;
            case 18:
                bean.setSitoWeb(attribute);
                break;
            case 19:
                if(!attribute.isBlank()){
                    System.out.println(UUID.fromString(attribute));
                    bean.setNoteId(UUID.fromString(attribute));
                }
                break;
            case 20:
                if (attribute.isEmpty()) {
                    bean.setOperator(Operatori.BLANK);
                } else {
                    bean.setOperator(Operatori.valueOf(attribute));
                }
                break;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        DBManager.init();
        if (GlobalContext.operator == Operatori.TOMMASO || GlobalContext.operator == Operatori.GAETANO || GlobalContext.operator == Operatori.VICTORIA){
            importButton.setVisible(true);
            importButton.setPrefSize(130.0,130.0);
            exportButton.setVisible(true);
            exportButton.setPrefSize(130.0,130.0);
        }
    }

    public void doShowCallsToMake() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("calls-page.fxml"));
            Parent root = loader.load();
            CallsPageController controller = loader.getController();
            controller.setCallList(DBManager.getCallList());
            Stage callStage = new Stage();
            callStage.setTitle("Elenco Chiamate");
            Scene scene = new Scene(root, 380, 285);
            callStage.setScene(scene);
            callStage.show();
        } catch (Exception e) {
            System.out.println("Errore durante la visualizzazione di callList con doShowCallList in FirstPageController");
        }
    }

    public void doShowReport() {
    }

    public void doExportForExcels() {
        DBManager.export();
    }
}