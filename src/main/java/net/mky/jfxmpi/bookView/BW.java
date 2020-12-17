/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi.bookView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
//import javafx.scene.web.HTMLEditor;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.mky.jfxmpi.Collage;
import net.mky.tools.StylesForAll;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author mkfs
 */
public class BW  extends Application {
      // private Pane root = new Pane();
    private Scene scene;

    //private ChatPane container = new ChatPane();
    private int index = 0;
    
    List<String> pageList=new LinkedList<>();
    

    @Override
    public void start(Stage primaryStage) throws Exception {
    TextArea textArea = new TextArea();
    textArea.setTranslateX(-250);textArea.setTranslateY(100);textArea.setMaxWidth(500);textArea.setWrapText(true);
    TextArea textArea2 = new TextArea();
    textArea2.setTranslateX(300);textArea2.setTranslateY(100);textArea2.setMaxWidth(500);textArea2.setWrapText(true);
//    final HTMLEditor htmlEditor = new HTMLEditor();
//        htmlEditor.setPrefHeight(245);
//        htmlEditor.setStyle(
//                " -fx-background-color: transparent ;"+
//    "-fx-font: 12 cambria;"
//    + "-fx-border-color: brown; "
//    + "-fx-border-style: dotted;"
//    + "-fx-border-width: 2;"
//);
    
   
    
    //Add twin pages
    Button newPage = new Button("Next");
        newPage.setStyle(StylesForAll.transparentAlive);
        newPage.setOnAction(event -> {
            index=(index+2)<=(pageList.size()+2)?(index+2):(pageList.size()+2);
        });
        newPage.setTranslateX(500);newPage.setTranslateY(-400);
        
        Button prevPage = new Button("Prev");
        prevPage.setStyle(StylesForAll.transparentAlive);
        prevPage.setOnAction(event -> {
            index=index-2>0?index-2:0;
        });
        prevPage.setTranslateX(-500);prevPage.setTranslateY(-400);
    
    FileChooser fileChooser = new FileChooser();
    DirectoryChooser dirChooser = new DirectoryChooser();

Button loadChat = new Button("Load");
        loadChat.setStyle(StylesForAll.transparentAlive);
        loadChat.setOnAction(event -> {
            File selectedFile = fileChooser.showOpenDialog(null);
            //Read all the file and conver to json
            //Render sppech boxes.
            try {
                String content = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));


            } catch (Exception ex) {
            }

        });
        
        //For testing purposes
        Button saveChat = new Button("Save");
        saveChat.setStyle(StylesForAll.transparentAlive);
        saveChat.setOnAction(event -> {

            //Read all the speech box 
            //and write to JSON
            JSONObject metaData = new JSONObject();
            JSONArray completeChat = new JSONArray();
//            for (Node spb : speechBubbles) {
//                JSONObject spbj = new JSONObject();
//                SpeechBox sb = (SpeechBox) spb;
//                spbj.put("direction", sb.direction);
//                spbj.put("message", new String(sb.message.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
//                spbj.put("base64Image", sb.base64Image);
//                completeChat.put(spbj);
//
//               //  System.out.println(spbj);
//            }

            metaData.put("chat_data", completeChat);

            File dir = dirChooser.showDialog(null);
            //Write JSON file
            // try (FileWriter file = new FileWriter(dir.getAbsolutePath()+"/Chat_"+System.currentTimeMillis()+".json")) {
            Writer fstream = null;
            BufferedWriter out = null;
            try {
                fstream = new OutputStreamWriter(new FileOutputStream(dir.getAbsolutePath() + "/Chat_" + System.currentTimeMillis() + ".json"), StandardCharsets.UTF_8);
                fstream.write(metaData.toString());
                fstream.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        
        StackPane root = new StackPane(prevPage,newPage,textArea,textArea2);
        //StackPane root = new StackPane(htmlEditor);
        
        
         /**************************************************************
 * COLLAGE IMPLEMENTATION
 */

        Button collageButton = new Button("Collage");
        EventHandler<ActionEvent> gocollageButtonAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ChoiceDialog<Collage.OPTIONS> choiceDialog = new ChoiceDialog<>();
                choiceDialog.getItems().addAll(Collage.OPTIONS.values());
                choiceDialog.showingProperty().addListener((ov, b, b1) -> {
                    
                    if (b1) {
                        choiceDialog.setContentText("");
                    } else {
                        choiceDialog.setContentText(null);
                    }

                });
                
                 Optional<Collage.OPTIONS> optionalResult =choiceDialog.showAndWait();
                 optionalResult.ifPresent(result->{
                     System.out.println(result);
                     Collage collage=new Collage(result);
                     root.getChildren().clear();//.remove(background.imageView);
                     root.getChildren().add(collage);
                     
                 });

            }
        };
        collageButton.setOnAction(gocollageButtonAction);
        collageButton.setStyle(StylesForAll.transparentAlive);
        root.getChildren().add(collageButton);
        
        
        
//root.getChildren().addAll(container);
root.setStyle("-fx-background-color: linear-gradient(to bottom right, white 0%, cornflowerblue 100% );");
        
        //String image = MobileView.class.getResource("/splash.png").toExternalForm();//container.setTranslateY(100);//container.setTranslateX(16);// scene = new Scene(root,300, 637);
        //String image = MobileView.class.getResource("/splash.png").toExternalForm();
        String image = BW.class.getResource("/book/8iAb8ex8T.jpg").toExternalForm();
        root.setStyle("-fx-background-image: url('" + image + "'); "
                + "-fx-background-position: center center; "
                + "-fx-background-repeat: stretch; -fx-background-size: cover;");

        scene = new Scene(root,1200, 860);
        scene.getStylesheets().add( BW.class.getResource("/book/bw_1.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
