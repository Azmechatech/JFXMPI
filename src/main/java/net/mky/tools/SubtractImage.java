/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.tools;

import java.awt.image.BufferedImage;
import java.io.File;   
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Maneesh
 */
public class SubtractImage {
    public static void main(String[] args) throws Exception {
        int[][][] ch = new int[4][4][4];
        BufferedImage image2 = ImageIO.read(new File(args.length>0?args[0]:"G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317409970.jpg"));
        BufferedImage image1 = ImageIO.read(new File(args.length>1?args[1]:"G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317391299.png"));
        BufferedImage image3 = getSubtracted(image1,image2);// new BufferedImage(image1.getWidth(), image1.getHeight(), image1.getType());

        ImageIO.write(image3, "png",  new File(args.length>2?args[2]:"G:/bkp/$AVG/baseDir/Imports/Sprites/WOMEN/765324Milfs-_1515317409970.png"));

        
        String itemsToSubtract[]={
        "G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317380863.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317391299.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317400871.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317409970.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317484083.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317496224.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317507138.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317513479.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317520991.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317531810.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317539665.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317548358.jpg"
        ,
        "G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317557820.jpg"
        ,
        "G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317567532.jpg",
        "G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317611576.jpg"};
        
        String from="G:/bkp/$AVG/baseDir/Imports/Game Series/Milfs/765324Milfs-_1515317391299.png";
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
}
