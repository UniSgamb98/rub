package com.example.rub.objects.mail;

import javafx.scene.control.Button;

import java.io.IOException;

public class Shortcut extends Button {
    MailBean bean;
    String outlook;

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
                if (!bean.getAttachments().isEmpty()) {
                    new ProcessBuilder("cmd.exe", "/c", "start", "Outlook.exe", "/c", "ipm.note", "/m", destinatario + "?subject=" + bean.getTitle() + "&body=" + bean.getContextText(), "/a", bean.getAttachments().get(0).toString()).start();
                } else {
                    new ProcessBuilder("cmd.exe", "/c", "start", "Outlook.exe", "/c", "ipm.note", "/m", destinatario + "?subject=" + bean.getTitle() + "&body=" + bean.getContextText()).start();
                    System.out.println(outlook+ " /c "+ " ipm.note "+ "/m "+ destinatario + "?subject=" + bean.getTitle() + "&body=" + bean.getContextText());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
