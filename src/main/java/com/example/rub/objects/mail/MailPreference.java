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
    ArrayList<MailBean> beansList;
    public MailPreference(){
        addDraftButton = new Button("Aggiungi Preset");
        addDraftButton.setOnAction(event -> addMailDraft(new MailBean()));
        this.getChildren().add(addDraftButton);
    }

    public void addMailDraft(MailBean bean){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mailDraft.fxml"));
            Parent node = loader.load();
            MailDraft mailDraft = loader.getController();
            mailDraft.setProperties(bean, node, this);
            beansList.add(bean);
            this.getChildren().add(node);
        } catch (Exception ignored)   {}
    }

    public void removeDraft(MailBean bean, Parent draft) {
        beansList.remove(bean);
        this.getChildren().remove(draft);
    }

    public void loadDrafts (ArrayList<MailBean> drafts){
        beansList = drafts;
        for (MailBean i : beansList){
            addMailDraft(i);
        }
    }

    public void saveDrafts(){
        MyUtils.write(beansList, GlobalContext.operator + "Mail");
    }
}
