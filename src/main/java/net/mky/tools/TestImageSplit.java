/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.tools;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
/**
 *
 * @author mkfs
 */
public class TestImageSplit extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // create a new Text shape
            Text messageText = new Text("Hello World! Lets learn JavaFX.");
             
            // stack page
            StackPane root = new StackPane();
             
            // add Text shape to Stack Pane
            root.getChildren().add(messageText);
             
            Scene scene = new Scene(root,400,400);
            
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
     
    public static void main(String[] args) {
        launch(args);
    }
}