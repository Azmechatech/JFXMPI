package net.mky.jfxmpi;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import net.mky.graphUI.ImagePool;
import net.mky.safeStore.EncryptionBatchProcess;
import net.mky.safeStore.MediaCryptoHelper;
import net.mky.safeStore.OpenEncryptedFile;
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
import net.mky.clustering.HilbertCurvePatternDetect;
import systemknowhow.tools.NERHelper;
import systemknowhow.tools.TextHelper;

public class MainApp extends Application {

    String dirPath = "";
    File selectedDirectory;
    String secrteKey="1234567893695412";
    String scenesFiles[];int sceneCounter=0;
    JSONObject projData;
    int width = 200;
    int height = 600;
    CharacterPane characterA = new CharacterPane(width, height, false);
    CharacterPane characterB = new CharacterPane(width, height, false);
    CharacterPane characterC = new CharacterPane(width, height, false);
     GraphPane graphBoard=new GraphPane();
    StoryBoard StoryBoard = new StoryBoard(width, height);
    StoryTimeline storyTimeline=new StoryTimeline();
    public static boolean IS_MKFS_DIRECTORY=false;
    
    List<CharacterPane> charactersArray;
    //List<TextBubble> textBubbleArray;
    String[] charPrefixes=new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q"};
    
    
    final MainPane pe = new MainPane();
    final BruteForceRenderer bfr=new BruteForceRenderer();
    static ConversationMaker convMaker;
    String thePath = "";
    private   String MEDIA_URL = Paths.get(thePath).toUri().toString();
    private MediaPlayer mediaPlayer;


    public static void main(String[] args) throws Exception {
        convMaker=new ConversationMaker();
        TextHelper.init();
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
         //textBubbleArray=new ArrayList<>();
         HBox characters=new HBox();
         characters.getChildren().add(storyTimeline);
         pe.setTranslateX(0);
         pe.setTranslateY(0);
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

        Button playButton = new Button("Background");
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
                        pe.setTranslateX(0);
                        pe.setTranslateY(0);
                        
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

                    int i=0;
                    for(CharacterPane cp:charactersArray){
                    //charPrefixes
                        projData.put("character"+charPrefixes[i++], cp.getCharacterData());
                    }
                    
                    projData.put("story", storyTimeline.STORY);

                    if (IS_MKFS_DIRECTORY) {//If the selected working directory is encrypted directory
                        String name = "game_" + System.currentTimeMillis() + ".JSON";
                        //selectedDirectory
                        MediaCryptoHelper.saveCryptoText(secrteKey, projData.toString(), selectedDirectory, name);

                    } else {
                        List<String> lines = Arrays.asList(projData.toString());
                        Path file = Paths.get(dirPath + "/game_" + System.currentTimeMillis() + ".JSON");
                        Files.write(file, lines, Charset.forName("UTF-8"));

                    }
                    
                    
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
        
        
        //Show StoryMatrix
        
        EventHandler<ActionEvent> storyMatrixPaneAction = new EventHandler<ActionEvent>() {
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
                    root.setContent(new StoryMatrix(charactersArray));
//                    dialog.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
//                    dialog.setHeight(Screen.getPrimary().getVisualBounds()
//                            .getHeight());
                    Scene dialogScene = new Scene(root, 400, 400);
                    dialog.setScene(dialogScene);
                    dialog.show();

            }
        };

        Button storyMatrixPane = new Button("Story Matrix");
        //addCharPane.getStyleClass().add("play");
        storyMatrixPane.setOnAction(storyMatrixPaneAction);
        storyMatrixPane.setStyle(StylesForAll.transparentAlive);
        buttonPane.getChildren().add(storyMatrixPane);
        
        
        EventHandler<ActionEvent> takeSnapshot = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                WritableImage writableImage = new WritableImage((int)scene.getWidth(), (int)scene.getHeight());
                SnapshotParameters sParam=new SnapshotParameters();
                WritableImage image = scene.snapshot(writableImage);
                //WritableImage image = pe.snapshot(sParam, writableImage);
                    
                // TODO: probably use a file chooser here
                File file = new File(dirPath + "/" + System.currentTimeMillis() + ".png");

                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                    //MediaCryptoHelper.saveCryptoMedia(secrteKey,SwingFXUtils.fromFXImage(image, null), selectedDirectory);
   
                } catch (IOException e) {
                    // TODO: handle exception here
                }
                
                //Store text as well
               

            }
        };

        Button TakeSnapShot = new Button("Snapshot");
        TakeSnapShot.getStyleClass().add("play");
        TakeSnapShot.setOnAction(takeSnapshot);
        TakeSnapShot.setStyle(StylesForAll.transparentAlive);
        buttonPane.getChildren().add(TakeSnapShot);
//Clipboard image
Button bnPaste = new Button("Ctrl+V");
bnPaste.setStyle(StylesForAll.transparentAlive);
bnPaste.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    try {
                        java.awt.Image image = getImageFromClipboard();
                        if (image != null) {
                            javafx.scene.image.Image fimage = awtImageToFX(image);
                            //pe.imageView.setFitHeight(scene.getHeight());
                           // pe.imageView.setFitWidth(scene.getWidth());
                            pe.imageView.setX(0);
                            pe.imageView.setY(0);
                            pe.imageView.setImage(fimage);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        buttonPane.getChildren().add(bnPaste);
 
        //Text bubble
        Button bnTextBubble = new Button("Text Bubble");
        bnTextBubble.setStyle(StylesForAll.transparentAlive);
        bnTextBubble.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
               characters.getChildren().add(new TextBubble( false));
               //characters.getChildren().add(new StoryTimeline());
            }
        });
        buttonPane.getChildren().add(bnTextBubble);

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

        EventHandler<ActionEvent> createGameAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Set textPane data
                FileChooser fileChooser = new FileChooser();

                fileChooser.setTitle("Open Text File");
                File file = fileChooser.showOpenDialog(new Stage());
                //CharacterFile=file.getAbsolutePath();
//                        if (file != null) {
//                            SeedFile = file.getAbsolutePath();
//                        }

                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                byte[] data = new byte[(int) file.length()];
                try {
                    fis.read(data);
                } catch (IOException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    String str = new String(data, "UTF-8");

                    HashMap<String, Life> peopleInStory = storyTimeline.addCustomTimeLine(0, 800, NERHelper.getStorySequence(str), 3);
                    //Generate Character Frames automatically
                    for (Map.Entry<String, Life> entry : peopleInStory.entrySet()) {
                        CharacterPane newCharacter = new CharacterPane(width, height, false);
                        newCharacter.setName(entry.getKey());
                        newCharacter.SeedFile=file.getAbsolutePath();
                        newCharacter.thisCharcter=entry.getValue();
                        newCharacter.refresh();
                        charactersArray.add(newCharacter);
                        characters.getChildren().add(newCharacter);
                    }

                    
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassCastException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        };

        Button createGame = new Button("Manuscript");
         createGame.setOnAction(createGameAction);
        createGame.setStyle(StylesForAll.transparentAlive);
        buttonPane.getChildren().add(createGame);
        
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

                    fileChooser.setInitialDirectory(selectedDirectory);
                    File file = fileChooser.showOpenDialog(stage);
                       FileInputStream input;
                       String str = null;  
                    
                    
                    
                    if (file != null) {
                        
                        if(IS_MKFS_DIRECTORY){
                       HashMap<String,String> availableGames= MediaCryptoHelper.getAvailableFiles(file, "game");
                        
                        ChoiceDialog<String> dialog = new ChoiceDialog<>("b", availableGames.keySet());
                        dialog.setTitle("Choice Dialog");
                        dialog.setHeaderText("Look, a Choice Dialog");
                        dialog.setContentText("Choose your letter:");
                        Optional<String> result = dialog.showAndWait();
                         if(result.isPresent()){
                            System.out.println("Your choice: " + availableGames.get(result.get()));
                            file=new File( availableGames.get(result.get()));
                            str =new String( MediaCryptoHelper.readCryptoText(secrteKey, file).getBytes(), "UTF-8");//new String(data, "UTF-8");
                        }
                        
                        }
                       //  result.ifPresent(letter -> System.out.println("Your choice: " + letter));
                      
                       else{
                        
                            input = new FileInputStream(file.getAbsolutePath());

                            byte[] data = new byte[(int) file.length()];
                            input.read(data);
                            input.close();

                             str = new String(data, "UTF-8");
                             
                        }
                        
                        
                        projData = new JSONObject(str);
                        
                        //Assuming MKFS file is loaded 
                        //Show available game files for selection.
                        
                        

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
                            if(projData.has("characterB"))characterB.loadCharacterData(projData.getJSONObject("characterB"));
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

                                
                                try {
                                    Image imageFA = SwingFXUtils.toFXImage(HilbertCurvePatternDetect.resizeImage(HilbertCurvePatternDetect.getMatchedRegion("C:/$AVG/baseDir/Imports/Sprites/OBJECTS/face.png", characterThis.CharacterFile), 75, 75), null);
                                    ImagePool.pool.put(characterThis.getLife().getName(), SwingFXUtils.toFXImage(HilbertCurvePatternDetect.getMatchedRegion("C:/$AVG/baseDir/Imports/Sprites/OBJECTS/face.png", characterThis.CharacterFile), null));
                                    LifePool.pool.put(characterThis.getLife().getName(), characterThis.getLife());
                                    StoryBoard.addCharacter(new ImageView(imageFA), characterThis.name);
                                } catch (Exception ex) {
                                    System.err.println("Face pattern image not found");
                                }
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
                selectedDirectory = directoryChooser.showDialog(stage);

                if (selectedDirectory == null) {
                    dirPath = "No Directory selected";
                } else {
                    //Check if mkfs file is present
                    //
                    IS_MKFS_DIRECTORY = EncryptionBatchProcess.checkEncryptionFolder(selectedDirectory);
                    if (IS_MKFS_DIRECTORY)
                        secrteKey = EncryptionBatchProcess.askForPassword();
                    else {
                        try {
                            IS_MKFS_DIRECTORY = EncryptionBatchProcess.askAndMakeMKFSFolder(selectedDirectory);
                        } catch (IOException ex) {
                            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
//                    String[] folderNames=OpenEncryptedFile.getDecryptedFolderNames(selectedDirectory, secrteKey);
//                    System.out.println(Arrays.toString(folderNames));
//                    
//                    try {
//                        HashMap<String,String> folderContent=OpenEncryptedFile.getMKFSFolderContent(selectedDirectory, secrteKey);
//                        
//                    } catch (IOException ex) {
//                        Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
//                    }
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
                
                showBubbleSelectionDialog();
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
    
   //Controls
    private void showBubbleSelectionDialog() {
        Dialog<ResultsOfControlDialog> dialog = new Dialog<>();
        dialog.setTitle("Dialog Test");
        dialog.setHeaderText("Please specify…");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        

        DatePicker datePicker = new DatePicker(LocalDate.now());
        ObservableList<TextBubbleStyles.STYLES> options
                = FXCollections.observableArrayList(TextBubbleStyles.STYLES.values());
        ComboBox<TextBubbleStyles.STYLES> comboBox = new ComboBox<>(options);
        //comboBox.getSelectionModel().selectFirst();
        //comboBox.getSelectionModel().select(tbss);
        dialogPane.setContent(new VBox(8,  comboBox));
        Platform.runLater(comboBox::requestFocus);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return new ResultsOfControlDialog("", comboBox.getValue());
            }
            return null;
        });
        Optional<ResultsOfControlDialog> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((ResultsOfControlDialog results) -> {
            
        });

    }

    private static class ResultsOfControlDialog {

        String SpeechText;

        TextBubbleStyles.STYLES venue;

        String TEXT_BUBBLE = "";

        public ResultsOfControlDialog(String name, TextBubbleStyles.STYLES venue) {
            this.SpeechText = name;
            this.venue = venue;
            TEXT_BUBBLE = TextBubbleStyles.getTextBubbleStyles().get(venue.toString());

        }
    }
    
     public static java.awt.Image getImageFromClipboard() {
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                try {
                    return (java.awt.Image) transferable.getTransferData(DataFlavor.imageFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public  static javafx.scene.image.Image awtImageToFX(java.awt.Image image) throws Exception {
            if (!(image instanceof RenderedImage)) {
                BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                        BufferedImage.TYPE_INT_ARGB);
                Graphics g = bufferedImage.createGraphics();
                g.drawImage(image, 0, 0, null);
                g.dispose();

                image = bufferedImage;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write((RenderedImage) image, "png", out);
            out.flush();
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            return new javafx.scene.image.Image(in);
        }
}
