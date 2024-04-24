package com.example.rub.objects.mail;

import com.example.rub.functionalities.GlobalContext;
import com.example.rub.functionalities.MyUtils;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class EmailSenderShortcut extends VBox {
    ArrayList<MailBean> drafts;
    String destinatario;

    public void refresh(){
        this.getChildren().clear();
        try{
            drafts = (ArrayList<MailBean>) MyUtils.read(GlobalContext.operator + "MailSettings");
        } catch (IOException | ClassNotFoundException e) {
            drafts = new ArrayList<>();
        }
        for (MailBean i : drafts){
            this.getChildren().add(new Shortcut(i, destinatario));
        }
    }

    public void setDestinatario(String destinatario){
        this.destinatario = destinatario;
        refresh();
    }
}
