package com.example.rub.objects.note;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.w3c.dom.Element;

public class DisplayableNote extends GridPane {
    Text content;
    Element element;
    public DisplayableNote(NoteDisplayer parent, Element element){
        this.element = element;
        Label operator = new Label("Operatore: " + element.getAttribute("operatore"));
        Label duration = new Label("Durata: " + element.getAttribute("durata"));
        content = new Text(element.getTextContent());
        Label day = new Label("Giorno: " + element.getAttribute("data"));
        GridPane.setColumnSpan(content,3);
        this.getChildren().addAll(operator, duration, content, day);
        GridPane.setConstraints(operator,1,0);
        GridPane.setConstraints(duration,2,0);
        GridPane.setConstraints(content,0,1);
        GridPane.setConstraints(day,0,0);
        this.setOnMouseClicked(event -> parent.openNoteModifications(element));
    }

    public void setWrapping(double wrapping){
         content.setWrappingWidth(wrapping - 16);   //16 vanno tolti perchè sono la differenza di pixel tra NoteDisplayer.width e DisplayableNote.width
    }
}
