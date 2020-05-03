/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author mkfs
 */
public class MobileView extends Application {

    private Pane root = new Pane();
    private Scene scene;

    private ChatPane container = new ChatPane();
    private int index = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
//        root.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        HBox inputsBox = new HBox(container.inputBox, container.add);
         inputsBox.setAlignment(Pos.BOTTOM_CENTER);
         VBox scrollView=new VBox(container);
         scrollView.setAlignment(Pos.CENTER);
         
     
        root.getChildren().addAll(scrollView, inputsBox);
        scene = new Scene(root, 300, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
