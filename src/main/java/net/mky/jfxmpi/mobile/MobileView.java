/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi.mobile;

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

    //private ChatPane container = new ChatPane();
    private int index = 0;
    
    ConversationView container=new ConversationView("User",550,400);

    @Override
    public void start(Stage primaryStage) throws Exception {
//        root.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
//        HBox inputsBox = new HBox(container.inputBox, container.add);
//         inputsBox.setAlignment(Pos.BOTTOM_CENTER);
//         VBox scrollView=new VBox(container);
//         scrollView.setAlignment(Pos.CENTER);
//         scrollView.setTranslateX(20);
//         scrollView.setTranslateY(100);
//         String bimage = MobileView.class.getResource("/background.jpg").toExternalForm();
//         container.setStyle("-fx-background-image: url("+bimage+");");
//         //scrollView.setPrefSize(321, 637);
//         container.add.setPrefSize(45, 30);
//         container.add.setTranslateX(20);
//        container.add.setTranslateY(520);
//        container.inputBox.setPrefSize(215, 30);
//        container.inputBox.setTranslateX(20);
//        container.inputBox.setTranslateY(520);
//root.getChildren().addAll(scrollView, inputsBox);

container.setTranslateX(95);
container.setTranslateY(140);
//container.messageScroller.setPrefSize(321, 637);
        
root.getChildren().addAll(container);
        
        //String image = MobileView.class.getResource("/splash.png").toExternalForm();//container.setTranslateY(100);//container.setTranslateX(16);// scene = new Scene(root,300, 637);
        //String image = MobileView.class.getResource("/splash.png").toExternalForm();
        String image = MobileView.class.getResource("/Nexus_5_Front_View.png").toExternalForm();
        root.setStyle("-fx-background-image: url('" + image + "'); "
                + "-fx-background-position: center center; "
                + "-fx-background-repeat: stretch; -fx-background-size: cover;");

        scene = new Scene(root,550, 860);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
