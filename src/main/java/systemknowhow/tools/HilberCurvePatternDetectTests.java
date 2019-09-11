/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow.tools;

import boofcv.alg.feature.detect.edge.EdgeContour;
import boofcv.alg.feature.detect.edge.EdgeSegment;
import georegression.struct.point.Point2D_I32;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import net.mky.tools.FitPolygon;

/**
 *
 * @author mkfs
 */
public class HilberCurvePatternDetectTests {

    public static void main(String... args) {
        //getColourWheelTest();

        getClusterPointsInteractiveTest();

    }

    public static void getClusterPointsInteractiveTest() {

        //Select image from JDialog
        JFileChooser fileOpener = new JFileChooser();
        int action = fileOpener.showOpenDialog(null);

        BufferedImage img = null;
        if (action == JFileChooser.APPROVE_OPTION) {
            try {
                img = ImageIO.read(fileOpener.getSelectedFile());

                HilbertCurvePatternDetect.displayImage(img, "getClusterPointsInteractiveTest >> baseImage");
                List< BufferedImage> result = HilbertCurvePatternDetect.getFeaturesInImage(img);
                List<EdgeContour> edges= FitPolygon.getCannyEdgesXY(img);
                
                //PREPARE FOR RESULT DISPLAY
                BufferedImage BIG_IMAGE = new BufferedImage(
                        500 * result.size() / 2, 500 * result.size() / 2, //work these out
                        BufferedImage.TYPE_INT_RGB);
                Graphics g = BIG_IMAGE.getGraphics();

                int x = 0, y = 0;
                for (BufferedImage resultImage : result) {
                    
                    //Draw edges
                    Graphics gr = resultImage.getGraphics();
                    for( EdgeContour edgeContour:edges){
                        for(EdgeSegment edgeSegment:edgeContour.segments)
                            for(Point2D_I32 p2d: edgeSegment.points)
                                gr.drawOval(p2d.x, p2d.y,2, 2);
                    }

                    g.drawImage(HilbertCurvePatternDetect.resizeImage(resultImage, 500, 500), x, y, null);
                    x += 500;
                    if (x > resultImage.getWidth()) {
                        x = 0;
                        y += 500;
                    }
                    
                    

                    HilbertCurvePatternDetect.displayImage(resultImage, "getClusterPointsInteractiveTest");
                }

                 g.drawImage(HilbertCurvePatternDetect.resizeImage(img, 500, 500), x, y, null);
                 
                HilbertCurvePatternDetect.displayImage(BIG_IMAGE, "getClusterPointsInteractiveTest");
            } catch (IOException e) {
            }
        }
        //Display image from JDialog
    }

    public static void getColourWheelTest() {
        HilbertCurvePatternDetect.displayImage(HilbertCurvePatternDetect.getColourWheel(63), "getColourWheelTest");
    }

}
