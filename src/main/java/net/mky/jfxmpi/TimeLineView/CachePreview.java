/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi.TimeLineView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import static net.mky.jfxmpi.MainApp.awtImageToFX;
import static net.mky.jfxmpi.MainApp.getImageFromClipboard;
import net.mky.jfxmpi.TextBubble;
import net.mky.safeStore.MapDB;
import net.mky.tools.StylesForAll;

/**
 *
 * @author mkfs
 */
public class CachePreview extends Application {

    Stage stage;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        ScrollPane root = new ScrollPane();
        TilePane tile = new TilePane();

        root.setStyle("-fx-background-color: DAE6F3;");
        tile.setPadding(new Insets(15, 15, 15, 15));
        tile.setHgap(15);

        Label Status = new Label("Image indexing");

        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        MapDB mapdb = new MapDB(selectedFile.getAbsolutePath(), true);

        tile.getChildren().clear();//Clear window
        Button previewDeleteDuplicateButton = new Button("Preview results");
        previewDeleteDuplicateButton.setOnAction(e -> {
            tile.getChildren().clear();//Clear window
            mapdb.index.entrySet().stream().forEach(fileSig -> {
                ImageView imageView;
                ByteArrayInputStream bis = new ByteArrayInputStream(mapdb.store.get(fileSig.getKey()[0].toString()).getBytes());
                InputStream in = Base64.getDecoder().wrap(bis);
                Image image = new Image(in);
                try {
                    bis.close();
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(CachePreview.class.getName()).log(Level.SEVERE, null, ex);
                }
//               
//                
//                ImageView imageViewFull = new ImageView(image);
//                imageViewFull.setFitWidth(100);
//                imageViewFull.setFitHeight(100);
//                imageViewFull.setPreserveRatio(true);
//                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Image", ButtonType.OK);
//                alert.setGraphic(imageViewFull);
//
//                alert.showAndWait();
                
                imageView = createImageViewV2(image, mapdb, fileSig.getKey()[0].toString(), stage);
                tile.getChildren().addAll(imageView);


            });

        });
        
        
        
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
        root.setFitToWidth(true);
        root.setContent(tile);

    
        Status.setStyle(StylesForAll.transparentAlive);
        

        HBox statusBox=new HBox(Status);
        VBox vbox = new VBox(statusBox,previewDeleteDuplicateButton, root);
        vbox.setStyle("-fx-background-color: linear-gradient(to right, rgb(203,53,107,0.25), rgb(189,63,50,0.25));");

        primaryStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth()/2);
        primaryStage.setHeight(Screen.getPrimary().getVisualBounds()
                .getHeight());
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String... args) throws IOException {
        launch(args);

    }

    public static ImageView createImageViewV2(Image image, MapDB mapdb, String bigImgKey, Stage stage) {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower)
        // resizing

        ImageView imageView = null;
        try {
            imageView = new ImageView(image);
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent mouseEvent) {

                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {

                        if (mouseEvent.getClickCount() == 1) {
                            try {
                                BorderPane borderPane = new BorderPane();
                                
                                ImageView imageView = new ImageView();
                                Image image = SpeechBox.imagefromBase64(mapdb.store.get(bigImgKey), 600, 600);///mapdb;
//                                 ByteArrayInputStream bis = new ByteArrayInputStream(mapdb.store.get(mapdb.store.get(bigImgKey)).getBytes());
//                                    InputStream in = Base64.getDecoder().wrap(bis);
//                                    Image image = new Image(in);
//                                    try {
//                                        bis.close();
//                                        in.close();
//                                    } catch (IOException ex) {
//                                        Logger.getLogger(CachePreview.class.getName()).log(Level.SEVERE, null, ex);
//                                    }
                                imageView.setImage(image);
                                imageView.setStyle("-fx-background-color: BLACK");
                                //imageView.setFitHeight(stage.getHeight());
                                imageView.setPreserveRatio(true);
                                imageView.setSmooth(true);
                                imageView.setCache(true);
                                borderPane.setCenter(imageView);
                                Button selectCharPic = new Button("Replace Image");
                                selectCharPic.setStyle(StylesForAll.aliveTheme);
                                selectCharPic.setOnAction(new EventHandler<ActionEvent>() {
                                    public void handle(ActionEvent event) {
                                        try {
                                            java.awt.Image image = getImageFromClipboard();
                                            if (image != null) {
                                                javafx.scene.image.Image fimage = awtImageToFX(image);
                                                //pe.imageView.setFitHeight(scene.getHeight());
                                                // pe.imageView.setFitWidth(scene.getWidth());
                                                imageView.setX(20);
                                                imageView.setY(0);
                                                imageView.setFitWidth(500);
                                                imageView.setFitHeight(500); 
                                                imageView.setPreserveRatio(true);
                                                imageView.setImage(fimage);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                borderPane.setRight(new VBox(selectCharPic,new TextBubble( false)));
                                //TextBubble textbubble=new TextBubble( false);
                                //textbubble.setPadding(new Insets(-100));
                                //StackPane spane=new StackPane(imageView,textbubble);
                                borderPane.setStyle("-fx-background-color: BLACK");
                                Stage newStage = new Stage();
                                newStage.setWidth(stage.getWidth()+200);
                                newStage.setHeight(stage.getHeight());
                                newStage.setTitle(bigImgKey);
                                Scene scene = new Scene(borderPane, Color.BLACK);
                                newStage.setScene(scene);
                                newStage.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return imageView;
    }
}
