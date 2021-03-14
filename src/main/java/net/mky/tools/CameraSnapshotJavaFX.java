/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.tools;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

/**
 * https://www.tutorialspoint.com/opencv/opencv_using_camera.htm
 * @author ryzen
 */
public class CameraSnapshotJavaFX extends Application {
   Mat matrix = null;

   @Override
   public void start(Stage stage) throws FileNotFoundException, IOException {
      // Capturing the snapshot from the camera
      CameraSnapshotJavaFX obj = new CameraSnapshotJavaFX();
      WritableImage writableImage = obj.capureSnapShot();

      // Saving the image
      obj.saveImage();

      // Setting the image view
      ImageView imageView = new ImageView(writableImage);

      // setting the fit height and width of the image view
      imageView.setFitHeight(400);
      imageView.setFitWidth(600);

      // Setting the preserve ratio of the image view
      imageView.setPreserveRatio(true);

      // Creating a Group object
      Group root = new Group(imageView);

      // Creating a scene object
      Scene scene = new Scene(root, 600, 400);

      // Setting title to the Stage
      stage.setTitle("Capturing an image");

      // Adding scene to the stage
      stage.setScene(scene);

      // Displaying the contents of the stage
      stage.show();
   }
   public WritableImage capureSnapShot() {
      WritableImage WritableImage = null;

      // Loading the OpenCV core library
      System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

      // Instantiating the VideoCapture class (camera:: 0)
      VideoCapture capture = new VideoCapture();

      // Reading the next video frame from the camera
      Mat matrix = new Mat();
      capture.read(matrix);

      // If camera is opened
      if( capture.isOpened()) {
         // If there is next video frame
         if (capture.read(matrix)) {
            // Creating BuffredImage from the matrix
            BufferedImage image = new BufferedImage(matrix.width(), 
               matrix.height(), BufferedImage.TYPE_3BYTE_BGR);
            
            WritableRaster raster = image.getRaster();
            DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
            byte[] data = dataBuffer.getData();
            matrix.get(0, 0, data);
            this.matrix = matrix;
            
            // Creating the Writable Image
            WritableImage = SwingFXUtils.toFXImage(image, null);
         }
      }
      return WritableImage;
   }
   public void saveImage() {
      // Saving the Image
      String file = "E:/OpenCV/chap22/sanpshot.jpg";

      // Instantiating the imgcodecs class
      Imgcodecs imageCodecs = new Imgcodecs();

      // Saving it again 
      imageCodecs.imwrite(file, matrix);
   }
   public static void main(String args[]) {
      launch(args);
   }
}