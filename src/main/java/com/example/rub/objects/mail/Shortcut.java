package com.example.rub.objects.mail;

import javafx.scene.control.Button;

import java.io.IOException;

public class Shortcut extends Button {
    MailBean bean;

    /**
     * Pulsante che crea un processo Windows per aprire una bozza in OUTLOOK precompilandolo con i dati salvati in bean e inviando al destinatario.
     * @param bean Il modello con Oggetto, Contenuto e allegati.
     * @param destinatario Il destinatario della mail.
     */
    public Shortcut(MailBean bean, String destinatario){
        this.bean = bean;
        this.setText(bean.getTitle());
        this.setOnAction(event -> {
            try {
                new ProcessBuilder("C:\\Program Files (x86)\\Microsoft Office\\root\\Office16\\OUTLOOK.EXE", "/c","ipm.note", "/m", destinatario + "?subject="+ bean.getTitle() + "&body=" + bean.getContextText(), "/a", bean.getAttachments().get(0).toString()).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
