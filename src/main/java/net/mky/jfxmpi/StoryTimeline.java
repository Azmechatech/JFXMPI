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
        initChatBox();
        getStylesheets().add(getClass().getResource("Style.css").toExternalForm());
        getChildren().addAll(container, add);
    }

    private void initChatBox() {

        container.setPrefSize(216, 400);
        container.setContent(chatBox);

        chatBox.getStyleClass().add("chatbox");

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

    }

}
