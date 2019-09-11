/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
//import org.openimaj.image.FImage;
//import org.openimaj.image.ImageUtilities;
//import org.openimaj.image.processing.face.detection.DetectedFace;
//import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
//import org.openimaj.math.geometry.shape.Rectangle;
 

/**
 *
 * @author Maneesh
 */
public class FaceDetectionTool {
    
     public static void main(String[] args)
    {
        
        withOpenCV(args);
       // withOpenimaj( args);

       
      //  Imgcodecs.imwrite("G:/bkp/$AVG/baseDir/Imports/Yugkatha/ShortOnes/StrokesInFamily/preview_"+filename, image);
    }
     
     public static void withOpenCV(String[] args){
          /**
   * "c:\Program Files\Java\jdk1.8.0_25\bin\java.exe" -Djava.library.path="G:\Softwares\OpenCV\opencv_java320" -cp JFXMPI-1.0-SNAPSHOT-jar-with-dependencies.jar net.mky.tools.FaceDetectionTool
   */
        // For proper execution of native libraries
        // Core.NATIVE_LIBRARY_NAME must be loaded before
        // calling any of the opencv methods
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
 
        // Face detector creation by loading source cascade xml file
        // using CascadeClassifier.
        // the file can be downloade from
        // https://github.com/opencv/opencv/blob/master/data/haarcascades/
        // haarcascade_frontalface_alt.xml
        // and must be placed in same directory of the source java file
        CascadeClassifier faceDetector = new CascadeClassifier();
        faceDetector.load("G:/delete/JFXMPI/src/main/resources/haar/haarcascade_frontalface_alt.xml");
        String inputImage=args[0]!=null?args[0]:"G:/*********.jpg";
        // Input image
        Mat image = Imgcodecs.imread(inputImage);
 
        // Detecting faces
        MatOfRect faceDetections = new MatOfRect();
        //faceDetections.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(20, 20), new Size());
        faceDetector.detectMultiScale(image, faceDetections);
         System.out.println("Number of faces >> "+faceDetections.toArray().length);
       
        
         BufferedImage bimage;
          // Saving the output image
        String filename = "G:/**********preview_Ouput.jpg";
         try {
             bimage = ImageIO.read(new File(inputImage));
             //new BufferedImage(200, 200, BufferedImage.TYPE_BYTE_INDEXED);
               Graphics2D g2d = bimage.createGraphics();
                // Creating a rectangular box showing faces detected
                    for (Rect rect : faceDetections.toArray())
                    {
                       
                        g2d.drawOval(rect.x, rect.y, rect.width, rect.height);
//                        Imgproc.rectangle(image, new Point(rect.x, rect.y),
//                         new Point(rect.x + rect.width, rect.y + rect.height),
//                                                       new Scalar(0, 255, 0));
                    }

               // g2d.setColor(Color.red);
               // g2d.fill(new Ellipse2D.Float(0, 0, 200, 100));
                g2d.dispose();
                ImageIO.write(bimage, "jpg", new File(filename));
         } catch (IOException ex) {
             Logger.getLogger(FaceDetectionTool.class.getName()).log(Level.SEVERE, null, ex);
         }
     }
     
//     public static void withOpenimaj(String[] args){
//         
//         try {
//             HaarCascadeDetector detector = new HaarCascadeDetector();
//             List < DetectedFace > faces = null;
//             BufferedImage bimage = ImageIO.read(new File(args[0]));
//             faces = detector.detectFaces(ImageUtilities.createFImage(bimage));
//             
//             if (faces == null) {
//                 System.out.println("No faces found in the captured image");
//                 return;
//             }
//             Iterator < DetectedFace > dfi = faces.iterator();
//             Graphics2D g2d = bimage.createGraphics();
//             while (dfi.hasNext()) {
//                 DetectedFace face = dfi.next();
//                 FImage image1 = face.getFacePatch();
//                 Rectangle rect=face.getBounds();
//                 g2d.drawOval((int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
////                 
////                 ImagePanel p = new ImagePanel(ImageUtilities.createBufferedImage(image1));
////                 fr.add(p);
//             }    
//              g2d.dispose();
//                ImageIO.write(bimage, "jpg", new File(args[1]));
//         } catch (IOException ex) {
//             Logger.getLogger(FaceDetectionTool.class.getName()).log(Level.SEVERE, null, ex);
//         }
//     
//     }

}
