/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi.TimeLineView;


import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.mky.jfxmpi.bookView.BW;

/**
 * Hilbert city map,
 * Vironi World map,
 * Chapter viewers,
 * Character and BG scene creations
 * Character hair and earing placements
 * Similar image suggestion
 * Characters in image taggings
 *
 * @author mkfs
 */
public class TLView extends Application {

    private Pane root = new Pane();
    private Scene scene;

    //private ChatPane container = new ChatPane();
    private int index = 0;
    
    TimeLineStory container=new TimeLineStory("User",650,500);

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

container.setTranslateX(50);
container.setTranslateY(50);
//container.messageScroller.setPrefSize(321, 637);
        
root.getChildren().addAll(container);
        
        //String image = MobileView.class.getResource("/splash.png").toExternalForm();//container.setTranslateY(100);//container.setTranslateX(16);// scene = new Scene(root,300, 637);
        //String image = MobileView.class.getResource("/splash.png").toExternalForm();
        String image = TLView.class.getResource("/Nexus_5_Front_View.png").toExternalForm();
        root.setStyle("-fx-background-image: url('" + image + "'); "
                + "-fx-background-position: center center; "
                + "-fx-background-repeat: stretch; -fx-background-size: cover;");
        root.setStyle("-fx-background-color:linear-gradient(to right, #fc5c7d, #6a82fb);");

        scene = new Scene(root,1024, 800);
        scene.getStylesheets().add( BW.class.getResource("/book/bw.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
