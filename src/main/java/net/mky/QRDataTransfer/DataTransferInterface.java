/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.QRDataTransfer;

import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;

/**
 *
 * @author mkfs
 */
public class DataTransferInterface extends Application {

    public static void main(String[] args) {
        
        JOptionPane.showMessageDialog(null, "", "QR Code", JOptionPane.PLAIN_MESSAGE, null);
        launch(args);
    }

    public void start(Stage stage) {

        Parameters params = getParameters();
        List<String> list = params.getRaw();
        System.out.println(list.size());
        for (String each : list) {
            System.out.println(each);
        }

        //Pagination p = new Pagination(10);
        String[] photos = {"housestark.jpg", "housefrey.jpg", "housebar.jpg",
            "HouseBolton.jpg", "housegreyjoy.jpg", "houseaaryn.jpg",
            "houselannis.jpg", "housemart.jpg", "housereed.jpg",
            "housetully.jpg", "housetyrel.jpg",};
        Pagination p = new Pagination(photos.length);
//        p.setPageFactory((Integer pageIndex) -> {
//            return new ImageView(getClass().getResource(photos[pageIndex])
//                    .toExternalForm());
//        });

        Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            int pos = (p.getCurrentPageIndex() + 1) % p.getPageCount();
            p.setCurrentPageIndex(pos);
        }));
        fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
        fiveSecondsWonder.play();

        stage.setScene(new Scene(p));
        stage.show();
    }
}
