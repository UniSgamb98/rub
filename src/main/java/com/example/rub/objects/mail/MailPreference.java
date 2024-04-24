package com.example.rub.objects.mail;

import com.example.rub.functionalities.GlobalContext;
import com.example.rub.functionalities.MyUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class MailPreference extends VBox {
    Button addDraftButton;
    ArrayList<MailDraft> mailDrafts;
    public MailPreference(){
        addDraftButton = new Button("Aggiungi Preset");
        addDraftButton.setOnAction(event -> addMailDraft(new MailBean()));
        mailDrafts = new ArrayList<>();
        this.getChildren().add(addDraftButton);
    }

    public void addMailDraft(MailBean bean){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mailDraft.fxml"));
            Parent node = loader.load();
            MailDraft mailDraft = loader.getController();
            mailDraft.setProperties(bean, node, this);
            mailDrafts.add(mailDraft);
            this.getChildren().add(node);
        } catch (Exception e)   {e.printStackTrace();}
    }

    public void removeDraft(MailDraft draft, Parent view) {
        mailDrafts.remove(draft);
        this.getChildren().remove(view);
    }

    public void loadDrafts (ArrayList<MailBean> drafts){
        for (MailBean i : drafts){
            addMailDraft(i);
        }
    }

    public void saveDrafts(){
        ArrayList<MailBean> beansList = new ArrayList<>();
        for (MailDraft i : mailDrafts){
            beansList.add(i.getBean());
        }
        MyUtils.write(beansList, GlobalContext.operator + "MailSettings");
    }
}
