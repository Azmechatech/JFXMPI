/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi.TimeLineView;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javax.imageio.ImageIO;
import sun.misc.BASE64Decoder;

/**
 *
 * @author mkfs
 */
enum SpeechDirection{
    LEFT, RIGHT, CENTER
}



public class SpeechBox extends HBox{
    private Color DEFAULT_SENDER_COLOR = Color.GOLD;
    private Color DEFAULT_RECEIVER_COLOR = Color.LIMEGREEN;
    private Color DEFAULT_SYSTEM_COLOR = Color.BLUEVIOLET;
    private Background DEFAULT_SENDER_BACKGROUND, DEFAULT_RECEIVER_BACKGROUND,DEFAULT_SYSTEM_BACKGROUND;

    public String message;
    public int number=-1;
    public String base64Image;
    private Image image;
    public SpeechDirection direction;

    private Label displayedText;
    Label boxCount;
    
    private TextArea textArea;
    private ImageView imageView ;
    private SVGPath directionIndicator;

    public SpeechBox(String message, SpeechDirection direction){
        this.message = message;
        this.direction = direction;
        initialiseDefaults();
        setupElements();
    }
    
    public SpeechBox(String message,String base64Image, SpeechDirection direction) throws IOException{
        this.message = message;
        this.base64Image=base64Image;
        //Parse Image
        ByteArrayInputStream bis = new ByteArrayInputStream(base64Image.getBytes());
        InputStream in = Base64.getDecoder().wrap(bis);
        this.image = new Image(in);
        bis.close();
        in.close();

        this.direction = direction;
        initialiseDefaults();
        setupElements();
    }
    
    
    public SpeechBox(String message,String base64Image, SpeechDirection direction,int number) throws IOException{
        this.message = message;
        this.base64Image=base64Image;
        this.number=number;
        //Parse Image
        ByteArrayInputStream bis = new ByteArrayInputStream(base64Image.getBytes());
        InputStream in = Base64.getDecoder().wrap(bis);
        this.image = new Image(in);
        bis.close();
        in.close();

        this.direction = direction;
        initialiseDefaults();
        setupElements();
    }

    private void initialiseDefaults(){
        DEFAULT_SENDER_BACKGROUND = new Background(
                new BackgroundFill(DEFAULT_SENDER_COLOR, new CornerRadii(5,0,5,5,false), Insets.EMPTY));
        DEFAULT_RECEIVER_BACKGROUND = new Background(
                new BackgroundFill(DEFAULT_RECEIVER_COLOR, new CornerRadii(0,5,5,5,false), Insets.EMPTY));
        
        DEFAULT_SYSTEM_BACKGROUND = new Background(
                new BackgroundFill(DEFAULT_SYSTEM_COLOR, new CornerRadii(0,5,5,5,false), Insets.EMPTY));
    }

    private void setupElements(){
        
        boxCount=new Label(number!=-1?number+"":"");
        boxCount.setStyle("-fx-font-family: \"Helvetica\";\n" +
"    -fx-font-size: 24px;\n" +
"    -fx-font-weight: bold;\n" +
"    -fx-text-fill: white;");
        
        boxCount.setPadding(new Insets(5));
         
        displayedText = new Label(message);
        displayedText.setPadding(new Insets(5));
        displayedText.setWrapText(true);
        
        textArea = new TextArea(message);
        textArea.setPrefRowCount(3);
        textArea.setPadding(new Insets(5));
        textArea.setWrapText(true);
        
        textArea.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                message=textArea.getText();
                
                System.out.println("updated message>>"+message);
            }
        });
        
        directionIndicator = new SVGPath();
        if(image!=null){
            imageView = new ImageView(image); 
            imageView.setFitWidth(800); 
            imageView.setFitHeight(800); 
            imageView.setPreserveRatio(true);
            
            imageView.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Clicked!"); // change functionality
            ImageView imageViewFull = new ImageView(image);
            imageViewFull.setFitWidth(800); 
            imageViewFull.setFitHeight(800); 
            imageViewFull.setPreserveRatio(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Image", ButtonType.OK);
            alert.setGraphic(imageViewFull);
        alert.showAndWait();
        });
        
        }

        if(direction == SpeechDirection.LEFT){
            configureForReceiver();
        }
        if(direction == SpeechDirection.RIGHT){
            configureForSender();
        }
        else{
            configureForSystem();
        }
    }

    private void configureForSender(){
        displayedText.setBackground(DEFAULT_SENDER_BACKGROUND);
        displayedText.setAlignment(Pos.CENTER_RIGHT);
        
      
        
        directionIndicator.setContent("M10 0 L0 10 L0 0 Z");
        directionIndicator.setFill(DEFAULT_SENDER_COLOR);
       
        HBox container = new HBox(boxCount,textArea, directionIndicator);
              if(imageView!=null){ VBox withImage=new VBox(textArea,imageView);
            withImage.setBackground(DEFAULT_SENDER_BACKGROUND);
            container = new HBox(withImage,directionIndicator);}
        //Use at most 75% of the width provided to the SpeechBox for displaying the message
        container.maxWidthProperty().bind(widthProperty().multiply(0.75));
        getChildren().setAll(container);
        setAlignment(Pos.CENTER_RIGHT);
    }

    private void configureForReceiver(){
        displayedText.setBackground(DEFAULT_RECEIVER_BACKGROUND);
        displayedText.setAlignment(Pos.CENTER_LEFT);
        directionIndicator.setContent("M0 0 L10 0 L10 10 Z");
        directionIndicator.setFill(DEFAULT_RECEIVER_COLOR);

        HBox container = new HBox(directionIndicator, boxCount,textArea);
        if(imageView!=null){ VBox withImage=new VBox(textArea,imageView);
            withImage.setBackground(DEFAULT_RECEIVER_BACKGROUND);
            container = new HBox(directionIndicator,withImage);}
        //Use at most 75% of the width provided to the SpeechBox for displaying the message
        container.maxWidthProperty().bind(widthProperty().multiply(0.75));
        getChildren().setAll(container);
        setAlignment(Pos.CENTER_LEFT);
    }
    
     private void configureForSystem(){
        displayedText.setBackground(DEFAULT_SYSTEM_BACKGROUND);
        displayedText.setAlignment(Pos.CENTER);
       // directionIndicator.setContent("M10 0 L0 10 L0 0 Z");
        directionIndicator.setFill(DEFAULT_SYSTEM_COLOR);

        HBox container = new HBox(textArea, directionIndicator);
              if(imageView!=null){ VBox withImage=new VBox(boxCount,textArea,imageView);
            withImage.setBackground(DEFAULT_SYSTEM_BACKGROUND);
            container = new HBox(withImage,directionIndicator);}
        //Use at most 75% of the width provided to the SpeechBox for displaying the message
        container.maxWidthProperty().bind(widthProperty().multiply(0.75));
        getChildren().setAll(container);
        setAlignment(Pos.CENTER);
    }
}