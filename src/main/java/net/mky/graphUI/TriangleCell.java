/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.graphUI;

/**
 *
 * @author Maneesh
 */
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import net.mky.tools.StylesForAll;



public class TriangleCell extends Cell {

    public TriangleCell( String id) {
        super( id);

        double width = 50;
        double height = 50;

        Polygon view = new Polygon( width / 2, 0, width, height, 0, height);
        ImageView iv=new ImageView(ImagePool.pool.get(id));
        iv.setFitWidth(100);
        iv.setFitHeight(80);
        view.setStroke(Color.RED);
        view.setFill(Color.TRANSPARENT);
        //setStyle(StylesForAll.transparentAlive);
        setView( iv);
         Label chatMessage = new Label(id);
        getChildren().add(chatMessage);
        StackPane.setAlignment(chatMessage, Pos.BOTTOM_CENTER);

    }

}