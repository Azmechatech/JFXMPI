/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import net.mky.tools.StylesForAll;
import systemknowhow.human.Life;
import systemknowhow.human.LifeTagFactory;

/**
 *
 * @author mkfs
 */
public class TextBubble extends StackPane {

    private Rectangle rect;
    private double pressedX, pressedY;
    private LongProperty frame = new SimpleLongProperty();

    FileInputStream input;

    public String CharacterFile = "";
    Life thisCharcter;//=""
    Life otherCharcter;//=""

    public void setOtherCharcter(Life otherCharcter) {
        this.otherCharcter = otherCharcter;
    }
    String name = "";
    String age = "";
    String SeedFile = "";
    LifeTagFactory ltf;
    public String CharacterPool[];

    Button playButton;

    private static final double W = 200;
    private static final double H = 200;
    private static final int SHADOW_LENGTH = 100;

    private static final double IMG_X = 20;
    private static final double IMG_Y = 20;

    private static final int FONT_SIZE = 200;

    private static final double SHADOW_SLOPE_FACTOR = 1.5;

    Color SHADOW_COLOR = Color.GRAY.brighter();
    Label chatMessage = new Label("Hello There! ");
    TextArea taxachatMessage = new TextArea("Ready to play! ");
    boolean SHOW_TEXT_BUBBLE = true;
    Circle cir2;
    boolean SHOW_IMG_BUBBLE = false;
    TextBubbleStyles.STYLES tbss=TextBubbleStyles.STYLES.PLAIN_PAPER;

    FileChooser fileChooser = new FileChooser();

    public TextBubble(boolean border) {
        setMinSize(200, 200);
        if (border) {
            setBorder(new Border(new BorderStroke(Color.GOLDENROD,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
        if (border) {
            setStyle("-fx-padding: 10;"
                    + "-fx-border-style: solid inside;"
                    + "-fx-border-width: 10;"
                    + "-fx-border-insets: 0;"
                    + "-fx-border-radius: 5;" + "-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #dc143c, #661a33)"
            /*"-fx-border-color: GOLDENROD;"*/);
        }

        //Use this http://www.drawsvg.org/drawsvg.html
        chatMessage.setStyle("-fx-shape: \"M177.16535,46.062967 C 177.16535,46.062967 106.29921,46.062966 70.866142,99.212573 C 35.433071,152.36218 35.433071,223.22832 70.866142,276.37793 C 101.94300,322.99322 177.16535,329.52753 177.16535,329.52753 L 194.10223,433.03688 L 230.31496,329.52753 L 478.34646,329.52753 C 478.34646,329.52753 549.21260,329.52753 584.64567,276.37793 C 620.07874,223.22832 620.07874,152.36218 584.64567,99.212573 C 549.21260,46.062967 478.34646,46.062967 478.34646,46.062967 L 177.16535,46.062967Z \";\n"
                + "    -fx-background-color: black, white;\n"
                + "    -fx-background-insets: 0,1;\n"
                + "    -fx-font-family: \"Comic Sans MS\";\n"
                + "    -fx-font-size: 20px;\n"
                + "    -fx-font-weight: bold;\n"
                + "    -fx-padding: 10 10 60 20;"); //TOP, RIGHT, BOTTOM, LEFT

        chatMessage.setWrapText(true);

        chatMessage.setTextAlignment(TextAlignment.CENTER);

        chatMessage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    showBubbleSelectionDialog();

                }
                if (e.getButton() == MouseButton.MIDDLE) {
                    showInputTextDialog();
                } else {
                    System.out.println("No right click");
                }
            }
        });;
        
       

        setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                pressedX = event.getX();
                pressedY = event.getY();
            }
        });

        EventHandler<MouseEvent> imgMove = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                pressedX = event.getX() + chatMessage.getLayoutX();
                pressedY = event.getY() + chatMessage.getLayoutY();
            }
        };

        EventHandler<MouseEvent> imgMove2 = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // imageView.setTranslateX(imageView.getTranslateX() + event.getX() - pressedX);
                //  imageView.setTranslateY(imageView.getTranslateY() + event.getY() - pressedY);

                chatMessage.setTranslateX(chatMessage.getTranslateX() + event.getX() - pressedX);
                chatMessage.setTranslateY(chatMessage.getTranslateY() + event.getY() - pressedY);
                event.consume();
            }
        };

        chatMessage.setOnMouseClicked(imgMove);
        chatMessage.setOnMouseDragged(imgMove2);

        EventHandler<ActionEvent> showHide = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // imageView.setVisible(!imageView.isVisible());
                chatMessage.setVisible(!chatMessage.isVisible());
                playButton.setVisible(!playButton.isVisible());
                cir2.setVisible(!cir2.isVisible());
//                chatMessage.setText((String) thisCharcter.talk(otherCharcter, name).toArray()[0]);
            }
        };

        ///Image bubble
        cir2 = new Circle(150, 150, 150);
        cir2.setStroke(Color.SEAGREEN);
        EventHandler<MouseEvent> imgMoveImg = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                pressedX = event.getX() + cir2.getLayoutX();
                pressedY = event.getY() + cir2.getLayoutY();
            }
        };

        EventHandler<MouseEvent> imgMoveImg2 = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                // imageView.setTranslateX(imageView.getTranslateX() + event.getX() - pressedX);
                //  imageView.setTranslateY(imageView.getTranslateY() + event.getY() - pressedY);

                cir2.setTranslateX(cir2.getTranslateX() + event.getX() - pressedX);
                cir2.setTranslateY(cir2.getTranslateY() + event.getY() - pressedY);
                event.consume();
            }
        };
        
         cir2.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    showBubbleSelectionDialog();

                }
                if (e.getButton() == MouseButton.MIDDLE) {
                    showInputTextDialog();
                } else {
                    System.out.println("No right click");
                }
            }
        });;
        
        cir2.setOnMouseClicked(imgMoveImg);
        cir2.setOnMouseDragged(imgMoveImg2);
        getChildren().add(cir2);
        //getChildren().add(taxachatMessage);
        StackPane.setAlignment(chatMessage, Pos.TOP_LEFT);
        getChildren().add(chatMessage);
        
        cir2.setVisible(SHOW_IMG_BUBBLE);
        chatMessage.setVisible(SHOW_TEXT_BUBBLE);
        

        
        Button showHideImg = new Button("[-]");
        showHideImg.setOnAction(showHide);
        showHideImg.setStyle(StylesForAll.transparentAlive);
        getChildren().add(showHideImg);
        StackPane.setAlignment(showHideImg, Pos.TOP_CENTER);
        
        EventHandler<ActionEvent> posResetAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                chatMessage.setTranslateX(0);
                chatMessage.setTranslateY(0);
                
                cir2.setTranslateX(0);
                cir2.setTranslateY(chatMessage.getHeight());
            }
        };
        
        
        Button posReset = new Button("[<]");
        posReset.setOnAction(posResetAction);
        posReset.setStyle(StylesForAll.transparentAlive);
        
        
       // HBox controlButtons=new HBox(posReset,showHideImg);
        
        getChildren().add(posReset);
        StackPane.setAlignment(posReset, Pos.TOP_LEFT);

    }

    public String[] getLines(String filePath) {
        Set<String> linesRead = new LinkedHashSet<>();
        StringBuilder readStrings = new StringBuilder();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                // read next line
                line = reader.readLine();
                readStrings.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] result = readStrings.toString().split("\\.");
        //linesRead.add(line);
        return result;
    }
    public static int WORDS_IN_ROW = 3;

    private void showInputTextDialog() {

        TextInputDialog dialog = new TextInputDialog("Tran");

        // dialog.setTitle("o7planning");
        dialog.setHeaderText("Enter text:");
        dialog.setContentText("Text:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(name -> {
            String[] words = name.split(" ");
            int closestMatch = (int) Math.sqrt(words.length);
            int i = 0;
            StringBuilder sb = new StringBuilder();
            for (String word : words) {
                if (i < closestMatch) {
                    sb.append(word).append(" ");
                    i++;
                } else {
                    i = 0;
                    sb.append(word).append("\n");
                }

            }
            this.chatMessage.setText(sb.toString());
        });
    }

    private void showBubbleSelectionDialog() {
        Dialog<ResultsOfDialog> dialog = new Dialog<>();
        dialog.setTitle("Dialog Test");
        dialog.setHeaderText("Please specify…");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        //TextField textField = new TextField("Name");
        TextArea taxachatMessage = new TextArea(this.chatMessage.getText());
        TextField  colour=new TextField("black");
        
        ImageView imageView = new ImageView();
        ImageView forCropiing=new ImageView();;
        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
            System.out.println("showBubbleSelectionDialog#imageView#EventHandler");
            Image cropped=ImageCropDialog.imageCropTool(forCropiing.getImage());
            imageView.setImage(cropped);
            cir2.setFill(new ImagePattern(cropped));
            cir2.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
        });

        //Add check box
        // create a checkbox 
        CheckBox cText = new CheckBox("Show Text Bubble");
        cText.setSelected(SHOW_TEXT_BUBBLE);
        // create a event handler 
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {

                SHOW_TEXT_BUBBLE = cText.isSelected();

            }
        };

        // set event to checkbox 
        cText.setOnAction(event);

        CheckBox cImage = new CheckBox("Show Image Bubble");
        cImage.setSelected(SHOW_IMG_BUBBLE);
        // create a event handler 
        EventHandler<ActionEvent> eventImage = new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                SHOW_IMG_BUBBLE = cImage.isSelected();
            }
        };

        // set event to checkbox 
        cImage.setOnAction(eventImage);

        //Bubble image section
        Button pasteButton = new Button("Ctrl+V");
        pasteButton.getStyleClass().add("play");
        EventHandler<ActionEvent> pasteAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                 try {
                        java.awt.Image image = MainApp.getImageFromClipboard();
                        if (image != null) {
                            javafx.scene.image.Image fimage = MainApp.awtImageToFX(image);
                            forCropiing.setImage(fimage);
                            imageView.setImage(fimage);

                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

            }
        };
        pasteButton.setOnAction(pasteAction);
        
        
        
        Button playButton = new Button("Load Bubble Image");
        playButton.getStyleClass().add("play");
        EventHandler<ActionEvent> goAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {

                    File file = fileChooser.showOpenDialog(null);

                    if (file != null) {
                        FileInputStream input = new FileInputStream(file.getAbsolutePath());
                        imageView.setImage(new Image(input, 200, 200, true, true));
                        
                        FileInputStream input2 = new FileInputStream(file.getAbsolutePath());
                        forCropiing.setImage(new Image(input2, 800, 800, true, true));
//                        cir2.setFill(new ImagePattern(new Image(input2, 200, 200, true, true)));
//                        cir2.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
//                        pe.bgImageFile = file.getAbsolutePath();
//                        pe.setTranslateX(0);
//                        pe.setTranslateY(0);

                        File existDirectory = file.getParentFile();
                        fileChooser.setInitialDirectory(existDirectory);

                    }

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
        playButton.setOnAction(goAction);

        DatePicker datePicker = new DatePicker(LocalDate.now());
        ObservableList<TextBubbleStyles.STYLES> options
                = FXCollections.observableArrayList(TextBubbleStyles.STYLES.values());
        ComboBox<TextBubbleStyles.STYLES> comboBox = new ComboBox<>(options);
        //comboBox.getSelectionModel().selectFirst();
        comboBox.getSelectionModel().select(tbss);
        dialogPane.setContent(new VBox(8, taxachatMessage,colour, comboBox, new HBox(new VBox(cText, cImage,pasteButton, playButton), imageView)));
        Platform.runLater(taxachatMessage::requestFocus);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return new ResultsOfDialog(taxachatMessage.getText(), comboBox.getValue(), colour.getText());
            }
            return null;
        });
        Optional<ResultsOfDialog> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((ResultsOfDialog results) -> {
            this.chatMessage.setText(results.SpeechText);
            this.chatMessage.setEffect(new DropShadow(+25d, 0d, +2d, Color.BLANCHEDALMOND));
            this.chatMessage.setStyle("-fx-shape: \"" + results.TEXT_BUBBLE + "\";\n"
                    + "    -fx-background-color: "+colour.getText()+", #E0C796;\n"
                    + "    -fx-background-insets: 0,1;\n"
                    + "    -fx-font-family: \"Comic Sans MS\";\n"
                    + "    -fx-font-size: 20px;\n"
                    + "    -fx-font-weight: bold;\n"
                    + "    -fx-padding: 10 10 60 20;");

            cir2.setVisible(SHOW_IMG_BUBBLE);
            chatMessage.setVisible(SHOW_TEXT_BUBBLE);
            tbss=results.venue; //Save last state
            //  System.out.println( results.SpeechText + " "  + " " + results.venue+ " " + results.TEXT_BUBBLE);
        });

    }

    public static class ResultsOfDialog {

        String SpeechText;

        TextBubbleStyles.STYLES venue;

        String TEXT_BUBBLE = "";
        
        String TEXT_COLOUR="white";

        public ResultsOfDialog(String name, TextBubbleStyles.STYLES venue, String our) {
            this.SpeechText = name;
            this.venue = venue;
            TEXT_BUBBLE = TextBubbleStyles.getTextBubbleStyles().get(venue.toString());
            TEXT_COLOUR=our;

        }
    }
}
