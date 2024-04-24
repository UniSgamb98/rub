package com.example.rub;

import com.example.rub.functionalities.GlobalContext;
import com.example.rub.functionalities.MyUtils;
import com.example.rub.objects.mail.MailBean;
import com.example.rub.objects.mail.MailPreference;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MailPreferencesController {
    @FXML
    public MailPreference preferences;
    private EntryDetailsPageController previousController;
    private Scene oldScene;
    public void goBack(ActionEvent event) {
        try {
            preferences.saveDrafts();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(oldScene);
            stage.show();
            previousController.refresh();
        } catch (Exception e) {
            System.out.println("Errore durante la transizione in firstPage con doLogin");
        }
    }

    public void loadPreferences(EntryDetailsPageController controller){
        previousController = controller;
        ArrayList<MailBean> drafts;
        try {
            drafts = (ArrayList<MailBean>) MyUtils.read(GlobalContext.operator + "MailSettings");
        } catch (IOException | ClassNotFoundException e) {
            drafts = new ArrayList<>();
        }
        preferences.loadDrafts(drafts);
    }

    public void setProperties(Scene oldScene){
        this.oldScene = oldScene;
    }
}
