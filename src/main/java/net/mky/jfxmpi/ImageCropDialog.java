/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
/**
 *
 * @author mkfs
 */
/**
 * Load image, provide rectangle for rubberband selection. Press right mouse button for "crop" context menu which then crops the image at the selection rectangle and saves it as jpg.
 */
public class ImageCropDialog extends Application  {
     RubberBandSelection rubberBandSelection;
    ImageView imageView;

    Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;

        primaryStage.setTitle("Image Crop");

        BorderPane root = new BorderPane();

        // container for image layers
        ScrollPane scrollPane = new ScrollPane();

        // image layer: a group of images
        Group imageLayer = new Group(); 

        // load the image
//      Image image = new Image( getClass().getResource( "cat.jpg").toExternalForm());
        Image image = new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Gatto_europeo4.jpg/1024px-Gatto_europeo4.jpg");

        // the container for the image as a javafx node
        imageView = new ImageView( image);

        // add image to layer
        imageLayer.getChildren().add( imageView);

        // use scrollpane for image view in case the image is large
        scrollPane.setContent(imageLayer);

        // put scrollpane in scene
        root.setCenter(scrollPane);

        // rubberband selection
        rubberBandSelection = new RubberBandSelection(imageLayer);

        // create context menu and menu items
        ContextMenu contextMenu = new ContextMenu();

        MenuItem cropMenuItem = new MenuItem("Crop");
        cropMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                // get bounds for image crop
                Bounds selectionBounds = rubberBandSelection.getBounds();

                // show bounds info
                System.out.println( "Selected area: " + selectionBounds);

                // crop the image
                crop( selectionBounds);

            }
        });
        contextMenu.getItems().add( cropMenuItem);

        // set context menu on image layer
        imageLayer.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isSecondaryButtonDown()) {
                    contextMenu.show(imageLayer, event.getScreenX(), event.getScreenY());
                }
            }
        });

        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
    }

    private void crop( Bounds bounds) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");

        File file = fileChooser.showSaveDialog( primaryStage);
        if (file == null)
            return;

        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        parameters.setViewport(new Rectangle2D( bounds.getMinX(), bounds.getMinY(), width, height));

        WritableImage wi = new WritableImage( width, height);
        imageView.snapshot(parameters, wi);

        // save image 
        // !!! has bug because of transparency (use approach below) !!!
        // --------------------------------
//        try {
//          ImageIO.write(SwingFXUtils.fromFXImage( wi, null), "jpg", file);
//      } catch (IOException e) {
//          e.printStackTrace();
//      }


        // save image (without alpha)
        // --------------------------------
        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
        BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);

        Graphics2D graphics = bufImageRGB.createGraphics();
        graphics.drawImage(bufImageARGB, 0, 0, null);

        try {

            ImageIO.write(bufImageRGB, "jpg", file); 

            System.out.println( "Image saved to " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }

        graphics.dispose();

    }

    private static Image cropStatic( Bounds bounds,ImageView imageView) {

//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save Image");
//
//        File file = fileChooser.showSaveDialog( null);
//        if (file == null)
//            return null;

        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        parameters.setViewport(new Rectangle2D( bounds.getMinX(), bounds.getMinY(), width, height));

        WritableImage wi = new WritableImage( width, height);
        imageView.snapshot(parameters, wi);

        // save image 
        // !!! has bug because of transparency (use approach below) !!!
        // --------------------------------
//        try {
//          ImageIO.write(SwingFXUtils.fromFXImage( wi, null), "jpg", file);
//      } catch (IOException e) {
//          e.printStackTrace();
//      }


        // save image (without alpha)
        // --------------------------------
//        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
//        BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);
//
//        Graphics2D graphics = bufImageRGB.createGraphics();
//        graphics.drawImage(bufImageARGB, 0, 0, null);
//
//        try {
//
//            ImageIO.write(bufImageRGB, "jpg", file); 
//
//            System.out.println( "Image saved to " + file.getAbsolutePath());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        graphics.dispose();
        
        return wi;
    }
    /**
     * Drag rectangle with mouse cursor in order to get selection bounds
     */
    public static class RubberBandSelection {

        final DragContext dragContext = new DragContext();
        Rectangle rect = new Rectangle();

        Group group;


        public Bounds getBounds() {
            return rect.getBoundsInParent();
        }

        public RubberBandSelection( Group group) {

            this.group = group;

            rect = new Rectangle( 0,0,0,0);
            rect.setStroke(Color.BLUE);
            rect.setStrokeWidth(1);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.6));

            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        }

        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if( event.isSecondaryButtonDown())
                    return;

                // remove old rect
                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove( rect);


                // prepare new drag operation
                dragContext.mouseAnchorX = event.getX();
                dragContext.mouseAnchorY = event.getY();

                rect.setX(dragContext.mouseAnchorX);
                rect.setY(dragContext.mouseAnchorY);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().add( rect);

            }
        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if( event.isSecondaryButtonDown())
                    return;

                double offsetX = event.getX() - dragContext.mouseAnchorX;
                double offsetY = event.getY() - dragContext.mouseAnchorY;

                if( offsetX > 0)
                    rect.setWidth( offsetX);
                else {
                    rect.setX(event.getX());
                    rect.setWidth(dragContext.mouseAnchorX - rect.getX());
                }

                if( offsetY > 0) {
                    rect.setHeight( offsetY);
                } else {
                    rect.setY(event.getY());
                    rect.setHeight(dragContext.mouseAnchorY - rect.getY());
                }
            }
        };


        EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                if( event.isSecondaryButtonDown())
                    return;

                // remove rectangle
                // note: we want to keep the ruuberband selection for the cropping => code is just commented out
                /*
                rect.setX(0);
                rect.setY(0);
                rect.setWidth(0);
                rect.setHeight(0);

                group.getChildren().remove( rect);
                */

            }
        };
        private static final class DragContext {

            public double mouseAnchorX;
            public double mouseAnchorY;


        }
    }
    
    
    public static Image imageCropTool(Image image ){
        RubberBandSelection rubberBandSelection;
        ImageView imageView;
        Dialog<Image> dialog = new Dialog<>();
        dialog.setTitle("Dialog Test");
        dialog.setHeaderText("Please specify…");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // container for image layers
        ScrollPane scrollPane = new ScrollPane();

        // image layer: a group of images
        Group imageLayer = new Group(); 

        // the container for the image as a javafx node
        imageView = new ImageView( image);

        // add image to layer
        imageLayer.getChildren().add( imageView);

        // use scrollpane for image view in case the image is large
        scrollPane.setContent(imageLayer);


        // rubberband selection
        rubberBandSelection = new RubberBandSelection(imageLayer);

        // create context menu and menu items
        ContextMenu contextMenu = new ContextMenu();
        Image result=null;
        MenuItem cropMenuItem = new MenuItem("Crop");
        cropMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                // get bounds for image crop
                Bounds selectionBounds = rubberBandSelection.getBounds();

                // show bounds info
                System.out.println( "Selected area: " + selectionBounds);

                // crop the image
               imageView.setImage(cropStatic( selectionBounds,imageView));

            }
        });
        contextMenu.getItems().add( cropMenuItem);

        // set context menu on image layer
        imageLayer.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isSecondaryButtonDown()) {
                    contextMenu.show(imageLayer, event.getScreenX(), event.getScreenY());
                }
            }
        });
        
        dialogPane.setContent(new VBox(8, scrollPane));
        Platform.runLater(imageView::requestFocus);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return imageView.getImage();
            }
            return imageView.getImage();
        });
        
        Optional<Image> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((Image results) -> {
            imageView.setImage(results);
        });
        
         return imageView.getImage();
    
    }
    
        /**
     * https://stackoverflow.com/questions/39851404/get-average-image-of-a-set-of-images-in-java
     *
     * @param input1
     * @param input2
     * @param awayFromCenter how far away from center
     * @return 
     */
    public static BufferedImage getImageAverage(BufferedImage input1, BufferedImage input2,int awayFromCenter) {
        // images are of same size that's why i'll use first one's width and height
        int width = input1.getWidth(), height = input1.getHeight();

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int[] rgb1 = input1.getRGB(0, 0, width, height, new int[width * height], 0, width);
        int[] rgb2 = input2.getRGB(0, 0, width, height, new int[width * height], 0, width);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgbIndex = i * width + j;
                if(i>awayFromCenter && i<width-awayFromCenter && j>awayFromCenter && j<height-awayFromCenter)
                rgb1[rgbIndex] = average(rgb1[rgbIndex], rgb2[rgbIndex]);
            }
        }

        output.setRGB(0, 0, width, height, rgb1, 0, width);
        return output;
    }

    public static int average(int argb1, int argb2) {
        return (((argb1 & 0xFF) + (argb2 & 0xFF)) >> 1)
                | //b
                (((argb1 >> 8 & 0xFF) + (argb2 >> 8 & 0xFF)) >> 1) << 8
                | //g
                (((argb1 >> 16 & 0xFF) + (argb2 >> 16 & 0xFF)) >> 1) << 16
                | //r
                (((argb1 >> 24 & 0xFF) + (argb2 >> 24 & 0xFF)) >> 1) << 24;  //a
    }
    
    public static BufferedImage toBufferedImage(java.awt.Image img)
{
    if (img instanceof BufferedImage)
    {
        return (BufferedImage) img;
    }

    // Create a buffered image with transparency
    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    // Draw the image on to the buffered image
    Graphics2D bGr = bimage.createGraphics();
    bGr.drawImage(img, 0, 0, null);
    bGr.dispose();

    // Return the buffered image
    return bimage;
}
}
