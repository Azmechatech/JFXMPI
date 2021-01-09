/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * From
 * :https://stackoverflow.com/questions/50307003/chess-border-around-pawns-javafx
 *
 * @author ryzen
 */
public class WorldBoard extends Application {

    int worldSizeW = 23;
    int worldSizeH = 13;
    WorldObjects wo = new WorldObjects();
    HashMap<String, String> WorldObjectsLocation = new HashMap<>();
    HashMap<ImageView, Long> objectStay = new HashMap<>();
    HashMap<ImageView, String> objectStayLocation = new HashMap<>();
    HashMap<ImageView, String> objectName = new HashMap<>();
    final GridPane group = new GridPane();

    @Override
    public void start(Stage primaryStage) {
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
        for (int i = 0; i < 3; i++) {
            blackPawn BlackP_2 = new blackPawn(1, 1);
            objectName.put(BlackP_2.IMG, "Character " + (i + 1));
            runTimer(BlackP_2);
        }

        //FIGURES
        //Black
        //Pawns
//    final blackPawn BlackP_1 = new blackPawn(0,1,64,65);
//    group.add(BlackP_1.IMG,BlackP_1.x,BlackP_1.y);
//
//    BlackP_1.IMG.setOnMouseEntered(new EventHandler<MouseEvent>() {
//        @Override
//        public void handle(MouseEvent event) {
//            group.getChildren().remove(64,65);
//            group.add(BlackP_1.IMGglow,0,1);
//        }
//    });
//
//
//
//    BlackP_1.IMGglow.setOnMouseExited(new EventHandler<MouseEvent>() {
//        @Override
//        public void handle(MouseEvent event) {
//            group.getChildren().remove(64,65);
//            group.add(BlackP_1.IMG,BlackP_1.x,BlackP_1.y);
//        }
//    });
//    group.add(BlackP_2.IMG,BlackP_2.x,BlackP_2.y);
        //   blackPawn BlackP_3 = new blackPawn(2,1);objectName.put(BlackP_3.IMG, "Number Two");
//    group.add(BlackP_3.IMG,BlackP_3.x,BlackP_3.y);
//    blackPawn BlackP_4 = new blackPawn(3,1);
//    group.add(BlackP_4.IMG,BlackP_4.x,BlackP_4.y);
//    blackPawn BlackP_5 = new blackPawn(4,1);
//    group.add(BlackP_5.IMG,BlackP_5.x,BlackP_5.y);
//    blackPawn BlackP_6 = new blackPawn(5,1);
//    group.add(BlackP_6.IMG,BlackP_6.x,BlackP_6.y);
//    blackPawn BlackP_7 = new blackPawn(6,1);
//    group.add(BlackP_7.IMG,BlackP_7.x,BlackP_7.y);
//    blackPawn BlackP_8 = new blackPawn(7,1);
//    group.add(BlackP_8.IMG,BlackP_8.x,BlackP_8.y);
        //Rooks
//    blackRook BlackR_1 = new blackRook();
//    group.add(BlackR_1.IMG,7,0);
//    blackRook BlackR_2 = new blackRook();
//    group.add(BlackR_2.IMG,0,0);
//    //Knights
//    blackKnight BlackK_1 = new blackKnight();
//    group.add(BlackK_1.IMG,1,0);
//    blackKnight BlackK_2 = new blackKnight();
//    group.add(BlackK_2.IMG,6,0);
//    //Bishop
//    blackBishop BlackB_1 = new blackBishop();
//    group.add(BlackE_1.IMG,2,0);
//    blackBishop BlackB_2 = new blackBishop();
//    group.add(BlackE_2.IMG,5,0);
//    //Queen
//    blackQueen blackQueen= new blackQueen();
//    group.add(blackQueen.IMG,3,0);
//    //King
//    blackKing blackking = new blackKing();
//    group.add(blackking.IMG,4,0);
//
//    //WHITE
//    //Pawns
//    final whitePawn WhiteP_1 = new whitePawn();
//    group.add(WhiteP_1.IMG,0,6);
//    whitePawn WhiteP_2 = new whitePawn();
//    group.add(WhiteP_2.IMG,1,6);
//    whitePawn WhiteP_3 = new whitePawn();
//    group.add(WhiteP_3.IMG,2,6);
//    whitePawn WhiteP_4 = new whitePawn();
//    group.add(WhiteP_4.IMG,3,6);
//    whitePawn WhiteP_5 = new whitePawn();
//    group.add(WhiteP_5.IMG,4,6);
//    whitePawn WhiteP_6 = new whitePawn();
//    group.add(WhiteP_6.IMG,5,6);
//    whitePawn WhiteP_7 = new whitePawn();
//    group.add(WhiteP_7.IMG,6,6);
//    whitePawn WhiteP_8 = new whitePawn();
//    group.add(WhiteP_8.IMG,7,6);
//    //Rooks
//    whiteRook WhiteR_1 = new whiteRook();
//    group.add(WhiteR_1.IMG,0,7);
//    whiteRook WhiteR_2 = new whiteRook();
//    group.add(WhiteR_2.IMG,7,7);
//    //Knights
//    whiteKnight WhiteK_1 = new whiteKnight();
//    group.add(WhiteK_1.IMG,1,7);
//    whiteKnight WhiteK_2 = new whiteKnight();
//    group.add(WhiteK_2.IMG,6,7);
//    //Bishop
//    whiteBishop WhiteB_1 = new whiteBishop();
//    group.add(WhiteB_1.IMG,2,7);
//    whiteBishop WhiteB_2 = new whiteBishop();
//    group.add(WhiteB_2.IMG,5,7);
//    //Queen
//    whiteQueen whitequeen = new whiteQueen();
//    group.add(whitequeen.IMG,3,7);
//    //King
//    whiteKing whiteking = new whiteKing();
//    group.add(whiteking.IMG,4,7);
        root.getChildren().add(group);
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
                            if ((System.currentTimeMillis() - getEntryTime) < 30 * 1000) {
                                System.out.println(objectName.get(character.IMG) + " is at " + objectStayLocation.get(character.IMG));
                                return;
                            } //Basically keep the obset at same location for some time.
                            else{
                                System.out.println(objectName.get(character.IMG) + " left " + objectStayLocation.get(character.IMG));
                                objectStay.remove(character.IMG);
                            }
                        }
                        group.getChildren().remove(character.IMG);
                        //double random=Math.random();//Bad idea as it will only go diagonally.
                        int newX = (int) ((worldSizeW - 1) * Math.random());
                        int newY = (int) ((worldSizeH - 1) * Math.random());
                        group.add(character.IMG, newX, newY);

                        if (WorldObjectsLocation.containsKey(newX + ":" + newY)) {
                            System.out.println(objectName.get(character.IMG) + " reached to " + WorldObjectsLocation.get(newX + ":" + newY));
                            objectStay.put(character.IMG, System.currentTimeMillis());
                            objectStayLocation.put(character.IMG, WorldObjectsLocation.get(newX + ":" + newY));

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

    }

    public Queue<WorldObject> getQ() {
        return q;
    }

    public void setQ(Queue<WorldObject> q) {
        this.q = q;
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

    public int x;
    public int y;
    public int start;
    public int end;
    String name;

    public ImageView IMG = new ImageView("/char/7JU7r.png");
    public ImageView IMGglow = new ImageView("/char/rd71Q.png");

    public blackPawn(int x, int y) {
        this.x = x;
        this.y = y;
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
