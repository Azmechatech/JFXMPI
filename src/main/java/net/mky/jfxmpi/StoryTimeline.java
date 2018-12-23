/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

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

    public StoryTimeline() {
        setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.5);"
                + "-fx-background-insets: " + 1 + ";"
        );

        container.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");
        initChatBox();
        // getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        getChildren().addAll(container, add);
        addMonthTimeLine(0, 900);
    }
    
    private void addMonthTimeLine(double startY, double endY) {

        //Creating a line object
        Line line = new Line();
        //Setting the properties to a line 
        line.setStartX(50.0);
        line.setStartY(startY);
        line.setEndX(50.0);
        line.setEndY(endY);
        getChildren().add(line);

        for (double delta = startY; delta < endY; delta = delta + (endY - startY) / 12) { //12 For monthly
            Line lineH = new Line();
            //Setting the properties to a line 
            lineH.setStartX(50);
            lineH.setStartY(delta);
            lineH.setEndX(100.0);
            lineH.setEndY(delta);
            getChildren().add(lineH);
            
            Label label1 = new Label("Month : "+(12-(endY - startY)/delta));
            label1.setTranslateY(delta+5);
            label1.setTranslateX(110);
            
            getChildren().add(label1);
        }

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

}
