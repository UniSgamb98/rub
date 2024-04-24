package com.example.rub.objects.mail;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class MailBean implements Serializable {
    private String title;
    private String contextText;
    private final ArrayList<File> attachments;

    public MailBean(){
        attachments = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void addAttachment(File attachment) {
        attachments.add(attachment);
    }
    public void removeAttachment(File attachment){
        attachments.remove(attachment);
    }
    public void setContextText(String contextText) {
        this.contextText = contextText;
    }

    public String getTitle() {
        return title;
    }
    public ArrayList<File> getAttachments() {
        return attachments;
    }
    public String getContextText() {
        return contextText;
    }
}
