package com.example.rub.objects.note;

import com.example.rub.Main;
import com.example.rub.ModifyNoteController;
import com.example.rub.functionalities.DBManager;
import com.example.rub.functionalities.NoteManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.UUID;

public class NoteDisplayer extends VBox {
    Document document;
    ObservableList<DisplayableNote> notes;
    ListView<DisplayableNote> notesView;
    String path;
    UUID entryID;

    public NoteDisplayer(){
        notesView = new ListView<>();
        notes = FXCollections.observableArrayList();
        notesView.setItems(notes);
        this.getChildren().add(new Label("Storico"));
        this.getChildren().add(notesView);
        this.widthProperty().addListener((obs, oldVal, newVal) -> setNoteWidth(this.getWidth()));
    }

    public void setDocument(UUID entryID) {
        this.entryID = entryID;
        path = "" + DBManager.retriveEntry(entryID).getNoteId();
        manageNote();
    }
    public void refresh(){
        notes.clear();
        manageNote();
    }
    private void manageNote(){
        notes.clear();
        try {
            document = new NoteManager().readXml(path);
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("chiamata");
            for (int i = 0; i < nodeList.getLength(); i++){
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element e = (Element) node;
                    if (!Boolean.parseBoolean(e.getAttribute("cancelled"))) {
                        notes.add(0, new DisplayableNote(this, e));
                    }
                }
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            System.out.println("no notes");
        }
        setNoteWidth(this.getWidth());
    }

    public void openNoteModifications(Element element){
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("modifyNote.fxml"));
        try {
            Parent root = loader.load();     //cambio scena
            ModifyNoteController controller = loader.getController();
            controller.setDocument(path, element, entryID);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setTitle("Modifica la nota");
            stage.getIcons().add(new Image("AppIcon.png"));
            stage.setScene(scene);
            stage.showAndWait();
            this.fireEvent(new ActionEvent());
        } catch (Exception e){
            System.out.println("Orrore!");
        }
    }

    public void setNoteWidth(double width) {
        for (DisplayableNote i : notes) {
            i.setWrapping(width);
        }
    }

    public void clear() {notes.clear();}
}
