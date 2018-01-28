/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.mky.tools.StylesForAll;
import systemknowhow.human.*;

/**
 *
 * @author Manoj
 */
public class TextPane extends StackPane {

    private Rectangle rect;
    private double pressedX, pressedY;
    private LongProperty frame = new SimpleLongProperty();

    FileInputStream input;
    ImageView imageView;

    private static final double W = 400;
    private static final double H = 400;
    private static final int SHADOW_LENGTH = 100;

    private static final double IMG_X = 20;
    private static final double IMG_Y = 20;

    private static final int FONT_SIZE = 200;

    private static final double SHADOW_SLOPE_FACTOR = 1.5;

    Color SHADOW_COLOR = Color.GRAY.brighter();
    public ConversationMaker convMaker = new ConversationMaker();
    TextField charAText = new TextField("Let me start the talk");
    TextField charBText = new TextField("OK, go ahead and start");
    TextField charAText1 = new TextField("Let me start the talk");
    TextField charBText1 = new TextField("OK, go ahead and start");

    public ConversationMaker getConvMaker() {
        return convMaker;
    }

    public TextPane(int width, int height) {
        FileInputStream input = null;
        try {
            setMinSize(300, 100);
            setBorder(new Border(new BorderStroke(Color.GOLDENROD,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            String image = "C:/Users/Maneesh/Desktop/bpForText.jpg";
//            setStyle("-fx-padding: 10;" + "-fx-background-image: url('" + image + "'); " +
//                    "-fx-border-style: solid inside;" +
//                    "-fx-border-width: 10;" +
//                    "-fx-border-insets: 0;" +
//                    "-fx-border-radius: 5;" +
//                    "-fx-border-color: GOLDENROD;");
            // new Image(url)
            File file = new File(image);
            input = new FileInputStream(file.getAbsolutePath());
            // new BackgroundSize(width, height, widthAsPercentage, heightAsPercentage, contain, cover)
            BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
            // new BackgroundImage(image, repeatX, repeatY, position, size)
            BackgroundImage backgroundImage = new BackgroundImage(new Image(input), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
            // new Background(images...)
            Background background = new Background(backgroundImage);
            setBackground(background);
            TextArea textArea = new TextArea();

            charBText.setAlignment(Pos.BASELINE_RIGHT);

            charAText.setStyle(" "
                    + "    -fx-font-family: \"Helvetica\";\n"
                    + "    -fx-font-size: 24px;\n"
                    + "    -fx-font-weight: bold;\n"
                    + "    -fx-text-fill: black;\n" + "-fx-background-color: transparent");
            charBText.setStyle("  "
                    + "    -fx-font-family: \"Helvetica\";\n"
                    + "    -fx-font-size: 24px;\n"
                    + "    -fx-font-weight: bold;\n"
                    + "    -fx-text-fill: black;\n" + "-fx-background-color: transparent");
            charAText1.setStyle("    "
                    + "    -fx-font-family: \"Helvetica\";\n"
                    + "    -fx-font-size: 24px;\n"
                    + "    -fx-font-weight: bold;\n"
                    + "    -fx-text-fill: black;\n" + "-fx-background-color: transparent");
            charBText1.setStyle("   "
                    + "    -fx-font-family: \"Helvetica\";\n"
                    + "    -fx-font-size: 24px;\n"
                    + "    -fx-font-weight: bold;\n"
                    + "    -fx-text-fill: black;\n" + "-fx-background-color: transparent");
            VBox vbox = new VBox(charAText);
            vbox.setSpacing(1.0);
            vbox.getChildren().add(charBText);
            vbox.getChildren().add(charAText1);
            vbox.getChildren().add(charBText1);

            //getChildren().add(charBText);
            //StackPane.setAlignment(imageView, Pos.TOP_LEFT);
            setOnMousePressed(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    pressedX = event.getX();
                    pressedY = event.getY();
                }
            });
            EventHandler<ActionEvent> goAction = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(convMaker.ATalks)charAText.setText(convMaker.getNextSenetce());
                    else charBText.setText(convMaker.getNextSenetce());
                }
            };
            Button playButton = new Button("Next>>");
            playButton.getStyleClass().add("play");
            playButton.setStyle(StylesForAll.transparentAlive);
            playButton.setOnAction(goAction);
            vbox.getChildren().add(playButton);
            getChildren().add(vbox);
//            // mainPane.setCenter(homePane);
//            HBox buttonPane = new HBox(12, playButton);
//            getChildren().add(buttonPane);
//            //changeImage( image,false);
//            StackPane.setAlignment(buttonPane, Pos.BOTTOM_CENTER);

//    Timeline t = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
//      @Override public void handle(ActionEvent event) {
//        frame.set(frame.get() + 1);
//
//        if (rect != null) {
//          getChildren().remove(rect);
//        }
//
//        rect = new Rectangle(10, 10, 200, 200);
//        rect.setFill(Color.RED);
//        rect.setMouseTransparent(true);
//        getChildren().add(0, rect);
//      }
//    }));
//    t.setCycleCount(Timeline.INDEFINITE);
//    t.play();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TextPane.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                Logger.getLogger(TextPane.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        convMaker = new ConversationMaker();
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
}
