/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.mky.tools.StylesForAll;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerArrayTuple;

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
    NavigableSet<Object[]> charLog;
    NavigableSet<Object[]> charCurrentLocation;
    HashMap<ImageView, Long> objectStay = new HashMap<>();
    HashMap<ImageView, String> objectStayLocation = new HashMap<>();
    HashMap<ImageView, String> objectName = new HashMap<>();
    final GridPane group = new GridPane();
     DB db;

    @Override
    public void start(Stage primaryStage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(primaryStage);
        
        db = DBMaker
                .fileDB(selectedDirectory.getAbsolutePath() + "/WorldBoard.db").transactionEnable()
                .fileMmapEnable()
                .make();
        WorldObjectsLocation = db.hashMap("WorldObjectsLocation")
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
        primaryStage.setTitle("ChessGame");
        primaryStage.getIcons().add(new Image("/char/7JU7r.png"));
        GridPane root = new GridPane();
        group.setPadding(new Insets(15, 25, 25, 25));

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
                    }
                } catch (Exception ex) {
                }
            }
        }

        //Add characters
        HashMap<String,blackPawn.TYPE> characters = new HashMap<>();
        characters.put("Mone", blackPawn.TYPE.MALE);
        characters.put("Fone", blackPawn.TYPE.FEMALE);
        characters.put("Mtwo", blackPawn.TYPE.MALE);
        characters.put("Ftwo", blackPawn.TYPE.FEMALE);
        for (Map.Entry<String,blackPawn.TYPE> charName : characters.entrySet()) {
            blackPawn BlackP_2 = new blackPawn(1, 1,charName.getValue());
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
        root.getChildren().add(group);
        root.getChildren().add(CharLogs);
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
                        //double random=Math.random();//Bad idea as it will only go diagonally.
                        int newX = (int) ((worldSizeW - 1) * Math.random());
                        int newY = (int) ((worldSizeH - 1) * Math.random());
                        group.add(character.IMG, newX, newY);

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
                            if (johnSubset.size() > 1) {
                                johnSubset.forEach(object -> {
                                    Object[] log = (Object[]) object;
                                    System.out.print(log[1] + "  ");
                                });
                                System.out.println("are at " + location);
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

    public blackPawn(int x, int y,TYPE gender) {
        this.x = x;
        this.y = y;
        this.gender=gender;
        if(TYPE.FEMALE==gender){IMG = new ImageView("/char/female.png");}
        if(TYPE.MALE==gender){IMG = new ImageView("/char/boy.png");}
        this.IMG.setFitHeight(75);
        this.IMG.setFitWidth(75);
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

}
