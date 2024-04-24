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
        controller.removeDraft(this, view);
    }

    /**
     * Fornisce il MailDraft con tutti i riferimenti per gestire logica e view. Inoltre carica i dati dal forniti dal bean
     * @param bean  Il modello da salvare
     * @param view  La vista
     * @param controller Il controller per chiamare le funzioni del Parent (MailPreferences) principalmente per add & remove di mailDrafts
     */
    public void setProperties(MailBean bean, Parent view, MailPreference controller) {
        this.bean = bean;
        this.view = view;
        this.controller = controller;
        title.setText(bean.getTitle());
        contextText.setText(bean.getContextText());
        for (File i : getBean().getAttachments()){
            Attachment attachment = new Attachment(i, this);
            attachmentList.getChildren().add(attachment);
        }
    }

    public MailBean getBean() {
        return bean;
    }

    /**
     * Salva nel modello l'input dell'utente a parte gli attachments che sono salvati in modello nel momento di creazione.
     */
    public void saveDraftChanges() {
        bean.setContextText(contextText.getText());
        bean.setTitle(title.getText());
    }
}
