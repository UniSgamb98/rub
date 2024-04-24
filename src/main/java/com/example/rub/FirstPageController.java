package com.example.rub;

import com.example.rub.beans.Contatto;
import com.example.rub.enums.Interessamento.InteressamentoStatus;
import com.example.rub.enums.Operatori;
import com.example.rub.enums.TipoCliente;
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

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.UUID;

public class FirstPageController implements Initializable {
    @FXML
    public Button importButton;
    @FXML
    public Button exportButton;
    public Button notesButton;
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
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("loading.fxml")));
        Stage stage1 = new Stage();
        LoadingController controller = null;
        try {
            Scene scene1 = new Scene(loader.load());
            controller = loader.getController();
            stage1.setScene(scene1);
            stage1.show();
        } catch (Exception ignored) {}

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
                try {
                    int subStringStart = 0;
                    int subStringEnd = in.indexOf(";");
                    String subString;
                    for (int i = 0; i <= 24; i++) {
                        subString = in.substring(subStringStart, subStringEnd);
                        fillAttribute(i, newEntryFromFile, subString);

                        subStringStart = subStringEnd + 1;
                        subStringEnd = in.indexOf(";", subStringStart);
                    }
                    System.out.println("   Inserimento di " + newEntryFromFile);
                    assert controller != null;
                    controller.increment();
                    DBManager.saveEntry(newEntryFromFile, true);
                }catch (Exception e)    {
                    assert controller != null;
                    controller.addFailure(newEntryFromFile);}
            }while((in = br.readLine()) != null );
            br.close();
        } catch (IOException e){
            System.out.println("Errore nell'importazione da file txt");
            alert.setContentText("Importazione fallita:" + e.getCause().toString());
        }
        DBManager.saveData();
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
                    bean.setInteressamento(InteressamentoStatus.BLANK);
                } else{
                    bean.setInteressamento(InteressamentoStatus.valueOf(attribute));
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
            case 21:
                bean.setVolteContattati(Integer.parseInt(attribute));
                break;
            case 22:
                LocalDate t;
                try{
                    t = LocalDate.parse(attribute);
                } catch (Exception e){
                    t = null;
                }
                bean.setUltimaChiamata(t);
                break;
            case 23:
                LocalDate j;
                try{
                    j = LocalDate.parse(attribute);
                }catch (Exception e){
                    j = null;
                }
                bean.setProssimaChiamata(j);
                break;
            case 24:
                double c;
                if (attribute.isEmpty()){
                    c = 0;
                }else {
                    c = Double.parseDouble(attribute);
                }
                bean.setCoinvolgimento(c);

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        DBManager.init();
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
        }
        try {
            GlobalContext.notProgrammedCalls = (LinkedList<UUID>) MyUtils.read(GlobalContext.operator.name());
            GlobalContext.notProgrammedCalls.removeIf(i -> DBManager.retriveEntry(i).getProssimaChiamata() != null);
            if (!GlobalContext.notProgrammedCalls.isEmpty()){
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("login-reminder.fxml")));
                Parent root = loader.load();
                LogoutController controller = loader.getController();
                controller.setProperties(scene);
                Scene newScene = new Scene(root);
                Stage newStage = new Stage();
                newStage.setScene(newScene);
                newStage.show();
            }
        }  catch (IOException | ClassNotFoundException e) {
            System.out.println("NIENTE DA RICORDARE");
        }
    }

    public void doShowCallsToMake() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("calls-page.fxml"));
            Parent root = loader.load();
            Stage callStage = new Stage();
            callStage.setTitle("Elenco Chiamate");
            Scene scene = new Scene(root);
            callStage.setScene(scene);
            callStage.getIcons().add(new Image("AppIcon.png"));
            callStage.show();
        } catch (Exception e) {
            System.out.println("Errore durante la visualizzazione di callList con doShowCallList in FirstPageController");
        }
    }

    public void doShowReport(ActionEvent event) {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("report.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) { System.out.println("Errore durante la transizione in new-entry con switchToNewEntry in FirstPage"); }
    }

    public void doExportForExcels() {
        DBManager.export();
    }

    public void doNumberNotes() {
        DBManager.notes();
    }
}