package net.mky.jfxmpi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import net.mky.graphUI.ImagePool;
import net.mky.tools.AnimatedGif;
import net.mky.tools.Animation;
import net.mky.tools.BruteForceRenderer;
import net.mky.tools.MediaControl;
import net.mky.tools.StylesForAll;
import org.json.JSONObject;
import systemknowhow.LifePool;
import systemknowhow.human.ConversationMaker;
import systemknowhow.human.Life;
import systemknowhow.humanactivity.SocialRelationTags;
import systemknowhow.tools.HilbertCurvePatternDetect;

public class MainApp extends Application {

    String dirPath = "";
    String scenesFiles[];int sceneCounter=0;
    JSONObject projData;
    int width = 200;
    int height = 600;
    CharacterPane characterA = new CharacterPane(width, height, false);
    CharacterPane characterB = new CharacterPane(width, height, false);
    CharacterPane characterC = new CharacterPane(width, height, false);
     GraphPane graphBoard=new GraphPane();
    StoryBoard StoryBoard = new StoryBoard(width, height);
    
    List<CharacterPane> charactersArray;
    String[] charPrefixes=new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q"};
    
    
    final MainPane pe = new MainPane();
    final BruteForceRenderer bfr=new BruteForceRenderer();
    static ConversationMaker convMaker;
    String thePath = "";
    private   String MEDIA_URL = Paths.get(thePath).toUri().toString();
    private MediaPlayer mediaPlayer;


    public static void main(String[] args) throws Exception {
        convMaker=new ConversationMaker();
        launch(args);

    }
   class WindowButtons extends HBox {

        public WindowButtons() {
            Button closeBtn = new Button("X");

            closeBtn.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent actionEvent) {
                    Platform.exit();
                }
            });
            closeBtn.setStyle(StylesForAll.buttonBoldBlack);
            this.getChildren().add(closeBtn);
        }
    }
    @Override
    public void start(final Stage stage) throws Exception {
         stage.initStyle(StageStyle.UNDECORATED);
         
         ToolBar toolBar = new ToolBar();
         charactersArray=new ArrayList<>();
         HBox characters=new HBox();
        //int height = 25;

     
    
        // Use a border pane as the root for scene
        BorderPane border = new BorderPane();
        Scene scene = new Scene(border);
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        //==========Generate IsoView Geometry==========
//        for(int j=1;j<3;j++)
//        for (int i = 0; i < 5; i++) {
//            double wd=300d;
//            double ht=300d;
//            double startX=300d*j;
//            double startY=300d;
//            //Trapozoid
//            double offsetX=300d;
//            double changeY=-30d;
//            
//            
//            Polygon polygon = new Polygon();
//            polygon.getPoints().addAll(i*startX,i*startY+changeY*(j-1)
//            ,i*startX+wd,i*startY+changeY*(j-1)
//            ,i*startX+wd,i*startY+ht+changeY*(j-1)
//            ,i*startX,i*startY+ht+changeY*(j-1));
//            polygon.setFill(Color.SKYBLUE);
//            polygon.setStrokeWidth(3);
//            polygon.setStroke(Color.BLACK);
//            border.getChildren().add(polygon);
//            
//            
//            
//            polygon = new Polygon();
//            polygon.getPoints().addAll(i*startX+offsetX,i*startY
//            ,i*startX+wd+offsetX,i*startY+changeY*j
//            ,i*startX+wd+offsetX,i*startY+ht+changeY*j
//            ,i*startX+offsetX,i*startY+ht);
//            polygon.setFill(Color.BEIGE);
//            polygon.setStrokeWidth(3);
//            polygon.setStroke(Color.BLACK);
//            border.getChildren().add(polygon);
//
//        }

        Button playButton = new Button("\u25b6 Change the Theme");
        playButton.getStyleClass().add("play");

        EventHandler<ActionEvent> goAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {

                    File file = fileChooser.showOpenDialog(stage);

                    if (file != null) {
                        FileInputStream input = new FileInputStream(file.getAbsolutePath());
                        pe.imageView.setImage(new Image(input, 1600, 1200, true, true));
                        pe.bgImageFile = file.getAbsolutePath();
                        
                        File existDirectory = file.getParentFile();
                        fileChooser.setInitialDirectory(existDirectory);

                    }

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
        playButton.setOnAction(goAction);
        playButton.setStyle(StylesForAll.transparentAlive);
        // mainPane.setCenter(homePane);
        HBox buttonPane = new HBox(12, playButton);
        buttonPane.setAlignment(Pos.CENTER_LEFT);
        buttonPane.setStyle("-fx-background-color:\n"
                + "        linear-gradient(#686868 0%, #232723 25%, #373837 75%, rgba(0, 100, 100, 0.5) 100%),\n"
                + "        linear-gradient(#020b02, #3a3a3a),\n"
                + "        linear-gradient(#9d9e9d 0%, #6b6a6b 20%, #343534 80%, rgba(0, 100, 100, 0.5) 100%),\n"
                + "        linear-gradient(#8a8a8a 0%, #6b6a6b 20%, #343534 80%, rgba(0, 100, 100, 0.5) 100%),\n"
                + "        linear-gradient(#777777 0%, #606060 50%, #505250 51%, rgba(0, 100, 100, 0.5) 100%);-fx-padding: 10;"
                + "-fx-border-style: solid inside;"
        /*+ "-fx-border-width: 10;"
                + "-fx-border-insets: 0;"
                + "-fx-border-radius: 5;"
                + "-fx-border-color: GOLDENROD;"*/);
        //Translucent
        buttonPane.setStyle("-fx-background-color: linear-gradient(to right, rgb(203,53,107,0.25), rgb(189,63,50,0.25));");
        
        //buttonPane.getChildren().add(toolBar);
        //Add Animation GIF
        EventHandler<ActionEvent> addAnimation = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    // TODO: provide gif file, ie exchange banana.gif with your file
                    Animation ani = new AnimatedGif(file.getAbsolutePath(), 1000);
                    ani.setCycleCount(10);
                    ani.play();

                    Button btPause = new Button("Pause");
                    btPause.setOnAction(e -> ani.pause());

                    Button btResume = new Button("Resume");
                    btResume.setOnAction(e -> ani.play());

                    pe.getChildren().addAll(ani.getView(), btPause, btResume);
                }

            }
        };

        Button addGIF = new Button("Add GIF");
        addGIF.getStyleClass().add("play");
        addGIF.setOnAction(addAnimation);
        addGIF.setStyle(StylesForAll.transparentAlive);
       // buttonPane.getChildren().add(addGIF);
        
        
   
        //Add Animation GIF
        EventHandler<ActionEvent> addVideoAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    
                     //Set name and other properties
                     final Stage dialog = new Stage();
                    // dialog.initStyle(StageStyle.UNDECORATED);
                    //dialog.sizeToScene();
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.initOwner(stage);
                    VBox dialogVbox = new VBox(20);
                    //dialogVbox.getChildren().add(new Text("This is a Dialog"));
                    //Group root = new Group();
                    
                    mediaPlayer = new MediaPlayer(new Media(Paths.get(file.getAbsolutePath()).toUri().toString()));
                    mediaPlayer.setAutoPlay(true);
                    MediaControl mediaControl = new MediaControl(mediaPlayer);
                    int mWid=1024,mhig=768;
                    //mWid=mediaPlayer.getMedia().getWidth();
                    //mhig=mediaPlayer.getMedia().getHeight();
                    mediaControl.setMinSize(mWid,mhig);
                    mediaControl.setPrefSize(mWid,mhig);
                    //mediaControl.setMinHeight(mediaPlayer.getMedia().getHeight());
                    mediaControl.setMaxSize(mWid,mhig);
                    dialogVbox.getChildren().add(mediaControl);
                    mediaPlayer.play();
                    //Show image gallery
                    ScrollPane root = new ScrollPane();
                    //root.getChildren().add(mediaControl);
                    root.setStyle("-fx-background-color: DAE6F3;");
                    root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
                    root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
                    root.setFitToWidth(true);
                    root.setContent(dialogVbox);
//                    dialog.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
//                    dialog.setHeight(Screen.getPrimary().getVisualBounds()
//                            .getHeight());
                    Scene dialogScene = new Scene(root,mWid,mhig+50);
                    dialog.setScene(dialogScene);
                    dialog.show();
                    

                }

            }
        };

        Button addVideo = new Button("Add Video");
        addVideo.getStyleClass().add("play");
        addVideo.setOnAction(addVideoAction);
        addVideo.setStyle(StylesForAll.transparentAlive);
       // buttonPane.getChildren().add(addVideo);

       //Save scene
         
        EventHandler<ActionEvent> saveGameAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    projData = new JSONObject();
                    projData.put("dirPath", dirPath);
                    projData.put("bgFile", pe.bgImageFile);
//                    projData.put("cpane", characterA.CharacterFile);
//                    projData.put("cpane2", characterB.CharacterFile);
//                    projData.put("cpane3", characterC.CharacterFile);
//                    JSONArray charlist=new JSONArray(characterA.CharacterPool);
//                    JSONArray charlist2=new JSONArray(characterB.CharacterPool);
//                    JSONArray charlist3=new JSONArray(characterC.CharacterPool);
//                    projData.put("charlist", charlist);
//                    projData.put("charlist2", charlist2);
//                    projData.put("charlist3", charlist3);
                    
//                    projData.put("characterA", characterA.getCharacterData());
//                    projData.put("characterB", characterB.getCharacterData());
//                    projData.put("characterC", characterC.getCharacterData());
                    int i=0;
                    for(CharacterPane cp:charactersArray){
                    //charPrefixes
                        projData.put("character"+charPrefixes[i++], cp.getCharacterData());
                    }
                    
                    List<String> lines = Arrays.asList(projData.toString());
                    Path file = Paths.get(dirPath + "/game_" + System.currentTimeMillis() + ".JSON");
                    Files.write(file, lines, Charset.forName("UTF-8"));
                } catch (IOException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };

        Button saveGame = new Button("Save Scene");
        saveGame.getStyleClass().add("play");
        saveGame.setOnAction(saveGameAction);
        saveGame.setStyle(StylesForAll.transparentAlive);
        buttonPane.getChildren().add(saveGame);
       
        //Add a character Pane

        EventHandler<ActionEvent> addCharacterPane = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CharacterPane newCharacter=new CharacterPane(width, height, false);
                charactersArray.add(newCharacter);
                characters.getChildren().add(newCharacter);

            }
        };

        Button addCharPane = new Button("Add Character");
        //addCharPane.getStyleClass().add("play");
        addCharPane.setOnAction(addCharacterPane);
        addCharPane.setStyle(StylesForAll.transparentAlive);
        buttonPane.getChildren().add(addCharPane);
        
        
        
        EventHandler<ActionEvent> takeSnapshot = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                 WritableImage writableImage = 
            new WritableImage((int)scene.getWidth(), (int)scene.getHeight());
                WritableImage image = scene.snapshot(writableImage);

                // TODO: probably use a file chooser here
                File file = new File(dirPath + "/" + System.currentTimeMillis() + ".png");

                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                } catch (IOException e) {
                    // TODO: handle exception here
                }

            }
        };

        Button TakeSnapShot = new Button("Snapshot");
        TakeSnapShot.getStyleClass().add("play");
        TakeSnapShot.setOnAction(takeSnapshot);
        TakeSnapShot.setStyle(StylesForAll.transparentAlive);
        buttonPane.getChildren().add(TakeSnapShot);

        
        
        //Image resizing
    EventHandler<ActionEvent> inc = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    if(sceneCounter>scenesFiles.length)sceneCounter=0;
                switchScene(scenesFiles[sceneCounter++]);
                }catch(Exception ex){}
            }
        };

    EventHandler<ActionEvent> dec = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                 try{
                     if(sceneCounter<0)sceneCounter=scenesFiles.length;
               switchScene(scenesFiles[sceneCounter--]);
                }catch(Exception ex){}
            }
        };
    
    Button incSize = new Button("Next Scene");
        incSize.setStyle(StylesForAll.transparentAlive);
        incSize.getStyleClass().add("play");
        incSize.setOnAction(inc);
       // buttonPane.getChildren().add(incSize);
        //StackPane.setAlignment(incSize, Pos.TOP_LEFT);
        Button decSize = new Button("Prev Scene");
        decSize.getStyleClass().add("play");
        decSize.setOnAction(dec);
        decSize.setStyle(StylesForAll.transparentAlive);
      //  buttonPane.getChildren().add(decSize);
        //StackPane.setAlignment(decSize, Pos.BOTTOM_LEFT);
        
        
        /////////////////////////////////////////
        //Character setup
        ////////////////////////////////////////
//        StoryBoard.imageChar1.setImage(new Image(characterA.input,200,300,true,true));
 //       StoryBoard.imageChar2.setImage(new Image(characterB.input,200,300,true,true));
 //       StoryBoard.imageChar3.setImage(new Image(characterC.input,200,300,true,true));

        EventHandler<ActionEvent> playGameAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               //Set textPane data
               Life charaA= characterA.getLife();
               Life charaB= characterB.getLife();
               StoryBoard.getConvMaker().init(charaA, charaB, StoryBoard.charAText.getText(), StoryBoard.charBText.getText());
               

            }
        };

        Button playGame = new Button("Play Scene");
        playGame.getStyleClass().add("play");
        playGame.setOnAction(playGameAction);
        playGame.setStyle(StylesForAll.transparentAlive);
        buttonPane.getChildren().add(playGame);
      

        EventHandler<ActionEvent> loadGameAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {

                    File file = fileChooser.showOpenDialog(stage);

                    if (file != null) {
                        FileInputStream input = new FileInputStream(file.getAbsolutePath());

                        byte[] data = new byte[(int) file.length()];
                        input.read(data);
                        input.close();

                        String str = new String(data, "UTF-8");
                        projData = new JSONObject(str);

                        //Set all
                        dirPath = projData.getString("dirPath");

                        if (!projData.getString("bgFile").equalsIgnoreCase("")) {
                            input = new FileInputStream(projData.getString("bgFile"));
                            pe.imageView.setImage(new Image(input, 1600, 1200, true, true));
                            pe.bgImageFile = projData.getString("bgFile");
                        }

                        if (projData.has("cpane")) //OLD Convention
                        {
                            input = new FileInputStream(projData.getString("cpane"));
                            characterA.CharacterFile = projData.getString("cpane");
                            Image image = new Image(input, 400, 600, true, true);
                            characterA.imageView.setImage(image);

                            input = new FileInputStream(projData.getString("cpane2"));
                            characterB.CharacterFile = projData.getString("cpane2");
                            image = new Image(input, 400, 600, true, true);
                            characterB.imageView.setImage(image);
                        }
                        

                        if(projData.has("charlist")) //OLD Convention

                        { //String[] charlist = projData.getJSONArray("charlist").toString().replace("},{", " ,").split(" ");
                            List<String> list = new ArrayList<String>();
                            for (int i = 0; i < projData.getJSONArray("charlist").length(); i++) {
                                list.add(projData.getJSONArray("charlist").getString(i));
                            }
                            characterA.CharacterPool = list.toArray(new String[0]);

                            //String[] charlist2 = projData.getJSONArray("charlist2").toString().replace("},{", " ,").split(" ");
                            list = new ArrayList<String>();
                            for (int i = 0; i < projData.getJSONArray("charlist2").length(); i++) {
                                list.add(projData.getJSONArray("charlist2").getString(i));
                            }

                            characterB.CharacterPool = list.toArray(new String[0]);
                        }
                        
                        //Populate life data -> New convention
                        if(projData.has("characterA"))
                        {
                            characterA.loadCharacterData(projData.getJSONObject("characterA"));
                            characterB.loadCharacterData(projData.getJSONObject("characterB"));
                            if(projData.has("characterC"))characterC.loadCharacterData(projData.getJSONObject("characterC"));

//                            Image imageFA = SwingFXUtils.toFXImage(HilbertCurvePatternDetect.resizeImage(HilbertCurvePatternDetect.getMatchedRegion("C:/Users/Maneesh/Desktop/face.png",characterA.CharacterFile),200,200), null);
//                            Image imageFB = SwingFXUtils.toFXImage(HilbertCurvePatternDetect.resizeImage(HilbertCurvePatternDetect.getMatchedRegion("C:/Users/Maneesh/Desktop/face.png",characterB.CharacterFile),200,200), null);
//                            Image imageFC = SwingFXUtils.toFXImage(HilbertCurvePatternDetect.resizeImage(HilbertCurvePatternDetect.getMatchedRegion("C:/Users/Maneesh/Desktop/face.png",characterC.CharacterFile),200,200), null);
//                            
//                            
//                            StoryBoard.imageChar1.setImage(imageFA);
//                            StoryBoard.imageChar2.setImage(imageFB);
                           // StoryBoard.imageChar3.setImage(imageFC);
                            
                        }
                        
                        //Unlimited Character Version - Active version
                        characters.getChildren().removeAll(charactersArray);
                        charactersArray=new ArrayList<>();
                        ArrayList<Life> lifeArray=new ArrayList<>();
                        for (String cPref : charPrefixes) {
                            if (projData.has("character" + cPref)) {

                                CharacterPane characterThis = new CharacterPane(width, height, false);
                                characterThis.loadCharacterData(projData.getJSONObject("character" + cPref));
                                
                                lifeArray.add(characterThis.getLife());
                                charactersArray.add(characterThis);
                                characters.getChildren().add(characterThis);

                                Image imageFA = SwingFXUtils.toFXImage(HilbertCurvePatternDetect.resizeImage(HilbertCurvePatternDetect.getMatchedRegion("C:/$AVG/baseDir/Imports/Sprites/OBJECTS/face.png", characterThis.CharacterFile), 75, 75), null);
                                ImagePool.pool.put(characterThis.getLife().getName(), SwingFXUtils.toFXImage(HilbertCurvePatternDetect.getMatchedRegion("C:/$AVG/baseDir/Imports/Sprites/OBJECTS/face.png", characterThis.CharacterFile),null));
                                LifePool.pool.put(characterThis.getLife().getName(), characterThis.getLife());
                                StoryBoard.addCharacter(new ImageView(imageFA), characterThis.name);
                            }
                        }
                        
                        for(CharacterPane cp :charactersArray)
                            charactersArray.stream().forEach(c->c.getLife().relations.put(cp.getLife(), SocialRelationTags.FRIENDSHIP));
                        
                         graphBoard.addLifeComponents(charactersArray);
                       
                        //Load all other scenes as well
                        final File folder = new File(dirPath);
                        List<String> scenesList=new ArrayList<>();
                        for (final File fileEntry : folder.listFiles()) {
                            if (fileEntry.isDirectory()) {
                                //listFilesForFolder(fileEntry);
                            } else {
                                
                                if(fileEntry.getAbsolutePath().contains(".JSON"))
                                scenesList.add(fileEntry.getAbsolutePath());
                               // System.out.println(fileEntry.getName());
                                
                            }
                        }
                        
                        scenesFiles=scenesList.toArray(new String[0]);

                    }

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };
        

        Button loadGame = new Button("Load Scene");
        loadGame.getStyleClass().add("play");
        loadGame.setOnAction(loadGameAction);
        loadGame.setStyle(StylesForAll.transparentAlive);
        buttonPane.getChildren().add(loadGame);
        
        EventHandler<ActionEvent> setDirectoryAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory
                        = directoryChooser.showDialog(stage);

                if (selectedDirectory == null) {
                    dirPath = "No Directory selected";
                } else {
                    dirPath = selectedDirectory.getAbsolutePath();
                }

            }
        };

        Button setDirectory = new Button("Set Directory");
        setDirectory.getStyleClass().add("play");
        setDirectory.setOnAction(setDirectoryAction);
        setDirectory.setStyle(StylesForAll.transparentAlive);
        buttonPane.getChildren().add(setDirectory);
        
         EventHandler<ActionEvent> showHide = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                characters.setVisible(!characters.isVisible());
            }
        };

        Button showHideImg = new Button("[-]");
        showHideImg.setOnAction(showHide);
        showHideImg.setStyle(StylesForAll.transparentAlive);
        buttonPane.getChildren().add(showHideImg);
        
        
        
        buttonPane.getChildren().add(new WindowButtons());
        
        border.setCenter(pe);//addAnchorPane(addGridPane())
        //border.setCenter(gp);
        //Old convention
//        characters.getChildren().add(characterA);
//        characters.getChildren().add(characterB);
//        characters.getChildren().add(characterC);
        border.setLeft(characters);// addVBox()
        
       // border.setRight(characters);
        border.setTop(buttonPane); //hbox
       

       // border.setBottom(StoryBoard);GraphPane
       border.setBottom(graphBoard);

        border.setStyle("-fx-background-color: linear-gradient(to right, #642b73, #c6426e);");
        border.setStyle("-fx-background-color:linear-gradient(to right, #40e0d0, #ff8c00, #ff0080);");
        border.setStyle("-fx-background-color:linear-gradient(to right, #2c3e50, #3498db);");
        border.setStyle("-fx-background-color:linear-gradient(to right, #fc5c7d, #6a82fb);");
        //border.setStyle(StylesForAll.checkBoxes);
        
        
        stage.setScene(scene);
        stage.setTitle("Game");
        stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        stage.setHeight(Screen.getPrimary().getVisualBounds()
                            .getHeight());
        stage.show();
    }

    
    public void switchScene(String file) {

                try {

            

                    if (file != null) {
                        FileInputStream input = new FileInputStream(new File(file));

                        byte[] data = new byte[(int) new File(file).length()];
                        input.read(data);
                        input.close();

                        String str = new String(data, "UTF-8");
                        projData = new JSONObject(str);

                        //Set all
                        dirPath = projData.getString("dirPath");

                       input = new FileInputStream(projData.getString("bgFile"));
                        pe.imageView.setImage(new Image(input, 1600, 1200, true, true));
                        pe.bgImageFile = projData.getString("bgFile");

                        input = new FileInputStream(projData.getString("cpane"));
                        characterA.CharacterFile = projData.getString("cpane");
                        Image image = new Image(input, 300, 800, true, true);
                        characterA.imageView.setImage(image);

                        input = new FileInputStream(projData.getString("cpane2"));
                        characterB.CharacterFile = projData.getString("cpane2");
                        image = new Image(input, 300, 800, true, true);
                        characterB.imageView.setImage(image);
                        
                        
                        
                        if(projData.has("charlist"))

                        { //String[] charlist = projData.getJSONArray("charlist").toString().replace("},{", " ,").split(" ");
                        List<String> list = new ArrayList<String>();
                        for(int i = 0; i < projData.getJSONArray("charlist").length(); i++){
                            list.add(projData.getJSONArray("charlist").getString(i));
                        }
                        characterA.CharacterPool=list.toArray(new String[0]);
                        
                        //String[] charlist2 = projData.getJSONArray("charlist2").toString().replace("},{", " ,").split(" ");
                        list = new ArrayList<String>();
                        for(int i = 0; i < projData.getJSONArray("charlist2").length(); i++){
                            list.add(projData.getJSONArray("charlist2").getString(i));
                        }
                        
                        characterB.CharacterPool=list.toArray(new String[0]);
                        }
                        
                        //Load all other scenes as well
                        final File folder = new File(dirPath);
                        List<String> scenesList=new ArrayList<>();
                        for (final File fileEntry : folder.listFiles()) {
                            if (fileEntry.isDirectory()) {
                                //listFilesForFolder(fileEntry);
                            } else {
                                
                                if(fileEntry.getAbsolutePath().contains(".JSON"))
                                scenesList.add(fileEntry.getAbsolutePath());
                               // System.out.println(fileEntry.getName());
                                
                            }
                        }
                        
                        scenesFiles=scenesList.toArray(new String[0]);



                    }

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
    
    /*
 * Creates an HBox with two buttons for the top region
     */
    private HBox addHBox() {

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);   // Gap between nodes
        hbox.setStyle("-fx-background-color: #336699;");

        Button buttonCurrent = new Button("Current");
        buttonCurrent.setPrefSize(100, 20);

        Button buttonProjected = new Button("Projected");
        buttonProjected.setPrefSize(100, 20);

        hbox.getChildren().addAll(buttonCurrent, buttonProjected);

        return hbox;
    }

    /*
 * Creates a VBox with a list of links for the left region
     */
    private VBox addVBox() {

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10)); // Set all sides to 10
        vbox.setSpacing(8);              // Gap between nodes

        Text title = new Text("Data");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);

        Hyperlink options[] = new Hyperlink[]{
            new Hyperlink("Sales"),
            new Hyperlink("Marketing"),
            new Hyperlink("Distribution"),
            new Hyperlink("Costs")};

        for (int i = 0; i < 4; i++) {
            // Add offset to left side to indent from title
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);
        }

        return vbox;
    }

    /*
 * Uses a stack pane to create a help icon and adds it to the right side of an HBox
 * 
 * @param hb HBox to add the stack to
     */
    private void addStackPane(HBox hb) {

        StackPane stack = new StackPane();
        Rectangle helpIcon = new Rectangle(30.0, 25.0);
        helpIcon.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop[]{
                    new Stop(0, Color.web("#4977A3")),
                    new Stop(0.5, Color.web("#B0C6DA")),
                    new Stop(1, Color.web("#9CB6CF")),}));
        helpIcon.setStroke(Color.web("#D0E6FA"));
        helpIcon.setArcHeight(3.5);
        helpIcon.setArcWidth(3.5);

        Text helpText = new Text("?");
        helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        helpText.setFill(Color.WHITE);
        helpText.setStroke(Color.web("#7080A0"));

        stack.getChildren().addAll(helpIcon, helpText);
        stack.setAlignment(Pos.CENTER_RIGHT);
        // Add offset to right for question mark to compensate for RIGHT 
        // alignment of all nodes
        StackPane.setMargin(helpText, new Insets(0, 10, 0, 0));

        hb.getChildren().add(stack);
        HBox.setHgrow(stack, Priority.ALWAYS);

    }

    /*
 * Creates a grid for the center region with four columns and three rows
     */
    private GridPane addGridPane() {

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));

        // Category in column 2, row 1
        Text category = new Text("Sales:");
        category.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        grid.add(category, 1, 0);

        // Title in column 3, row 1
        Text chartTitle = new Text("Current Year");
        chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        grid.add(chartTitle, 2, 0);

        // Subtitle in columns 2-3, row 2
        Text chartSubtitle = new Text("Goods and Services");
        grid.add(chartSubtitle, 1, 1, 2, 1);

        // House icon in column 1, rows 1-2
        ImageView imageHouse = new ImageView(
                new Image(MainApp.class.getResourceAsStream("graphics/house.png")));
        grid.add(imageHouse, 0, 0, 1, 2);

        // Left label in column 1 (bottom), row 3
        Text goodsPercent = new Text("Goods\n80%");
        GridPane.setValignment(goodsPercent, VPos.BOTTOM);
        grid.add(goodsPercent, 0, 2);

        // Chart in columns 2-3, row 3
        ImageView imageChart = new ImageView(
                new Image(MainApp.class.getResourceAsStream("graphics/piechart.png")));
        grid.add(imageChart, 1, 2, 2, 1);

        // Right label in column 4 (top), row 3
        Text servicesPercent = new Text("Services\n20%");
        GridPane.setValignment(servicesPercent, VPos.TOP);
        grid.add(servicesPercent, 3, 2);

//        grid.setGridLinesVisible(true);
        return grid;
    }

    /*
 * Creates a horizontal flow pane with eight icons in four rows
     */
    private FlowPane addFlowPane() {

        FlowPane flow = new FlowPane();
        flow.setPadding(new Insets(5, 0, 5, 0));
        flow.setVgap(4);
        flow.setHgap(4);
        flow.setPrefWrapLength(170); // preferred width allows for two columns
        flow.setStyle("-fx-background-color: DAE6F3;");

        ImageView pages[] = new ImageView[8];
        for (int i = 0; i < 8; i++) {
            pages[i] = new ImageView(
                    new Image(MainApp.class.getResourceAsStream(
                            "graphics/chart_" + (i + 1) + ".png")));
            flow.getChildren().add(pages[i]);
        }

        return flow;
    }

    /*
 * Creates a horizontal (default) tile pane with eight icons in four rows
     */
    private TilePane addTilePane() {

        TilePane tile = new TilePane();
        tile.setPadding(new Insets(5, 0, 5, 0));
        tile.setVgap(4);
        tile.setHgap(4);
        tile.setPrefColumns(2);
        tile.setStyle("-fx-background-color: DAE6F3;");

        ImageView pages[] = new ImageView[8];
        for (int i = 0; i < 8; i++) {
            pages[i] = new ImageView(
                    new Image(MainApp.class.getResourceAsStream(
                            "graphics/chart_" + (i + 1) + ".png")));
            tile.getChildren().add(pages[i]);
        }

        return tile;
    }

    /*
 * Creates an anchor pane using the provided grid and an HBox with buttons
 * 
 * @param grid Grid to anchor to the top of the anchor pane
     */
    private AnchorPane addAnchorPane(GridPane grid) {

        AnchorPane anchorpane = new AnchorPane();

        Button buttonSave = new Button("Save");
        Button buttonCancel = new Button("Cancel");

        HBox hb = new HBox();
        hb.setPadding(new Insets(0, 10, 10, 10));
        hb.setSpacing(10);
        hb.getChildren().addAll(buttonSave, buttonCancel);

        anchorpane.getChildren().addAll(grid, hb);
        // Anchor buttons to bottom right, anchor grid to top
        AnchorPane.setBottomAnchor(hb, 8.0);
        AnchorPane.setRightAnchor(hb, 5.0);
        AnchorPane.setTopAnchor(grid, 10.0);

        return anchorpane;
    }
}
