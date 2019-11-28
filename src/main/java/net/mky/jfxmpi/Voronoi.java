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
import java.util.concurrent.ThreadLocalRandom;
import javafx.geometry.Point3D;

// 2018 TheFlyingKeyboard and released under MIT License
// theflyingkeyboard.net
public class Voronoi extends Application {
    private final int width = 1024;
    private final int height = 768;
    private final double distanceOrder = 2.0d;
    private final int numberOfPoints = 50;
    private final Point3D[] points = new Point3D[numberOfPoints];
    private final Color[] colors = new Color[numberOfPoints];
    private final int pointSize = 10;
    private Canvas canvas;

    @Override
    public void init() {
        for (int i = 0; i < numberOfPoints; ++i) {
            points[i] = new Point3D(ThreadLocalRandom.current().nextInt(width), ThreadLocalRandom.current().nextInt(height),0);
            colors[i] = Color.rgb(ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255));
        }
    }

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(width, height);
        generateVoronoi();

        Pane root = new Pane();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);

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
                pixelWriter.setColor(x, y, colors[findClosestPoint(new Point3D(x, y,0))]);
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
