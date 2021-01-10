/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import net.mky.jfxmpi.TimeLineView.SpeechBox;
import net.mky.jfxmpi.TimeLineView.TimeLineStory;
import static net.mky.jfxmpi.TimeLineView.TimeLineStory.getImageB64From;
import static net.mky.jfxmpi.Voronoi.chapterShow;
import net.mky.safeStore.MapDB;
import net.mky.tools.StylesForAll;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerArrayTuple;
import systemknowhow.human.Female;
import systemknowhow.human.Life;
import systemknowhow.human.LifeTagFactory;
import systemknowhow.human.Male;
import systemknowhow.human.RuleFactory;
import systemknowhow.human.guns.CompositeGun;
import systemknowhow.humanactivity.SocialRelationTags;

/**
 * From
 * :https://stackoverflow.com/questions/50307003/chess-border-around-pawns-javafx
 * Create character log, use character log for their conversation
 * Introduce character goals. 
 * Build over mapdb
 * @author ryzen
 */
public class WorldBoard extends Application {

    int worldSizeW = 23;
    int worldSizeH = 13;
    WorldObjects wo = new WorldObjects();
    public HTreeMap<String, String> WorldObjectsLocation;// = new HashMap<>();
    public HTreeMap<String, String> TimeLineStoryLink;
    NavigableSet<Object[]> charLog;
    NavigableSet<Object[]> charCurrentLocation;
    HashMap<ImageView, Long> objectStay = new HashMap<>();
    HashMap<ImageView, String> objectStayLocation = new HashMap<>();
    HashMap<ImageView, String> objectName = new HashMap<>();
    HashMap<String, Life> objectLife = new HashMap<>();
    
    final GridPane group = new GridPane();
     DB db;
     boolean PAUSE=false;

    @Override
    public void start(Stage primaryStage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(primaryStage);
        RuleFactory.init();

        db = DBMaker
                .fileDB(selectedDirectory.getAbsolutePath() + "/WorldBoard.db").transactionEnable()
                .fileMmapEnable()
                .make();
        WorldObjectsLocation = db.hashMap("WorldObjectsLocation")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.STRING)
                .createOrOpen();
        TimeLineStoryLink = db.hashMap("TimeLineStoryLink")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.STRING)
                .createOrOpen();

        charLog = db.treeSet("charLog")//Log of the character's actions and stay
                //set tuple serializer //Name, Systemmillisec,location, stay duration, action, action duration
                .serializer(new SerializerArrayTuple(Serializer.STRING, Serializer.LONG, Serializer.STRING, Serializer.LONG, Serializer.STRING, Serializer.LONG))
                .counterEnable()
                .counterEnable()
                .counterEnable()
                .createOrOpen();
        charCurrentLocation = db.treeSet("charCurrentLocation")//Log of the character's actions and stay
                //set tuple serializer //Location Character action
                .serializer(new SerializerArrayTuple(Serializer.STRING, Serializer.STRING, Serializer.STRING))
                .counterEnable()
                .counterEnable()
                .counterEnable()
                .createOrOpen();
        charCurrentLocation.clear();
        primaryStage.setTitle("World Board");
        primaryStage.getIcons().add(new Image("/char/7JU7r.png"));
        GridPane root = new GridPane();
        group.setPadding(new Insets(15, 25, 25, 25));

        //Set the world board
        for (int i = 0; i < worldSizeW; i++) {
            for (int j = 0; j < worldSizeH; j++) {
                Rectangle rectangle = new Rectangle(75, 75);
                if (j % 2 == 0 && (i % 2 == 0)) {
                    rectangle.setFill(Color.BEIGE);
                } else if (!((j + 2) % 2 == 0) && !((i + 2) % 2 == 0)) {
                    rectangle.setFill(Color.BEIGE);
                } else {
                    rectangle.setFill(Color.GRAY);
                }
                group.add(rectangle, i, j);
                try {
                    if (Math.random() > .95) {
                        WorldObject wob = wo.getQ().remove();
                        group.add(wob.IMG, i, j);
                        WorldObjectsLocation.put(i + ":" + j, wob.getName());

                        //Action button
                        Button timeLine = new Button("Time Line");
                        group.add(timeLine, i, j);
                        timeLine.setStyle(StylesForAll.transparentAlive);
                        timeLine.setOnAction(event -> {
                            
                            if (!TimeLineStoryLink.containsKey(wob.getName())) {
                                 Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                                a.setContentText("No characters available.");
                                a.show();
                                return;
                            }
                            String location = TimeLineStoryLink.get(wob.getName());

                            File arcFile = new File(selectedDirectory.getAbsolutePath() + "/" + location + ".jmpc");
                            if (arcFile.exists()) {//Check if it already exists.
                                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                                a.setContentText("Chapter file already exists. Not creating again.");
                                a.show();
                                
                            }else
                            try {
                                arcFile.createNewFile();
                            } catch (IOException ex) {
                                Logger.getLogger(Voronoi.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            String timeOfEvent = new Date().toString();
                            List<Pair<String, String>> messages = new LinkedList<>();
                            messages.add(new Pair(location + ": " + timeOfEvent, ""));

                            JSONArray allChapters = new JSONArray();
                            TimeLineStory tls = new TimeLineStory("Maker", 900, 1024);
                            //tls.speechBubbles.add(sb);
                            for (Pair<String, String> message : messages) {
                                if (message.getValue().contentEquals("")) {
                                    SpeechBox sb2 = new SpeechBox(message.getKey(), SpeechBox.SpeechDirection.CENTER);
                                    tls.speechBubbles.add(sb2);

                                } else {
                                    SpeechBox sb2;
                                    try {
                                        if (message.getValue().length() < 500) {
                                            sb2 = new SpeechBox("What happened next?", getImageB64From(new File(message.getValue())), SpeechBox.SpeechDirection.CENTER, SpeechBox.SpeechTheme.NEUTRAL);
                                        } else {//Value is base64Image
                                            sb2 = new SpeechBox("What happened next?", message.getValue(), SpeechBox.SpeechDirection.CENTER, SpeechBox.SpeechTheme.NEUTRAL);

                                        }

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
                            Voronoi.saveFile(arcFile, chapterData.toString(1));//Save chapter
                            MapDB mdb=new MapDB(selectedDirectory.getAbsolutePath());
                            chapterShow(arcFile, timeOfEvent,mdb);
                            mdb.close();
                        });
                    }
                } catch (Exception ex) {
                }
            }
        }

        //Add characters
        HashMap<String,blackPawn.TYPE> characters = new HashMap<>();
        characters.put("Mone", blackPawn.TYPE.MALE);Male m1=new Male("Mone",40,5.5,new double[]{5});objectLife.put("Mone",m1);
        characters.put("Fone", blackPawn.TYPE.FEMALE);Female f1=new Female("Fone",40,5.5,new double[]{5});objectLife.put("Fone",f1);
        characters.put("Mtwo", blackPawn.TYPE.MALE);Male m2=new Male("Mtwo",40,5.5,new double[]{5});objectLife.put("Mtwo",m2);
        characters.put("Ftwo", blackPawn.TYPE.FEMALE);Female f2=new Female("Ftwo",40,5.5,new double[]{5});objectLife.put("Ftwo",f2);
        m1.GUN = new CompositeGun(.1f, .1f, -.3f);
        m1.TAG = LifeTagFactory.MALE;
        m1.relations.put(f1, SocialRelationTags.GIRLFRIEND);m1.relations.put(f2, SocialRelationTags.DIVORCE);
        m2.GUN = new CompositeGun(.1f, .9f, -.3f);
        m2.TAG = LifeTagFactory.MALE;
        m2.relations.put(f2, SocialRelationTags.WIFE);m2.relations.put(f1, SocialRelationTags.CASUAL_RELATIONSHIP);

        f1.GUN = new CompositeGun(.6f, .1f, -.3f);
        f1.TAG = LifeTagFactory.FEMALE;
        f1.relations.put(m1, SocialRelationTags.BOYFRIEND);f1.relations.put(m2, SocialRelationTags.CASUAL_RELATIONSHIP);

        f2.GUN = new CompositeGun(.6f, .1f, -.3f);
        f2.TAG = LifeTagFactory.FEMALE;
        f2.relations.put(m2, SocialRelationTags.HUSBAND);f2.relations.put(m1, SocialRelationTags.DIVORCE);
        for (Map.Entry<String,blackPawn.TYPE> charName : characters.entrySet()) {
            blackPawn BlackP_2 = new blackPawn(1, 1,charName.getValue(),charName.getKey());
            objectName.put(BlackP_2.IMG, charName.getKey());
            runTimer(BlackP_2);
        }
        
        Button CharLogs = new Button("Character Logs");
        //group.add(CharLogs, worldSizeW-1, worldSizeH-1);//Add to last cell
        CharLogs.setStyle(StylesForAll.transparentAlive);
        CharLogs.setOnAction(event -> {
            for (String chars : objectName.values()) { // print all values associated with John:
                Set johnSubset = charLog.subSet(
                        new Object[]{chars}, // lower interval bound
                        new Object[]{chars, null});  // upper interval bound, null is positive infinity
                
                johnSubset.forEach(object->{
                    Object[] log=(Object[]) object;
                    System.out.println(new Date((long) log[1])+":"+log[0]+" at "+log[2]+" did "+log[4]);
                });
            }

        });
        
         Button pause = new Button("PAUSE: "+PAUSE);
        pause.setStyle(StylesForAll.transparentAlive);
        pause.setOnAction(event -> {
            PAUSE=!PAUSE;
            pause.setText("PAUSE: "+PAUSE);
        });
        
        root.getChildren().add(group);
       // root.getChildren().add(CharLogs);
        root.getChildren().add(pause);
        root.setStyle("-fx-background-color: #C1D1E8;");

        Scene scene = new Scene(root, 1650, 1024);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void runTimer(blackPawn character) {
        int stayDuration = 3;
        Timeline fiveSecondsWonder = new Timeline(
                new KeyFrame(Duration.seconds(stayDuration),
                        new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        if(PAUSE) return;//No action
                        if (objectStay.containsKey(character.IMG)) {
                            long getEntryTime = objectStay.get(character.IMG);
                            if ((System.currentTimeMillis() - getEntryTime) < wo.getStayDuration().get(objectStayLocation.get(character.IMG))*1000) {//15 min
                                //System.out.println(objectName.get(character.IMG) + " is at " + objectStayLocation.get(character.IMG));
                                return;
                            } //Basically keep the obset at same location for some time.
                            else{
                                System.out.println(objectName.get(character.IMG) + " left " + objectStayLocation.get(character.IMG));
                                charLog.add(new Object[]{objectName.get(character.IMG),System.currentTimeMillis(),objectStayLocation.get(character.IMG),30L,"Exit",5L});
                                charCurrentLocation.remove(new Object[]{objectStayLocation.get(character.IMG),objectName.get(character.IMG),"Enter"});
                                objectStay.remove(character.IMG);
                                db.commit();
                            }
                        }
                        group.getChildren().remove(character.IMG);
                        group.getChildren().remove(character.getNameLabel());
                        //double random=Math.random();//Bad idea as it will only go diagonally.
                        int newX = (int) ((worldSizeW - 1) * Math.random());
                        int newY = (int) ((worldSizeH - 1) * Math.random());
                        group.add(character.IMG, newX, newY);
                        group.add(character.getNameLabel(), newX, newY);

                        if (WorldObjectsLocation.containsKey(newX + ":" + newY)) {
                            String location= WorldObjectsLocation.get(newX + ":" + newY);
                            System.out.println(objectName.get(character.IMG) + " reached to " +location);
                            charLog.add(new Object[]{objectName.get(character.IMG),System.currentTimeMillis(),location,30L,"Enter",5L});
                            charCurrentLocation.add(new Object[]{location,objectName.get(character.IMG),"Enter"});
                            objectStay.put(character.IMG, System.currentTimeMillis());
                            objectStayLocation.put(character.IMG, WorldObjectsLocation.get(newX + ":" + newY));
                            db.commit();
                            
                            //Check same location
                            Set johnSubset = charCurrentLocation.subSet(
                                    new Object[]{location}, // lower interval bound
                                    new Object[]{location, null});  // upper interval bound, null is positive infinity
                            Set<String> inSameLocation=new LinkedHashSet<>();
                            if (johnSubset.size() > 1) {
                                johnSubset.forEach(object -> {
                                    Object[] log = (Object[]) object;
                                   // System.out.print(log[1] + "  ");
                                    inSameLocation.add((String) log[1]);
                                });
                                //System.out.println("are at " + location);
                                //What characters take as action.
                                for (String charname : inSameLocation) {
                                    for (String charnameagain : inSameLocation) {
                                        if (!charname.contentEquals(charnameagain)) {
                                            try {
                                                System.out.println(charname + " wants to " + objectLife.get(charname).makeIndoorDecision(objectLife.get(charnameagain))+" at "+location);
                                                System.out.println(charname + " wants to " + objectLife.get(charname).makeSecretDecision(objectLife.get(charnameagain))+" at "+location);
                                                
                                                TimeLineStoryLink.put(location, location+"_"+charname+"_"+charnameagain);
                                                
                                            } catch (Exception ex) {
                                                System.out.println(charname+" and "+ charnameagain + " are just talking.");
                                            }
                                        }
                                    }
                                }
                                
                            }

                        }
                    }
                }));

        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

class WorldObjects {

    Queue<WorldObject> q
            = new LinkedList<>();
    HashMap<String, Long> stayDuration=new HashMap<>();
    public WorldObjects() {
        q.add(new WorldObject("Bedroom One", new ImageView("/char/bedroom.png")));
        q.add(new WorldObject("Bedroom Two", new ImageView("/char/bedroom.png")));
        q.add(new WorldObject("Bedroom Three", new ImageView("/char/bedroom.png")));
        q.add(new WorldObject("Restroom One", new ImageView("/char/Restroom.png")));
        q.add(new WorldObject("Bath room One", new ImageView("/char/bathroom.png")));
        q.add(new WorldObject("Bath room Two", new ImageView("/char/bathroom.png")));
        q.add(new WorldObject("Bath room Three", new ImageView("/char/bathroom.png")));
        //q.add(new WorldObject("Greeting",new ImageView("/char/Greeting.png")));
        q.add(new WorldObject("Kitchen", new ImageView("/char/kitchen.png")));
        q.add(new WorldObject("Living room", new ImageView("/char/Livingroom.png")));
        q.add(new WorldObject("Dining table", new ImageView("/char/Dining-table.png")));
        q.add(new WorldObject("Gym", new ImageView("/char/gym.jpg")));
        q.add(new WorldObject("Dance Room", new ImageView("/char/dance.png")));
        q.add(new WorldObject("House", new ImageView("/char/house_with_garden.png")));
        
        //Set stay duration
        stayDuration.put("Bedroom One", 30L);
        stayDuration.put("Bedroom Two",  30L);
        stayDuration.put("Bedroom Three",  30L);
        stayDuration.put("Restroom One",  5L);
        stayDuration.put("Bath room One",  15L);
        stayDuration.put("Bath room Two", 15L);
        stayDuration.put("Bath room Three",  15L);
        //stayDuration.put("Greeting",new ImageView("/char/Greeting.png")));
        stayDuration.put("Kitchen",  30L);
        stayDuration.put("Living room",  30L);
        stayDuration.put("Dining table",  15L);
        stayDuration.put("Gym", 30L);
        stayDuration.put("Dance Room",  45L);
        stayDuration.put("House",  5L);

    }

    public Queue<WorldObject> getQ() {
        return q;
    }

    public void setQ(Queue<WorldObject> q) {
        this.q = q;
    }

    public HashMap<String, Long> getStayDuration() {
        return stayDuration;
    }
    
    

}

class WorldObject {

    String name;
    public ImageView IMG = new ImageView("/char/7JU7r.png");

    public WorldObject(String name, ImageView IMG) {
        this.name = name;
        this.IMG = IMG;
        this.IMG.setFitHeight(75);
        this.IMG.setFitWidth(75);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageView getIMG() {
        return IMG;
    }

    public void setIMG(ImageView IMG) {
        this.IMG = IMG;
    }

}

class blackPawn {
public enum TYPE {MALE,FEMALE}
    public int x;
    public int y;
    public int start;
    public int end;
    String name;
    TYPE gender;
    public ImageView IMG = new ImageView("/char/boy.png");
    public ImageView IMGglow = new ImageView("/char/female.png");
    public Label nameLabel=new Label();

    public blackPawn(int x, int y,TYPE gender,String name) {
        this.x = x;
        this.y = y;
        this.gender=gender;
        if(TYPE.FEMALE==gender){IMG = new ImageView("/char/female.png");}
        if(TYPE.MALE==gender){IMG = new ImageView("/char/boy.png");}
        this.IMG.setFitHeight(75);
        this.IMG.setFitWidth(75);
         nameLabel=new Label(name);
         nameLabel.setStyle(StylesForAll.pigletTheme);
         this.name=name;
    }

    public blackPawn(int x, int y, int start, int end) {
        this.x = x;
        this.y = y;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Label getNameLabel() {
        return nameLabel;
    }
    

}
