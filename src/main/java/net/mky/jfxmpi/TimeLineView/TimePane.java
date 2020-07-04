/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi.TimeLineView;

import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 *
 * @author mkfs
 */
public class TimePane  extends StackPane {
    
    enum dayType{MORNING,AFTERNOON,EVENING,DINNER,MIDNIGHT}
    enum weatherType{SUMMER,WINTER,RAINY}
    
    String daySun=TimePane.class.getResource("/time/PikPng.com_sun-rays-png_1224036.png").toExternalForm();
    String nightMoon=TimePane.class.getResource("/time/PinClipart.com_half-moon-clip-art_5495876.png").toExternalForm();
    String summer=TimePane.class.getResource("/time/PinClipart.com_summer-reading-clip-art_5499146.png").toExternalForm();
    String winter=TimePane.class.getResource("/time/PinClipart.com_winter-tree-clipart_5818696.png").toExternalForm();
    String rainy=TimePane.class.getResource("/time/PinClipart.com_rain-clip-art_5215118.png").toExternalForm();
    
    ToggleButton MORNING = new ToggleButton();
    ToggleButton AFTERNOON = new ToggleButton();
    ToggleButton EVENING = new ToggleButton();
    ToggleButton DINNER = new ToggleButton();
    ToggleButton MIDNIGHT = new ToggleButton();
    ToggleGroup dayTime = new ToggleGroup();
    
    
    ToggleButton SUMMER = new ToggleButton();
    ToggleButton WINTER = new ToggleButton();
    ToggleButton RAINY = new ToggleButton();
    ToggleGroup weatherTime = new ToggleGroup();

    public TimePane(){
        
        MORNING.setGraphic(prepareImageViews(daySun));MORNING.setStyle("-fx-background-color:linear-gradient(to right, #fc5c7d, #6a82fb);");
        AFTERNOON.setGraphic(prepareImageViews(daySun));AFTERNOON.setStyle("-fx-background-color:linear-gradient(to right, #fc5c7d, #6a82fb);");
        EVENING.setGraphic(prepareImageViews(daySun));EVENING.setStyle("-fx-background-color:linear-gradient(to right, #fc5c7d, #6a82fb);");
        DINNER.setGraphic(prepareImageViews(nightMoon));DINNER.setStyle("-fx-background-color:linear-gradient(to right, #fc5c7d, #6a82fb);");
        MIDNIGHT.setGraphic(prepareImageViews(nightMoon));MIDNIGHT.setStyle("-fx-background-color:linear-gradient(to right, #fc5c7d, #6a82fb);");

        SUMMER.setGraphic(prepareImageViews(summer));SUMMER.setStyle("-fx-background-color:linear-gradient(to right, #fc5c7d, #6a82fb);");
        WINTER.setGraphic(prepareImageViews(winter));WINTER.setStyle("-fx-background-color:linear-gradient(to right, #fc5c7d, #6a82fb);");
        RAINY.setGraphic(prepareImageViews(rainy));RAINY.setStyle("-fx-background-color:linear-gradient(to right, #fc5c7d, #6a82fb);");

        

        MORNING.setToggleGroup(dayTime);
        AFTERNOON.setToggleGroup(dayTime);
        EVENING.setToggleGroup(dayTime);
        DINNER.setToggleGroup(dayTime);
        MIDNIGHT.setToggleGroup(dayTime);
        
        SUMMER.setToggleGroup(weatherTime);
        WINTER.setToggleGroup(weatherTime);
        RAINY.setToggleGroup(weatherTime);

        HBox hbox = new HBox(SUMMER,WINTER,RAINY,new Button(),MORNING, AFTERNOON, EVENING, DINNER,MIDNIGHT);
        
        
        getChildren().add(hbox);
    }
    
    /**
     * Get current dat type
     * @return 
     */
    public dayType getCurrentTime() {

        ToggleButton selectedToggleButton
                = (ToggleButton) dayTime.getSelectedToggle();
        dayType result = null;
        if (selectedToggleButton.hashCode() == MORNING.hashCode()) {
            result =  dayType.MORNING;
        }
        if (selectedToggleButton.hashCode() == AFTERNOON.hashCode()) {
            result =  dayType.AFTERNOON;
        }
        if (selectedToggleButton.hashCode() == EVENING.hashCode()) {
            result =  dayType.EVENING;
        }
        if (selectedToggleButton.hashCode() == DINNER.hashCode()) {
            result =  dayType.DINNER;
        }
        if (selectedToggleButton.hashCode() == MIDNIGHT.hashCode()) {
            result =  dayType.MIDNIGHT;
        }
        
        System.out.println("Time selected>>"+result);

        return result;

    }
    
     public weatherType getCurrentWeather() {

        ToggleButton selectedToggleButton
                = (ToggleButton) weatherTime.getSelectedToggle();
        weatherType result = null;
        if (selectedToggleButton.hashCode() == SUMMER.hashCode()) {
            result =  weatherType.SUMMER;
        }
        if (selectedToggleButton.hashCode() == WINTER.hashCode()) {
            result =  weatherType.WINTER;
        }
        if (selectedToggleButton.hashCode() == RAINY.hashCode()) {
            result =  weatherType.RAINY;
        }
      
        
        System.out.println("Weather selected>>"+result);

        return result;

    }
    
    public ImageView prepareImageViews(String imageURL) {
        ImageView MORNINGSunImage = new ImageView(imageURL);
        MORNINGSunImage.setFitWidth(50);
        MORNINGSunImage.setFitHeight(50);
        MORNINGSunImage.setPreserveRatio(true);

        return MORNINGSunImage;
    }
    
    
    
}
