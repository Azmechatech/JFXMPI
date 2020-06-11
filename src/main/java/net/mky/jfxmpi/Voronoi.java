/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javafx.geometry.Point3D;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import net.mky.jfxmpi.TimeLineView.SpeechBox;
import net.mky.jfxmpi.TimeLineView.TimeLineStory;
import net.mky.jfxmpi.bookView.BW;
import net.mky.tools.StylesForAll;

// 2018 TheFlyingKeyboard and released under MIT License
// theflyingkeyboard.net
public class Voronoi extends Application {
    private final int width = 1400;
    private final int height = 800;
    private final double distanceOrder = 2.0d;
    private final int numberOfPoints = 15;
    private final Point3D[] points = new Point3D[numberOfPoints];
    private final Color[] colors = new Color[numberOfPoints];
    private final int pointSize = 5;
    private Canvas canvas;
    Color water=Color.BLUE;
    Color desert=Color.BEIGE;
    Color mountains=Color.CORAL;
    Color grassLand=Color.GREEN;
    Color land=Color.BROWN;
    Color road=Color.GREY;
    
    Color[] geographyColors={water,desert,mountains,grassLand,land};
    List<File> storyLine=new LinkedList<>();
    

    @Override
    public void init() {
        for (int i = 0; i < numberOfPoints; ++i) {
            points[i] = new Point3D(ThreadLocalRandom.current().nextInt(width), ThreadLocalRandom.current().nextInt(height),0);
            //colors[i] = Color.rgb(ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255));
           colors[i] = geographyColors[(int)((geographyColors.length-1)*Math.random())];
       
        }
    }

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(width, height);
        generateVoronoi();
        
        Pane root = new Pane();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        
        
        //Add the scenes
        Button loadChapters = new Button("Load");
        loadChapters.setStyle(StylesForAll.transparentAlive);
        loadChapters.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
        List<File> imageList = fileChooser.showOpenMultipleDialog(null);
            //Read all the file and conver to json
            //Render sppech boxes.
            try {
                int counter=0;
                for (File file : imageList) {
                    storyLine.add(file);
                    
                   
                    SpeechBox sb=TimeLineStory.previewHelper(file);
                    //sb.setPrefHeight(300);
                    //sb.setPrefWidth(300);
                    sb.setScaleX(.3);sb.setScaleY(.3);sb.setScaleZ(.3);
                    sb.setTranslateX( .3*points[counter].getX());
                    sb.setTranslateY( .3*points[counter].getY());
                    
                     Button name=new Button(file.getName());
                    name.setStyle(StylesForAll.buttonBoldBlack);
                    name.setTranslateX( points[counter].getX());
                    name.setTranslateY( points[counter].getY());
                    name.setOnAction(event2 -> {
                        Dialog dialog = new Dialog();
                        dialog.getDialogPane().setStyle("-fx-background-color:linear-gradient(to right, #fc5c7d, #6a82fb);");
                        dialog.getDialogPane().getStylesheets().add( BW.class.getResource("/book/bw.css").toExternalForm());
                        dialog.getDialogPane().setContent(new TimeLineStory(file,"User",650,500));
                        dialog.getDialogPane().getButtonTypes().addAll( ButtonType.CLOSE);
                        dialog.showAndWait();
                    });
                    
                    root.getChildren().add(name);
                    root.getChildren().add(sb);
                    counter++;
                }
            } catch (Exception ex) {
            }

        });
        
        
        root.getChildren().add(loadChapters);


        scene.getStylesheets().add( BW.class.getResource("/book/bw.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Voronoi Diagram");
        stage.show();

        File file = new File("Voronoi " + width + "X" + height + " " + numberOfPoints + " points.png");
        WritableImage writableImage = new WritableImage(width, height);
        canvas.snapshot(null, writableImage);
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        try {
            ImageIO.write(renderedImage, "png", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateVoronoi() {
        PixelWriter pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                
                pixelWriter.setColor(x, y, Math.random()> Math.random()?colors[findClosestPoint(new Point3D(x, y,0))].brighter():colors[findClosestPoint(new Point3D(x, y,0))].darker());
            
            }
        }

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.BLACK);

        for (int i = 0; i < numberOfPoints; ++i) {
            graphicsContext.fillOval(points[i].getX(), points[i].getY(), pointSize, pointSize);
        }
    }

    private int findClosestPoint(Point3D point) {
        int index = 0;
        double minDistance = distance(point, points[index]);
        double currentDistance;

        for (int i = 1; i < numberOfPoints; ++i) {
            currentDistance = distance(point, points[i]);

            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                index = i;
            }
        }

        return index;
    }

    private double distance(Point3D pointA, Point3D pointB) {
        if (distanceOrder == 1.0d) {
            return Math.abs(pointA.getX() - pointB.getX()) + Math.abs(pointA.getY() - pointB.getY());
        }

        if (distanceOrder == 2.0d) {
            return Math.sqrt((pointA.getX() - pointB.getX()) * (pointA.getX() - pointB.getX())
                    + (pointA.getY() - pointB.getY()) * (pointA.getY() - pointB.getY()));
        }

        return Math.pow(Math.pow(Math.abs(pointA.getX() - pointB.getX()), distanceOrder) +
                Math.pow(Math.abs(pointA.getY() - pointB.getY()), distanceOrder), (1.0d / distanceOrder));
    }
    
        public static void main(String[] args) {
        Application.launch(Voronoi.class, args);
    }
}
