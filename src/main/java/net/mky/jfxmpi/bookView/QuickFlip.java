/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi.bookView;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 * https://stackoverflow.com/questions/19896562/flip-a-card-animation
 * @author mkfs
 */
public class QuickFlip extends Application {
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
        root.getChildren().addAll(card);

        Scene scene = new Scene(root, 600, 700, true, SceneAntialiasing.BALANCED);
        scene.setCamera(new PerspectiveCamera());

        return scene;
    }

    private Node createCard() {
        return new ImageView(
            new Image(
                "http://www.ohmz.net/wp-content/uploads/2012/05/Game-of-Throne-Magic-trading-cards-2.jpg"
            )
        );
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

    public static void main(String[] args) {
        launch();
    }
}