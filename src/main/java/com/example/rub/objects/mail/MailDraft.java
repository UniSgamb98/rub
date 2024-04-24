package com.example.rub.objects.mail;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MailDraft extends HBox{
    @FXML
    public TextField title;
    @FXML
    public VBox attachmentList;
    @FXML
    public Button removeButton;
    @FXML
    public Button addAttachmentButton;
    @FXML
    public TextArea contextText;
    private MailBean bean;
    private Parent view;
    private MailPreference controller;

    public void addAttachment(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona Allegato");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tutti i files", "*.*"));
        Stage stage = new Stage();
        File selectedFile = fileChooser.showOpenDialog(stage);
        bean.addAttachment(selectedFile);
        Attachment attachment = new Attachment(selectedFile, this);
        attachmentList.getChildren().add(attachment);
    }
    public void removeAttachment(Attachment attachment){
        attachmentList.getChildren().remove(attachment);
        bean.removeAttachment(attachment.getFile());
    }
    public void selfRemove() {
        controller.removeDraft(bean, view);
    }

    public void setProperties(MailBean bean, Parent view, MailPreference controller) {
        this.bean = bean;
        this.view = view;
        this.controller = controller;
        title.setText(bean.getTitle());
        contextText.setText(bean.getContextText());
    }

    public MailBean getBean() {
        return bean;
    }
}
