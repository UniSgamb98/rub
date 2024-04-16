package com.example.rub.objects;

import com.example.rub.beans.Contatto;
import com.example.rub.functionalities.DBManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.UUID;

public class DisplayableEntry extends HBox {
    private final Contatto entry;

    public DisplayableEntry(UUID entryRequested){
        entry = DBManager.retriveEntry(entryRequested);
        Color backgroundPaint = null;
        switch ((int) entry.getCoinvolgimento()){
            case 3:
                backgroundPaint = Color.YELLOW;
                break;
            case 4:
                backgroundPaint = Color.AQUA;
                break;
            case 5:
                backgroundPaint = Color.CHARTREUSE;
                break;

        }
        this.getChildren().add(getIcon());
        this.setBackground(new Background(new BackgroundFill(backgroundPaint, CornerRadii.EMPTY, Insets.EMPTY)));
        this.getChildren().addAll(new Label(entry.getRagioneSociale()), new Label(" in "));
        this.getChildren().add(new Label(entry.getPaese()));
    }
    public Contatto getEntry(){
        return DBManager.retriveEntry(entry.getId());
    }
    @Override
    public String toString(){
        return entry.getRagioneSociale();
    }
    private StackPane getIcon(){
        StackPane icon = new StackPane();
        icon.setAlignment(Pos.CENTER);
        switch (entry.getInteressamento()){
            case INFO:
                Circle c = new Circle(8, Color.CADETBLUE);
                Text t = new Text("i");
                t.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, 14));
                icon.getChildren().addAll(c, t);
                break;
            case LISTINO:
                Circle c3 = new Circle(8, Color.PAPAYAWHIP);
                Text t3 = new Text("Li");
                t3.setFont(Font.font("Verdana", FontWeight.LIGHT, FontPosture.ITALIC, 14));
                icon.getChildren().addAll(c3, t3);
                break;
            case CAMPIONE:
                Circle c2 = new Circle(8, Color.CORAL);
                Text t2 = new Text("C");
                t2.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
                icon.getChildren().addAll(c2, t2);
                break;
            case CLIENTE:
                Polyline polyline = new Polyline();
                polyline.getPoints().addAll(0.0, 8.0,
                        2.0, 11.0,
                        8.0, 0.0);
                polyline.setStroke(Color.DARKGREEN);
                polyline.setFill(Color.ALICEBLUE);
                icon.getChildren().addAll(polyline);
                break;
        }
        return icon;
    }

}
