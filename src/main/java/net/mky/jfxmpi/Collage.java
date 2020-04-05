/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.MotionBlur;
import javafx.scene.effect.Reflection;
import javafx.scene.effect.SepiaTone;
import javafx.scene.effect.Shadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import static net.mky.jfxmpi.MainApp.awtImageToFX;
import static net.mky.jfxmpi.MainApp.getImageFromClipboard;
import net.mky.tools.StylesForAll;

/**
 *
 * @author mkfs
 */
public class Collage  extends StackPane {
    public static enum OPTIONS{SINGLE,DOUBLE_VERT,DOUBLE_HORZ,TRIPLE_VERT,TRIPPLE_HORZ,ONE_IS_TO_2_VERT,ONE_IS_TO_2_HORZ};
    
    public Collage (OPTIONS option){
        GridPane gridPane = new GridPane();
//        ColumnConstraints colConstraint = new ColumnConstraints(120);
//        //colConstraint.setHalignment(HPos.LEFT);
//        RowConstraints rowConstraints = new RowConstraints(130);
//        //rowConstraints.setValignment(VPos.CENTER);
//
//        // add constraints for columns
//        gridPane.getColumnConstraints().addAll(colConstraint, colConstraint, colConstraint);

        switch (option){
                case SINGLE:
                   
                   
                    HBox component=getOneImageComponent();
                    gridPane.add(component, 0, 0, 1, 1);
                    GridPane.setMargin(component, new Insets(-1, -1, -1, -1));

                    getChildren().add(gridPane);

                break;

                case DOUBLE_VERT:
                    
                    component=getOneImageComponent();
                    gridPane.add(component, 0, 0, 1, 1);
                    GridPane.setMargin(component, new Insets(-1, -1, -1, -1));
                    
                    HBox component2=getOneImageComponent();
                    gridPane.add(component2,1, 0, 1, 1);
                    GridPane.setMargin(component2, new Insets(-1, -1, -1, -1));

                    getChildren().add(gridPane);
                    
                    
                break;
                case DOUBLE_HORZ:
                    
                    gridPane.add(getOneImageComponent(), 0, 0, 1, 1);
                    
                    gridPane.add(getOneImageComponent(),0, 1, 1, 1);

                    getChildren().add(gridPane);
                    
                break;
                
        }
                
    }
    
    /**
     * Get one component.
     * @return 
     */
    
    HBox getOneImageComponent() {
        HBox buttonPane = new HBox();
        ImageView imageView = new ImageView();
//        imageView.setFitHeight(500);
//        imageView.setFitWidth(800);
        Button selectCharPic = new Button("P");

        selectCharPic.setStyle(StylesForAll.aliveTheme);
        selectCharPic.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    java.awt.Image image = getImageFromClipboard();
                    if (image != null) {
                        javafx.scene.image.Image fimage = awtImageToFX(image);
                        //pe.imageView.setFitHeight(scene.getHeight());
                        // pe.imageView.setFitWidth(scene.getWidth());
                        imageView.setX(0);
                        imageView.setY(0);
                        imageView.setImage(fimage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //OPERATIONS
        Button imgIncrease = new Button("+");imgIncrease.setStyle(StylesForAll.transparentAlive);
        Button imgDecrease = new Button("-");imgDecrease.setStyle(StylesForAll.transparentAlive);
        Button imgBlurr = new Button("#");imgBlurr.setStyle(StylesForAll.transparentAlive);
        Button imgNoblurr = new Button("=");imgNoblurr.setStyle(StylesForAll.transparentAlive);
        Button imgGrey = new Button("g");imgGrey.setStyle(StylesForAll.transparentAlive);
        Button imgNoGrey = new Button("E");imgNoGrey.setStyle(StylesForAll.transparentAlive);
        
        Button imgLighting  = new Button("L");imgLighting .setStyle(StylesForAll.transparentAlive);
        Button imgDropShadow  = new Button("S");imgDropShadow .setStyle(StylesForAll.transparentAlive);
        Button imgShadow  = new Button("s");imgShadow .setStyle(StylesForAll.transparentAlive);
        Button imgSepiaTone  = new Button("T");imgSepiaTone .setStyle(StylesForAll.transparentAlive);
        Button imgReflection  = new Button("R");imgReflection .setStyle(StylesForAll.transparentAlive);
        Button imgMotionBlur  = new Button("M");imgMotionBlur .setStyle(StylesForAll.transparentAlive);
        Button imgGlow  = new Button("G");imgGlow .setStyle(StylesForAll.transparentAlive);
        Button imgBloom  = new Button("B");imgBloom.setStyle(StylesForAll.transparentAlive);

        imgIncrease.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                float scaleInc = (float) (imageView.getScaleX() + imageView.getScaleX() * .1);
                Scale scale = new Scale(scaleInc, scaleInc);
                imageView.getTransforms().add(scale); //rotate by 45 degrees
                buttonPane.resize(imageView.getFitWidth(), imageView.getFitHeight());
            }
        });

        imgDecrease.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                float scaleInc = (float) (imageView.getScaleX() - imageView.getScaleX() * .1);
                Scale scale = new Scale(scaleInc, scaleInc);
                imageView.getTransforms().add(scale); //rotate by 45 degrees
                buttonPane.resize(imageView.getFitWidth(), imageView.getFitHeight());

            }
        });

        imgBlurr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BoxBlur bb = new BoxBlur();
                bb.setWidth(5);
                bb.setHeight(5);
                bb.setIterations(3);
                imageView.setEffect(bb);

            }
        });

        imgNoblurr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                BoxBlur bb = new BoxBlur();
                bb.setWidth(0);
                bb.setHeight(0);
                bb.setIterations(0);
                imageView.setEffect(bb);

            }
        });

        imgGrey.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                ColorAdjust grayscale = new ColorAdjust();
                grayscale.setSaturation(-1);
                imageView.setEffect(grayscale);

            }
        });

        imgNoGrey.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                ColorAdjust grayscale = new ColorAdjust();
                grayscale.setSaturation(1);
                imageView.setEffect(grayscale);

            }
        });

        imgLighting.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //instantiating the Light.Point class 
                Light.Point light = new Light.Point();

                //Setting the color of the light
                light.setColor(Color.GREEN);

                //Setting the position of the light 
                light.setX(70);
                light.setY(55);
                light.setZ(45);

                //Instantiating the Lighting class  
                Lighting lighting = new Lighting();

                //Setting the light 
                lighting.setLight(light);

                imageView.setEffect(lighting);

            }
        });

        imgDropShadow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                DropShadow dropShadow = new DropShadow();

                //setting the type of blur for the shadow 
                dropShadow.setBlurType(BlurType.GAUSSIAN);

                //Setting color for the shadow 
                dropShadow.setColor(Color.ROSYBROWN);

                //Setting the height of the shadow
                dropShadow.setHeight(5);

                //Setting the width of the shadow 
                dropShadow.setWidth(5);

                //Setting the radius of the shadow 
                dropShadow.setRadius(5);

                //setting the offset of the shadow 
                dropShadow.setOffsetX(3);
                dropShadow.setOffsetY(2);

                //Setting the spread of the shadow 
                dropShadow.setSpread(12);
                imageView.setEffect(dropShadow);

            }
        });

        imgShadow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //Instantiating the Shadow class 
                Shadow shadow = new Shadow();

                //setting the type of blur for the shadow 
                shadow.setBlurType(BlurType.GAUSSIAN);

                //Setting color of the shadow 
                shadow.setColor(Color.ROSYBROWN);

                //Setting the height of the shadow 
                shadow.setHeight(5);

                //Setting the width of the shadow 
                shadow.setWidth(5);

                //Setting the radius of the shadow 
                shadow.setRadius(5);

                imageView.setEffect(shadow);

            }
        });

        imgSepiaTone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //Instanting the SepiaTone class 
                SepiaTone sepiaTone = new SepiaTone();

                //Setting the level of the effect 
                sepiaTone.setLevel(0.8);

                imageView.setEffect(sepiaTone);

            }
        });

        imgReflection.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //Instanting the reflection class 
                Reflection reflection = new Reflection();

                //setting the bottom opacity of the reflection 
                reflection.setBottomOpacity(0.0);

                //setting the top opacity of the reflection 
                reflection.setTopOpacity(0.5);

                //setting the top offset of the reflection 
                reflection.setTopOffset(0.0);

                //Setting the fraction of the reflection 
                reflection.setFraction(0.7);

                imageView.setEffect(reflection);

            }
        });

        imgMotionBlur.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//Instantiating the MotionBlur class 
                MotionBlur motionBlur = new MotionBlur();

                //Setting the radius to the effect 
                motionBlur.setRadius(10.5);

                //Setting angle to the effect 
                motionBlur.setAngle(45);
                imageView.setEffect(motionBlur);

            }
        });

        imgGlow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //Instantiating the Glow class 
                Glow glow = new Glow();

                //setting level of the glow effect 
                glow.setLevel(0.9);
                imageView.setEffect(glow);

            }
        });

        imgBloom.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Instantiating the Bloom class 
                Bloom bloom = new Bloom();

                //setting threshold for bloom 
                bloom.setThreshold(0.1);
                imageView.setEffect(bloom);

            }
        });
        
        VBox vBox=new VBox(selectCharPic,imgIncrease,imgDecrease,imgBlurr,
                imgNoblurr,imgGrey,imgNoGrey,imgLighting,imgDropShadow,imgShadow,imgSepiaTone
        ,imgReflection,imgMotionBlur,imgGlow,imgBloom);
        
        
        
        buttonPane.getChildren().add(vBox);
        buttonPane.getChildren().add(imageView);
        
        buttonPane.prefWidthProperty().bind(imageView.fitHeightProperty());
        buttonPane.prefWidthProperty().bind(imageView.fitWidthProperty());

        return buttonPane;

    }

}
