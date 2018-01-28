/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.tools;

import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Translate;

/**
 *
 * @author Maneesh
 */
public class BruteForceRenderer extends StackPane {

    double TILE_WIDTH = 100;
    double TILE_HEIGHT = 50;
    double tilesWidth = 10;
    Group tilesGroup = new Group();

    public BruteForceRenderer() {


        int numberTiles = (int) Math.pow(tilesWidth, 2);

        for (int i = 0; i < numberTiles - 1; i++) {
             Polygon polygon = new Polygon();
            int x = (int) (i / tilesWidth);
            int y = (int) (i % tilesWidth);
            polygon.getPoints().addAll(new Double[]{
                TILE_WIDTH / 2, 0.0, TILE_WIDTH, TILE_HEIGHT / 2, TILE_WIDTH / 2, TILE_HEIGHT, 0.0, TILE_HEIGHT / 2
            });

            //Creating the translation transformation 
            Translate translate = new Translate();
            //Setting the X,Y,Z coordinates to apply the translation 
            translate.setX(TILE_WIDTH / 2 * (x - y));
            translate.setY(TILE_HEIGHT / 2 * (x + y));
            polygon.getTransforms().addAll(translate);
            polygon.setStyle("-fx-background-color: DAE6F3;");
            polygon.setEffect(new DropShadow(10, 5, 5, Color.GRAY));
            tilesGroup.getChildren().add(polygon);
        }
        
    }

}
