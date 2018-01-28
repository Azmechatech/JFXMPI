/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package systemknowhow;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.imageio.ImageIO;

/**
 *
 * @author mkfs
 */
public class Tools {
    
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String dumpImageToFile(BufferedImage image, String outputFilePrefix, String ext) {
        try {
            String outputFilename = outputFilePrefix + System.currentTimeMillis() + ext;
            ImageIO.write(image, "png", new File(outputFilename));
            return outputFilename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        
        
    }
    
    public static BufferedImage getScaledImage(BufferedImage originalImage,int IMG_WIDTH, int IMG_HEIGHT){
        
        Dimension imgSize = new Dimension(originalImage.getWidth(), originalImage.getHeight());
Dimension boundary = new Dimension(IMG_WIDTH, IMG_HEIGHT);
Dimension desired=getScaledDimension( imgSize,  boundary);
            int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
            BufferedImage resizedImage = new BufferedImage( desired.width, desired.height, type);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(originalImage, 0, 0, desired.width, desired.height, null);
            g.dispose();
            return resizedImage;
        }
    
    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

    int original_width = imgSize.width;
    int original_height = imgSize.height;
    int bound_width = boundary.width;
    int bound_height = boundary.height;
    int new_width = original_width;
    int new_height = original_height;

    // first check if we need to scale width
    if (original_width > bound_width) {
        //scale width to fit
        new_width = bound_width;
        //scale height to maintain aspect ratio
        new_height = (new_width * original_height) / original_width;
    }

    // then check if we need to scale even with the new height
    if (new_height > bound_height) {
        //scale height to fit instead
        new_height = bound_height;
        //scale width to maintain aspect ratio
        new_width = (new_height * original_width) / original_height;
    }

    return new Dimension(new_width, new_height);
}
    
    public static String moveFile(File file,String newName,String PrecautionName){
        
        //move file from one directory to another
        //file = new File("/Users/pankaj/DB.properties");
        File newFile = new File(newName);
        if (!newFile.exists()) {
            if (file.renameTo(newFile)) {
                System.out.println("File move success");
                return newName;
            } else {
                System.out.println("File move failed");
                return "";
            }
        } else { //If the file already exists, recursively get the job done
            if (!(newFile.length() == file.length())) {//And file sizes are different
                newName = newName + PrecautionName;
                newFile = new File(newName);
                if (file.renameTo(newFile)) {
                    moveFile(file, newName, PrecautionName);
                };
            }
        }

        return newName;
    }
    
    public static void moveFiles(File[] files,String Location){
        Location=Location.endsWith("\\")?Location:Location+"\\";
     for(File file:files){
            moveFile( file,Location+file.getName(),"_");
     }
    }
    
    
    public static class PixelInformation extends Component {

        /*
         * public static void main(String[] foo) { new
         * JavaWalkBufferedImageTest1(); }
         */
        public void printPixelARGB(int pixel) {
            int alpha = (pixel >> 24) & 0xff;
            int red = (pixel >> 16) & 0xff;
            int green = (pixel >> 8) & 0xff;
            int blue = (pixel) & 0xff;
            System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);
        }

        public int getAlpha(int pixel) {
            int alpha = (pixel >> 24) & 0xff;
            int red = (pixel >> 16) & 0xff;
            int green = (pixel >> 8) & 0xff;
            int blue = (pixel) & 0xff;
            //System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);
            return alpha;
        }

        public void marchThroughImage(BufferedImage image) {
            int w = image.getWidth();
            int h = image.getHeight();
            System.out.println("width, height: " + w + ", " + h);

            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    System.out.println("x,y: " + j + ", " + i);
                    int pixel = image.getRGB(j, i);
                    printPixelARGB(pixel);
                    System.out.println("");
                }
            }
        }

        public int[][] getPixelData(BufferedImage image) {
            int w = image.getWidth();
            int h = image.getHeight();
            int[][] result = new int[w][h];
            System.out.println("width, height: " + w + ", " + h);

            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {

                    // System.out.println("x,y: " + j + ", " + i);
                    int pixel = image.getRGB(j, i);
                    result[i][j] = pixel;
                    // printPixelARGB(pixel);
                    //System.out.println("");
                }
            }
            return result;
        }

        public int[] getPixelData_(BufferedImage image) {
            int w = image.getWidth();
            int h = image.getHeight();
            int[] result = new int[w * h];
            System.out.println("width, height: " + w + ", " + h);

            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {

                    // System.out.println("x,y: " + j + ", " + i);
                    int pixel = image.getRGB(j, i);
                    result[i * w + j] = pixel;
                    // printPixelARGB(pixel);
                    //System.out.println("");
                }
            }
            return result;
        }

//        public int[][] getEdges(BufferedImage image) {
//            int w = image.getWidth();
//            int h = image.getHeight();
//            int[] tempResult = new EdgeFilter().filterPixels(w, h, getPixelData_(image), null);
//            int result[][] = new int[w][h];
//            for (int i = 0; i < h; i++) {
//                for (int j = 0; j < w; j++) {
//                    result[j][i] = tempResult[i * w + j];
//                }
//            }
//
//            return result;
//        }
//
//        public int[][] getShape(BufferedImage image) {
//            int w = image.getWidth();
//            int h = image.getHeight();
//            int[] tempResult = new ShapeFilter().filterPixels(w, h, getPixelData_(image), null);
//            int result[][] = new int[w][h];
//            for (int i = 0; i < h; i++) {
//                for (int j = 0; j < w; j++) {
//                    result[j][i] = tempResult[i * w + j];
//                }
//            }
//
//            return result;
//        }

        //ShapeFilter
        public void setRGB(BufferedImage image, int[][] XY_pixels) {
            int type = image.getType();
            if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB) {
                for (int i = 0; i < XY_pixels.length; i++) {
                    image.getRaster().setDataElements(XY_pixels[i][0], XY_pixels[i][1], 1, 1, XY_pixels[i][2]);
                }
            } else {
                for (int i = 0; i < XY_pixels.length; i++) {
                    image.setRGB(XY_pixels[i][0], XY_pixels[i][1], XY_pixels[i][2]);
                }
            }
        }

        public void setRGB(BufferedImage image, int x, int y, int width, int height, int[] pixels) {
            int type = image.getType();
            if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB) {
                image.getRaster().setDataElements(x, y, width, height, pixels);
            } else {
                image.setRGB(x, y, width, height, pixels, 0, width);
            }
        }

        public static BufferedImage getSubimage(BufferedImage image, int x, int y, int w, int h) {
            BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = newImage.createGraphics();
            g.drawRenderedImage(image, AffineTransform.getTranslateInstance(-x, -y));
            g.dispose();
            return newImage;
        }
        /*
         * public JavaWalkBufferedImageTest1() { try { // get the BufferedImage,
         * using the ImageIO class BufferedImage image =
         * ImageIO.read(this.getClass().getResource("WhiteSpot.jpg"));
         * marchThroughImage(image); } catch (IOException e) {
         * System.err.println(e.getMessage()); }
        }
         */
    }
}
