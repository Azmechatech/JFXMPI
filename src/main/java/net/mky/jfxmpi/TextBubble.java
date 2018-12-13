/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.mky.tools.StylesForAll;
import org.json.JSONArray;
import org.json.JSONObject;
import systemknowhow.human.Life;
import systemknowhow.human.LifeTagFactory;
import systemknowhow.human.Male;
import systemknowhow.human.guns.CompositeGun;
import systemknowhow.tools.HilbertCurvePatternDetect;
/**
 *
 * @author mkfs
 */
public class TextBubble  extends StackPane {
    
    private Rectangle rect;
    private double pressedX, pressedY;
    private LongProperty frame = new SimpleLongProperty();

    FileInputStream input;
   
    public String CharacterFile = "";
    Life thisCharcter;//=""
    Life otherCharcter;//=""

    public void setOtherCharcter(Life otherCharcter) {
        this.otherCharcter = otherCharcter;
    }
    String name = "";
    String age = "";
    String SeedFile = "";
    LifeTagFactory ltf;
    public String CharacterPool[];

    Button playButton;

    private static final double W = 200;
    private static final double H = 200;
    private static final int SHADOW_LENGTH = 100;

    private static final double IMG_X = 20;
    private static final double IMG_Y = 20;

    private static final int FONT_SIZE = 200;

    private static final double SHADOW_SLOPE_FACTOR = 1.5;

    Color SHADOW_COLOR = Color.GRAY.brighter();
    Label chatMessage = new Label("Hello There! ");
    TextArea taxachatMessage = new TextArea("Ready to play! ");

    FileChooser fileChooser = new FileChooser();

    public TextBubble(final int width, final int height, boolean border) {
        setMinSize(200, 200);
        if (border) {
            setBorder(new Border(new BorderStroke(Color.GOLDENROD,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
        if (border) {
            setStyle("-fx-padding: 10;"
                    + "-fx-border-style: solid inside;"
                    + "-fx-border-width: 10;"
                    + "-fx-border-insets: 0;"
                    + "-fx-border-radius: 5;" + "-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #dc143c, #661a33)"
            /*"-fx-border-color: GOLDENROD;"*/);
        }

        //Use this http://www.drawsvg.org/drawsvg.html
        chatMessage.setStyle("-fx-shape: \"M177.16535,46.062967 C 177.16535,46.062967 106.29921,46.062966 70.866142,99.212573 C 35.433071,152.36218 35.433071,223.22832 70.866142,276.37793 C 101.94300,322.99322 177.16535,329.52753 177.16535,329.52753 L 194.10223,433.03688 L 230.31496,329.52753 L 478.34646,329.52753 C 478.34646,329.52753 549.21260,329.52753 584.64567,276.37793 C 620.07874,223.22832 620.07874,152.36218 584.64567,99.212573 C 549.21260,46.062967 478.34646,46.062967 478.34646,46.062967 L 177.16535,46.062967Z \";\n"
                + "    -fx-background-color: black, white;\n"
                + "    -fx-background-insets: 0,1;\n"
                + "    -fx-font-family: \"Helvetica\";\n"
                + "    -fx-font-size: 16px;\n"
                + "    -fx-font-weight: bold;\n"
                + "    -fx-padding: 30");
        
         chatMessage.setWrapText(true);
         
        chatMessage.setTextAlignment(TextAlignment.CENTER);


        

        getChildren().add(chatMessage);
        chatMessage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    showInputTextDialog();
                } else {
                    System.out.println("No right click");
                }
            }
        });;
        //getChildren().add(taxachatMessage);
        StackPane.setAlignment(chatMessage, Pos.TOP_LEFT);
        setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                pressedX = event.getX();
                pressedY = event.getY();
            }
        });



        EventHandler<MouseEvent> imgMove = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                pressedX = event.getX() + chatMessage.getLayoutX();
                pressedY = event.getY() + chatMessage.getLayoutY();
            }
        };

        EventHandler<MouseEvent> imgMove2 = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
               // imageView.setTranslateX(imageView.getTranslateX() + event.getX() - pressedX);
              //  imageView.setTranslateY(imageView.getTranslateY() + event.getY() - pressedY);

                chatMessage.setTranslateX(chatMessage.getTranslateX() + event.getX() - pressedX);
                chatMessage.setTranslateY(chatMessage.getTranslateY() + event.getY() - pressedY);
                event.consume();
            }
        };

        chatMessage.setOnMouseClicked(imgMove);
        chatMessage.setOnMouseDragged(imgMove2);



        EventHandler<ActionEvent> showHide = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
               // imageView.setVisible(!imageView.isVisible());
                chatMessage.setVisible(!chatMessage.isVisible());
                playButton.setVisible(!playButton.isVisible());
//                chatMessage.setText((String) thisCharcter.talk(otherCharcter, name).toArray()[0]);
            }
        };

        Button showHideImg = new Button("[-]");
        showHideImg.setOnAction(showHide);
        showHideImg.setStyle(StylesForAll.transparentAlive);
        getChildren().add(showHideImg);
        StackPane.setAlignment(showHideImg, Pos.TOP_CENTER);

    }

   
    private Life createLife(String name, LifeTagFactory gender, String[] learnToTalk) {
        Male male1 = new Male(name, 40, 5.5, new double[]{5});
        male1.GUN = new CompositeGun(.1f, .1f, -.3f);
        male1.TAG = gender;
        male1.learnToSentence(learnToTalk);

        return male1;
    }

    public Life getLife() {
        return thisCharcter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setLtf(LifeTagFactory ltf) {
        this.ltf = ltf;
    }

    public JSONObject getCharacterData() {
        JSONObject data = new JSONObject();
        data.put("name", name);
        data.put("age", age);
        data.put("ltf", ltf);
        data.put("cpane", CharacterFile);
        data.put("SeedFile", SeedFile);
        JSONArray charlist = new JSONArray(CharacterPool);
        data.put("CharacterPool", charlist);
        return data;
    }

    public void loadCharacterData(JSONObject data) {
        name = data.has("name") ? data.getString("name") : name;
        age = data.has("age") ? data.getString("age") : age;
        SeedFile = data.has("SeedFile") ? data.getString("SeedFile") : SeedFile;
        ltf = data.has("ltf") ? LifeTagFactory.valueOf(data.getString("ltf")) : ltf;
        CharacterFile = data.has("cpane") ? data.getString("cpane") : CharacterFile;
        if (data.has("CharacterPool")) { //String[] charlist = projData.getJSONArray("charlist").toString().replace("},{", " ,").split(" ");
            List<String> list = new ArrayList<String>();
            for (int i = 0; i < data.getJSONArray("CharacterPool").length(); i++) {
                list.add(data.getJSONArray("CharacterPool").getString(i));
            }
            CharacterPool = list.toArray(new String[0]);
        }

        thisCharcter = createLife(name, ltf, getLines(SeedFile));
        playButton.setText(name);

        try {
            input = new FileInputStream(CharacterFile);
            Image image = new Image(input, 400, 600, true, true);
         //   imageView.setImage(image);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CharacterPane.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String[] getLines(String filePath) {
        Set<String> linesRead = new LinkedHashSet<>();
        StringBuilder readStrings = new StringBuilder();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while (line != null) {
                //System.out.println(line);
                // read next line
                line = reader.readLine();
                readStrings.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] result = readStrings.toString().split("\\.");
        //linesRead.add(line);
        return result;
    }
    
    private void showInputTextDialog() {
 
        TextInputDialog dialog = new TextInputDialog("Tran");
 
        dialog.setTitle("o7planning");
        dialog.setHeaderText("Enter your name:");
        dialog.setContentText("Name:");
 
        Optional<String> result = dialog.showAndWait();
 
        result.ifPresent(name -> {
            this.chatMessage.setText(name);
        });
    }
}