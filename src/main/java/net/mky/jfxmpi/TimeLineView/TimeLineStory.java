/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi.TimeLineView;


import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import static net.mky.jfxmpi.MainApp.awtImageToFX;
import static net.mky.jfxmpi.MainApp.getImageFromClipboard;
import net.mky.tools.StylesForAll;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author mkfs
 */
public class TimeLineStory extends VBox {

    private String conversationPartner;
    private int prefHeight, prefWidth;
    private ObservableList<Node> speechBubbles = FXCollections.observableArrayList();
    FileChooser fileChooser = new FileChooser();
    DirectoryChooser dirChooser = new DirectoryChooser();
    String activeChatFile="";

    private Label contactHeader;
    public ScrollPane messageScroller;
    private VBox messageContainer;
    private HBox inputContainer;
    private HBox contactHeaderBar;

    public TimeLineStory(String conversationPartner, int prefHeight, int prefWidth) {
        super(5);
        this.conversationPartner = conversationPartner;
        this.prefHeight = prefHeight;
        this.prefWidth = prefWidth;
        setStyle("-fx-background-color: transparent;");
        setupElements();
    }
    
    public TimeLineStory(File chapter,String conversationPartner, int prefHeight, int prefWidth) {
        super(5);
        this.conversationPartner = conversationPartner;
        this.prefHeight = prefHeight;
        this.prefWidth = prefWidth;
        setStyle("-fx-background-color: transparent;");
        setupElements();
        
        activeChatFile=chapter.getName();
            //Read all the file and conver to json
            //Render sppech boxes.
            try {
                String content = new String(Files.readAllBytes(Paths.get(chapter.getAbsolutePath())));
                JSONObject metaData = new JSONObject(content);
                JSONArray chat_data = metaData.getJSONArray("chat_data");

                for (int i = 0; i < chat_data.length(); i++) {
                    if (chat_data.getJSONObject(i).has("base64Image")) {
                        speechBubbles.add(new SpeechBox(chat_data.getJSONObject(i).getString("message"), chat_data.getJSONObject(i).getString("base64Image"), SpeechDirection.valueOf(chat_data.getJSONObject(i).getString("direction")),i));

                    } else {
                        speechBubbles.add(new SpeechBox(chat_data.getJSONObject(i).getString("message"), SpeechDirection.valueOf(chat_data.getJSONObject(i).getString("direction"))));
                    }
                }

            } catch (Exception ex) {
            }
            
    }
    

    private void setupElements() {
        setupContactHeader();
        setupMessageDisplay();
        setupInputDisplay();
        getChildren().setAll(contactHeaderBar, messageScroller, inputContainer);
        setPadding(new Insets(5));
    }

    private void setupContactHeader() {
        contactHeaderBar = new HBox(5);
        contactHeaderBar.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, CornerRadii.EMPTY, Insets.EMPTY)));

         TextField userInput = new TextField();
        userInput.setPromptText("...");
        userInput.setPrefWidth(580);//Widht control
        
        //Save and load
        //For testing purposes
        Button saveChat = new Button("Save");
        saveChat.setStyle(StylesForAll.transparentAlive);
        saveChat.setOnAction(event -> {
            
            //Get file Prefix
             // create a text input dialog 
        TextInputDialog td = new TextInputDialog("Chat file name"); 
  
        // setHeaderText 
        td.setHeaderText("Chat file name"); 
        td.getEditor().setText(activeChatFile);
        td.showAndWait();
        activeChatFile=td.getEditor().getText();

            //Read all the speech box 
            //and write to JSON
            JSONObject metaData = new JSONObject();
            JSONArray completeChat = new JSONArray();
            for (Node spb : speechBubbles) {
                JSONObject spbj = new JSONObject();
                SpeechBox sb = (SpeechBox) spb;
                spbj.put("direction", sb.direction);
                
                spbj.put("message", new String(sb.message.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
                
                spbj.put("base64Image", sb.base64Image);
                completeChat.put(spbj);

               //  System.out.println(spbj);
            }

            metaData.put("chat_data", completeChat);

            File dir = dirChooser.showDialog(null);
            //Write JSON file
            // try (FileWriter file = new FileWriter(dir.getAbsolutePath()+"/Chat_"+System.currentTimeMillis()+".json")) {
            Writer fstream = null;
            BufferedWriter out = null;
            try {
                fstream = new OutputStreamWriter(new FileOutputStream(dir.getAbsolutePath() + "/Chat_"+activeChatFile + System.currentTimeMillis() + ".json"), StandardCharsets.UTF_8);
                fstream.write(metaData.toString());
                fstream.flush();
                
                System.out.println("Saved the file");

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        Button loadChat = new Button("Load");
        loadChat.setStyle(StylesForAll.transparentAlive);
        loadChat.setOnAction(event -> {
            File selectedFile = fileChooser.showOpenDialog(null);
            activeChatFile=selectedFile.getName();
            //Read all the file and conver to json
            //Render sppech boxes.
            try {
                String content = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));
                JSONObject metaData = new JSONObject(content);
                JSONArray chat_data = metaData.getJSONArray("chat_data");

                for (int i = 0; i < chat_data.length(); i++) {
                    if (chat_data.getJSONObject(i).has("base64Image")) {
                        speechBubbles.add(new SpeechBox(chat_data.getJSONObject(i).getString("message"), chat_data.getJSONObject(i).getString("base64Image"), SpeechDirection.valueOf(chat_data.getJSONObject(i).getString("direction")),i));

                    } else {
                        speechBubbles.add(new SpeechBox(chat_data.getJSONObject(i).getString("message"), SpeechDirection.valueOf(chat_data.getJSONObject(i).getString("direction"))));
                    }
                }

            } catch (Exception ex) {
            }

        });

        Button loadGameImages = new Button("Game Images");
        loadGameImages.setStyle(StylesForAll.transparentAlive);
        loadGameImages.setOnAction(event -> {
            List<File> imageList = fileChooser.showOpenMultipleDialog(null);
            //Read all the file and conver to json
            //Render sppech boxes.
            try {
                int counter=0;
                for (File file : imageList) {

                    speechBubbles.add(new SpeechBox("What happened next?", getImageB64From(file), SpeechDirection.CENTER,counter++));

                }

            } catch (Exception ex) {
            }

        });
        
        //Clipboard image
        Button bnPaste = new Button("Ctrl+V");
        bnPaste.setStyle(StylesForAll.transparentAlive);
        bnPaste.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    java.awt.Image image = getImageFromClipboard();
                    if (image != null) {
                        javafx.scene.image.Image fimage = awtImageToFX(image);
                        //pe.imageView.setFitHeight(scene.getHeight());
                        // pe.imageView.setFitWidth(scene.getWidth());
                        String b64Image = getImageB64From(fimage);
                        speechBubbles.add(new SpeechBox(userInput.getText(), b64Image, SpeechDirection.CENTER));
                        userInput.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        contactHeader = new Label(conversationPartner);
        contactHeader.setAlignment(Pos.CENTER);
        contactHeader.setFont(Font.font("Comic Sans MS", 14));
        contactHeaderBar.getChildren().add(contactHeader);
        contactHeaderBar.getChildren().add(loadGameImages);
        contactHeaderBar.getChildren().add(saveChat);
        contactHeaderBar.getChildren().add(loadChat);
        contactHeaderBar.getChildren().add(userInput);
        contactHeaderBar.getChildren().add(bnPaste);
    }

    public static SpeechBox previewHelper(File selectedFile){
          try {
                String content = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));
                JSONObject metaData = new JSONObject(content);
                JSONArray chat_data = metaData.getJSONArray("chat_data");

                for (int i = 0; i < chat_data.length(); i++) {
                    if (chat_data.getJSONObject(i).has("base64Image")) {
                      return   new SpeechBox(chat_data.getJSONObject(i).getString("message"), chat_data.getJSONObject(i).getString("base64Image"), SpeechDirection.valueOf(chat_data.getJSONObject(i).getString("direction")));

                    } else {
                        return new SpeechBox(chat_data.getJSONObject(i).getString("message"), SpeechDirection.valueOf(chat_data.getJSONObject(i).getString("direction")));
                    }
                }

            } catch (Exception ex) {
            }
        return new SpeechBox("No chapters here.", SpeechDirection.CENTER);
    }
    private void setupMessageDisplay() {
        messageContainer = new VBox(5);
        Bindings.bindContentBidirectional(speechBubbles, messageContainer.getChildren());

        messageScroller = new ScrollPane(messageContainer);
        messageScroller.setStyle("-fx-background-color: transparent;");
        messageContainer.setStyle("-fx-background-color: transparent;");
       
        messageScroller.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        messageScroller.setHbarPolicy(ScrollBarPolicy.NEVER);
        messageScroller.setMaxHeight(prefHeight);//420
        //messageScroller.setPrefWidth(prefWidth);//420
        messageScroller.prefWidthProperty().bind(messageContainer.prefWidthProperty().subtract(5));
        messageScroller.setFitToWidth(true);
        //Make the scroller scroll to the bottom when a new message is added
        speechBubbles.addListener((ListChangeListener<Node>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    messageScroller.setVvalue(messageScroller.getVmax());
                }
            }
        });
    }

    public static String getImageB64From(File selectedFile) {
        try {

            FileInputStream fis = new FileInputStream(selectedFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = fis.read(buffer)) > -1) {
                baos.write(buffer, 0, read);
            }
            fis.close();
            baos.close();
            byte pgnBytes[] = baos.toByteArray();
            Base64.Encoder base64_enc = Base64.getEncoder();
            return base64_enc.encodeToString(pgnBytes);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(TimeLineStory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TimeLineStory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String getImageB64From(Image image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", baos);
        } catch (IOException e) {
            //bla
        }
        byte pgnBytes[] = baos.toByteArray();
        Base64.Encoder base64_enc = Base64.getEncoder();
        String base64_image = base64_enc.encodeToString(pgnBytes);

        return base64_enc.encodeToString(pgnBytes);

    }

    private void setupInputDisplay() {
        inputContainer = new HBox(5);

        TextField userInput = new TextField();
        userInput.setPromptText("Enter message");
        userInput.setPrefWidth(700);//Widht control

        Button sendMessageButton = new Button("Set");
        sendMessageButton.disableProperty().bind(userInput.lengthProperty().isEqualTo(0));
        sendMessageButton.setOnAction(event -> {
            sendMessage(userInput.getText());
            userInput.setText("");
        });

        //For testing purposes
        Button receiveMessageButton = new Button("Get");
        receiveMessageButton.disableProperty().bind(userInput.lengthProperty().isEqualTo(0));
        receiveMessageButton.setOnAction(event -> {
            receiveMessage(userInput.getText());
            userInput.setText("");
        });

        //For testing purposes
        Button attachImage = new Button("+");
        attachImage.disableProperty().bind(userInput.lengthProperty().isEqualTo(0));
        attachImage.setOnAction(event -> {

            try {
                java.awt.Image image = getImageFromClipboard();
                if (image != null) {
                    javafx.scene.image.Image fimage = awtImageToFX(image);
                    //pe.imageView.setFitHeight(scene.getHeight());
                    // pe.imageView.setFitWidth(scene.getWidth());
                    String b64Image = getImageB64From(fimage);
                    receivePhoto(userInput.getText(), b64Image);
                } else {
                    File selectedFile = fileChooser.showOpenDialog(null);
                    receivePhoto(userInput.getText(), getImageB64From(selectedFile));
                }
            } catch (Exception ex) {

            }

            userInput.setText("");

        });

        //For testing purposes
        Button attachImage2 = new Button("+");
        attachImage2.disableProperty().bind(userInput.lengthProperty().isEqualTo(0));
        attachImage2.setOnAction(event -> {
//            File selectedFile = fileChooser.showOpenDialog(null);
//      
//           sendPhoto(userInput.getText(),getImageB64From(selectedFile));
//           
            try {
                java.awt.Image image = getImageFromClipboard();
                if (image != null) {
                    javafx.scene.image.Image fimage = awtImageToFX(image);
                    //pe.imageView.setFitHeight(scene.getHeight());
                    // pe.imageView.setFitWidth(scene.getWidth());
                    String b64Image = getImageB64From(fimage);
                    sendPhoto(userInput.getText(), b64Image);
                } else {
                    File selectedFile = fileChooser.showOpenDialog(null);
                    sendPhoto(userInput.getText(), getImageB64From(selectedFile));
                }
            } catch (Exception ex) {

            }

            userInput.setText("");

        });

        inputContainer.getChildren().setAll(receiveMessageButton, attachImage, userInput, sendMessageButton, attachImage2);
    }

    public void sendMessage(String message) {
        speechBubbles.add(new SpeechBox(message, SpeechDirection.RIGHT));
    }

    public void receiveMessage(String message) {
        speechBubbles.add(new SpeechBox(message, SpeechDirection.LEFT));
    }

    public void receivePhoto(String message, String imageB64) {
        try {
            speechBubbles.add(new SpeechBox(message, imageB64, SpeechDirection.LEFT));
        } catch (IOException ex) {
            Logger.getLogger(TimeLineStory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendPhoto(String message, String imageB64) {
        try {
            speechBubbles.add(new SpeechBox(message, imageB64, SpeechDirection.RIGHT));
        } catch (IOException ex) {
            Logger.getLogger(TimeLineStory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
