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
import java.util.Optional;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javax.imageio.ImageIO;
import net.mky.jfxmpi.Collage;
import static net.mky.jfxmpi.MainApp.awtImageToFX;
import static net.mky.jfxmpi.MainApp.getImageFromClipboard;
import net.mky.jfxmpi.TextBubble;
import static net.mky.jfxmpi.TimeLineView.TimeLineStory.getImageB64From;
import net.mky.jfxmpi.bookView.BW;
import net.mky.tools.StylesForAll;


/**
 *
 * @author mkfs
 */



public class SpeechBox extends HBox{
    
    public static enum SpeechDirection{
    LEFT, RIGHT, CENTER
}

    public static enum SpeechTheme{
    NEUTRAL, BOOK,MEME, TEXT_BUBBLE,MYTHOLOGY
}
    public static enum imageStitch{RIGHT,LEFT,TOP,BOTTOM,FOREGROUND,BACKGROUND}
    
    private Color DEFAULT_SENDER_COLOR = Color.GOLD;
    private Color DEFAULT_RECEIVER_COLOR = Color.LIMEGREEN;
    private Color DEFAULT_SYSTEM_COLOR = Color.BLUEVIOLET;
    private Background DEFAULT_SENDER_BACKGROUND, DEFAULT_RECEIVER_BACKGROUND,DEFAULT_SYSTEM_BACKGROUND;

    public String message;
    public int number=-1;
    public String dateTime="Today";
    public String base64Image;
    private Image image;
    private Button bnPaste;
    public SpeechDirection direction;
    public SpeechTheme theme = SpeechTheme.NEUTRAL;

    private Label displayedText;
    Label boxCount;
    
    public TextArea textArea;
    private ImageView imageView ; int img_width=900,img_height=800;
    private SVGPath directionIndicator;

    public SpeechBox(String message, SpeechDirection direction){
        this.message = message;
        this.direction = direction;
        initialiseDefaults();
        setupElements();
    }
    
    public SpeechBox(String message,String base64Image, SpeechDirection direction,SpeechTheme theme) throws IOException{
        this.message = message;
        this.base64Image=base64Image;
        //Parse Image
        ByteArrayInputStream bis = new ByteArrayInputStream(base64Image.getBytes());
        InputStream in = Base64.getDecoder().wrap(bis);
        this.image = new Image(in);
        bis.close();
        in.close();

        this.direction = direction;
        this.theme=theme;
        initialiseDefaults();
        setupElements();
    }
    
    public SpeechBox(String message,String base64Image, SpeechDirection direction,int img_width,int img_height) throws IOException{
        this.message = message;
        this.base64Image=base64Image;
        this.img_width=img_width;
        this.img_height=img_height;
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
    
    public SpeechBox(String message,String base64Image, SpeechDirection direction,int img_width,int img_height,boolean preview) throws IOException{
        this.message = message;
        this.base64Image=base64Image;
        this.img_width=img_width;
        this.img_height=img_height;
        //Parse Image
        ByteArrayInputStream bis = new ByteArrayInputStream(base64Image.getBytes());
        InputStream in = Base64.getDecoder().wrap(bis);
        this.image = new Image(in);
        bis.close();
        in.close();

        this.direction = direction;
        initialiseDefaults();
        //Minimal for preview
        if(preview)setupPreviewElements();
        else
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
"    -fx-font-size: 12px;\n" +
"    -fx-font-weight: bold;\n" +
"    -fx-text-fill: white;");
        
        boxCount.setPadding(new Insets(5));
         
        displayedText = new Label(message);
        displayedText.setPadding(new Insets(5));
        displayedText.setWrapText(true);
        
        textArea = new TextArea(message);
        textArea.setPrefRowCount(message.contentEquals("")?1:3);
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
            imageView.setFitWidth(img_width); 
            imageView.setFitHeight(img_height); 
            imageView.setPreserveRatio(true);
            
            imageView.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Clicked!"); // change functionality
            ImageView imageViewFull = new ImageView(image);
            imageViewFull.setFitWidth(img_width); 
            imageViewFull.setFitHeight(img_height); 
            imageViewFull.setPreserveRatio(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Image", ButtonType.OK);
            alert.setGraphic(imageViewFull);
            alert.showAndWait();
            });
        
        }
        
        //If image is null
            //Clipboard image
            bnPaste = new Button("[+]");
            bnPaste.setStyle(StylesForAll.transparentAlive);
            bnPaste.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    try {
                        java.awt.Image imageThis = getImageFromClipboard();
                        if (imageThis != null) {
                            javafx.scene.image.Image fimage = awtImageToFX(imageThis);
                            //pe.imageView.setFitHeight(scene.getHeight());
                            // pe.imageView.setFitWidth(scene.getWidth());
                            //base64Image = getImageB64From(fimage);
                            if (image != null) {
                                ChoiceDialog<imageStitch> choiceDialog = new ChoiceDialog<>();
                                choiceDialog.getItems().addAll(imageStitch.values());
                                choiceDialog.showingProperty().addListener((ov, b, b1) -> {

                                    if (b1) {
                                        choiceDialog.setContentText("");
                                    } else {
                                        choiceDialog.setContentText(null);
                                    }

                                });
                                Optional<imageStitch> optionalResult = choiceDialog.showAndWait();
                                optionalResult.ifPresent(result -> {
                                    try {
                                        switch (result) {
                                            case RIGHT:
                                                BufferedImage newUpdatedImage = TimeLineStory.joinBufferedImage(base64Image, getImageB64From(fimage), true);
                                                //Image imagen = SwingFXUtils.toFXImage(newUpdatedImage, null);
                                                image = SwingFXUtils.toFXImage(newUpdatedImage, null);
                                                break;

                                            case LEFT:
                                                newUpdatedImage = TimeLineStory.joinBufferedImage(getImageB64From(fimage),base64Image,  true);
                                                //Image imagen = SwingFXUtils.toFXImage(newUpdatedImage, null);
                                                image = SwingFXUtils.toFXImage(newUpdatedImage, null);
                                                break;

                                            case TOP:
                                                newUpdatedImage = TimeLineStory.joinBufferedImage( getImageB64From(fimage),base64Image, false);
                                                //Image imagen = SwingFXUtils.toFXImage(newUpdatedImage, null);
                                                image = SwingFXUtils.toFXImage(newUpdatedImage, null);
                                                break;

                                            case BOTTOM:
                                                newUpdatedImage = TimeLineStory.joinBufferedImage(base64Image, getImageB64From(fimage), false);
                                                //Image imagen = SwingFXUtils.toFXImage(newUpdatedImage, null);
                                                image = SwingFXUtils.toFXImage(newUpdatedImage, null);
                                                break;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });

                                   
                               } else {
                                   image = fimage;
                               }
                        base64Image = getImageB64From(image);
                        imageView = new ImageView(image);
                        imageView.setFitWidth(img_width);
                        imageView.setFitHeight(img_height);
                        imageView.setPreserveRatio(true);

                        imageView.setOnMouseClicked((MouseEvent e) -> {
                            System.out.println("Clicked!"); // change functionality
                            ImageView imageViewFull = new ImageView(image);
                            imageViewFull.setFitWidth(img_width);
                            imageViewFull.setFitHeight(img_height);
                            imageViewFull.setPreserveRatio(true);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Image", ButtonType.OK);
                            alert.setGraphic(imageViewFull);
                            alert.showAndWait();
                        });
                        //bnPaste.setVisible(false);
                        configureForSystem();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


      

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
    
    private void setupPreviewElements(){
        
        textArea = new TextArea(message);
        textArea.setPrefRowCount(message.contentEquals("")?1:3);
        textArea.setPadding(new Insets(5));
        textArea.setWrapText(true);
        
        directionIndicator = new SVGPath();
        if(image!=null){
            imageView = new ImageView(image); 
            imageView.setFitWidth(img_width); 
            imageView.setFitHeight(img_height); 
            imageView.setPreserveRatio(true);
            
            imageView.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Clicked!"); // change functionality
            ImageView imageViewFull = new ImageView(image);
            imageViewFull.setFitWidth(img_width); 
            imageViewFull.setFitHeight(img_height); 
            imageViewFull.setPreserveRatio(true);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Image", ButtonType.OK);
            alert.setGraphic(imageViewFull);
        alert.showAndWait();
        });
        
        }
        configureForPreview();
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
         
           
        if(theme==SpeechTheme.BOOK){
            String imagebg = BW.class.getResource("/book/nEI5WlE.jpg").toExternalForm();
            setStyle("-fx-background-image: url('" + imagebg + "'); "
                    + "-fx-background-position: center center; "
                    + "-fx-background-repeat: stretch; -fx-background-size: cover;");
            textArea.setPrefRowCount(10);
            textArea.setStyle(" -fx-background-color: transparent ;\n"
                    + "    -fx-font-family: \"Helvetica\";\n"
                    + "    -fx-font-size: 20px;\n"
                    + "    -fx-font-weight: bold;\n"
                    + "    -fx-text-fill: black;");

            
//        ImageView bhView = new ImageView(image);
//
//        ColorAdjust colorAdjust = new ColorAdjust();
//        colorAdjust.setBrightness(-1);
//        bhView.setEffect(colorAdjust);
            if (imageView != null) {
                //Very basic
//                ColorAdjust colorAdjust = new ColorAdjust();
//                colorAdjust.setBrightness(-1);
//                imageView.setEffect(colorAdjust);
//                imageView.setFitWidth(img_width); 
//                imageView.setFitHeight(img_height/2); 
                //Advanced
                WritableImage writableImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
                PixelWriter pixelWriter = writableImage.getPixelWriter();
                PixelReader pixelReader = writableImage.getPixelReader();
                for (int i = 0; i < writableImage.getHeight(); i++) {
                    for (int j = 0; j < writableImage.getWidth(); j++) {
                        Color c = pixelReader.getColor(j, i);
                        if (c.getOpacity() < 1) {
                            pixelWriter.setColor(j, i, Color.WHITE);
                        }
                        
                        if ((c.getRed() +c.getGreen()+c.getBlue())/2 < .2) {
                            //pixelWriter.setColor(j, i, Color.TRANSPARENT);
                            pixelWriter.setColor(j, i, Color.GRAY.darker().darker());
                        } if ((c.getRed() +c.getGreen()+c.getBlue())/2 < .4) {
                            //pixelWriter.setColor(j, i, Color.TRANSPARENT);
                            pixelWriter.setColor(j, i, Color.GRAY.darker().darker());
                        }else if ((c.getRed() +c.getGreen()+c.getBlue())/2 < .6) {
                            //pixelWriter.setColor(j, i, Color.TRANSPARENT);
                            pixelWriter.setColor(j, i, Color.GRAY.darker());
                        }else if ((c.getRed() +c.getGreen()+c.getBlue())/2 < .8) {
                            //pixelWriter.setColor(j, i, Color.TRANSPARENT);
                            pixelWriter.setColor(j, i, Color.GRAY);
                        }else {
                           // pixelWriter.setColor(j, i, Color.GRAY);
                            pixelWriter.setColor(j, i, Color.TRANSPARENT);
                        }
                        
                        //any pixel which is not white should be transformed to black,
//                        if (c.getRed() > 0 || c.getGreen() > 0 || c.getBlue() > 0) {
//                            pixelWriter.setColor(j, i, Color.BLACK);
//                        }
                    }
                }
                imageView = new ImageView(writableImage);
                imageView.setFitWidth(img_width); 
                imageView.setFitHeight(img_height); 
                imageView.setPreserveRatio(true);
                
            };

         } else if (theme == SpeechTheme.MEME) {
             setStyle(" -fx-background-color: transparent ;\n");
             textArea.setPrefRowCount(2);
             textArea.setStyle(" -fx-background-color: transparent ;\n" //transparent
                     + "    -fx-font-family: \"Helvetica\";\n"
                     + "    -fx-font-size: 25px;\n"
                     + "    -fx-font-weight: bold;\n"
                     + "    -fx-text-fill: white; "
                     + "-fx-text-alignment: center;");
             textArea.setPadding(new Insets(-65));
             //This slows down
//             if (imageView != null) {
//                 //Instantiating the Glow class 
//                 Glow glow = new Glow();
//
//                 //setting level of the glow effect 
//                 glow.setLevel(0.9);
//                 imageView.setEffect(glow);
//             }

         }
        
        //displayedText.setBackground(DEFAULT_SYSTEM_BACKGROUND);
        displayedText.setAlignment(Pos.CENTER);
       // directionIndicator.setContent("M10 0 L0 10 L0 0 Z");
        directionIndicator.setFill(DEFAULT_SYSTEM_COLOR);

         HBox container = new HBox(textArea, directionIndicator);
         if (imageView != null) {
             VBox withImage = new VBox(boxCount, textArea,bnPaste, imageView);
             if (theme != SpeechTheme.BOOK) {
                 withImage.setBackground(DEFAULT_SYSTEM_BACKGROUND);
                 withImage.setPadding(new Insets(5));
             }
             
             if(theme == SpeechTheme.MEME){
                 withImage = new VBox(bnPaste,imageView, textArea);
             }
             
             if(theme == SpeechTheme.TEXT_BUBBLE){
                 withImage = new VBox(bnPaste,imageView,new TextBubble( false));
             }
             
             container = new HBox(withImage, directionIndicator);
             withImage.setAlignment(Pos.CENTER);
         } else {
             VBox withImage = new VBox(boxCount, textArea, bnPaste);
             if (theme != SpeechTheme.BOOK) {
                 withImage.setBackground(DEFAULT_SYSTEM_BACKGROUND);
             }
             container = new HBox(withImage, directionIndicator);
         }
         //Use at most 75% of the width provided to the SpeechBox for displaying the message
         container.maxWidthProperty().bind(widthProperty().multiply(0.75));
         getChildren().setAll(container);
         setAlignment(Pos.CENTER);
    }
     
      private void configureForPreview() {
      
        directionIndicator.setFill(DEFAULT_SYSTEM_COLOR);
        Label sample = new Label(message);
        sample.setStyle(StylesForAll.transparentAliveLabel);
        HBox container = new HBox(textArea, directionIndicator);
        if (imageView != null) {
            VBox withImage = new VBox(imageView, sample);
            withImage.setBackground(DEFAULT_SYSTEM_BACKGROUND);
            container = new HBox(withImage, directionIndicator);
        }
        //Use at most 75% of the width provided to the SpeechBox for displaying the message
        container.maxWidthProperty().bind(widthProperty().multiply(0.75));
        getChildren().setAll(container);
        setAlignment(Pos.CENTER);
    }
       
    public static Image imagefromBase64(String base64Image,int width,int height) {
        try {
            InputStream in;
            Image image;
            try (ByteArrayInputStream bis = new ByteArrayInputStream(base64Image.getBytes())) {
                in = Base64.getDecoder().wrap(bis);
                image = new Image(in, width, height, true, true);//Image(in);
            }
            in.close();

            return image;
        } catch (Exception ex) {
        }
        return null;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    
    
}