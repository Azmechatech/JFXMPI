/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import com.truegeometry.mkhilbertml.GridImage;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import static net.mky.jfxmpi.TimeLineView.TimeLineStory.toBufferedImage;
import net.mky.tools.Utils;

/**
 *
 * @author mkfs
 */
public class DrawOnCanvas extends StackPane {

    SplitPane splitPane = new SplitPane();
    VBox leftControl  = new VBox();//new Label("Left Control")
    VBox rightControl = new VBox(new Label("Right Control"));
    Canvas canvas = new Canvas(900, 800);
    ImageCropDialog.RubberBandSelection rubberBandSelection;
    LinkedList<Point2D> points = new LinkedList<Point2D>();
    LinkedList<int[]> pointsInt = new LinkedList<int[]>();
    public List<BufferedImage> cropedImages=new LinkedList<>();

    public DrawOnCanvas(Image bimg) {
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        initDraw(graphicsContext, bimg);
        
        // container for image layers
        ScrollPane scrollPane = new ScrollPane();

        // image layer: a group of images
        Group imageLayer = new Group(); 

        // the container for the image as a javafx node
        //imageView = new ImageView( image);

        // add image to layer
        imageLayer.getChildren().add( canvas);

        // use scrollpane for image view in case the image is large
        scrollPane.setContent(imageLayer);
        
       
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                graphicsContext.beginPath();
                graphicsContext.moveTo(event.getX(), event.getY());
                graphicsContext.stroke();
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                graphicsContext.lineTo(event.getX(), event.getY());
                graphicsContext.stroke();
                Point2D p2d = new Point2D(event.getX(), event.getY());
                points.add(p2d);
                pointsInt.add(new int[]{(int)event.getX(), (int)event.getY()});
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

                BufferedImage bimgCrop=crop(SwingFXUtils.fromFXImage(bimg, null), pointsInt,0);
                Utils.setClipboard(bimgCrop); //Copy to clipboard
                BufferedImage bimgCropHFlip=crop(SwingFXUtils.fromFXImage(bimg, null), pointsInt,1);
                BufferedImage bimgCropVFlip=crop(SwingFXUtils.fromFXImage(bimg, null), pointsInt,2);
                cropedImages.add(bimgCrop);
                //cropedImages.add(bimgCropHFlip);cropedImages.add(bimgCropVFlip);
                
                Image img = SwingFXUtils.toFXImage(bimgCrop, null);
                Image imgHFlip = SwingFXUtils.toFXImage(bimgCropHFlip, null);
                Image imgVFlip = SwingFXUtils.toFXImage(bimgCropVFlip, null);
                ImageView imgv = new ImageView();ImageView imgvHFlip = new ImageView();ImageView imgvVFlip = new ImageView();
                imgv.setImage(img);imgvHFlip.setImage(imgHFlip);imgvVFlip.setImage(imgVFlip);
                rightControl.getChildren().add(imgv);rightControl.getChildren().add(imgvHFlip);rightControl.getChildren().add(imgvVFlip);
                pointsInt.clear();
                imgv.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                            if (mouseEvent.getClickCount() == 1) {
                                Utils.setClipboard(bimgCrop); //Copy to clipboard
                            }
                        }
                    }
                });
                imgvHFlip.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                            if (mouseEvent.getClickCount() == 1) {
                                Utils.setClipboard(bimgCropHFlip); //Copy to clipboard
                            }
                        }
                    }
                });
                imgvVFlip.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                            if (mouseEvent.getClickCount() == 1) {
                                Utils.setClipboard(bimgCropVFlip); //Copy to clipboard
                            }
                        }
                    }
                });

            }
        });
        
         // rubberband selection
        //rubberBandSelection = new ImageCropDialog.RubberBandSelection(imageLayer);
        
        leftControl.getChildren().add(scrollPane);
        
        splitPane.getItems().addAll(leftControl, rightControl);
        
        getChildren().add(splitPane);
    }

    public DrawOnCanvas() {

        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        initDraw(graphicsContext);

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                graphicsContext.beginPath();
                graphicsContext.moveTo(event.getX(), event.getY());
                graphicsContext.stroke();
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                graphicsContext.lineTo(event.getX(), event.getY());
                graphicsContext.stroke();
                Point2D p2d = new Point2D(event.getX(), event.getY());
                points.add(p2d);
            }
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {

            }
        });

        getChildren().add(canvas);

    }

    public LinkedList<Point2D> getFullPath() {
        return points;
    }

    private void initDraw(GraphicsContext gc) {
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.setFill(Color.LIGHTGRAY);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);

        gc.fill();
        gc.strokeRect(
                0, //x of the upper left corner
                0, //y of the upper left corner
                canvasWidth, //width of the rectangle
                canvasHeight);  //height of the rectangle

        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);

    }

    private void initDraw(GraphicsContext gc, Image bimg) {
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();
        gc.setFill(Color.LIGHTGRAY);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.fill();
        gc.strokeRect(
                0, //x of the upper left corner
                0, //y of the upper left corner
                canvasWidth, //width of the rectangle
                canvasHeight);  //height of the rectangle
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);
        gc.drawImage(bimg, 0, 0);
    }
    
      /**
     * https://stackoverflow.com/questions/43541086/crop-image-by-polygon-area-in-java
     * https://stackoverflow.com/questions/9558981/flip-image-with-graphics2d
     * @param source
     * @param path
     * @return 
     */
    public static BufferedImage crop(BufferedImage source, LinkedList<int[]> path, int flipType) {
        //  BufferedImage source = ImageIO.read(new File("Example.jpg"));

        GeneralPath clip = new GeneralPath();
        clip.moveTo(path.get(0)[0], path.get(0)[1]); //Move to location
        int minX=Integer.MAX_VALUE;
        int minY=Integer.MAX_VALUE;
        for (int i = 1; i < path.size(); i++) {
            clip.lineTo(path.get(i)[0], path.get(i)[1]);
            minX=minX<path.get(i)[0]?minX:path.get(i)[0];
            minY=minY<path.get(i)[1]?minY:path.get(i)[1];
        }
//        
//        clip.lineTo(241, 178);
//        clip.lineTo(268, 405);
//        clip.lineTo(145, 512);
        clip.closePath();

        Rectangle bounds = clip.getBounds();
        BufferedImage img = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        clip.transform(AffineTransform.getTranslateInstance(-minX, -minY));
        g2d.setClip(clip);
        g2d.translate(-minX, -minY);
        g2d.drawImage(source, 0, 0, null);
        
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

//        if (flipType == 0) {
//            g2d.drawImage(source, 0, 0, null);
//        }
        if (flipType == 1) {
            //g2d.drawImage(source, source.getWidth(), 0, -source.getWidth(), source.getHeight(), null);
            tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-img.getWidth(null), 0);
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            img = op.filter(img, null);
        }
        if (flipType == 2) {
            tx = AffineTransform.getScaleInstance(1, -1);
            tx.translate(0, -img.getHeight(null));
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            img = op.filter(img, null);
        }
        if (flipType == 3) {
            // Flip the image vertically and horizontally; equivalent to rotating the image 180 degrees
            tx = AffineTransform.getScaleInstance(-1, -1);
            tx.translate(-img.getWidth(null), -img.getHeight(null));
            op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            img = op.filter(img, null);
        }

         g2d.dispose();
        return img;
//ImageIO.write(img, "png", new File("Clipped.png"));
    }
    
 
}
