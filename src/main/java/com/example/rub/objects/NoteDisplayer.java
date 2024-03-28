package com.example.rub.objects;

import com.example.rub.functionalities.NoteManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class NoteDisplayer extends VBox {
    Document document;
    ObservableList<DisplayableNote> notes;
    ListView<DisplayableNote> notesView;
    public NoteDisplayer(){
        this.setMinHeight(250);
        this.setMinWidth(500);
        notesView = new ListView<>();
        notes = FXCollections.observableArrayList();
        notesView.setItems(notes);
        this.getChildren().add(new Label("Note"));
        this.getChildren().add(notesView);
    }

    public void setDocument(String docPath) {
        try {
            document = new NoteManager().readXml(docPath);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("chiamata");
            for (int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element e = (Element) node;
                    notes.add(new DisplayableNote(e));
                }
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            System.out.println("no notes");
        }
    }
}
