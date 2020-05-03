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
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author mkfs
 */
public class ChatPane extends ScrollPane {

    private final VBox chatBox = new VBox(5);
    private List<Label> messages = new ArrayList<>();
    public final Button add = new Button("Add");
    public final TextField  inputBox=new TextField();
    private int index = 0;

    public ChatPane() {
        this.setPrefSize(216, 400);
        setContent(chatBox);

        add.setOnAction(evt -> {
            Label chatMessage=new Label();
            chatMessage.setText(inputBox.getText());
            chatMessage.setEffect(new DropShadow(+25d, 0d, +2d, Color.BLANCHEDALMOND));
            chatMessage.setStyle("-fx-shape: \"" +TextBubbleStyles.SPEECH_BUBBLE_RECTOVAL + "\";\n"
                 //   + "    -fx-background-color: "+colour.getText()+", #E0C796;\n"
                    + "    -fx-background-insets: 0,1;\n"
                    + "    -fx-font-family: \"Comic Sans MS\";\n"
                    + "    -fx-font-size: 20px;\n"
                    + "    -fx-font-weight: bold;\n"
                    + "    -fx-padding: 10 10 60 20;");

            messages.add(chatMessage);

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
