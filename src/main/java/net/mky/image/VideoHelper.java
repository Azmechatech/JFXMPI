/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.image;


import com.truegeometry.mkhilbertml.HilbertCurvePatternDetect;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.IplImage;

/**
 *
 * @author mkfs
 */
public class VideoHelper {

    public static void main(String[] args) throws Exception {
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber("....mp4");
        Java2DFrameConverter bimConverter = new Java2DFrameConverter();
        frameGrabber.start();
        List<BufferedImage> uniqueImages = new LinkedList<>();
        // int i=0;
        BufferedImage previous = null;
        for (int i = 0; i < frameGrabber.getLengthInFrames(); i++) {
            try {
                //frameGrabber.getFrameRate()
                BufferedImage img = bimConverter.convert(frameGrabber.grab());
                BufferedImage result = convertToBufferedImage(img.getScaledInstance(img.getWidth(), img.getHeight(), java.awt.Image.SCALE_DEFAULT));
                img.flush();
                //ImageIO.write(result, "png", new File("..."+i+".png"));

                if (previous != null) {
                    com.truegeometry.mkhilbertml.pojo.Statistic st = HilbertCurvePatternDetect.match2ImagesScore(result, previous, 32);
                    if (st.getSimilarityScore() < .5) {
                        uniqueImages.add(result);
                    }
                }

                previous = result;
                i++;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        frameGrabber.stop();

    }

    /**
     * Get unique images.
     * @param vidFile
     * @param threshold0to1
     * @return
     * @throws Exception 
     */
    public static List<BufferedImage> getUniqueImages(String vidFile, float threshold0to1) throws Exception {
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(vidFile);
        Java2DFrameConverter bimConverter = new Java2DFrameConverter();
        frameGrabber.start();
        List<BufferedImage> uniqueImages = new LinkedList<>();
        // int i=0;
        BufferedImage previous = null;
        for (int i = 0; i < frameGrabber.getLengthInFrames(); i++) {
            try {
                //frameGrabber.getFrameRate()
                BufferedImage img = bimConverter.convert(frameGrabber.grab());
                BufferedImage result = convertToBufferedImage(img.getScaledInstance(img.getWidth(), img.getHeight(), java.awt.Image.SCALE_DEFAULT));
                img.flush();
                //ImageIO.write(result, "png", new File("C:\\$AVG\\baseDir\\Imports\\Yugkatha\\ShortOnes\\SouthDiaries\\Img"+i+".png"));

                if (previous != null) {
                    com.truegeometry.mkhilbertml.pojo.Statistic st = HilbertCurvePatternDetect.match2ImagesScore(result, previous, 32);
                    if (st.getSimilarityScore() < threshold0to1) {
                        uniqueImages.add(result);
                        System.out.println("getUniqueImages>> Found: "+uniqueImages.size()+"/"+frameGrabber.getLengthInFrames());
                    }
                }

                previous = result;
                i++;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }
        frameGrabber.stop();
        return uniqueImages;
    }

    public static BufferedImage convertToBufferedImage(Image image) {
        BufferedImage newImage = new BufferedImage(
                image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    public static BufferedImage toBufferedImage(IplImage src) {
        OpenCVFrameConverter.ToIplImage iplConverter = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter bimConverter = new Java2DFrameConverter();
        Frame frame = iplConverter.convert(src);
        BufferedImage img = bimConverter.convert(frame);
        BufferedImage result = (BufferedImage) img.getScaledInstance(
                img.getWidth(), img.getHeight(), java.awt.Image.SCALE_DEFAULT);
        img.flush();
        return result;
    }
}
