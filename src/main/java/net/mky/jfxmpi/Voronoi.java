/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Point3D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.util.Pair;
import net.mky.jfxmpi.TimeLineView.ArcPane;
import net.mky.jfxmpi.TimeLineView.CharactersMenu;
import net.mky.jfxmpi.TimeLineView.SpeechBox;
import net.mky.jfxmpi.TimeLineView.TimeLineStory;
import static net.mky.jfxmpi.TimeLineView.TimeLineStory.getImageB64From;
import net.mky.jfxmpi.TimeLineView.TimePane;
import net.mky.jfxmpi.bookView.BW;
import net.mky.tools.StylesForAll;
import org.json.JSONArray;
import org.json.JSONObject;

// 2018 TheFlyingKeyboard and released under MIT License
// theflyingkeyboard.net
public class Voronoi extends Application {

    private final int width = (int) Screen.getPrimary().getVisualBounds().getWidth();
    private final int height = (int) Screen.getPrimary().getVisualBounds().getHeight();
    private final double distanceOrder = 2.0d;
    private final int numberOfPoints = 40;
    private Point3D[] points = new Point3D[numberOfPoints];
    private Color[] colors = new Color[numberOfPoints];
    private final int pointSize = 5;
    private Canvas canvas;
    Color water = Color.BLUE;
    Color desert = Color.BEIGE;
    Color mountains = Color.CORAL;
    Color grassLand = Color.GREEN;
    Color land = Color.DARKGREEN;
    Color road = Color.GREY;

    Color[] geographyColors = {water, grassLand, grassLand, grassLand, land, grassLand, grassLand, grassLand, land};
    List<File> storyLine = new LinkedList<>();

    BorderPane border = new BorderPane();
    public ScrollPane messageScroller;
    VBox SystemButtons = new VBox();
    TimePane timePane = new TimePane();
    CharactersMenu charactermenu=new CharactersMenu(new CharacterPane(400, 600, false));
    VBox Chapters = new VBox();

    File gameFile = new File("GameTemp.jmp");
    JSONObject gameJSON = new JSONObject();

    //World objects
    String castle = Voronoi.class.getResource("/buildings/castle_7.png").toExternalForm();
    String roads = Voronoi.class.getResource("/road/dirt-path-png-1.png").toExternalForm();
    String ruins = Voronoi.class.getResource("/buildings/ruins.png").toExternalForm();
    List<Point3D> ruinsLocs = new LinkedList<>();
    Point3D castleXY = new Point3D(0, 0, 0);

    String trees = Voronoi.class.getResource("/trees/tree-13.png").toExternalForm();
    List<Point3D> treeLocs = new LinkedList<>();

    String village = Voronoi.class.getResource("/buildings/barracks_1.png").toExternalForm();
    List<Point3D> villageLocs = new LinkedList<>();

    @Override
    public void init() {
        boolean castlePlaced = false;
        for (int i = 0; i < numberOfPoints; ++i) {
            points[i] = new Point3D(ThreadLocalRandom.current().nextInt(width), ThreadLocalRandom.current().nextInt(height), 0);
            //colors[i] = Color.rgb(ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255));
            int region = (int) ((geographyColors.length - 1) * Math.random());
            colors[i] = geographyColors[region];
            if (!castlePlaced && region == 4) {
                castleXY = points[i];
                castlePlaced = false;
            } else if (region == 3 || region == 4) {
                treeLocs.add(points[i]);
            } else if (region == 5 || region == 6) {
                villageLocs.add(points[i]);
            } else if (region != 0) {
                ruinsLocs.add(points[i]);
            }

        }
        
        Chapters.setSpacing(10);
        messageScroller = new ScrollPane(Chapters);
        messageScroller.setStyle("-fx-background-color: transparent;");
        messageScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        messageScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageScroller.setMaxHeight(800);//420
        messageScroller.setPrefWidth(1000);//420
        
       // messageScroller.prefWidthProperty().bind(Chapters.prefWidthProperty().subtract(5));
        messageScroller.setFitToWidth(true);
     
    }

    void loadWorld(Pane root) {
        //root.getChildren().clear();
        //root.getChildren().add(canvas);

        //Roads
        ruinsLocs.stream().forEach(treelocs -> {

            //Road to castle
            ImageView imgvtr = new ImageView(ruins);
            imgvtr.setFitWidth(300);
            imgvtr.setFitHeight(300);
            imgvtr.setPreserveRatio(true);
            imgvtr.setTranslateX(treelocs.getX() + 300 * Math.random());
            imgvtr.setTranslateY(treelocs.getY() + 300 * Math.random());
            root.getChildren().add(imgvtr);
        });

        //Roads
        villageLocs.stream().forEach(treelocs -> {

            //Road to castle
            ImageView imgvtr = new ImageView(roads);
            imgvtr.setFitWidth(300);
            imgvtr.setFitHeight(300);
            imgvtr.setPreserveRatio(true);
            imgvtr.setTranslateX(treelocs.getX() + 300 * Math.random());
            imgvtr.setTranslateY(treelocs.getY() + 300 * Math.random());
            root.getChildren().add(imgvtr);
        });
        //Villages
        villageLocs.stream().forEach(treelocs -> {

            for (int i = 0; i < 10; i++) {
                ImageView imgvt = new ImageView(village);
                imgvt.setFitWidth(80);
                imgvt.setFitHeight(80);
                imgvt.setPreserveRatio(true);
                imgvt.setTranslateX(treelocs.getX() + 300 * Math.random());
                imgvt.setTranslateY(treelocs.getY() + 300 * Math.random());
                root.getChildren().add(imgvt);
            }

        });

        //Trees
        treeLocs.stream().forEach(treelocs -> {
            for (int i = 0; i < 20; i++) {
                ImageView imgvt = new ImageView(trees);
                imgvt.setFitWidth(100);
                imgvt.setFitHeight(100);
                imgvt.setPreserveRatio(true);
                imgvt.setTranslateX(treelocs.getX() + 200 * Math.random());
                imgvt.setTranslateY(treelocs.getY() + 200 * Math.random());
                root.getChildren().add(imgvt);
            }
        });

        //Castle
        ImageView imgv = new ImageView(castle);
        imgv.setFitWidth(250);
        imgv.setFitHeight(250);
        imgv.setPreserveRatio(true);
        imgv.setTranslateX((int) Screen.getPrimary().getVisualBounds().getWidth() / 2);
        imgv.setTranslateY(100);
        root.getChildren().add(imgv);

        //Set Game Board
        gameJSON = new JSONObject();

        //Characters
        CharacterPane newCharacter = new CharacterPane(width, height, false);
        newCharacter.setName("MAKER");
        newCharacter.setAge("ETERNAL");
        JSONArray characters = new JSONArray();
        characters.put(newCharacter.getCharacterData());
        gameJSON.put("Characters", new JSONArray());

        JSONArray Chapters = new JSONArray();
        TimeLineStory tls = new TimeLineStory("Maker", 800, 700);
        Chapters.put(tls.getChapterData());
        gameJSON.put("Chapters", new JSONArray());

    }

    @Override
    public void start(Stage stage) {
        canvas = new Canvas(width, height);
        generateVoronoi();

        Pane root = new Pane();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        loadWorld(root);
        root.getChildren().add(border);
        HBox menu=new HBox(timePane,charactermenu);
        border.setTop(menu);
        border.setLeft(messageScroller);
        border.setTranslateX(100);
        border.setTranslateY(0);
        
        Chapters.setStyle(StylesForAll.transparentAlive);

        //Add the scenes
        Button loadWorld = new Button("Load World");
        loadWorld.setStyle(StylesForAll.transparentAlive);
        loadWorld.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(null);
            gameFile = selectedFile;

            String content;
            try {
                content = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));
                gameJSON = new JSONObject(content);
                JSONArray ChaptersJSON = gameJSON.getJSONArray("Chapters");
                for (int i = 0; i < ChaptersJSON.length(); i++) {
                    if (ChaptersJSON.getJSONObject(i).has("file")) {
                        Button name = new Button(ChaptersJSON.getJSONObject(i).getString("file"));
                        File chapter = new File(gameFile.getParentFile().getAbsolutePath()+"/"+ChaptersJSON.getJSONObject(i).getString("file"));
                        SpeechBox sb = TimeLineStory.previewHelper(chapter);
                        List<SpeechBox> sbs = TimeLineStory.previewHelper(chapter,5);
                        
                        name.setText("Chapter: "+sb.message);
                        name.setStyle(StylesForAll.transparentAlive);
                        name.setOnAction(event2 -> {
                            chapterShow(chapter,"Today");
                        });
                        Chapters.getChildren().add(new ArcPane(sbs, name,sb.getDateTime()));
                    } else {

                    }
                }
                
                /***************************************************************
                 * LOAD CHARACTERS
                 */
                JSONArray characters = gameJSON.getJSONArray("Characters");
                for (int i = 0; i < characters.length(); i++) {
                    CharacterPane characterThis = new CharacterPane(width, height, false);
                    characterThis.loadCharacterData(characters.getJSONObject(i));
                    charactermenu.addCharacter(characterThis);
                };

            } catch (IOException ex) {
                Logger.getLogger(Voronoi.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        /***********************************************************************
         * SAVE CHAPTERS
         */
        Button saveChat = new Button("Save");
        saveChat.setStyle(StylesForAll.transparentAlive);
        saveChat.setOnAction(event -> {
            //Write JSON file
            // try (FileWriter file = new FileWriter(dir.getAbsolutePath()+"/Chat_"+System.currentTimeMillis()+".json")) {
            Writer fstream = null;
            BufferedWriter out = null;
            try {
                
                //Save characters
                JSONArray characters = new JSONArray();
                for(CharacterPane cp:charactermenu.charactersArray){
                    //charPrefixes
                        characters.put(cp.getCharacterData());
                    }
                gameJSON.put("Characters", characters);
                
                fstream = new OutputStreamWriter(new FileOutputStream(gameFile), StandardCharsets.UTF_8);
                fstream.write(gameJSON.toString(1));
                fstream.flush();

                System.out.println("Saved the file");
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setContentText("Saved the file"); 
                a.show();

            } catch (IOException e) {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setContentText("Failed to saved the file"); 
                a.show();
                e.printStackTrace();
            }

        });

        //Create Chapter
        //Add the scenes
        /***********************************************************************
         * ADD ARC/Chapter
         */
        Button createChapter = new Button("Add Arc");
        createChapter.setStyle(StylesForAll.transparentAlive);
        createChapter.setOnAction(event -> {

            TextInputDialog td = new TextInputDialog("Arc Description");

            // setHeaderText 
            td.setHeaderText("Chat file name");
            td.getEditor().setText("");
            td.showAndWait();
            String newArc = td.getEditor().getText();

            td = new TextInputDialog("Time of event");

            // setHeaderText 
            td.setHeaderText("When?");
            td.getEditor().setText("Day one");
            td.showAndWait();
            String timeOfEvent = td.getEditor().getText();
             List<Pair<String, String>> messages = new LinkedList<>();
             messages.add(new Pair(newArc+": "+timeOfEvent,""));
            addArc( newArc, timeOfEvent,messages );


        });

        /*********************************************************************
         * LOAD ANY CHAT
         */
        //Add the scenes
        Button loadChapters = new Button("Load Random");
        loadChapters.setStyle(StylesForAll.transparentAlive);
        loadChapters.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            List<File> imageList = fileChooser.showOpenMultipleDialog(null);
            //Read all the file and conver to json
            //Render sppech boxes.
            try {

                int counter = 0;
                for (File file : imageList) {
                    storyLine.add(file);

                    SpeechBox sb = TimeLineStory.previewHelper(file);
                    sb.setScaleX(.3);
                    sb.setScaleY(.3);
                    sb.setScaleZ(.3);
                    sb.setTranslateX(.3 * points[counter].getX());
                    sb.setTranslateY(.3 * points[counter].getY());
                    
                    List<SpeechBox> sbs = TimeLineStory.previewHelper(file,3);

                    Button name = new Button(file.getName());
                    name.setStyle(StylesForAll.transparentAlive);
                    name.setOnAction(event2 -> {
                        chapterShow(file,"Today");
                    });

                    Chapters.getChildren().add(new ArcPane(sbs, name,sb.getDateTime()));
                    root.getChildren().add(sb);
                    counter++;
                    
                    
                    //Add to game temp
                    JSONArray allChapters = gameJSON.has("Chapters") ? gameJSON.getJSONArray("Chapters") : new JSONArray();
                    JSONObject chapterData = new TimeLineStory(file, " ", height, width).getChapterData();
                    chapterData.put("file", file.getName());
                    allChapters.put(chapterData);
                    saveFile(file, allChapters.toString(1));
                    gameJSON.put("Chapters", allChapters);
                }
            } catch (Exception ex) {
            }

        });
        
        /***********************************************************************
         * GENERATE CHARACTER INTERACTIONS
         */
        //Generate Character combinations
        //Add the scenes
        Button characterInteractionCombinations = new Button("Generate Interactions");
        characterInteractionCombinations.setStyle(StylesForAll.transparentAlive);
        characterInteractionCombinations.setOnAction(event -> {
            //Get maximum number of possible pairs to generate
            TextInputDialog td = new TextInputDialog("Number of characters in one chapter");

            // setHeaderText 
            td.setHeaderText("Count");
            td.getEditor().setText("3");
            td.showAndWait();

            int numOfChar = Integer.parseInt(td.getEditor().getText());

            HashMap<String, List<CharacterPane>> result = charactermenu.generateCombinations();
            for (Map.Entry<String, List<CharacterPane>> comb : result.entrySet()) {

                //Check if Primary character exists
                if (!charactermenu.hasPrimaryCharacter(comb.getValue())) {
                    continue;
                }
                if (numOfChar < comb.getValue().size()) {
                    continue;
                }//Skip if number of chars are more

                List<Pair<String, String>> messages = new LinkedList<>();
                
                try {//Generate group image
                    messages.add(new Pair("Exposition", charactermenu.b64ImageCharacter(comb.getValue())));
                } catch (IOException ex) {
                    Logger.getLogger(Voronoi.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                for (CharacterPane ch : comb.getValue()) {
                    System.out.print(ch.getLife().getName() + ",");
                    messages.add(new Pair("Name : " + ch.getLife().getName() + "(" + ch.getLife().TAG + ")", ch.CharacterFile));

                }

                HashMap<String, List<CharacterPane>> interactions = charactermenu.generateCombinations(comb.getValue());
                for (Map.Entry<String, List<CharacterPane>> combIn : interactions.entrySet()) {
                    if (combIn.getValue().size() < 2) {
                        continue;//Minimum 2 characters needed.
                    }
                    if (!charactermenu.hasPrimaryCharacter(combIn.getValue())) {
                        continue;//Check if Primary character exists
                    }
                    messages.add(new Pair("Interaction: " + combIn.getKey(), ""));
                    messages.add(new Pair("Rising Action:" + combIn.getKey(), ""));
                    messages.add(new Pair("Rising Action:" + combIn.getKey(), ""));
                    messages.add(new Pair("Rising Action:" + combIn.getKey(), ""));
                    messages.add(new Pair("Climax:" + combIn.getKey(), ""));
                }
                messages.add(new Pair("Falling Action:" + comb.getKey(), ""));
                messages.add(new Pair("Falling Action" + comb.getKey(), ""));

                //Every characters resolution.
                for (CharacterPane ch : comb.getValue()) {
                    System.out.print(ch.getLife().getName() + ",");
                    messages.add(new Pair("Resolution : " + ch.getLife().getName() + "(" + ch.getLife().TAG + ")", ch.CharacterFile));
                }
                
                 try {//Generate group image
                    messages.add(new Pair("Resolution", charactermenu.b64ImageCharacter(comb.getValue())));
                } catch (IOException ex) {
                    Logger.getLogger(Voronoi.class.getName()).log(Level.SEVERE, null, ex);
                }

                addArc(comb.getKey(), "When", messages);//One chapter per set.

                System.out.println();
            }
        });

        SystemButtons.getChildren().add(loadChapters);
        SystemButtons.getChildren().add(createChapter);
        SystemButtons.getChildren().add(characterInteractionCombinations);
        SystemButtons.getChildren().add(loadWorld);
        SystemButtons.getChildren().add(saveChat);

        root.getChildren().add(SystemButtons);

        scene.getStylesheets().add(BW.class.getResource("/book/bw.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Voronoi Diagram");
        stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
        stage.show();

        File file = new File("Voronoi " + width + "X" + height + " " + numberOfPoints + " points.png");
        WritableImage writableImage = new WritableImage(width, height);
        canvas.snapshot(null, writableImage);
        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        try {
            ImageIO.write(renderedImage, "png", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show chapter
     *
     * @param file
     */
    public void chapterShow(File file,String timeOfEvent) {
        Dialog dialog = new Dialog();
        dialog.getDialogPane().setStyle("-fx-background-color:linear-gradient(to right, #fc5c7d, #6a82fb);");
        dialog.getDialogPane().getStylesheets().add(BW.class.getResource("/book/bw.css").toExternalForm());
        TimeLineStory tls=new TimeLineStory(file, "User", 650, 500);
        tls.setDateTime(timeOfEvent);
        dialog.getDialogPane().setContent(tls);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.showAndWait();

    }

    public void saveFile(File file, String content) {
        Writer fstream = null;
        BufferedWriter out = null;
        try {
            fstream = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            fstream.write(content);
            fstream.flush();

            System.out.println("Saved the file");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param newArc File name
     * @param timeOfEvent Time
     * @param messages Messages
     */
    public void addArc(String newArc,String timeOfEvent,List<Pair<String,String>> messages ){

        Button name = new Button(newArc);
        SpeechBox sb = new SpeechBox(newArc + ": " + timeOfEvent, SpeechBox.SpeechDirection.CENTER);
        name.setStyle(StylesForAll.buttonGradientRedYellow);
        List<SpeechBox> listOfSpb=new LinkedList<>();
        
        File arcFile = new File(gameFile.getParentFile().getAbsolutePath() + "/" + newArc + ".jmpc");
        if (arcFile.exists()) {//Check if it already exists.
            Alert a = new Alert(AlertType.CONFIRMATION);
            a.setContentText("Chapter file already exists. Not creating again.");
            a.show();
            return;
        }
        try {
            arcFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Voronoi.class.getName()).log(Level.SEVERE, null, ex);
        }
        name.setOnAction(event2 -> {
            chapterShow(arcFile, timeOfEvent);
        });

        JSONArray allChapters = gameJSON.has("Chapters") ? gameJSON.getJSONArray("Chapters") : new JSONArray();
        TimeLineStory tls = new TimeLineStory("Maker", 800, 700);
        //tls.speechBubbles.add(sb);
        for (Pair<String,String> message : messages) {
            if (message.getValue().contentEquals("")) {
                SpeechBox sb2 = new SpeechBox(message.getKey(), SpeechBox.SpeechDirection.CENTER);
                tls.speechBubbles.add(sb2);
                listOfSpb.add(sb2);
            } else {
                SpeechBox sb2;
                try {
                    if(message.getValue().length()<500)
                    {
                        sb2 = new SpeechBox("What happened next?", getImageB64From(new File(message.getValue())), SpeechBox.SpeechDirection.CENTER,1);
                    }else{//Value is base64Image
                         sb2 = new SpeechBox("What happened next?", message.getValue(), SpeechBox.SpeechDirection.CENTER,1);
                   
                    }
                    listOfSpb.add(sb2);
                    tls.speechBubbles.add(sb2);
                } catch (IOException ex) {
                    Logger.getLogger(Voronoi.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }

        //Save Chapter
        JSONObject chapterData = tls.getChapterData();
        chapterData.put("file", arcFile.getName());
        chapterData.put("timeOfEvent", timeOfEvent);
        saveFile(arcFile, chapterData.toString(1));//Save chapter
        
        //Save Game
        chapterData = new JSONObject();//Save only file links
        chapterData.put("file", arcFile.getName());
        chapterData.put("timeOfEvent", timeOfEvent);
        allChapters.put(chapterData);
        gameJSON.put("Chapters", allChapters);
        //Save game file as well.
        saveFile(gameFile, gameJSON.toString(1));
        
        //Display
        Chapters.getChildren().add(new ArcPane(listOfSpb, name, timeOfEvent));
        
        //Free memory
        tls=null;
        messages=null;
        listOfSpb=null;
        
    }

    /**
     *
     */
    private void generateVoronoi() {
        PixelWriter pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                double random = Math.random();
                pixelWriter.setColor(x, y,
                        random > .7 ? colors[findClosestPoint(new Point3D(x, y, 0))] : colors[findClosestPoint(new Point3D(x, y, 0))].darker());
                pixelWriter.setColor((int) (x + 20 * Math.sin(random * Math.PI / 2)),
                        (int) (y - 20 * Math.cos(random * Math.PI * 2)),
                        random > .7 ? colors[findClosestPoint(new Point3D(x, y, 0))] : colors[findClosestPoint(new Point3D(x, y, 0))].darker());

            }
        }

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.BLACK);

        for (int i = 0; i < numberOfPoints; ++i) {
            graphicsContext.fillOval(points[i].getX(), points[i].getY(), pointSize, pointSize);
        }
        
        //The free up
      points=null;
      colors=null;

    }

    private int findClosestPoint(Point3D point) {
        int index = 0;
        double minDistance = distance(point, points[index]);
        double currentDistance;

        for (int i = 1; i < numberOfPoints; ++i) {
            currentDistance = distance(point, points[i]);

            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                index = i;
            }
        }

        return index;
    }

    private double distance(Point3D pointA, Point3D pointB) {
        if (distanceOrder == 1.0d) {
            return Math.abs(pointA.getX() - pointB.getX()) + Math.abs(pointA.getY() - pointB.getY());
        }

        if (distanceOrder == 2.0d) {
            return Math.sqrt((pointA.getX() - pointB.getX()) * (pointA.getX() - pointB.getX())
                    + (pointA.getY() - pointB.getY()) * (pointA.getY() - pointB.getY()));
        }

        return Math.pow(Math.pow(Math.abs(pointA.getX() - pointB.getX()), distanceOrder)
                + Math.pow(Math.abs(pointA.getY() - pointB.getY()), distanceOrder), (1.0d / distanceOrder));
    }

    public static void main(String[] args) {
        Application.launch(Voronoi.class, args);
    }
}
