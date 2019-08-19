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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.mky.jfxmpi.DataHelpers.BotHelper;
import net.mky.jfxmpi.DataHelpers.ImageFactory;
import static net.mky.jfxmpi.TextBubble.WORDS_IN_ROW;
import net.mky.tools.StylesForAll;
import org.json.JSONArray;
import org.json.JSONObject;
import systemknowhow.human.Life;
import systemknowhow.human.LifeTagFactory;
import systemknowhow.human.Male;
import systemknowhow.human.guns.CompositeGun;
import systemknowhow.tools.HilbertCurvePatternDetect;

/**
 *
 * @author Manoj
 */
public class CharacterPane extends StackPane {

    private Rectangle rect;
    private double pressedX, pressedY;
    private LongProperty frame = new SimpleLongProperty();

    FileInputStream input;
    ImageView imageView;
    public String CharacterFile = "";
    Life thisCharcter;//=""
    Life otherCharcter;//=""
    
    //Copied from text bubble
    boolean SHOW_TEXT_BUBBLE = true;
    Circle cir2;
    boolean SHOW_IMG_BUBBLE = false;
    TextBubbleStyles.STYLES tbss=TextBubbleStyles.STYLES.PLAIN_PAPER;

    public void setOtherCharcter(Life otherCharcter) {
        this.otherCharcter = otherCharcter;
    }
    String name = "";
    String age = "";
    String SeedFile = "";
    LifeTagFactory ltf;
    public String CharacterPool[];

    Button selectCharPic;

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

    FileChooser fileChooser = new FileChooser();
    BotHelper botHelper;
    public CharacterPane(final int width, final int height, boolean border) {
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

Image image;
        try {
            input = new FileInputStream("C:/Users/Maneesh/Desktop/Emiko_1513270388079.png");
             image = new Image(input, 200, 300, true, true);
        } catch (FileNotFoundException ex) {
             //image = SwingFXUtils.toFXImage(HilbertCurvePatternDetect.getRandomImage(100, 500), null);
             image=SwingFXUtils.toFXImage(ImageFactory.getRandomBufferedImage(), null); //Image from factory
           // Logger.getLogger(CharacterPane.class.getName()).log(Level.SEVERE, null, ex);
        }

        imageView = new ImageView(image);

        getChildren().add(imageView);
        getChildren().add(chatMessage);
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
        //getChildren().add(taxachatMessage);
        StackPane.setAlignment(chatMessage, Pos.TOP_LEFT);
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

        EventHandler<ActionEvent> pickPic = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                //dialog.initStyle(StageStyle.TRANSPARENT);

                dialog.initOwner(null);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text("This is a Dialog"));
                //Show image gallery
                ScrollPane root = new ScrollPane();

                //Profile data
                //Creating a GridPane container
                GridPane grid = new GridPane();
                grid.setPadding(new Insets(10, 10, 10, 10));
                grid.setVgap(5);
                grid.setHgap(5);
                //Defining the Name text field
                final TextField nameFiled = new TextField(name);
                nameFiled.setPromptText("Name");
                nameFiled.setPrefColumnCount(10);
                nameFiled.getText();
                GridPane.setConstraints(nameFiled, 0, 0);
                grid.getChildren().add(nameFiled);
                //Defining the Last Name text field
                //final TextField lastName = new TextField();
                final ComboBox<LifeTagFactory> cbxStatus = new ComboBox<>();
                cbxStatus.getItems().setAll(LifeTagFactory.values());
                //lastName.setPromptText("Gender");
                GridPane.setConstraints(cbxStatus, 0, 1);
                grid.getChildren().add(cbxStatus);
                //Defining the Comment text field
                final TextField comment = new TextField(age);
                comment.setPrefColumnCount(4);
                comment.setPromptText("Age");
                GridPane.setConstraints(comment, 0, 2);
                grid.getChildren().add(comment);
                //Defining the Submit button
                EventHandler<ActionEvent> pickSeedFile = (ActionEvent event1) -> {
                    FileChooser fileChooser1 = new FileChooser();
                    fileChooser1.setTitle("Open Resource File");
                    File file = fileChooser1.showOpenDialog(new Stage());
                    //CharacterFile=file.getAbsolutePath();
                    if (file != null) {
                        SeedFile = file.getAbsolutePath();
                    }
                };
                Button submit = new Button("Seed File");
                GridPane.setConstraints(submit, 0, 3);
                submit.setOnAction(pickSeedFile);
                grid.getChildren().add(submit);
                
                
                //Defining the Submit button
                EventHandler<ActionEvent> editDressAction = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //Set nameFiled and other properties
                        final Stage dialog = new Stage();
                        dialog.initModality(Modality.APPLICATION_MODAL);
                        dialog.initOwner(null);
                        VBox dialogVbox = new VBox(20);
                        dialogVbox.getChildren().add(new Text("This is a Dialog"));

                        //Show image gallery
                        ScrollPane root = new ScrollPane();
                        TilePane tile = new TilePane();
                        root.setStyle("-fx-background-color: DAE6F3;");
                        tile.setPadding(new Insets(15, 15, 15, 15));
                        tile.setHgap(15);
                        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
                        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
                        root.setFitToWidth(true);
                        root.setContent(new DrawOnCanvas(imageView.getImage()));
//                    dialog.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
//                    dialog.setHeight(Screen.getPrimary().getVisualBounds()
//                            .getHeight());
                        Scene dialogScene = new Scene(root, 400, 400);
                        dialog.setScene(dialogScene);
                        dialog.show();
                    }
                };
                
                Button editDress = new Button("Dress Up");
                GridPane.setConstraints(editDress, 0, 4);
                submit.setOnAction(editDressAction);
                grid.getChildren().add(editDress);
                
//                //Defining the Clear button
                Label clear = new Label(SeedFile);
                GridPane.setConstraints(clear, 0, 5);
                grid.getChildren().add(clear);

                TilePane tile = new TilePane();
                root.setStyle("-fx-background-color: DAE6F3;");
                tile.setPadding(new Insets(15, 15, 15, 15));
                tile.setHgap(15);

                tile.getChildren().add(grid);

                EventHandler<ActionEvent> setImage = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        FileInputStream input = null;
                        try {
                            input = new FileInputStream(((Button) event.getSource()).getId());
                            Image image = new Image(input, 400, 600, true, true);
                            imageView.setImage(image);
                            CharacterFile = ((Button) event.getSource()).getId();
                            name = nameFiled.getText();
                            age = comment.getText();
                            ltf = cbxStatus.getValue();
                            thisCharcter = createLife(name, ltf, getLines(SeedFile));
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(CharacterPane.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            try {
                                input.close();
                            } catch (IOException ex) {
                                Logger.getLogger(CharacterPane.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                };
                for (final String character : CharacterPool) {
                    ImageView imageView;
                    imageView = createImageView(new File(character));
                    //imageView.setOnMouseClicked(selectAction);
                    VBox vbx = new VBox();
                    Button select = new Button("Select");
                    select.setId(character);
                    select.setOnAction(setImage);
                    vbx.getChildren().addAll(imageView, select);
                    tile.getChildren().addAll(vbx);
                }
                root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
                root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
                root.setFitToWidth(true);
                root.setContent(tile);
//                dialog.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
//                dialog.setHeight(Screen.getPrimary().getVisualBounds()
//                        .getHeight());
                Scene dialogScene = new Scene(root, 1024, 800);
                dialogScene.setFill(Color.TRANSPARENT);
                dialog.setScene(dialogScene);
                dialog.show();


            }
        };

        EventHandler<ActionEvent> addMultipleFilesAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                List<File> list = fileChooser.showOpenMultipleDialog(new Stage());
                if (list != null) {
                    CharacterPool = new String[list.size()];

                    for (int i = 0; i < CharacterPool.length; i++) {
                        CharacterPool[i] = list.get(i).getAbsolutePath();
                    }

                    //Set nameFiled and other properties
                    final Stage dialog = new Stage();
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.initOwner(null);
                    VBox dialogVbox = new VBox(20);
                    dialogVbox.getChildren().add(new Text("This is a Dialog"));
                    
                    //Show image gallery
                    ScrollPane root = new ScrollPane();
                    TilePane tile = new TilePane();
                    root.setStyle("-fx-background-color: DAE6F3;");
                    tile.setPadding(new Insets(15, 15, 15, 15));
                    tile.setHgap(15);
                    root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
                    root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
                    root.setFitToWidth(true);
                    root.setContent(new DrawOnCanvas());
//                    dialog.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
//                    dialog.setHeight(Screen.getPrimary().getVisualBounds()
//                            .getHeight());
                    Scene dialogScene = new Scene(root, 400, 400);
                    dialog.setScene(dialogScene);
                    dialog.show();
                }

            }
        };

        selectCharPic = new Button("*" + name);
        selectCharPic.getStyleClass().add("play");
        selectCharPic.setStyle(StylesForAll.aliveTheme);
        selectCharPic.setOnAction(pickPic);
        // mainPane.setCenter(homePane);
        HBox buttonPane = new HBox(12, selectCharPic);
        getChildren().add(selectCharPic);
        StackPane.setAlignment(selectCharPic, Pos.BOTTOM_RIGHT);

        Button addAllChars = new Button("Set All");
        addAllChars.setStyle(StylesForAll.aliveTheme);
        addAllChars.setOnAction(addMultipleFilesAction);
        getChildren().add(addAllChars);

        //changeImage( image,false);
        StackPane.setAlignment(addAllChars, Pos.TOP_RIGHT);

        EventHandler<MouseEvent> imgMove3 = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                pressedX = event.getX() + imageView.getX();
                pressedY = event.getY() + imageView.getY();
            }
        };

        EventHandler<MouseEvent> imgMove4 = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                imageView.setTranslateX(imageView.getTranslateX() + event.getX() - pressedX);
                imageView.setTranslateY(imageView.getTranslateY() + event.getY() - pressedY);

                chatMessage.setTranslateX(chatMessage.getTranslateX() + event.getX() - pressedX);
                chatMessage.setTranslateY(chatMessage.getTranslateY() + event.getY() - pressedY);
                event.consume();
            }
        };

        imageView.setOnMouseClicked(imgMove3);
        imageView.setOnMouseDragged(imgMove4);

        //Image resizing
        EventHandler<ActionEvent> inc = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                float scaleInc = (float) (imageView.getScaleX() + imageView.getScaleX() * .1);
                Scale scale = new Scale(scaleInc, scaleInc);
                imageView.getTransforms().add(scale); //rotate by 45 degrees
            }
        };

        EventHandler<ActionEvent> dec = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                float scaleInc = (float) (imageView.getScaleX() - imageView.getScaleX() * .1);
                Scale scale = new Scale(scaleInc, scaleInc);
                imageView.getTransforms().add(scale); //rotate by 45 degrees
            }
        };

        Button incSize = new Button("+");
        incSize.setStyle(StylesForAll.pigletTheme);
        incSize.setOnAction(inc);
        getChildren().add(incSize);
        StackPane.setAlignment(incSize, Pos.TOP_LEFT);
        Button decSize = new Button("-");
        decSize.setOnAction(dec);
        decSize.setStyle(StylesForAll.pigletTheme);
        getChildren().add(decSize);
        StackPane.setAlignment(decSize, Pos.BOTTOM_LEFT);

        EventHandler<ActionEvent> flipAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Has issues with drag translation
                //imageView.setScaleX(-1*imageView.getScaleX());
                imageView.setTranslateX(pressedX);
                imageView.setTranslateY(pressedY);
            }
        };
        Button flipImg = new Button("/");
        flipImg.setOnAction(flipAction);
        flipImg.setStyle(StylesForAll.transparentAlive);
        getChildren().add(flipImg);
        StackPane.setAlignment(flipImg, Pos.BOTTOM_CENTER);

        EventHandler<ActionEvent> showHide = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                imageView.setVisible(!imageView.isVisible());
                chatMessage.setVisible(!chatMessage.isVisible());
                selectCharPic.setVisible(!selectCharPic.isVisible());
//                chatMessage.setText((String) thisCharcter.talk(otherCharcter, name).toArray()[0]);
            }
        };

        Button showHideImg = new Button("[-]");
        showHideImg.setOnAction(showHide);
        showHideImg.setStyle(StylesForAll.transparentAlive);
        getChildren().add(showHideImg);
        StackPane.setAlignment(showHideImg, Pos.TOP_CENTER);
        
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
//        getChildren().add(chatMessage);
        
        cir2.setVisible(SHOW_IMG_BUBBLE);
        chatMessage.setVisible(SHOW_TEXT_BUBBLE);

    }

    public void changeImage(Image image, boolean replace) {
        Canvas canvas = new Canvas(W, H);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawWithShadowUsingStencil(gc, image, IMG_X, IMG_Y, SHADOW_LENGTH, SHADOW_COLOR);
        //   drawWithShadowUsingColorAdjust(gc, image, IMG_X, IMG_Y, SHADOW_LENGTH);
        if (replace) {
            getChildren().remove(getChildren().size() - 1);
        }
        getChildren().add(getChildren().size() - 1, canvas);
        //return canvas;
    }

    private void drawWithShadowUsingColorAdjust(GraphicsContext gc, Image image, double x, double y, int shadowLength) {
        // here the color adjust for the shadow is based upon the intensity of the input image color
        // which is a weird way to calculate a shadow color, but does come out nicely
        // because it appropriately handles antialiased input images.
        ColorAdjust monochrome = new ColorAdjust();
        monochrome.setBrightness(+0.5);
        monochrome.setSaturation(-1.0);

        gc.setEffect(monochrome);

        for (int offset = shadowLength; offset > 0; --offset) {
            gc.drawImage(image, x + offset, y + offset / SHADOW_SLOPE_FACTOR);
        }

        gc.setEffect(null);

        gc.drawImage(image, x, y);
    }

    private void drawWithShadowUsingStencil(GraphicsContext gc, Image image, double x, double y, int shadowLength, Color shadowColor) {
        Image shadow = createShadowImage(image, shadowColor);

        for (int offset = shadowLength; offset > 0; --offset) {
            gc.drawImage(shadow, x + offset, y + offset / SHADOW_SLOPE_FACTOR);
        }

        gc.drawImage(image, x, y);
    }

    private Image createShadowImage(Image image, Color shadowColor) {
        WritableImage shadow = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
        PixelReader reader = shadow.getPixelReader();
        PixelWriter writer = shadow.getPixelWriter();
        for (int ix = 0; ix < image.getWidth(); ix++) {
            for (int iy = 0; iy < image.getHeight(); iy++) {
                int argb = reader.getArgb(ix, iy);
                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                // because we use a binary choice, we lose anti-alising info in the shadow so it looks a bit jaggy.
                Color fill = (r > 0 || g > 0 || b > 0) ? shadowColor : Color.TRANSPARENT;

                writer.setColor(ix, iy, fill);
            }
        }
        return shadow;
    }

    private Image getImage() {
        Label label = new Label("a");
        label.setStyle("-fx-text-fill: forestgreen; -fx-background-color: transparent; -fx-font-size: " + FONT_SIZE + "px;");
        Scene scene = new Scene(label, Color.TRANSPARENT);
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        return label.snapshot(snapshotParameters, null);
    }

    private ImageView createImageView(final File imageFile) {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower)
        // resizing

        ImageView imageView = null;
        try {
            final Image image = new Image(new FileInputStream(imageFile), 150, 0, true,
                    true);
            imageView = new ImageView(image);
            imageView.setFitWidth(150);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent mouseEvent) {

                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {

                        if (mouseEvent.getClickCount() == 2) {
                            try {
                                BorderPane borderPane = new BorderPane();
                                ImageView imageView = new ImageView();
                                //Image image = new Image(new FileInputStream(imageFile));
                                Image image = new Image(new FileInputStream(imageFile), 200, 400, true, true);
                                imageView.setImage(image);
                                imageView.setStyle("-fx-background-color: BLACK");
                                imageView.setFitHeight(1024 - 10);
                                imageView.setPreserveRatio(true);
                                imageView.setSmooth(true);
                                imageView.setCache(true);
                                borderPane.setCenter(imageView);
                                borderPane.setStyle("-fx-background-color: BLACK");
                                Stage newStage = new Stage();
                                newStage.setWidth(1024);
                                newStage.setHeight(800);
                                newStage.setTitle(imageFile.getName());
                                Scene scene = new Scene(borderPane, Color.BLACK);
                                newStage.setScene(scene);
                                newStage.show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            });
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return imageView;
    }

    private Life createLife(String name, LifeTagFactory gender, String[] learnToTalk) {
        Male male1 = new Male(name, 40, 5.5, new double[]{5});
        male1.GUN = new CompositeGun(.1f, .1f, -.3f);
        male1.TAG = gender;
        male1.learnToSentence(learnToTalk);

        return male1;
    }

    public Life getLife() {
        return thisCharcter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setLtf(LifeTagFactory ltf) {
        this.ltf = ltf;
    }

    public JSONObject getCharacterData() {
        JSONObject data = new JSONObject();
        data.put("name", name);
        data.put("age", age);
        data.put("ltf", ltf);
        data.put("cpane", CharacterFile);
        data.put("SeedFile", SeedFile);
        data.put("currentText", chatMessage.getText());
        JSONArray charlist = new JSONArray(CharacterPool);
        data.put("CharacterPool", charlist);
        return data;
    }
public void refresh(){
     botHelper=new BotHelper(name);
     selectCharPic.setText(name);
     chatMessage.setText(StoryTimeline.textReformat(botHelper.chatSession.multisentenceRespond("What is your name?")+"\n"+"You will be glad to know that I can learn while I chat!").toString());
    
}
    public void loadCharacterData(JSONObject data) {
        name = data.has("name") ? data.getString("name") : name;
        age = data.has("age") ? data.getString("age") : age;
        chatMessage.setText( data.has("currentText") ? data.getString("currentText") : "I am ready to play!");
        SeedFile = data.has("SeedFile") ? data.getString("SeedFile") : SeedFile;
        ltf = data.has("ltf") ? LifeTagFactory.valueOf(data.getString("ltf")) : ltf;
        CharacterFile = data.has("cpane") ? data.getString("cpane") : CharacterFile;
        if (data.has("CharacterPool")) { //String[] charlist = projData.getJSONArray("charlist").toString().replace("},{", " ,").split(" ");
            List<String> list = new ArrayList<String>();
            for (int i = 0; i < data.getJSONArray("CharacterPool").length(); i++) {
                list.add(data.getJSONArray("CharacterPool").getString(i));
            }
            CharacterPool = list.toArray(new String[0]);
        }

        thisCharcter = createLife(name, ltf, getLines(SeedFile));
        selectCharPic.setText(name);

        try {
            input = new FileInputStream(CharacterFile);
            Image image = new Image(input, 400, 600, true, true);
            imageView.setImage(image);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CharacterPane.class.getName()).log(Level.SEVERE, null, ex);
        }

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
    
  private void showInputTextDialog() {
 
        TextInputDialog dialog = new TextInputDialog("Tran");
 
       // dialog.setTitle("o7planning");
        dialog.setHeaderText("Enter text:");
        dialog.setContentText("Text:");
 
        Optional<String> result = dialog.showAndWait();
 
        result.ifPresent(name -> {
            String[] words=name.split(" ");
            int closestMatch=(int) Math.sqrt(words.length);
            int i=0;
            StringBuilder sb=new StringBuilder();
            for(String word:words){
                if(i<closestMatch){
                    sb.append(word).append(" ");
                    i++;
                }else{
                    i=0;
                    sb.append(word).append("\n");
                }
                
            }
            this.chatMessage.setText(sb.toString());
        });
    }
  
   private void showBubbleSelectionDialog() {
        Dialog<TextBubble.ResultsOfDialog> dialog = new Dialog<>();
        dialog.setTitle("Dialog Test");
        dialog.setHeaderText("Please specify…");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        //TextField textField = new TextField("Name");
        TextArea taxachatMessage = new TextArea(this.chatMessage.getText());
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
        dialogPane.setContent(new VBox(8, taxachatMessage, comboBox, new HBox(new VBox(cText, cImage,pasteButton, playButton), imageView)));
        Platform.runLater(taxachatMessage::requestFocus);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return new TextBubble.ResultsOfDialog(taxachatMessage.getText(), comboBox.getValue());
            }
            return null;
        });
        Optional<TextBubble.ResultsOfDialog> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((TextBubble.ResultsOfDialog results) -> {
            this.chatMessage.setText(results.SpeechText);
            this.chatMessage.setEffect(new DropShadow(+25d, 0d, +2d, Color.BLANCHEDALMOND));
            this.chatMessage.setStyle("-fx-shape: \"" + results.TEXT_BUBBLE + "\";\n"
                    + "    -fx-background-color: black, white;\n"
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

}
