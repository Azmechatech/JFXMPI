/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import com.truegeometry.mkhilbertml.HilbertCurvePatternDetect;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.web.HTMLEditor;
import javafx.util.StringConverter;
import net.mky.tools.StylesForAll;
import org.json.JSONArray;
import systemknowhow.human.*;


/**
 *
 * @author Manoj
 */
public class StoryBoard extends StackPane {

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

    TextArea textArea1 = new TextArea("I have an ugly white background :-(");
    TextArea textArea2 = new TextArea("I have an ugly white background :-(");
    TextArea textArea3 = new TextArea("I have an ugly white background :-(");
    
    public ImageView imageChar1=new ImageView();
    public ImageView imageChar2=new ImageView();
    public ImageView imageChar3=new ImageView();
            
    public List<ImageView> charImages;
    public List<TextArea> charTextAreas;
    public HashMap<String,String> talkTo=new HashMap<>();
    GraphPane gp=new GraphPane();
    private HTMLEditor htmlEditor;
    Slider slider ;
    
    JSONArray sliderTexts;
    
    Environment env=new Environment();
        String textAreaStyle="-fx-shape: \"M617.796387,96.331444c0,-10.000282 -13.401611,-18.051064 -30.04834,-18.051064l-471.95871,0c-16.64682,0 -30.048409,8.050781 -30.048409,18.051064l0,14.354454l-70.861718,23.273758l70.861718,7.245163l0,73.639435c0,10.00029 13.401588,18.051056 30.048409,18.051056l471.95871,0c16.646729,0 30.04834,-8.050766 30.04834,-18.051056l0,-118.51281Z \";\n"
                + "    -fx-background-color: black, white;\n"
            + "-fx-control-inner-background: linear-gradient(to bottom, #f2994a, #f2c94c);;"
                + "    -fx-background-insets: 0,1;\n"
                 + "    -fx-font-family: \"Helvetica\";\n"
                    + "    -fx-font-size: 18px;\n"
                    + "    -fx-font-weight: bold;\n"
                + "    -fx-padding: 2 10 2 35";


    public ConversationMaker getConvMaker() {
        return convMaker;
    }

    public StoryBoard(int width, int height) {
        charImages= new ArrayList<ImageView>();
        charTextAreas=new ArrayList<TextArea>();
        
        htmlEditor = new HTMLEditor();
        htmlEditor.setHtmlText("Text<img src='urltosmile'/>Text Text Text");
        sliderTexts=new JSONArray();
        sliderTexts.put("Observe");sliderTexts.put("Introduction");sliderTexts.put("Conflict");sliderTexts.put("Win");
        slider =  new Slider(0, sliderTexts.length(), 0);
        slider.setMin(0);
        slider.setMax(sliderTexts.length());
        slider.setValue(1);
        slider.setMinorTickCount(0);
        slider.setMajorTickUnit(1);
        slider.setSnapToTicks(true);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
         slider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double n) {
                
                for(int i=0;i<sliderTexts.length();i++){
                    if (n < i) return sliderTexts.getString(i);
                }
//                
//                if (n < 0.5) return "Observe";
//                if (n < 1.5) return "Introduction";
//                if (n < 2.5) return "Conflict";
//                if (n < 3.5) return "Win";
//                if (n < 4.5) return "Reveal";
//                if (n < 5.5) return "Experiance";

                return "Reveal";
            }

            @Override
            public Double fromString(String s) {
                for(int i=0;i<sliderTexts.length();i++){
                    if (s.equalsIgnoreCase(sliderTexts.getString(i))) return (double)i;
                }
                
//                switch (s) {
//                    case "Observe":
//                        return 0d;
//                    case "Introduction":
//                        return 1d;
//                    case "Conflict":
//                        return 2d;
//                    case "Win":
//                        return 3d;
//                    case "Reveal":
//                        return 4d;
//                    case "Experiance":
//                        return 5d;
//                    default:
//                        return 2d;
//                }
                return 1d;
            }
        });
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
            setStyle("-fx-background-color:linear-gradient(to right, #2c3e50, #3498db);");
            setStyle("-fx-background-color:linear-gradient(to bottom, #f2994a, #f2c94c);");
           // setBackground(background);
            TextArea textArea = new TextArea();

            charBText.setAlignment(Pos.BASELINE_RIGHT);

            slider.setStyle(" "
                    + "    -fx-font-family: \"Helvetica\";\n"
                    + "    -fx-font-size: 18px;\n"
                    + "    -fx-font-weight: bold;\n"
                    + "    -fx-text-fill: linear-gradient(to bottom, #bc4e9c, #f80759);\n" + "-fx-background-color: transparent");
            
            charAText.setStyle(" "
                    + "    -fx-font-family: \"Helvetica\";\n"
                    + "    -fx-font-size: 18px;\n"
                    + "    -fx-font-weight: bold;\n"
                    + "    -fx-text-fill: black;\n" + "-fx-background-color: transparent");
            charBText.setStyle("  "
                    + "    -fx-font-family: \"Helvetica\";\n"
                    + "    -fx-font-size: 18px;\n"
                    + "    -fx-font-weight: bold;\n"
                    + "    -fx-text-fill: black;\n" + "-fx-background-color: transparent");
            charAText1.setStyle("    "
                    + "    -fx-font-family: \"Helvetica\";\n"
                    + "    -fx-font-size: 18px;\n"
                    + "    -fx-font-weight: bold;\n"
                    + "    -fx-text-fill: black;\n" + "-fx-background-color: transparent");
            charBText1.setStyle("   "
                    + "    -fx-font-family: \"Helvetica\";\n"
                    + "    -fx-font-size: 18px;\n"
                    + "    -fx-font-weight: bold;\n"
                    + "    -fx-text-fill: black;\n" + "-fx-background-color: transparent");
            

    // we don't use lambdas to create the change listener since we use
    // the instance twice via 'this' (see *)
    textArea.skinProperty().addListener(new ChangeListener<Skin<?>>() {

        @Override
        public void changed(
          ObservableValue<? extends Skin<?>> ov, Skin<?> t, Skin<?> t1) {
            if (t1 != null && t1.getNode() instanceof Region) {
                Region r = (Region) t1.getNode();
                r.setBackground(Background.EMPTY);

                r.getChildrenUnmodifiable().stream().
                        filter(n -> n instanceof Region).
                        map(n -> (Region) n).
                        forEach(n -> n.setBackground(Background.EMPTY));

                r.getChildrenUnmodifiable().stream().
                        filter(n -> n instanceof Control).
                        map(n -> (Control) n).
                        forEach(c -> c.skinProperty().addListener(this)); // *
            }
        }
    });

            textArea1.setStyle(textAreaStyle);
            textArea1.setPrefRowCount(3);
            textArea1.setPrefColumnCount(10);
            textArea1.setWrapText(true);
            
            textArea2.setStyle(textAreaStyle);
            textArea2.setPrefRowCount(5);
            textArea2.setPrefColumnCount(10);
            textArea2.setWrapText(true);
            
            textArea3.setStyle(textAreaStyle);
            textArea3.setPrefRowCount(5);
            textArea3.setPrefColumnCount(10);
            textArea3.setWrapText(true);
        //textArea.setPrefWidth(150);
        Image imageChar;
            try {
            input = new FileInputStream("C:/Users/Maneesh/Desktop/Emiko_1513270388079.png");
            imageChar = new Image(input, 200, 300, false, true);
        } catch (FileNotFoundException ex) {
             imageChar = SwingFXUtils.toFXImage(HilbertCurvePatternDetect.getRandomImage(100, 500), null);
            Logger.getLogger(CharacterPane.class.getName()).log(Level.SEVERE, null, ex);
        }
       

        //Image image = getImage();
        imageView = new ImageView(imageChar);
            VBox vbox = new VBox(slider);
            vbox.setSpacing(1.0);
           // HBox theChatBox=new HBox();
            
           // vbox.getChildren().add(charBText1);

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
//                    textArea1.setText(convMaker.getNextSenetce());
//                    textArea2.setText(convMaker.getNextSenetce());
//                    textArea3.setText(convMaker.getNextSenetce());
                    for(TextArea tx:charTextAreas){
                        
                        tx.setText(convMaker.getNextSenetce());
                        //"I want to "+convMaker.getOutdoorAction()+" "+
                        
                    }
                    
//                    if(convMaker.ATalks){charAText.setText(convMaker.getNextSenetce()); }
//                    else charBText.setText(convMaker.getNextSenetce());
                }
            };
            Button playButton = new Button(">");
            playButton.getStyleClass().add("play");
            playButton.setStyle(StylesForAll.transparentAlive);
            playButton.setOnAction(goAction);
            theChatBox.getChildren().add(playButton);
//            theChatBox.getChildren().add( imageChar1);
//            theChatBox.getChildren().add(textArea1);
//            //theChatBox.getChildren().add( vbox);
//            theChatBox.getChildren().add( imageChar2);
//            theChatBox.getChildren().add( textArea2);
//            theChatBox.getChildren().add( imageChar3);
//            theChatBox.getChildren().add( textArea3);
           // getChildren().add(theChatBox);
            
            
            
  
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StoryBoard.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
//            try {
//                input.close();
//            } catch (IOException ex) {
//                Logger.getLogger(StoryBoard.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }

        convMaker = new ConversationMaker();
        getChildren().add(theChatBox);
    }
    HBox theChatBox = new HBox();
    public void addCharacter(ImageView imageView,String Character){
        
        theChatBox.getChildren().add(imageView);
        TextArea textArea = new TextArea("I am " + Character+". I love to play this game.");
        textArea.setStyle(textAreaStyle);
        textArea.setPrefRowCount(3);
        textArea.setPrefColumnCount(10);
        textArea.setWrapText(true);
        theChatBox.getChildren().add(textArea);

        charImages.add(imageView);
        charTextAreas.add(textArea);
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
