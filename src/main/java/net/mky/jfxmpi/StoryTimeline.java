/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import systemknowhow.human.Female;
import systemknowhow.human.Life;

/**
 *
 * @author mkfs
 */
public class StoryTimeline extends Pane {

    private final Button add = new Button("Add");
    private final VBox chatBox = new VBox(5);
    private List<Label> messages = new ArrayList<>();
    private ScrollPane container = new ScrollPane();
    private int index = 0;
    public List<HashMap<String,String>> STORY=new LinkedList<>();

    public StoryTimeline() {
        setStyle(
                "-fx-background-color: rgba(255, 255, 255,0.1);"
                + "-fx-background-insets: " + 1 + ";"
        );

        container.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");
        initChatBox();
        // getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
       // getChildren().addAll(container, add);
       
        addNTimeLine(10, 800, 5);
        getChildren().add(add);
    }

   public  void addNTimeLine(double startY, double endY, int intervals) {
        
        //Creating a line object
        Line line = new Line();
        //Setting the properties to a line 
        line.setStartX(10.0);
        line.setStartY(startY);
        line.setEndX(10.0);
        line.setEndY(endY);
        getChildren().add(line);

        for (double delta = startY; delta < endY; delta = delta + (endY - startY) / intervals) { //12 For monthly
            Line lineH = new Line();
            //Setting the properties to a line 
            lineH.setStartX(10);
            lineH.setStartY(delta);
            lineH.setEndX(20.0);
            lineH.setEndY(delta);
            getChildren().add(lineH);
            
            VBox Plot = new VBox(5);
            Plot.setStyle( "  -fx-min-height:450px;\n"
                + "  -fx-min-width:200px; ");
            Plot.setTranslateY(delta-12);
            
            
            //Time
            Label TimeLabel = new Label("\u2B24 " + (int)(intervals - (endY - startY) / delta));
            TimeLabel.setTranslateX(25);
            Plot.getChildren().add(TimeLabel);
            TimeLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    if (e.getButton() == MouseButton.SECONDARY) {
                        showInputTextDialog(TimeLabel);
                    } else {
                        System.out.println("No right click");
                    }
                }
            });
            messages.add(TimeLabel);
            //Location
            Label LocationLabel = new Label("\u2B24 Location:" );
            LocationLabel.setTranslateX(25);
            Plot.getChildren().add(LocationLabel);
            LocationLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    if (e.getButton() == MouseButton.SECONDARY) {
                        showInputTextDialog(LocationLabel);
                    } else {
                        System.out.println("No right click");
                    }
                }
            });
            messages.add(LocationLabel);
            //Characters
            Label CharactersLabel = new Label("\u2B24 Characters:" );
            CharactersLabel.setTranslateX(25);
            Plot.getChildren().add(CharactersLabel);
            CharactersLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    if (e.getButton() == MouseButton.SECONDARY) {
                        showInputTextDialog(CharactersLabel);
                    } else {
                        System.out.println("No right click");
                    }
                }
            });
            messages.add(CharactersLabel);
            //Conflict
            Label ConflictLabel = new Label(textReformat("# Conflict: Every story must "
                    + " have a conflict, i.e. a "
                    + " challenge or problem  "
                    + "around which the story "
                    + " is based.\n").toString() );
            ConflictLabel.setFont(Font.loadFont(getClass().getResource("/fonts/mangati.ttf").toExternalForm(), 11));
            ConflictLabel.setTranslateX(25);
            Plot.getChildren().add(ConflictLabel);
            ConflictLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    if (e.getButton() == MouseButton.SECONDARY) {
                        showInputTextDialog(ConflictLabel);
                    } else {
                        System.out.println("No right click");
                    }
                }
            });
            messages.add(ConflictLabel);

            
            
            getChildren().add(Plot);
        }

    }

   public  HashMap<String,Life> addCustomTimeLine(double startY, double endY, List<HashMap<String,String>> story, int mincharacterPerScene) {
        this.STORY=story;
        double delta=100;
        getChildren().clear();
        HashMap<String,Life> result=new HashMap<>();
        //Creating a line object
        
        Line lineHb = new Line();
        //Setting the properties to a line 
        lineHb.setStartX(10);
        lineHb.setStartY(delta);
        lineHb.setEndX(100.0);
        lineHb.setEndY(delta);
        getChildren().add(lineHb);
            
            
        Line line = new Line();
        //Setting the properties to a line 
        line.setStartX(10);
        line.setStartY(delta+startY);
        line.setEndX(10.0);
        line.setEndY(delta+endY);
        line.setFill(Color.WHITESMOKE);
        getChildren().add(line);
        delta=delta+10;
        for (HashMap<String,String> elements: story) { 
            if(elements.size()<mincharacterPerScene) continue; //Min 2 characters in a scene
            Line lineH = new Line();
            //Setting the properties to a line 
            lineH.setStartX(10);
            lineH.setStartY(delta);
            lineH.setEndX(20.0);
            lineH.setEndY(delta);
            lineH.setFill(Color.WHITESMOKE);
            getChildren().add(lineH);
            
            VBox Plot = new VBox(5);
            Plot.setStyle( "  -fx-min-height:450px;\n"
                + "  -fx-min-width:200px; ");
            Plot.setTranslateY(delta-12);
            
            
            //Time
            Label TimeLabel = new Label("\u2B24 \u2B24 \u2B24 \u2B24 ");
            TimeLabel.setTranslateX(25);
            TimeLabel.setTextFill(Color.WHITE);
            Plot.getChildren().add(TimeLabel);
            TimeLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    if (e.getButton() == MouseButton.SECONDARY) {
                        showInputTextDialog(TimeLabel);
                    } else {
                        System.out.println("No right click");
                    }
                }
            });
            messages.add(TimeLabel);
            //Location
            Label LocationLabel = new Label("\u2B24 \u2B24 Location:\u2B24 \u2B24 " );
            LocationLabel.setTranslateX(25);
            LocationLabel.setTextFill(Color.WHITE);
            Plot.getChildren().add(LocationLabel);
            LocationLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    if (e.getButton() == MouseButton.SECONDARY) {
                        showInputTextDialog(LocationLabel);
                    } else {
                        System.out.println("No right click");
                    }
                }
            });
            messages.add(LocationLabel);
            //Characters
            String characters="";
            for (Map.Entry<String, String> entry : elements.entrySet())
            {
                if(entry.getValue().contentEquals("PERSON"))
                {
                    characters=characters+", "+entry.getKey();
                    
                    if(result.containsKey(entry.getKey())){
                        Life l=result.get(entry.getKey());
                        l.learnToSentence(new String[]{elements.get("sentence")});
                    }else{
                        Life l=new Female(entry.getKey(), 25, 165, null);
                        result.put(entry.getKey(), l);
                    }
                }
                //System.out.println(entry.getKey() + "/" + entry.getValue());
            }
            Label CharactersLabel = new Label("\u2B24 "+characters );
            CharactersLabel.setTranslateX(25);
            CharactersLabel.setTextFill(Color.WHITE);
            Plot.getChildren().add(CharactersLabel);
            CharactersLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    if (e.getButton() == MouseButton.SECONDARY) {
                        showInputTextDialog(CharactersLabel);
                    } else {
                        System.out.println("No right click");
                    }
                }
            });
            messages.add(CharactersLabel);
            //Conflict
            Label ConflictLabel = new Label(textReformat(elements.get("sentence")).toString() );
            ConflictLabel.setFont(Font.loadFont(getClass().getResource("/fonts/mangati.ttf").toExternalForm(), 11));
            ConflictLabel.setTranslateX(25);
            ConflictLabel.setTextFill(Color.WHITE);
            Plot.getChildren().add(ConflictLabel);
            ConflictLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    if (e.getButton() == MouseButton.SECONDARY) {
                        showInputTextDialog(ConflictLabel);
                    } else {
                        System.out.println("No right click");
                    }
                }
            });
            messages.add(ConflictLabel);

            
            
            getChildren().add(Plot);
            
            delta=delta+ 14*3+14*Math.sqrt(elements.get("sentence").length());
        }
        //Heading
        String allChars = "";
        for (String key : result.keySet()) {
            allChars = allChars + ", " + key;
            //System.out.println( key );
        }
        Label summary = new Label("#" + result.size() + " Characters(s)\n" + textReformat(allChars).toString());
        summary.setFont(Font.loadFont(getClass().getResource("/fonts/mangat.ttf").toExternalForm(), 14));
        summary.setTranslateX(25);
        summary.setTranslateY(5);
        summary.setTextFill(Color.WHITE);
        getChildren().add(summary);
        
        return result;
    }

    
    private void initChatBox() {

        container.setPrefSize(216, 400);
        container.setContent(chatBox);

        //chatBox.getStyleClass().add("chatbox");
        add.setOnAction(evt -> {

            messages.add(new Label("I'm a message"));

            if (index % 2 == 0) {

                messages.get(index).setAlignment(Pos.CENTER_LEFT);
                System.out.println("1");

            } else {

                messages.get(index).setAlignment(Pos.CENTER_RIGHT);
                System.out.println("2");

            }

            chatBox.getChildren().add(messages.get(index));
            index++;

        });

        add.setStyle(" -fx-background-color:purple;\n"
                + " -fx-text-fill:white;\n"
                + " -fx-pref-height:25px;\n"
                + " -fx-pref-width:50px;\n"
                + " -fx-translate-x:75;\n"
                + " -fx-translate-y:410; ");

        chatBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);\n"
                + "  -fx-min-height:400px;\n"
                + "  -fx-min-width:200px; ");

    }

    public static int WORDS_IN_ROW=3;
    private void showInputTextDialog(Label chatMessage) {
 
        TextInputDialog dialog = new TextInputDialog(chatMessage.getText());
 
       // dialog.setTitle("o7planning");
        dialog.setHeaderText("Enter text:");
        dialog.setContentText("Text:");
 
        Optional<String> result = dialog.showAndWait();
 
        result.ifPresent(name -> {
            String[] words=name.split(" ");
            int closestMatch=(int) Math.sqrt(words.length);
            int i=0;
            StringBuilder sb=new StringBuilder();
            for(String word:words){
                if(i<closestMatch){
                    sb.append(word).append(" ");
                    i++;
                }else{
                    i=0;
                    sb.append(word).append("\n");
                }
                
            }
            chatMessage.setText(sb.toString());
           
        });
    }
    
    public static StringBuilder textReformat(String toReformat){
    
        String[] words=toReformat.split(" ");
            int closestMatch=(int) Math.sqrt(words.length);
            int i=0;
            StringBuilder sb=new StringBuilder();
            for(String word:words){
                if(i<closestMatch){
                    sb.append(word).append(" ");
                    i++;
                }else{
                    i=0;
                    sb.append(word).append("\n");
                }
                
            }
            
            return sb;
    }
}
