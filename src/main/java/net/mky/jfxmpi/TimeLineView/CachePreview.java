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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
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
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
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
import static net.mky.jfxmpi.Voronoi.chapterShow;
import static net.mky.jfxmpi.Voronoi.pickFile;
import net.mky.tools.StylesForAll;
import net.mky.tools.ZipHelper;
import org.json.JSONArray;
import org.json.JSONObject;
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
    SplitPane splitPane = new SplitPane();
    VBox leftControl  = new VBox();//new Label("Left Control")
    VBox rightControl = new VBox(new Label("Right Control"));
        
    DB db;
    public HTreeMap<String, String> store;
    public BTreeMap<Object[], String> index;
    NavigableSet<Object[]> jmpc;
    int CurrentPageIndex=0;
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        splitPane.getItems().addAll(leftControl, rightControl);
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
        
        jmpc = db.treeSet("jmpc")
        //set tuple serializer
        .serializer(new SerializerArrayTuple(Serializer.STRING, Serializer.STRING, Serializer.STRING, Serializer.STRING))//jmp,jmpc,imgKey,text
        .counterEnable()
        .counterEnable()
        .counterEnable()
        .createOrOpen();

        ScrollPane root = new ScrollPane();
        TilePane tile = new TilePane();
        ListView listView = new ListView();


        root.setStyle("-fx-background-color: DAE6F3;");
        tile.setPadding(new Insets(15, 15, 15, 15));
        tile.setHgap(15);

        Label Status = new Label("Image indexing");
        final TextField searchText = new TextField("images");
        final TextField worldName = new TextField("World");
        final TextField chapterName = new TextField("Chapter-1");
        Label pageLimitLabel = new Label("Images per page");
        final TextField PageLimit = new TextField("300");

         Button loadImgIndex = new Button("Load Index");
         List<String> imgIndex=new LinkedList<>();
         List<String> imgTextIndex=new LinkedList<>();
        loadImgIndex.setOnAction(e -> {
            tile.getChildren().clear();//Clear window
            index.entrySet().stream().forEach(fileSig -> {
                imgIndex.add(fileSig.getKey()[0].toString());
                imgTextIndex.add(fileSig.getKey()[2].toString());
            });});
        
         Button next100 = new Button("Next >>");
          
        next100.setOnAction(e -> {
            tile.getChildren().clear();//Clear window
          
            System.out.println("Begin...");
            for (int i = CurrentPageIndex; i < CurrentPageIndex + Integer.parseInt(PageLimit.getText()); i++) {
                if(i>0 && i<imgIndex.size())
                try {
                    VBox imageView;
                    //System.out.println("index >>" + imgIndex.get(i));
                   // System.out.println("store.containsKey >>" + store.containsKey(imgIndex.get(i)));
                    ByteArrayInputStream bis = new ByteArrayInputStream(store.get(imgIndex.get(i)).getBytes());
                    InputStream in = Base64.getDecoder().wrap(bis);
                    Image image = new Image(in);

                    bis.close();
                    in.close();
                    imageView = createImageViewV2(image,imgTextIndex.get(i), store, index, imgIndex.get(i), stage);

                    tile.getChildren().addAll(imageView);
                } catch (IOException ex) {
                    Logger.getLogger(CachePreview.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("Done...");
            CurrentPageIndex=CurrentPageIndex+Integer.parseInt(PageLimit.getText());
        
        });
        
        Button random100 = new Button("<< Random >>");
          
        random100.setOnAction(e -> {
            tile.getChildren().clear();//Clear window
          CurrentPageIndex=(int) (Math.random()*imgIndex.size());
          System.out.println("Begin...");
            for (int i = CurrentPageIndex; i < CurrentPageIndex + Integer.parseInt(PageLimit.getText()); i++) {
                if(i>0 && i<imgIndex.size())
                try {
                    VBox imageView;
                    //System.out.println("index >>" + imgIndex.get(i));
                    //System.out.println("store.containsKey >>" + store.containsKey(imgIndex.get(i)));
                    ByteArrayInputStream bis = new ByteArrayInputStream(store.get(imgIndex.get(i)).getBytes());
                    InputStream in = Base64.getDecoder().wrap(bis);
                    Image image = new Image(in);

                    bis.close();
                    in.close();
                    imageView = createImageViewV2(image,imgTextIndex.get(i), store, index, imgIndex.get(i), stage);

                    tile.getChildren().addAll(imageView);
                } catch (IOException ex) {
                    Logger.getLogger(CachePreview.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("Done.");
            CurrentPageIndex=CurrentPageIndex+Integer.parseInt(PageLimit.getText());
        
        });
        
         Button prev100 = new Button("<<Prev ");
          
        prev100.setOnAction(e -> {
            tile.getChildren().clear();//Clear window
            System.out.println("Begin...");
            for (int i = CurrentPageIndex; i > CurrentPageIndex - Integer.parseInt(PageLimit.getText()); i--) {
                 if(i>0 && i<imgIndex.size())
                try {
                    VBox imageView;
                   // System.out.println("index >>" + imgIndex.get(i));
                    //System.out.println("store.containsKey >>" + store.containsKey(imgIndex.get(i)));
                    ByteArrayInputStream bis = new ByteArrayInputStream(store.get(imgIndex.get(i)).getBytes());
                    InputStream in = Base64.getDecoder().wrap(bis);
                    Image image = new Image(in);

                    bis.close();
                    in.close();
                    imageView = createImageViewV2(image,imgTextIndex.get(i), store, index, imgIndex.get(i), stage);

                    tile.getChildren().addAll(imageView);
                } catch (IOException ex) {
                    Logger.getLogger(CachePreview.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            CurrentPageIndex=CurrentPageIndex-Integer.parseInt(PageLimit.getText());
            System.out.println("Done.");
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
                    VBox imageView;
                    //System.out.println("index >>"+fileSig.getKey()[0].toString());
                    //System.out.println("store.containsKey >>"+store.containsKey(fileSig.getKey()[0].toString()));
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
                    imageView = createImageViewV2(image,"-", store,index, fileSig.getKey()[0].toString(), stage);
                    
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
                             VBox imageView;
                             imageView = createImageViewV2(awtImageToFX(bimg),"-", store,index, key, stage);
                    
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
        
        Button SaveToChapter = new Button("Ctrl+V to Chapter");
        SaveToChapter.setOnAction(e -> {
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
                             VBox imageView;
                             imageView = createImageViewV2(awtImageToFX(bimg),"-", store,index, key, stage);
                             jmpc.add(new Object[]{worldName.getText(),chapterName.getText(),key,searchText.getText()});
                    
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
        
        Button viewChapter = new Button("View Chapter");
        viewChapter.setStyle(StylesForAll.transparentAlive);
        viewChapter.setOnAction(event -> {
            Set johnSubset = jmpc.subSet(
                    new Object[]{worldName.getText(), chapterName.getText()}, // lower interval bound
                    new Object[]{worldName.getText(), chapterName.getText(), null});  // upper interval bound, null is positive infinity
            
            johnSubset.forEach(kv->{
                Object[] resultr=(Object[]) kv;
                 try {
                    VBox imageView;
                   // System.out.println("index >>" + imgIndex.get(i));
                    //System.out.println("store.containsKey >>" + store.containsKey(imgIndex.get(i)));
                    ByteArrayInputStream bis = new ByteArrayInputStream(store.get(resultr[2]).getBytes());
                    InputStream in = Base64.getDecoder().wrap(bis);
                    Image image = new Image(in);

                    bis.close();
                    in.close();
                    imageView = createImageViewV2(image,resultr[3].toString(), store, index, resultr[2].toString(), stage);

                    tile.getChildren().addAll(imageView);
                } catch (IOException ex) {
                    Logger.getLogger(CachePreview.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            });
        });
        
        
        //Import JMPC
        
        Button loadChapter = new Button("Import JMPC");
        loadChapter.setStyle(StylesForAll.transparentAlive);
        loadChapter.setOnAction(event -> {
            File selected = pickFile("Select JMPC", new File("/"));//fileChooser.showOpenDialog(null);
            tile.getChildren().clear();//Clear view panel..free up memory
            importJMPC( "Solo",selected,  tile);
        });

        Button loadWorld = new Button("Import JMP");
        loadWorld.setStyle(StylesForAll.transparentAlive);
        loadWorld.setOnAction(event -> {
             File jmpFile = pickFile("Select World",new File("/"));//fileChooser.showOpenDialog(null);
              String content;
            try {
                content = new String(Files.readAllBytes(Paths.get(jmpFile.getAbsolutePath())), StandardCharsets.UTF_8);
                JSONArray ChaptersJSON =  new JSONObject(content).getJSONArray("Chapters");
                for (int i = 0; i < ChaptersJSON.length(); i++) {
                    if (ChaptersJSON.getJSONObject(i).has("file")) {
                        Button name = new Button(ChaptersJSON.getJSONObject(i).getString("file"));
                        File chapter = new File(jmpFile.getParentFile().getAbsolutePath()+"/"+ChaptersJSON.getJSONObject(i).getString("file"));
                        tile.getChildren().clear();//Clear view panel..free up memory
                        importJMPC(jmpFile.getName(),chapter,tile);
                        
                    } else {

                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(CachePreview.class.getName()).log(Level.SEVERE, null, ex);
            }
               
                
        });
        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
        root.setFitToWidth(true);
//        TimeLineStory tls=new TimeLineStory(new File(selectedFile.getAbsolutePath()+"/Curr.jmpc"),"User", 900, 800,jmpc);
//        tls.setDateTime("");
//        tile.setPrefWidth(800);
        
        
       // HBox content=new HBox(tile,tls);
        leftControl.getChildren().add(root);
        root.setContent(tile);

        Status.setStyle(StylesForAll.transparentAlive);

        HBox statusBox = new HBox(loadImgIndex,loadWorld/*,previewDeleteDuplicateButton*/
                ,loadChapter,SaveDB,/*Status,*/pageLimitLabel,PageLimit,prev100,random100,next100,worldName,chapterName,searchText,SaveToChapter,viewChapter);
        VBox vbox = new VBox(statusBox,  splitPane);
        vbox.setStyle("-fx-background-color: linear-gradient(to right, rgb(203,53,107,0.25), rgb(189,63,50,0.25));");
        leftControl.setStyle("-fx-background-color: linear-gradient(to right, rgb(203,53,107,0.25), rgb(189,63,50,0.25));");

        primaryStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth() / 2);
        primaryStage.setHeight(Screen.getPrimary().getVisualBounds()
                .getHeight());
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    
    public void importJMPC(String worldName,File selected, TilePane tile) {

        try {
            
            String content = "";// ZipHelper.decompress(Files.readAllBytes(Paths.get(selected.getAbsolutePath())));

            if (selected.getAbsolutePath().endsWith(".zip")) {
                content = ZipHelper.decompress(Files.readAllBytes(Paths.get(selected.getAbsolutePath())));
            } else if (new File(selected.getAbsolutePath() + ".zip").exists()) {

                content = ZipHelper.decompress(Files.readAllBytes(Paths.get(selected.getAbsolutePath() + ".zip")));

            } else {
                content = new String(Files.readAllBytes(Paths.get(selected.getAbsolutePath())));
            }
               
            JSONObject metaData = new JSONObject(content);
            JSONArray chat_data = metaData.getJSONArray("chat_data");
            System.out.println("Importing..");

            for (int i = 0; i < chat_data.length(); i++) {
                if (chat_data.getJSONObject(i).has("theme")) {

                }
                if (chat_data.getJSONObject(i).has("base64Image")) {
                    String b64Image = chat_data.getJSONObject(i).getString("base64Image");
                    String key = "T" + System.currentTimeMillis() + Math.random() * System.currentTimeMillis();
                    store.put(key, b64Image);
                    index.put(new Object[]{key, b64Image.length() + "", chat_data.getJSONObject(i).getString("message")}, getImageB64From(getScaledImage(b64Image, 100, 100), "jpeg"));
                    jmpc.add(new Object[]{worldName,selected.getName(),key,chat_data.getJSONObject(i).getString("message")});
                    VBox imageView;
                    imageView = createImageViewV2(awtImageToFX(getScaledImage(b64Image, 200, 200)),"-", store, index, key, stage);
                    try {
                        tile.getChildren().addAll(imageView);
                    } catch (Exception ex) {
                    }
                } else {

                }
            }
            db.commit();
            System.out.println("Done.");
        } catch (IOException ex) {

            db.rollback();
            System.out.println("Changes to db reverted");
            Logger.getLogger(CachePreview.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CachePreview.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    public static void main(String... args) throws IOException {
        launch(args);

    }

    public  VBox createImageViewV2(Image image,String labelText, HTreeMap<String, String> store,BTreeMap<Object[], String> index, String bigImgKey, Stage stage) {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower)
        // resizing

        ImageView imageView = null;
        VBox vbox=new VBox();
        
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
                                Image image = SpeechBox.imagefromBase64(store.get(bigImgKey), 800, 800);///mapdb;
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
                                borderPane.setRight(new VBox(selectCharPic, new TextBubble(false), new TextBubble(false)));
                                //TextBubble textbubble=new TextBubble( false);
                                //textbubble.setPadding(new Insets(-100));
                                //StackPane spane=new StackPane(imageView,textbubble);
                                borderPane.setStyle("-fx-background-color: BLACK");
                                rightControl.getChildren().clear();//Clear all
                                rightControl.getChildren().add(borderPane);
                                rightControl.setStyle("-fx-background-color: BLACK");
                                
//                                Stage newStage = new Stage();
//                                newStage.setWidth(1000);
//                                newStage.setHeight(stage.getHeight());
//                                newStage.setTitle(bigImgKey);
//                                Scene scene = new Scene(borderPane, Color.BLACK);
//                                newStage.setScene(scene);
//                                newStage.show();
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
        
       // NavigableMap<Object[],String> map=index.prefixSubMap(new Object[]{bigImgKey});
        
        vbox.getChildren().add(imageView);
        vbox.getChildren().add(new TextField(labelText));
        
        return vbox;
    }
    
     public void save(String key, String data, String preview) {
        store.put(key, data);
        index.put(new Object[]{key, data.length()+"", ""}, preview);
    }
}
