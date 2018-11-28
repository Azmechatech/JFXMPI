/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;

import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import net.mky.graphUI.CellType;
import net.mky.graphUI.Graph;
import net.mky.tools.MediaControl;
import net.mky.tools.Model;
import net.mky.tools.StylesForAll;
import systemknowhow.tools.HilbertCurvePatternDetect;

/**
 *
 * @author Manoj
 */
public class MainPane extends StackPane {
  private Rectangle rect;
  private double pressedX, pressedY;
  private LongProperty frame = new SimpleLongProperty();
  public String bgImageFile="";
  public String bgFiles[];
  FileInputStream input ;
  ImageView imageView;
  Graph graph;
  private MediaPlayer mediaPlayer;
  private static final String MEDIA_URL = "http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv";
  public void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setScene(new Scene(root));
        mediaPlayer = new MediaPlayer(new Media(MEDIA_URL));
        mediaPlayer.setAutoPlay(true);
        MediaControl mediaControl = new MediaControl(mediaPlayer);
        mediaControl.setMinSize(480,280);
        mediaControl.setPrefSize(480,280);
        mediaControl.setMaxSize(480,280);
        root.getChildren().add(mediaControl);
        mediaPlayer.play();
    }
  
  public MainPane() {
      graph = new Graph();
      addGraphComponents();
      
    setMinSize(600, 600);
     
    //setStyle("-fx-border-color: blue;");
    Label count = new Label();
    count.textProperty().bind(Bindings.convert(frame));
    //getChildren().add(count);
    count.setMouseTransparent(true);

   
    
    Image image ;
      try {
          input = new FileInputStream("C:/Users/Maneesh/Desktop/bpForText.jpg");
          image = new Image(input);
      } catch (FileNotFoundException ex) {
           image = SwingFXUtils.toFXImage(HilbertCurvePatternDetect.getRandomImage(100, 500), null);
          Logger.getLogger(MainPane.class.getName()).log(Level.SEVERE, null, ex);
      }
      
        
         imageView = new ImageView();
        //imageView.getTransforms().add(new Rotate(45,0,0)); //rotate by 45 degrees
        
         Scale scale = new Scale(1,1); 
         imageView.getTransforms().add(scale); //rotate by 45 degrees
         BoxBlur bb = new BoxBlur();
            bb.setWidth(5);
            bb.setHeight(5);
            bb.setIterations(3);
         //imageView.setEffect(bb);
         
        getChildren().add(imageView);
        
        //getChildren().add(graph.getScrollPane());

      
    setOnMousePressed(new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        pressedX = event.getX();
        pressedY = event.getY();
      }
    });

    setOnMouseDragged(new EventHandler<MouseEvent>() {
      public void handle(MouseEvent event) {
        setTranslateX(getTranslateX() + event.getX() - pressedX);
        setTranslateY(getTranslateY() + event.getY() - pressedY);

        event.consume();
      }
    });
    
    EventHandler<ActionEvent> goAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                float scaleInc=(float) (imageView.getScaleX()+imageView.getScaleX()*.1);
                Scale scale = new Scale(scaleInc,scaleInc);
                imageView.getTransforms().add(scale); //rotate by 45 degrees
            }
        };

    EventHandler<ActionEvent> goAction2 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                float scaleInc=(float) (imageView.getScaleX()-imageView.getScaleX()*.1);
                Scale scale = new Scale(scaleInc,scaleInc);
                imageView.getTransforms().add(scale); //rotate by 45 degrees
            }
        };
      
    
      EventHandler<ActionEvent> blurMore = new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {

              BoxBlur bb = new BoxBlur();
              bb.setWidth(5);
              bb.setHeight(5);
              bb.setIterations(3);

//                float scaleInc=(float) (imageView.getScaleX()-imageView.getScaleX()*.1);
//                Scale scale = new Scale(scaleInc,scaleInc);
              imageView.setEffect(bb);
          }
      };
    
     EventHandler<ActionEvent> blurNoMore = new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {

              BoxBlur bb = new BoxBlur();
              bb.setWidth(0);
              bb.setHeight(0);
              bb.setIterations(0);

//                float scaleInc=(float) (imageView.getScaleX()-imageView.getScaleX()*.1);
//                Scale scale = new Scale(scaleInc,scaleInc);
              imageView.setEffect(bb);
          }
      };
     
        Button playButton = new Button("+");
        playButton.getStyleClass().add("play");
        playButton.setOnAction(goAction);
        getChildren().add(playButton);
        StackPane.setAlignment(playButton, Pos.TOP_LEFT);
        Button playButton2 = new Button("-");
        playButton2.getStyleClass().add("play");
        playButton2.setOnAction(goAction2);
        getChildren().add(playButton2);
        StackPane.setAlignment(playButton2, Pos.BOTTOM_LEFT);
        Button blr = new Button("#");
        blr.setStyle(StylesForAll.transparentAlive);
        blr.setOnAction(blurMore);
        getChildren().add(blr);
        StackPane.setAlignment(blr, Pos.BOTTOM_RIGHT);
        
        Button blrNo = new Button("=");
        blrNo.setStyle(StylesForAll.transparentAlive);
        blrNo.setOnAction(blurNoMore);
        getChildren().add(blrNo);
        StackPane.setAlignment(blrNo, Pos.TOP_RIGHT);
        
        
       //         Seed text field
       TextArea textArea = new TextArea("I have an ugly white background :-(");
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
        //textField.setStyle(StylesForAll.buttonBoldBlack);
        //VBox vbox = new VBox(textField);
        //vbox.getChildren().add(textField);
//        getChildren().add(textArea);
//        StackPane.setAlignment(textArea, Pos.TOP_CENTER);
        
//        //Circile Animation
//        Group circles = new Group();
//        for (int i = 0; i < 30; i++) {
//            Circle circle = new Circle(150, Color.web("white", 0.05));
//            circle.setStrokeType(StrokeType.OUTSIDE);
//            circle.setStroke(Color.web("white", 0.16));
//            circle.setStrokeWidth(4);
//            circles.getChildren().add(circle);
//        }
//        Rectangle colors = new Rectangle(getWidth(),getHeight(),
//                new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, new Stop[]{
//                    new Stop(0, Color.web("#f8bd55")),
//                    new Stop(0.14, Color.web("#c0fe56")),
//                    new Stop(0.28, Color.web("#5dfbc1")),
//                    new Stop(0.43, Color.web("#64c2f8")),
//                    new Stop(0.57, Color.web("#be4af7")),
//                    new Stop(0.71, Color.web("#ed5fc2")),
//                    new Stop(0.85, Color.web("#ef504c")),
//                    new Stop(1, Color.web("#f2660f")),}));
//        colors.widthProperty().bind(widthProperty());
//        colors.heightProperty().bind(heightProperty());
//        Group blendModeGroup =
//                new Group(new Group(new Rectangle(getWidth(), getHeight(),
//                     Color.BLACK), circles), colors);
//        colors.setBlendMode(BlendMode.OVERLAY);
//        getChildren().add(blendModeGroup);      
//        circles.setEffect(new BoxBlur(10, 10, 3));
//        Timeline timeline = new Timeline();
//        for (Node circle : circles.getChildren()) {
//            timeline.getKeyFrames().addAll(
//                    new KeyFrame(Duration.ZERO, // set start position at 0
//                    new KeyValue(circle.translateXProperty(), random() * 800),
//                    new KeyValue(circle.translateYProperty(), random() * 600)),
//                    new KeyFrame(new Duration(40000), // set end position at 40s
//                    new KeyValue(circle.translateXProperty(), random() * 800),
//                    new KeyValue(circle.translateYProperty(), random() * 600)));
//        }
//        // play 40s of animation
//        timeline.play();
        
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
  }
  
  private void addGraphComponents() {

        Model model = graph.getModel();

        graph.beginUpdate();

        model.addCell("Cell A", CellType.RECTANGLE);
        model.addCell("Cell B", CellType.RECTANGLE);
        model.addCell("Cell C", CellType.RECTANGLE);
        model.addCell("Cell D", CellType.TRIANGLE);
        model.addCell("Cell E", CellType.TRIANGLE);
        model.addCell("Cell F", CellType.RECTANGLE);
        model.addCell("Cell G", CellType.RECTANGLE);

        model.addEdge("Cell A", "Cell B");
        model.addEdge("Cell A", "Cell C");
        model.addEdge("Cell B", "Cell C");
        model.addEdge("Cell C", "Cell D");
        model.addEdge("Cell B", "Cell E");
        model.addEdge("Cell D", "Cell F");
        model.addEdge("Cell D", "Cell G");

        graph.endUpdate();

    }
}