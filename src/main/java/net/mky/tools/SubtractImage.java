/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.tools;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;   
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import net.mky.jfxmpi.CharacterPane;
import org.json.JSONArray;
import org.json.JSONObject;
import systemknowhow.tools.HilbertCurvePatternDetect;

/**
 *
 * @author Maneesh
 */
public class SubtractImage {
    public static void main(String[] args) throws Exception {

        //testmakeImageBlack();
        
        
       String generatedFile= runDataGenerationTask(args.length>0?args[0]:"C:/$AVG/baseDir/Imports/Sprites/MULTIPLEHUMAN/");
        
        testGeneratedData(generatedFile);
                
        // Subtract Test
        int[][][] ch = new int[4][4][4];
        BufferedImage image2 = ImageIO.read(new File(args.length>0?args[0]:"G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317409970.jpg"));
        BufferedImage image1 = ImageIO.read(new File(args.length>1?args[1]:"G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317391299.png"));
        BufferedImage image3 = getSubtracted(image1,image2);// new BufferedImage(image1.getWidth(), image1.getHeight(), image1.getType());

        ImageIO.write(image3, "png",  new File(args.length>2?args[2]:"G:/bkp/$AVG/baseDir/Imports/Sprites/WOMEN/765324---_1515317409970.png"));

        
        String itemsToSubtract[]={
        "G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317380863.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317391299.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317400871.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317409970.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317484083.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317496224.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317507138.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317513479.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317520991.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317531810.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317539665.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317548358.jpg"
        ,
        "G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317557820.jpg"
        ,
        "G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317567532.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317611576.jpg"};
        
        String from="G:/bkp/$AVG/baseDir/Imports/Game Series/--/765324---_1515317391299.png";
        generateImages(from,itemsToSubtract,"G:/bkp/$AVG/baseDir/Imports/Sprites/WOMEN/");

    }
    
    public static void generateImages(String From,String[] itemsToSubtract,String outputFolder){
        
        for(String itemToSubtract: itemsToSubtract){
            try {
                ImageIO.write(getSubtracted(ImageIO.read(new File(From)),ImageIO.read(new File(itemToSubtract))), "png",  new File(outputFolder+System.currentTimeMillis()+".png"));
            } catch (IOException ex) {
                Logger.getLogger(SubtractImage.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
    }
    
    public static BufferedImage getSubtracted(BufferedImage From,BufferedImage itemToSubtract){
    
        BufferedImage resulting= new BufferedImage(From.getWidth(), From.getHeight(), From.getType());
        for(int x = 0; x < From.getWidth(); x++)
            for(int y = 0; y < From.getHeight(); y++) {

                int argb0 = From.getRGB(x, y);
                int argb1 = itemToSubtract.getRGB(x, y);

                int a0 = (argb0 >> 24) & 0xFF;
                int r0 = (argb0 >> 16) & 0xFF;
                int g0 = (argb0 >>  8) & 0xFF;
                int b0 = (argb0      ) & 0xFF;

                int a1 = (argb1 >> 24) & 0xFF;
                int r1 = (argb1 >> 16) & 0xFF;
                int g1 = (argb1 >>  8) & 0xFF;
                int b1 = (argb1      ) & 0xFF;

                int aDiff = Math.abs(a1 - a0);
                int rDiff = Math.abs(r1 - r0);
                int gDiff = Math.abs(g1 - g0);
                int bDiff = Math.abs(b1 - b0);

                int diff = 
                    (aDiff << 24) | (rDiff << 16) | (gDiff << 8) | bDiff;
                resulting.setRGB(x, y, diff);

                
            }
        
        return resulting;
        
    }
    
    public static String runDataGenerationTask(String pathToDirectory) {
        long startTime=System.currentTimeMillis();
        final File folderObjects = new File(pathToDirectory);
        ArrayList<String> ObjectFiles = new ArrayList<>();
        JSONArray result=new JSONArray();
        for (final File fileEntry : folderObjects.listFiles()) {
            if (fileEntry.isDirectory()) {
                // listFilesForFolder(fileEntry);
            } else {
                FileInputStream input = null;
                try {
                    ObjectFiles.add(fileEntry.getAbsolutePath());
                    input = new FileInputStream(fileEntry.getAbsolutePath());
                    Image image = new Image(input);
                    BufferedImage bimg = makeImageBlack(image);
                    BufferedImage boundingCurve=FitPolygon.getCannyEdges(bimg);
                    //displayImage(getBoundingCurveXYImage(boundingCurve),"testmakeImageBlack::boundingCurve" );
                    JSONObject resultToSave=new JSONObject();
                    List<int[]> data=getBoundingCurveXY(boundingCurve);
                    if(data.size()==0){data=getBoundingCurveXY(bimg);}//This is to make use of data with background as well.
                    resultToSave.put("data", data);
                    resultToSave.put("width", bimg.getWidth());
                    resultToSave.put("height", bimg.getHeight());
                    resultToSave.put("type", bimg.getType());
                    resultToSave.put("statistics", FitPolygon.getImageStatistics(bimg).getJSONObject());
                    resultToSave.put("statisticsBoundingCurve", FitPolygon.getImageStatistics(boundingCurve).getJSONObject());
                    result.put(resultToSave);
                    if(Math.random()>.98){
                        System.out.println(result.length()+"/"+folderObjects.listFiles().length+" Files processed."  );
                    }
                    //System.out.println(result.toString(1));
                } catch (Exception ex) {
                   // Logger.getLogger(SubtractImage.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        input.close();
                    } catch (Exception ex) {
                       // Logger.getLogger(SubtractImage.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }
        
        BufferedWriter writer;
        try {
            
            writer = new BufferedWriter(new FileWriter(pathToDirectory+"/JFMPIImageData.json"));
            writer.write(result.toString());
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(SubtractImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(ObjectFiles.size()+" Files processed." );
        System.out.println(result.toString().length()*8/1024 +"KB of Data saved to "+pathToDirectory+"/JFMPIImageData.json" );
        System.out.println("Time taken: "+((System.currentTimeMillis()-startTime)/1000)+" sec" );
        
        return pathToDirectory+"/JFMPIImageData.json";
    }
    
    public static void testGeneratedData(String dataFileFullPath) throws FileNotFoundException, IOException {
        File file = new File(dataFileFullPath);
        FileInputStream fis = new FileInputStream(file);
        byte[] textdata = new byte[(int) file.length()];
        fis.read(textdata);
        fis.close();

        String str = new String(textdata, "UTF-8");

        JSONArray ja = new JSONArray(str);
        double tempMean=0;
        for (Object jb : ja) {
            JSONObject jb_ = (JSONObject) jb;

            JSONArray data = jb_.getJSONArray("data");
            double mean=jb_.getJSONObject("statistics").getDouble("mean");
           // double mean=jb_.getJSONObject("statisticsBoundingCurve").getDouble("mean");
            
            if (tempMean != mean) {
                displayImage(getBoundingCurveXYImage(data, jb_.getInt("width"), jb_.getInt("height"), jb_.getInt("type")), "testGeneratedData");
                tempMean = mean;

            }
                
        }
    }
    public static void testmakeImageBlack(){
        Image image;
        try {
            FileInputStream input = new FileInputStream("C:/$AVG/baseDir/Imports/Sprites/MEN/521211-1505671144743.gif");
             image = new Image(input);
             BufferedImage bimg= makeImageBlack( image);
             displayImage(bimg,"testmakeImageBlack" );
             BufferedImage scaledDown=scale(bimg,bimg.getType(),bimg.getWidth()+10,bimg.getHeight()+10,0,0);
             displayImage(scaledDown,"testmakeImageBlack::scaledDown" );
             BufferedImage subtracted=getSubtracted(bimg,scaledDown);
             displayImage(subtracted,"testmakeImageBlack::subtracted" );
             
             BufferedImage boundingCurve=FitPolygon.getCannyEdges(bimg);
             
             
             displayImage(boundingCurve,"testmakeImageBlack::getCannyBinary");
             
             displayImage(getBoundingCurveXYImage(boundingCurve),"testmakeImageBlack::boundingCurve" );
              
        } catch (FileNotFoundException ex) {
             image = SwingFXUtils.toFXImage(HilbertCurvePatternDetect.getRandomImage(100, 500), null);
            Logger.getLogger(CharacterPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * @param image
     * @return 
     */
    public static BufferedImage makeImageBlack(Image image){
    
        WritableImage writableImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        PixelReader pixelReader = writableImage.getPixelReader();
        for (int i = 0; i < writableImage.getHeight(); i++) {
            for (int j = 0; j < writableImage.getWidth(); j++) {
                Color c = pixelReader.getColor(j, i);
                if (c.getOpacity() < 1) {
                    pixelWriter.setColor(j, i, Color.WHITE);
                }
                if (c.getRed() > 0 || c.getGreen() > 0 || c.getBlue() > 0) {
                    pixelWriter.setColor(j, i, Color.BLACK);
                }
            }
        }
       return  SwingFXUtils.fromFXImage(writableImage, null);

    }
    /**
     * scale image
     *
     * @param sbi image to scale
     * @param imageType type of image
     * @param dWidth width of destination image
     * @param dHeight height of destination image
     * @param fWidth x-factor for transformation / scaling
     * @param fHeight y-factor for transformation / scaling
     * @return scaled image
     */
    public static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) {

        BufferedImage scaledImage = new BufferedImage(dWidth, dHeight, imageType);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(sbi, 0, 0, dWidth, dHeight, null);
        graphics2D.dispose();
        return scaledImage;
//    BufferedImage dbi = null;
//    if(sbi != null) {
//        dbi = new BufferedImage(dWidth, dHeight, imageType);
//        Graphics2D g = dbi.createGraphics();
//        AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
//        g.drawRenderedImage(sbi, at);
//    }
//    return dbi;
    }
    
    public static BufferedImage getBoundingCurve(BufferedImage img) {
        //get image width and height
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics2D graphics2D = result.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        long counter=0;
        for (int i = 0; i < width; i++) {
            boolean startSet=false;
            boolean endSet=false;
           Color lastColor=Color.WHITE;
            for (int j = 0; j < height; j++) {

                //get pixel value
                int p = img.getRGB(i, j);
                if(getColor(p).equals(java.awt.Color.BLACK)){continue;}//Only interested in black
                //get alpha
                int a = (p >> 24) & 0xff;

                //get red
                int r = (p >> 16) & 0xff;

                //get green
                int g = (p >> 8) & 0xff;

                //get blue
                int b = p & 0xff;

                /**
                 * to keep the project simple we will set the ARGB value to 255,
                 * 100, 150 and 200 respectively.
                 */
                a = 255;
                r = 100;
                g = 150;
                b = 200;

               
                    //set the pixel value
                    p = (a << 24) | (r << 16) | (g << 8) | b;
                    result.setRGB(i, j, p);
                    counter++;
                
            }
        }
        System.out.println("#getBoundingCurve Pixels "+counter+" / "+width*height+ " % "+(100.0*counter/(width*height)));
        
        return result;
    }
    
    
    public static List<int[]> getBoundingCurveXY(BufferedImage img) {
        //get image width and height
        int width = img.getWidth();
        int height = img.getHeight();
        List<int[]> result = new ArrayList<>();

        long counter = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                //get pixel value
                int p = img.getRGB(i, j);
                if (getColor(p).equals(java.awt.Color.BLACK)) {
                    continue;
                }//Only interested in black

                result.add(new int[]{i, j});
                counter++;

            }
        }
        //System.out.println("#getBoundingCurve Pixels " + counter + " / " + width * height + " % " + (100.0 * counter / (width * height)));

        return result;
    }
    
    public static BufferedImage getBoundingCurveXYImage(BufferedImage img) {
        List<int[]> pixelLocations = getBoundingCurveXY(img);
        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        Graphics2D graphics2D = result.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        for (int[] pixelLocation : pixelLocations) {
            /**
             * to keep the project simple we will set the ARGB value to 255,
             * 100, 150 and 200 respectively.
             */
            int a = 255;
            int r = 100;
            int g = 150;
            int b = 200;

            //set the pixel value
            int p = (a << 24) | (r << 16) | (g << 8) | b;
            result.setRGB(pixelLocation[0], pixelLocation[1], p);
        }
        return result;
    }
    
    public static BufferedImage getBoundingCurveXYImage(JSONArray pixelLocations,int width, int height,int imageType) {
        //List<int[]> pixelLocations = getBoundingCurveXY(img);
        BufferedImage result = new BufferedImage(width, height, imageType);
        Graphics2D graphics2D = result.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        for (Object pixelLocation : pixelLocations) {
            JSONArray pixelLocation_=(JSONArray) pixelLocation;
            /**
             * to keep the project simple we will set the ARGB value to 255,
             * 100, 150 and 200 respectively.
             */
            int a = 255;
            int r = 100;
            int g = 150;
            int b = 200;

            //set the pixel value
            int p = (a << 24) | (r << 16) | (g << 8) | b;
            result.setRGB(pixelLocation_.getInt(0),pixelLocation_.getInt(1), p);
        }
        return result;
    }
    
     public static void displayImage(BufferedImage bimage,String message ){
        Icon icon = new ImageIcon(bimage);
              JLabel picLabel = new JLabel(icon);
              JOptionPane.showMessageDialog(null, picLabel,message, JOptionPane.PLAIN_MESSAGE, null);
    }
     
     public static java.awt.Color getColor(int c) {

        int red = (c & 0x00ff0000) >> 16;
        int green = (c & 0x0000ff00) >> 8;
        int blue = c & 0x000000ff;
      //  System.out.println(red+" "+green+" "+blue);
        return new java.awt.Color(red, blue, green);

    }
}
