package com.example.rub.objects.mail;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.Serializable;

public class Attachment extends HBox implements Serializable {
    private final File file;
    private final MailDraft draft;

    public Attachment(File file, MailDraft draft){
        this.file = file;
        this.draft = draft;
        Label title = new Label(file.getName());
        Button removeButton = new Button("-");
        removeButton.setOnAction(event -> selfRemove());
        this.getChildren().addAll(removeButton, title);
    }

    public File getFile() {
        return file;
    }

    public void selfRemove(){
        System.out.println("ha");
        draft.removeAttachment(this);
    }
}
