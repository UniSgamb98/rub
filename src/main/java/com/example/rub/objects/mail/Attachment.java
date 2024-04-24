package com.example.rub.objects.mail;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.Serializable;

public class Attachment extends HBox implements Serializable {
    private final File file;
    private final MailDraft draft;

    /**
     * Classe utilizzata per gli allegati delle mail per Serializzare, per il momento, il path dei file da allegare
     * @param file il file da serializzare
     * @param draft il Parent (MailDraft) per comunicare al MailDraft di rimuovere this Attachment
     */
    public Attachment(File file, MailDraft draft){
        this.file = file;
        this.draft = draft;
        Label title = new Label(file.getName());
        Button removeButton = new Button("-");
        removeButton.setOnAction(event -> selfRemove());
        this.getChildren().addAll(removeButton, title);
    }

    /**
     * Usato per la rimuovere l'Attachment dal MailDraft
     * @return il file allegato
     */
    public File getFile() {
        return file;
    }

    public void selfRemove(){
        draft.removeAttachment(this);
    }
}
