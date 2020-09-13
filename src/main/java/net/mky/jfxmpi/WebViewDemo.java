/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

/**
 *
 * @author mkfs
 */
import java.util.logging.Level;
import java.util.logging.Logger;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
 
public class WebViewDemo extends Application {
 
     @Override
   public void start(final Stage stage) {
 
       TextField addressBar = new TextField();
       addressBar.setText("https://eclipse.org");
       Button goButton = new Button("Go!");
       Label stateLabel = new Label();
 
       stateLabel.setTextFill(Color.RED);
       ProgressBar progressBar = new ProgressBar();
 
       final WebView browser = new WebView();
       final WebEngine webEngine = browser.getEngine();
       webEngine.documentProperty().addListener((o, old, doc) -> listenToButton(doc));
       //webEngine.documentProperty().addListener((o, old, doc) ->  webEngineExecuteScript(webEngine));
      
       // A Worker load the page
       Worker<Void> worker = webEngine.getLoadWorker();
 
        // Listening to the status of worker
       worker.stateProperty().addListener(new ChangeListener<State>() {
 
           @Override
           public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
               stateLabel.setText("Loading state: " + newValue.toString());
               if (newValue == Worker.State.SUCCEEDED) {
                   stage.setTitle(webEngine.getLocation());
                   stateLabel.setText("Finish!");
               }
           }
       });
 
       // Bind the progress property of ProgressBar
       // with progress property of Worker
       progressBar.progressProperty().bind(worker.progressProperty());
 
       goButton.setOnAction(new EventHandler<ActionEvent>() {
 
           @Override
           public void handle(ActionEvent event) {
               String url = addressBar.getText();
               // Load the page.
               webEngine.load(url);
           }
       });
       //
 
       VBox root = new VBox();
       root.getChildren().addAll(addressBar, goButton, stateLabel, progressBar, browser);
 
       Scene scene = new Scene(root);
 
       stage.setTitle("JavaFX WebView (o7planning.org)");
       stage.setScene(scene);
       stage.setWidth(650);
       stage.setHeight(600);
 
       stage.show();
    }
   
   public boolean webEngineExecuteScript(WebEngine webEngine){
       
       
        System.out.println(webEngine.executeScript("nextPanel()"));
        return true;
   }
    
         private void listenToButton(Document doc) {
      
            if (doc == null) {
                return;
            }

            System.out.println(doc.getElementById("comicImages").getTextContent());
            
//            HTMLInputElement element = (HTMLInputElement)doc.getElementById("nextPanel");
//            element.click();
           
            
            
//            Element el = doc.getElementById("nextPanel");
//((EventTarget) el).addEventListener("click", listener, false);
//
//             String id = "nextPanel";
//             Element button = doc.getElementById(id);
//    
//            ((EventTarget) button).addEventListener("click", e -> doSomeAction(), false);
            
            
             NodeList nList = doc.getChildNodes();//.getElementsByTagName("img");
       for (int i = 0; i < nList.getLength(); i++) {
             try {
           Node node = nList.item(i);
         //  if (node.getNodeType() == Element.ELEMENT_NODE) {
              Element eElement = (Element) node;
               System.out.println(eElement.getTextContent());
         //  }
         } catch (Exception ex) {
            Logger.getLogger(WebViewDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
           }
            
            // ...
        
}
 
    public static void main(String[] args) {
        launch(args);
    }
 
}