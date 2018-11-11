/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;


    public class ChatField extends Application{

    private HTMLEditor htmlEditor;

    public void hideHTMLEditorToolbars() {
        htmlEditor.setVisible(false);
        Platform.runLater(() -> {
            Node[] nodes = htmlEditor.lookupAll(".tool-bar").toArray(new Node[0]);
            for (Node node : nodes) {
                node.setVisible(false);
                node.setManaged(false);
            }
            htmlEditor.setVisible(true);
        });
    }


    @Override public void start(Stage primaryStage) throws Exception {
        htmlEditor = new HTMLEditor();
        hideHTMLEditorToolbars();
        htmlEditor.setHtmlText("Text<img src='urltosmile'/>Text Text Text");
        primaryStage.setScene(new Scene(htmlEditor, 300, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}