/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi.bookView;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * https://stackoverflow.com/questions/19896562/flip-a-card-animation
 * @author mkfs
 */
public class CardFlip extends Application {
    final Image CARD_IMAGE = new Image(
        "http://www.ohmz.net/wp-content/uploads/2012/05/Game-of-Throne-Magic-trading-cards-2.jpg"
        // sourced from: http://obviouschild.deviantart.com/art/Vitam-et-Mortem-189267194
    );
    final int W = (int) (CARD_IMAGE.getWidth()  / 2);
    final int H = (int) CARD_IMAGE.getHeight();

    @Override
    public void start(Stage stage) throws Exception {
        Node card = createCard();

        stage.setScene(createScene(card));
        stage.show();

        RotateTransition rotator = createRotator(card);
        rotator.play();
    }

    private Scene createScene(Node card) {
        StackPane root = new StackPane();
        root.getChildren().addAll(card, new AmbientLight(Color.WHITE));

        Scene scene = new Scene(root, W + 200, H + 200, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.MIDNIGHTBLUE.darker().darker().darker().darker());
        scene.setCamera(new PerspectiveCamera());

        return scene;
    }

    private RotateTransition createRotator(Node card) {
        RotateTransition rotator = new RotateTransition(Duration.millis(10000), card);
        rotator.setAxis(Rotate.Y_AXIS);
        rotator.setFromAngle(0);
        rotator.setToAngle(360);
        rotator.setInterpolator(Interpolator.LINEAR);
        rotator.setCycleCount(10);

        return rotator;
    }

    private Node createCard() {
        MeshView card = new MeshView(createCardMesh());

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(CARD_IMAGE);
        card.setMaterial(material);

        return card;
    }

    private TriangleMesh createCardMesh() {
        TriangleMesh mesh = new TriangleMesh();

        mesh.getPoints().addAll(-1 * W/2, -1 * H/2 , 0, 1 * W/2, -1 * H/2, 0, -1 * W/2, 1 * H/2, 0, 1 * W/2, 1 * H/2, 0);
        mesh.getFaces().addAll(0, 0, 2, 2, 3, 3, 3, 3, 1, 1, 0, 0);
        mesh.getFaces().addAll(0, 4, 3, 7, 2, 6, 3, 7, 0, 4, 1, 5);
        mesh.getTexCoords().addAll(0, 0, 0.5f, 0, 0, 1, 0.5f, 1, 0.5f, 0, 1, 0, 0.5f, 1, 1, 1);

        return mesh;
    }

    public static void main(String[] args) {
        launch();
    }
}