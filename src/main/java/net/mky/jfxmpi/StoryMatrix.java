/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import net.mky.tools.StylesForAll;

/**
 *
 * @author mkfs
 */
public class StoryMatrix extends StackPane {

    Canvas canvas = new Canvas(400, 400);
    List<Point2D> points = new ArrayList<Point2D>();

    public StoryMatrix(List<CharacterPane> charactersArray) {
        for(int i=0;i<charactersArray.size();i++){
            for(int j=0;j<charactersArray.size();j++){
                double x=i*50;
                double y=j*50;
                Rectangle rect1 = new Rectangle(x, y, x+50, y+50);
                //rect1.setStyle(StylesForAll.transparentAlive);
                rect1.setFill(Color.TRANSPARENT);
            rect1.setStroke(Color.DARKORCHID);
                Label CharactersLabel = new Label(charactersArray.get(i).name );
                CharactersLabel.setTranslateX(x);
                CharactersLabel.setTranslateY(y);
            
            getChildren().addAll(rect1, CharactersLabel);
            }
        }
       
    }

    public StoryMatrix() {

       //Filled rectangle
            Rectangle rect1 = new Rectangle(10, 10, 200, 200);
            rect1.setFill(Color.BLUE);

            //Transparent rectangle with Stroke
            Rectangle rect2 = new Rectangle(60, 60, 200, 200);
            rect2.setFill(Color.TRANSPARENT);
            rect2.setStroke(Color.RED);
            rect2.setStrokeWidth(10);

            //Rectangle with Stroke, no Fill color specified
            Rectangle rect3 = new Rectangle(110, 110, 200, 200);
            rect3.setStroke(Color.GREEN);
            rect3.setStrokeWidth(10);
            getChildren().addAll(rect1, rect2, rect3);
    }

    public List<Point2D> getFullPath() {
        return points;
    }

    private void initDraw(GraphicsContext gc) {
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.setFill(Color.LIGHTGRAY);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);

        gc.fill();
        gc.strokeRect(
                0, //x of the upper left corner
                0, //y of the upper left corner
                canvasWidth, //width of the rectangle
                canvasHeight);  //height of the rectangle

        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);

    }

    private void initDraw(GraphicsContext gc, Image bimg) {
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();
        gc.setFill(Color.LIGHTGRAY);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.fill();
        gc.strokeRect(
                0, //x of the upper left corner
                0, //y of the upper left corner
                canvasWidth, //width of the rectangle
                canvasHeight);  //height of the rectangle
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);
        gc.drawImage(bimg, 0, 0);
    }
}