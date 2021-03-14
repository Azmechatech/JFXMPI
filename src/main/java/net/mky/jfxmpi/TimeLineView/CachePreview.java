/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi.TimeLineView;

import com.truegeometry.mkhilbertml.GridImage;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import static net.mky.jfxmpi.MainApp.awtImageToFX;
import static net.mky.jfxmpi.MainApp.getImageFromClipboard;
import net.mky.jfxmpi.TextBubble;
import static net.mky.jfxmpi.TimeLineView.TimeLineStory.getImageB64From;
import static net.mky.jfxmpi.TimeLineView.TimeLineStory.getScaledImage;
import static net.mky.jfxmpi.TimeLineView.TimeLineStory.toBufferedImage;
import net.mky.jfxmpi.Voronoi;
import net.mky.tools.StylesForAll;
import org.mapdb.BTreeMap;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerArray;
import org.mapdb.serializer.SerializerArrayTuple;

/**
 *
 * @author mkfs
 */
public class CachePreview extends Application {

    Stage stage;
    DB db;
    public HTreeMap<String, String> store;
    public BTreeMap<Object[], String> index;
    int CurrentPageIndex=0;
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        FileChooser fileChooser = new FileChooser();
        File selectedFile = Voronoi.pickFolder("Select Folder");//fileChooser.showOpenDialog(null);

        db = DBMaker
                .fileDB(selectedFile + "/" + "CachePreview.db").transactionEnable()
                .closeOnJvmShutdown().cleanerHackEnable().fileChannelEnable().fileMmapEnableIfSupported()
                .make();
        store = db.hashMap("store")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.STRING)
                .createOrOpen();

        index = db.treeMap("index")
                // use array serializer for unknown objects
                .keySerializer(new SerializerArrayTuple(
                        Serializer.STRING, Serializer.STRING, Serializer.STRING))
                // or use wrapped serializer for specific objects such as String
                .keySerializer(new SerializerArray(Serializer.STRING))
                .createOrOpen();//Issue with keys may be...

        ScrollPane root = new ScrollPane();
        TilePane tile = new TilePane();

        root.setStyle("-fx-background-color: DAE6F3;");
        tile.setPadding(new Insets(15, 15, 15, 15));
        tile.setHgap(15);

        Label Status = new Label("Image indexing");
        final TextField searchText = new TextField("images");

         Button loadImgIndex = new Button("Load Index");
         List<String> imgIndex=new LinkedList<>();
        loadImgIndex.setOnAction(e -> {
            tile.getChildren().clear();//Clear window
            index.entrySet().stream().forEach(fileSig -> {
                imgIndex.add(fileSig.getKey()[0].toString());
            });});
        
         Button next100 = new Button("Next >>");
          
        next100.setOnAction(e -> {
            tile.getChildren().clear();//Clear window
          
            for (int i = CurrentPageIndex; i < CurrentPageIndex + 200; i++) {
                if(i>0 && i<imgIndex.size())
                try {
                    ImageView imageView;
                    System.out.println("index >>" + imgIndex.get(i));
                    System.out.println("store.containsKey >>" + store.containsKey(imgIndex.get(i)));
                    ByteArrayInputStream bis = new ByteArrayInputStream(store.get(imgIndex.get(i)).getBytes());
                    InputStream in = Base64.getDecoder().wrap(bis);
                    Image image = new Image(in);

                    bis.close();
                    in.close();
                    imageView = createImageViewV2(image, store, index, imgIndex.get(i), stage);

                    tile.getChildren().addAll(imageView);
                } catch (IOException ex) {
                    Logger.getLogger(CachePreview.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            CurrentPageIndex=CurrentPageIndex+100;
        
        });
        
         Button prev100 = new Button("<<Prev ");
          
        prev100.setOnAction(e -> {
            tile.getChildren().clear();//Clear window

            for (int i = CurrentPageIndex; i > CurrentPageIndex - 200; i--) {
                 if(i>0 && i<imgIndex.size())
                try {
                    ImageView imageView;
                    System.out.println("index >>" + imgIndex.get(i));
                    System.out.println("store.containsKey >>" + store.containsKey(imgIndex.get(i)));
                    ByteArrayInputStream bis = new ByteArrayInputStream(store.get(imgIndex.get(i)).getBytes());
                    InputStream in = Base64.getDecoder().wrap(bis);
                    Image image = new Image(in);

                    bis.close();
                    in.close();
                    imageView = createImageViewV2(image, store, index, imgIndex.get(i), stage);

                    tile.getChildren().addAll(imageView);
                } catch (IOException ex) {
                    Logger.getLogger(CachePreview.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            CurrentPageIndex=CurrentPageIndex-200;
        });
//        FileChooser fileChooser = new FileChooser();
//        //File selectedFile = fileChooser.showOpenDialog(null);
//        File selectedFile = pickFile("Select World",new File("/"));//fileChooser.showOpenDialog(null);
//        MapDB mapdb = new MapDB(selectedFile.getAbsolutePath(), true);
        tile.getChildren().clear();//Clear window
        Button previewDeleteDuplicateButton = new Button("Preview results");
        previewDeleteDuplicateButton.setOnAction(e -> {
            tile.getChildren().clear();//Clear window
            index.entrySet().stream().forEach(fileSig -> {
                try {
                    ImageView imageView;
                    System.out.println("index >>"+fileSig.getKey()[0].toString());
                    System.out.println("store.containsKey >>"+store.containsKey(fileSig.getKey()[0].toString()));
                    ByteArrayInputStream bis = new ByteArrayInputStream(store.get(fileSig.getKey()[0].toString()).getBytes());
                    InputStream in = Base64.getDecoder().wrap(bis);
                    Image image = new Image(in);

                    bis.close();
                    in.close();

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
                    imageView = createImageViewV2(image, store,index, fileSig.getKey()[0].toString(), stage);
                    
                    tile.getChildren().addAll(imageView);
                } catch (Exception ex) {
                    Logger.getLogger(CachePreview.class.getName()).log(Level.SEVERE, null, ex);
                }

            });

        });
        
        Button SaveDB = new Button("Ctrl+V");
        SaveDB.setOnAction(e -> {
            try {
                java.awt.Image image = getImageFromClipboard();
                if (image != null) {
                    javafx.scene.image.Image fimage = awtImageToFX(image);

                    List<BufferedImage> bimgs = GridImage.splitImages(toBufferedImage(image), GridImage.rowLevelSplit(toBufferedImage(image)));

                    if(bimgs.size()==0)bimgs.add(toBufferedImage(image));
                    bimgs.forEach(bimg -> {
                        try {
                            String b64Image = getImageB64From(bimg, "jpeg");
                            String key="T" + System.currentTimeMillis()+Math.random()*System.currentTimeMillis();
                            store.put(key, b64Image);
                            index.put(new Object[]{key, b64Image.length() + "", searchText.getText()}, getImageB64From(getScaledImage(b64Image, 100, 100), "jpeg"));
                             ImageView imageView;
                             imageView = createImageViewV2(awtImageToFX(bimg), store,index, key, stage);
                    
                    tile.getChildren().addAll(imageView);
                    
                        } catch (Exception ex) {
                            Logger.getLogger(TimeLineStory.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            } catch (Exception ed) {
                ed.printStackTrace();
            }
            db.commit();
        });

        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
        root.setFitToWidth(true);
        root.setContent(tile);

        Status.setStyle(StylesForAll.transparentAlive);

        HBox statusBox = new HBox(loadImgIndex,previewDeleteDuplicateButton,searchText,SaveDB,Status,prev100,next100);
        VBox vbox = new VBox(statusBox,  root);
        vbox.setStyle("-fx-background-color: linear-gradient(to right, rgb(203,53,107,0.25), rgb(189,63,50,0.25));");

        primaryStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth() / 2);
        primaryStage.setHeight(Screen.getPrimary().getVisualBounds()
                .getHeight());
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String... args) throws IOException {
        launch(args);

    }

    public static ImageView createImageViewV2(Image image, HTreeMap<String, String> store,BTreeMap<Object[], String> index, String bigImgKey, Stage stage) {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower)
        // resizing

        ImageView imageView = null;
        try {
            imageView = new ImageView(image);
            imageView.setFitWidth(300);
            imageView.setFitHeight(300);
            imageView.setPreserveRatio(true);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent mouseEvent) {

                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {

                        if (mouseEvent.getClickCount() == 1) {
                            try {
                                BorderPane borderPane = new BorderPane();

                                ImageView imageView = new ImageView();
                                Image image = SpeechBox.imagefromBase64(store.get(bigImgKey), 900, 900);///mapdb;
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
                                                
                                                
                                                 List<BufferedImage> bimgs=GridImage.splitImages(toBufferedImage(image),  GridImage.rowLevelSplit(toBufferedImage( image)));
                        
                                                bimgs.forEach(bimg -> {
                                                    try {
                                                        //pe.imageView.setFitHeight(scene.getHeight());
                                                        // pe.imageView.setFitWidth(scene.getWidth());
                                                        String b64Image = getImageB64From(bimg, "jpeg");
                                                        //save(""+System.currentTimeMillis(), b64Image, getImageB64From(getScaledImage(b64Image, 100, 100), "jpeg"));
                                                      String key="T" + System.currentTimeMillis()+Math.random()*System.currentTimeMillis();
                                                     store.put(key, b64Image);
                                                 index.put(new Object[]{key, b64Image.length() + "", ""}, getImageB64From(getScaledImage(b64Image, 100, 100), "jpeg")); } catch (Exception ex) {
                                                        Logger.getLogger(TimeLineStory.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                });
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                borderPane.setRight(new VBox(selectCharPic, new TextBubble(false)));
                                //TextBubble textbubble=new TextBubble( false);
                                //textbubble.setPadding(new Insets(-100));
                                //StackPane spane=new StackPane(imageView,textbubble);
                                borderPane.setStyle("-fx-background-color: BLACK");
                                Stage newStage = new Stage();
                                newStage.setWidth(stage.getWidth() + 200);
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
    
     public void save(String key, String data, String preview) {
        store.put(key, data);
        index.put(new Object[]{key, data.length()+"", ""}, preview);
    }
}
