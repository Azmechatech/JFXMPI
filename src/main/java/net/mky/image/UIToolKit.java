/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.mky.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.*;
import java.awt.image.DataBufferInt;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.util.Random;
import java.util.Vector;


/**
 *
 * @author PDI
 */
public class UIToolKit {
    
        public static void main(String[] args) {

        //Tests
        ImageIcon icon = new ImageIcon();
        
        //getWin7StyleRectNewsInk
        icon.setImage(getWithShadow(getWin7StyleRectNewsInk(800, 600,1.0f, true),1.0f,false));
        JOptionPane.showMessageDialog(null, icon);
        
        //getSphereImage
        
        icon.setImage(getSphereImage(800, 600,1.0f, true));
        JOptionPane.showMessageDialog(null, icon);
        
        //getRandomShape
        icon.setImage(getRandomShape(800, 600,1.0f, true));
        JOptionPane.showMessageDialog(null, icon);
        
        //getRandomCurve
        icon.setImage(getRandomCurve(800, 600,8,1.0f, true));
        JOptionPane.showMessageDialog(null, icon);
        
        icon.setImage(getWithShadow(getWin7StyleRectWC(800, 600,1.0f, true),1.0f,false));
        JOptionPane.showMessageDialog(null, icon);
        
        icon.setImage(getWithShadow(getWin7StyleRect(800, 600,1.0f, true),1.0f,false));
        JOptionPane.showMessageDialog(null, icon);
        
        icon.setImage(getWithShadow(getWin7StyleRect2(800, 600,1.0f, true),1.0f,false));
        JOptionPane.showMessageDialog(null, icon);
        
        icon.setImage(getWithShadow(getWin7StyleRect3(800, 600,1.0f, true),1.0f,false));
        JOptionPane.showMessageDialog(null, icon);
        
        icon.setImage(getNewsPrintBG(800, 600, 5,1.0f, true));
        JOptionPane.showMessageDialog(null, icon);
        
        icon.setImage(getParametricCurve(800, 600,1.0f, true));
        JOptionPane.showMessageDialog(null, icon);
        //getNewsPrintBG//getParametricCurve
        
        }
        
      public static BufferedImage getWithShadow( BufferedImage toTessilate,float Transparency, boolean debug) {    
        BufferedImage bimg=new DropShadowPanel().createDropShadow(toTessilate);//
        BufferedImage img = new BufferedImage(bimg.getWidth(), bimg.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
        g.drawImage(bimg, 0, 0, null);
        g.drawImage(toTessilate, 0, 0, null);
        return img;
    }
    
    
        /**
         * Returns Khakee color combination 
         * 0- Khakee, 1-LightInk, 2- DarkInk, 3- DarkPage
         * @return 
         */
        public static Color[] KhakeeTheme(){
            Color[] result=new Color[4];
            Color Khakee = new Color(242,242,192);
            result[0]=Khakee;
            Color LightInk = new Color(6, 112, 154);
            result[1]=LightInk;
            Color DarkInk = new Color(14,16,93);
            result[2]=DarkInk;
            Color DarkPage = new Color(164, 160, 135);
            result[3]=DarkPage;
            
            return result;
        }
      /**
     * Takes integer as input and returns the number of rows and columns needs to put them in a GUI.
     * @param BlocksToDisplay
     * @param debug
     * @return 
     */
      public static int[] getBestVisualArrangement(int BlocksToDisplay,boolean debug){
        int rows=1;
        int columns=1;
        boolean incrementRow=true;
        while(true){
            if(rows*columns>=BlocksToDisplay){
                if(debug)
                //System.out.println("#getBestVisualArrangement BlocksToDisplay="+BlocksToDisplay+" rows="+rows+"\t columns="+columns +"\t scale= 1/"+Math.max(rows, columns));
                return new int[]{rows,columns,Math.max(rows, columns)};
            }
            if(incrementRow) // Start with row increment
            {rows++; incrementRow=false;}
            else {columns++; incrementRow=true;}
            
        }

    }

      /**
       * This will speed up the animation.Based on Cos()
       * Using the array in reverse will slow down animation.
       * @param MaxDelay
       * @return 
       */
      public static int[] getFastMotionDelay(int MaxDelay){
        
          double LocXScale=0.01;
          int[] result=new int[(int)(1.0/LocXScale)];
          for (int i = 0; i< result.length; i++) {
                  
                        MaxDelay = MaxDelay - (int) (MaxDelay * Math.cos(LocXScale * Math.PI / 2));
                        result[i]=MaxDelay;
                        //System.out.println("Animdelay="+Animdelay);
                  LocXScale = LocXScale + 0.01;

                }
          
          return result;
      }

      /**
       * This will speed up the animation.Based on Cos()
       * Using the array in reverse will slow down animation.
       * 
       * @param MaxDelay
       * @return 
       */
      public static int[] getSlowMotionDelay(int MaxDelay){
        
          double LocXScale=0.001;
          int[] result=new int[(int)(1.0/LocXScale)];
          for (int i = 0; i< result.length; i++) {
                  
                        MaxDelay = MaxDelay - (int) (MaxDelay * Math.cos(LocXScale * Math.PI / 2));
                        result[i]=MaxDelay;
                        //System.out.println("Animdelay="+Animdelay);
                  LocXScale = LocXScale + 0.001;

                }
          
          return result;
      }

      public static BufferedImage getParametricCurve(int Width, int Height,float Transparency, boolean debug) {
        Color blue4 = new Color(6,112,154);
        Color Khakee = new Color(242,242,142);
        // 14 16 93
        BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
        int temp[]=ParametricCurve.RandomCurve3(0.0, 200, 100);
        for(int i=0;i<10000;i++){
            int[] result=ParametricCurve.RandomCurve3(i/10000.0, 200, 100);
            //g.fillOval(Width/2+result[0], Height/2+result[2], 3, 3);
            g.drawLine(Width+result[0], Height/2+result[2], Width+temp[0], Height/2+temp[2]);
            g.drawLine(0+result[0], 0/2+result[2], 0+temp[0], 0/2+temp[2]);
            g.setColor(blue4);
            //result=ParametricCurve.RandomCurve2(i/10000.0, 200, 100);
            //g.fillOval(Width/2+result[0], Height/2+result[2], 3, 3);
            //g.drawLine(Width/2+result[0], Height/2+result[2], Width/2+result[0], Height/2+result[2]);
            temp=result;
        }
        return img;
    }
    
       public static BufferedImage getRandomShape(int Width, int Height,float Transparency, boolean debug) {
        Color blue4 = new Color(6,112,154);
        Color Khakee = new Color(242,242,142);
        // 14 16 93
        BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
       // int result[][]=ParametricCurve.RandomClosedLoopCurve(10, 500);
         Double result[][]=ParametricCurve.RandomShape(4);
         
        //RandomClosedLoopSmoothCurve
        for(int i=0;i<result.length-1;i++){
            g.setColor(blue4);
           // g.drawLine(Width+result[0], Height/2+result[2], Width+temp[0], Height/2+temp[2]);
            g.drawLine((int)(Width*result[i][0]), (int)(Height*result[i][1]),(int)( Width*result[i+1][0]),(int)(Height*result[i+1][1]));
      
        }
        g.drawLine((int)(Width*result[0][0]), (int)(Height*result[0][1]),(int)( Width*result[result.length-1][0]),(int)(Height*result[result.length-1][1]));
            
        return img;
    }
    
   
       public static BufferedImage getSphereImage(int Width, int Height,float Transparency, boolean debug) {
        Color blue4 = new Color(6,112,154);
        Color Khakee = new Color(242,242,142);
        // 14 16 93
        BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
       // int result[][]=ParametricCurve.RandomClosedLoopCurve(10, 500);
         double temp[]=ParametricCurve.Sphere(0);
         
        //RandomClosedLoopSmoothCurve
        for(int i=0;i<10000;i++){
             double result[]=ParametricCurve.Sphere(i/10000.0);
            g.setColor(blue4);
           // g.drawLine(Width+result[0], Height/2+result[2], Width+temp[0], Height/2+temp[2]);
            g.drawLine((int)(.5*Width*result[2]), (int)(.5*Height*result[1]),(int)( .5*Width*temp[2]),(int)(.5*Height*temp[1]));
           temp=result;
      
        }
       // g.drawLine((int)(Width*result[0][0]), (int)(Height*result[0][1]),(int)( Width*result[result.length-1][0]),(int)(Height*result[result.length-1][1]));
            
        return img;
    }
    
       
       
      public static BufferedImage getRandomCurve(int Width, int Height,int numOfPoints,float Transparency, boolean debug) {
        Color blue4 = new Color(6,112,154);
        Color Khakee = new Color(242,242,142);
        // 14 16 93
        BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
       // int result[][]=ParametricCurve.RandomClosedLoopCurve(10, 500);
         int result[][]=ParametricCurve.RandomClosedLoopSmoothCurve(numOfPoints,Width<Height?Width:Height);
         
        //RandomClosedLoopSmoothCurve
        for(int i=0;i<result.length-1;i++){
            g.setColor(blue4);
           // g.drawLine(Width+result[0], Height/2+result[2], Width+temp[0], Height/2+temp[2]);
            g.drawLine(result[i][0], result[i][1], result[i+1][0], result[i+1][1]);
            
            
      
        }
        
        g.drawLine(result[0][0], result[0][1], result[result.length-1][0], result[result.length-1][1]);
        return img;
    }
    
      /**
       * Returns Image with repeat
       * @param Width
       * @param Height
       * @param Transparency
       * @param toTessilate
       * @param debug
       * @return 
       */
      public static BufferedImage getTessilatedImage(int Width, int Height,float Transparency, BufferedImage toTessilate, boolean debug) {
        BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
        int imageWidth = toTessilate.getWidth();
        int imageHeight = toTessilate.getHeight();
        int componentWidth = Width;
        int componentHeigth = Height;
        int ImageRepeatX = componentWidth / imageWidth;
        int ImageRepeatY = componentHeigth / imageHeight;
        //System.out.println("#OldBackground ImageRepeat="+ImageRepeatX);
        //if ((ImageRepeatX >= 1) | (ImageRepeatY >= 1)) {
            for (int i = 0; i <= ImageRepeatX; i++) {
                for (int j = 0; j <= ImageRepeatY; j++) {
                    //System.out.println("#OldBackground Drawing="+i*imageWidth);
                    g.drawImage(toTessilate, i * imageWidth, j * imageHeight, null);
                }
            }
      //  }
        return img;
    }
    
      
      /**
       * Preview the image from byte data
       * @param ImageData
       * @param width
       * @param height 
       */
      
//      public static void PreviewImage(byte[] ImageData, int width,int height){
//          BufferedImage originalImage = ScreenCapture.getByteAsImage(ImageData);
//          double xRatio = (double) width / originalImage.getWidth();
//          double yRatio = (double) height / originalImage.getHeight();
//          double imageScaleRatio = Math.min(xRatio, yRatio);
//          AffineTransform trans = AffineTransform.getScaleInstance(imageScaleRatio, imageScaleRatio);
//
//          BufferedImage PreviewImage = new BufferedImage((int) (imageScaleRatio * originalImage.getWidth()),
//                  (int) (imageScaleRatio * originalImage.getHeight()), BufferedImage.TYPE_INT_RGB);
//          Graphics2D g = PreviewImage.createGraphics();
//          // g.drawImage(originalImage, 0, 0, getWidth(), getHeight(), null);
//          g.drawRenderedImage(originalImage, trans);
//          g.dispose();
//          g.setComposite(AlphaComposite.Src);
//          g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//          g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//          g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//          ImageIcon icon = new ImageIcon();
//          icon.setImage(PreviewImage);
//          JOptionPane.showMessageDialog(null, icon);
//                      
//      }

      public static BufferedImage getNewsPrintBG(int Width, int Height, int BorderThickness, float Transparency, boolean DropShadow) {
        Color Khakee = new Color(242,242,192);
        BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
        g.setStroke(new BasicStroke(8));
        g.setColor(new Color(14,16,93)); //News Print Dark Ink
        g.fillRoundRect(0, 0, Width, Height, Width/10, Width/10);
        g.setColor(Khakee);
        g.fillRoundRect(BorderThickness, BorderThickness, Width-2*BorderThickness, Height-2*BorderThickness, Width/10-BorderThickness, Width/10-BorderThickness);
        
        if(DropShadow) return  getWithShadow( img, Transparency, DropShadow);
       // g.drawRoundRect(0, 0, Width, Height, Width/10, Height/10);
        return img;
    }
   
      public static BufferedImage getNewsPrintBG2(int Width, int Height, int BorderThickness, float Transparency, boolean DropShadow) {
        Color Khakee = new Color(242,242,192);
        BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
        g.setStroke(new BasicStroke(8));
        g.setColor(new Color(14,16,93)); //News Print Dark Ink
        g.fillRect(0, 0, Width, Height);
        g.setColor(Khakee);
        g.fillRect(BorderThickness, BorderThickness, Width-2*BorderThickness, Height-2*BorderThickness);
        
        if(DropShadow) return  getWithShadow( img, Transparency, DropShadow);
       // g.drawRoundRect(0, 0, Width, Height, Width/10, Height/10);
        return img;
    }
   
      public static BufferedImage getWin7StyleRectNewsInk(int Width, int Height,float Transparency, boolean debug) {

        // Color clrHi = new Color(0, 229, 0);
        Color border = new Color(50, 50,73);
        Color blue = KhakeeTheme()[1];
        Color blue2 = new Color(157, 166, 254);
        Color blue3 = new Color(150, 156, 254);
        Color blue4 = KhakeeTheme()[1];
        
        BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_4BYTE_ABGR_PRE);

        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
        GradientPaint gp = new GradientPaint(0, 0, blue2, Height/2, Height,blue, true);
        //Here we are setting our Gradient Paint as the color we want to use
        g.setPaint(gp);
        /* Here we are creating a rectangle and filling it
        * with our GradientPaint, this will serve as the background to our JPanel
        */
        //g.fillRect(0, 0, Width, Height);
        g.fillPolygon(new int[]{0,0,Width/10,Width/5,Width/10}, new int[]{0,Height,Height,Height/2,0}, 5);
        g.fillRoundRect(0, 0, Width, Height, 10, 10);
        gp = new GradientPaint(0, 0, blue4, Height-Height/10, Height,blue3, false);
        g.setPaint(gp);
        g.fillRoundRect(0, Height/2, Width, Height, 0, 10);
        gp = new GradientPaint(0, 0, blue, Height/2, Height,blue2, true);
        g.setPaint(gp);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
        
        g.fillPolygon(new int[]{Width,Width,Width-Width/5,Width-Width/10}, new int[]{0,Height,Height,0}, 4);
        
        g.setColor(border);//orange
        //g.setColor(orange);//orange
        //g.drawRect(0, 0, Width-1, Height-1);
        g.drawRoundRect(0, 0, Width-1, Height-1, 10, 10);
        g.setColor(Color.GREEN);
        return img;
    }

      /**
       * 
       * @param Width
       * @param Height
       * @param Transparency
       * @param debug
       * @return 
       */
      public static BufferedImage getWin7StyleRect(int Width, int Height,float Transparency, boolean debug) {

        // Color clrHi = new Color(0, 229, 0);
        Color border = new Color(50, 50,73);
        Color blue = new Color(133, 137, 192);
        Color blue2 = new Color(157, 166, 254);
        Color blue3 = new Color(150, 156, 254);
        Color blue4 = new Color(200, 206, 254);
        
        BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_4BYTE_ABGR_PRE);

        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
        GradientPaint gp = new GradientPaint(0, 0, blue2, Height/2, Height,blue, true);
        //Here we are setting our Gradient Paint as the color we want to use
        g.setPaint(gp);
        /* Here we are creating a rectangle and filling it
        * with our GradientPaint, this will serve as the background to our JPanel
        */
        //g.fillRect(0, 0, Width, Height);
        g.fillPolygon(new int[]{0,0,Width/10,Width/5,Width/10}, new int[]{0,Height,Height,Height/2,0}, 5);
        g.fillRoundRect(0, 0, Width, Height, 10, 10);
        gp = new GradientPaint(0, 0, blue4, Height-Height/10, Height,blue3, false);
        g.setPaint(gp);
        g.fillRoundRect(0, Height/2, Width, Height, 0, 10);
        gp = new GradientPaint(0, 0, blue, Height/2, Height,blue2, true);
        g.setPaint(gp);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
        
        g.fillPolygon(new int[]{Width,Width,Width-Width/5,Width-Width/10}, new int[]{0,Height,Height,0}, 4);
        
        g.setColor(border);//orange
        //g.setColor(orange);//orange
        //g.drawRect(0, 0, Width-1, Height-1);
        g.drawRoundRect(0, 0, Width-1, Height-1, 10, 10);
        g.setColor(Color.GREEN);
        return img;
    }
    
       public static BufferedImage getWin7StyleRectWC(int Width, int Height,float Transparency, boolean debug) {

        // Color clrHi = new Color(0, 229, 0);
        Color border = new Color(50, 50,73);
        Color blue = new Color(133, 137, 192);
        Color blue2 = new Color(157, 166, 254);
        Color blue3 = new Color(150, 156, 254);
        Color blue4 = new Color(200, 206, 254);
        
        BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_4BYTE_ABGR_PRE);

        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
        GradientPaint gp = new GradientPaint(0, 0, blue2, Height/2, Height,blue, true);
        //Here we are setting our Gradient Paint as the color we want to use
        g.setPaint(gp);
        /* Here we are creating a rectangle and filling it
        * with our GradientPaint, this will serve as the background to our JPanel
        */
        //g.fillRect(0, 0, Width, Height);
        g.fillPolygon(new int[]{0,0,Width/10,Width/5,Width/10}, new int[]{0,Height,Height,Height/2,0}, 5);
        g.fillRoundRect(0, 0, Width, Height, 10, 10);
        gp = new GradientPaint(0, 0, blue4, Height-Height/10, Height,blue3, false);
        g.setPaint(gp);
        g.fillRoundRect(0, Height/2, Width, Height, 0, 10);
        gp = new GradientPaint(0, 0, blue, Height/2, Height,blue2, true);
        g.setPaint(gp);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
        
        g.fillPolygon(new int[]{Width,Width,Width-Width/5,Width-Width/10}, new int[]{0,Height,Height,0}, 4);
        
        //g.setColor(border);//orange
        //g.setColor(orange);//orange
        //g.drawRect(0, 0, Width-1, Height-1);
        g.drawRoundRect(0, 0, Width-1, Height-1, 10, 10);
        //g.setColor(Color.GREEN);
        
        int temp[]=ParametricCurve.RandomCurve3(0.0, 200, 100);
        for(int i=0;i<10000;i++){
            int[] result=ParametricCurve.RandomCurve3(i/10000.0, 200, 100);
            //g.fillOval(Width/2+result[0], Height/2+result[2], 3, 3);
            g.drawLine(Width+result[0], Height/2+result[2], Width+temp[0], Height/2+temp[2]);
            g.setColor(blue4);
            //result=ParametricCurve.RandomCurve2(i/10000.0, 200, 100);
            //g.fillOval(Width/2+result[0], Height/2+result[2], 3, 3);
            //g.drawLine(Width/2+result[0], Height/2+result[2], Width/2+result[0], Height/2+result[2]);
            temp=result;
        }
        
        return img;
    }
    
      /**
       * 
       * @param Width
       * @param Height
       * @param Transparency
       * @param debug
       * @return 
       */
      public static BufferedImage getWin7StyleRect2(int Width, int Height,float Transparency, boolean debug) {
//        if (Width < 100) {
//            Width = 100;
//        } //Check and set Threshold to 100px
//        if (Height < 70) {
//            Height = 70;
//        } //Check and set Threshold to 70px

        // Color clrHi = new Color(0, 229, 0);
        Color border = new Color(50, 73,50);
        Color blue = new Color(133, 192, 137);
        Color blue2 = new Color(157, 254, 166);
        Color blue3 = new Color(150, 254, 156);
        Color blue4 = new Color(200, 254, 206);
        Color orange = new Color(255, 63, 229);
        
        BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_4BYTE_ABGR_PRE);

        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
        GradientPaint gp = new GradientPaint(0, 0, blue2, Height/2, Height,blue, true);
        //Here we are setting our Gradient Paint as the color we want to use
        g.setPaint(gp);
        /* Here we are creating a rectangle and filling it
        * with our GradientPaint, this will serve as the background to our JPanel
        */
        //g.fillRect(0, 0, Width, Height);
        g.fillPolygon(new int[]{0,0,Width/10,Width/5,Width/10}, new int[]{0,Height,Height,Height/2,0}, 5);
        g.fillRoundRect(0, 0, Width, Height, 10, 10);
        gp = new GradientPaint(0, 0, blue4, Height-Height/10, Height,blue3, false);
        g.setPaint(gp);
        g.fillRoundRect(0, Height/2, Width, Height, 0, 10);
        gp = new GradientPaint(0, 0, blue, Height/2, Height,blue2, true);
        g.setPaint(gp);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
        
        g.fillPolygon(new int[]{Width,Width,Width-Width/5,Width-Width/10}, new int[]{0,Height,Height,0}, 4);
        
        g.setColor(border);//orange
        //g.setColor(orange);//orange
        //g.drawRect(0, 0, Width-1, Height-1);
        g.drawRoundRect(0, 0, Width-1, Height-1, 10, 10);
        g.setColor(Color.GREEN);
        return img;
    }
    
      /**
       * 
       * @param Width
       * @param Height
       * @param Transparency
       * @param debug
       * @return 
       */
      public static BufferedImage getWin7StyleRect3(int Width, int Height,float Transparency, boolean debug) {
//        if (Width < 100) {
//            Width = 100;
//        } //Check and set Threshold to 100px
//        if (Height < 70) {
//            Height = 70;
//        } //Check and set Threshold to 70px

        // Color clrHi = new Color(0, 229, 0);
        Color border = new Color(73,50,50);
        Color blue = new Color(192,133,137);
        Color blue2 = new Color(254,157,166);
        Color blue3 = new Color(254,150,156);
        Color blue4 = new Color(254,200,206);
        Color orange = new Color(63,255,229);
        
        BufferedImage img = new BufferedImage(Width, Height, BufferedImage.TYPE_4BYTE_ABGR_PRE);

        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
        GradientPaint gp = new GradientPaint(0, 0, blue2, Height/2, Height,blue, true);
        //Here we are setting our Gradient Paint as the color we want to use
        g.setPaint(gp);
        /* Here we are creating a rectangle and filling it
        * with our GradientPaint, this will serve as the background to our JPanel
        */
        //g.fillRect(0, 0, Width, Height);
        g.fillPolygon(new int[]{0,0,Width/10,Width/5,Width/10}, new int[]{0,Height,Height,Height/2,0}, 5);
        g.fillRoundRect(0, 0, Width, Height, 10, 10);
        gp = new GradientPaint(0, 0, blue4, Height-Height/10, Height,blue3, false);
        g.setPaint(gp);
        g.fillRoundRect(0, Height/2, Width, Height, 0, 10);
        gp = new GradientPaint(0, 0, blue, Height/2, Height,blue2, true);
        g.setPaint(gp);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
        
        g.fillPolygon(new int[]{Width,Width,Width-Width/5,Width-Width/10}, new int[]{0,Height,Height,0}, 4);
        
        g.setColor(border);//orange
        //g.setColor(orange);//orange
        //g.drawRect(0, 0, Width-1, Height-1);
        g.drawRoundRect(0, 0, Width-1, Height-1, 10, 10);
        g.setColor(Color.GREEN);
        return img;
    }

      public static void applyShadow(BufferedImage image) {
          int shadowSize = 5;
         float shadowOpacity = 0.5f;
         Color shadowColor = new Color(0x000000);
        int dstWidth = image.getWidth();
        int dstHeight = image.getHeight();

        int left = (shadowSize - 1) >> 1;
        int right = shadowSize - left;
        int xStart = left;
        int xStop = dstWidth - right;
        int yStart = left;
        int yStop = dstHeight - right;

        int shadowRgb = shadowColor.getRGB() & 0x00FFFFFF;

        int[] aHistory = new int[shadowSize];
        int historyIdx = 0;

        int aSum;

        int[] dataBuffer = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        int lastPixelOffset = right * dstWidth;
        float sumDivider = shadowOpacity / shadowSize;

        // horizontal pass

        for (int y = 0, bufferOffset = 0; y < dstHeight; y++, bufferOffset = y * dstWidth) {
            aSum = 0;
            historyIdx = 0;
            for (int x = 0; x < shadowSize; x++, bufferOffset++) {
                int a = dataBuffer[bufferOffset] >>> 24;
                aHistory[x] = a;
                aSum += a;
            }

            bufferOffset -= right;

            for (int x = xStart; x < xStop; x++, bufferOffset++) {
                int a = (int) (aSum * sumDivider);
                dataBuffer[bufferOffset] = a << 24 | shadowRgb;

                // substract the oldest pixel from the sum
                aSum -= aHistory[historyIdx];

                // get the lastest pixel
                a = dataBuffer[bufferOffset + right] >>> 24;
                aHistory[historyIdx] = a;
                aSum += a;

                if (++historyIdx >= shadowSize) {
                    historyIdx -= shadowSize;
                }
            }
        }

        // vertical pass
        for (int x = 0, bufferOffset = 0; x < dstWidth; x++, bufferOffset = x) {
            aSum = 0;
            historyIdx = 0;
            for (int y = 0; y < shadowSize; y++, bufferOffset += dstWidth) {
                int a = dataBuffer[bufferOffset] >>> 24;
                aHistory[y] = a;
                aSum += a;
            }

            bufferOffset -= lastPixelOffset;

            for (int y = yStart; y < yStop; y++, bufferOffset += dstWidth) {
                int a = (int) (aSum * sumDivider);
                dataBuffer[bufferOffset] = a << 24 | shadowRgb;

                // substract the oldest pixel from the sum
                aSum -= aHistory[historyIdx];

                // get the lastest pixel
                a = dataBuffer[bufferOffset + lastPixelOffset] >>> 24;
                aHistory[historyIdx] = a;
                aSum += a;

                if (++historyIdx >= shadowSize) {
                    historyIdx -= shadowSize;
                }
            }
        }
    }
      

      
      /**
       * 
       * @param imgSize
       * @param boundary
       * @return 
       */
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
       /**
        * we want the x and o to be resized when the JFrame is resized
        *
        * @param originalImage an x or an o. Use cross or oh fields.
        *
        * @param biggerWidth
        * @param biggerHeight
        */ 
    public static BufferedImage scaleImage(BufferedImage originalImage, int biggerWidth, int biggerHeight) {
      
        Dimension newDimension= getScaledDimension(new Dimension( originalImage.getWidth(),  originalImage.getHeight()), new Dimension(biggerWidth, biggerHeight)); ;
        biggerWidth=newDimension.width;
        biggerHeight=newDimension.height;
        
        int type = BufferedImage.TYPE_INT_ARGB;

        BufferedImage resizedImage = new BufferedImage(biggerWidth, biggerHeight, type);
        Graphics2D g = resizedImage.createGraphics();

        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(originalImage, 0, 0, biggerWidth, biggerHeight, null);
        g.dispose();


        return resizedImage;
    }
    
      /**
     * Method to overlay Images
     *
     * @param bgImage --> The background Image
     * @param fgImage --> The foreground Image
     * @return --> overlayed image (fgImage over bgImage)
     */
    public static BufferedImage overlayImages(BufferedImage bgImage,
            BufferedImage fgImage) {
 
        /**
         * Doing some preliminary validations.
         * Foreground image height cannot be greater than background image height.
         * Foreground image width cannot be greater than background image width.
         *
         * returning a null value if such condition exists.
         */
        if (fgImage.getHeight() > bgImage.getHeight()
                || fgImage.getWidth() > fgImage.getWidth()) {
            JOptionPane.showMessageDialog(null,
                    "Foreground Image Is Bigger In One or Both Dimensions"
                            + "nCannot proceed with overlay."
                            + "nn Please use smaller Image for foreground");
            return null;
        }
 
        /**Create a Graphics  from the background image**/
        Graphics2D g = bgImage.createGraphics();
        /**Set Antialias Rendering**/
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        /**
         * Draw background image at location (0,0)
         * You can change the (x,y) value as required
         */
        g.drawImage(bgImage, 0, 0, null);
 
        /**
         * Draw foreground image at location (0,0)
         * Change (x,y) value as required.
         */
        g.drawImage(fgImage, 0, 0, null);
 
        g.dispose();
        return bgImage;
    }


    public static BufferedImage particleEffects(int WIDE,int HIGH,float Transparency){
        //screen dimensions

   //particles
        int MAX_PARTICLES = 50;
        int MAX_FRAMES = 2;

    
        BufferedImage screen = new BufferedImage(WIDE, HIGH,  1); //BufferedImage.
         int[]  pixl = ((DataBufferInt) screen.getRaster().getDataBuffer()).getData();
         Graphics2D g = screen.createGraphics();
         g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,Transparency));
        
        g.setColor(new Color(0x00000000));
        // Graphics gCanvas = getGraphics();

         Random ran = new Random();

         //prep particle lightarray
         //x,y,fade-frames
         int[][][] particleFrame = new int[10][10][MAX_FRAMES];

         for (int i = 0; i < MAX_FRAMES; i++)
         {

            for (int x = 0; x < 10; x++)
            {
               for (int y = 0; y < 10; y++)
               {
                  double dist = Math.sqrt((x - 5d) * (x - 5d) + (y - 5d) * (y - 5d));

                  dist = 255 - dist * 60 - i;

                  dist = dist < 0 ? 0 : dist;
                  dist = dist > 255 ? 255 : dist;

                  particleFrame[x][y][i] = (int) dist;
               }
            }
         }

         int[] particlesFrame = new int[MAX_PARTICLES]; //frame
         float[][] particlesBase = new float[MAX_PARTICLES][4]; //x,y , velocity x,y

         for (int i = 0; i < MAX_PARTICLES; i++)
         {
            particlesBase[i][0] = ran.nextInt(WIDE);//WIDE/2+ ran.nextInt(WIDE/2) - WIDE/4;
            particlesBase[i][1] = HIGH/2+ ran.nextInt(HIGH/2)- HIGH/4;

            particlesBase[i][2] = ran.nextFloat()*2 - 1f;
            particlesBase[i][3] = ran.nextFloat()*2 - 1f;

            particlesFrame[i] = ran.nextInt(MAX_FRAMES);
         }

      

            g.fillRect(0, 0, screen.getWidth(), screen.getHeight());

            for (int i = 0; i < MAX_PARTICLES; i++)
            {

               particlesFrame[i]++;
               if (particlesFrame[i] >= MAX_FRAMES) particlesFrame[i] = 0;

               if (particlesBase[i][2] < 0d && particlesBase[i][0] < 10) particlesBase[i][2] = -particlesBase[i][2];
               else if (particlesBase[i][2] > 0d && particlesBase[i][0] > WIDE - 15) particlesBase[i][2] = -particlesBase[i][2];

               if (particlesBase[i][3] < 0d && particlesBase[i][1] < 10) particlesBase[i][3] = -particlesBase[i][3];
               else if (particlesBase[i][3] > 0d && particlesBase[i][1] > HIGH - 15) particlesBase[i][3] = -particlesBase[i][3];

               particlesBase[i][0] += particlesBase[i][2];
               particlesBase[i][1] += particlesBase[i][3];

               int px = (int) particlesBase[i][0];
               int py = (int) particlesBase[i][1];

               for (int x = 0; x < 10; x++)
               {
                  for (int y = 0; y < 10; y++)
                  {

                     int pc = (pixl[WIDE * (y + py) + x + px]) & 0xFF;
                     pc += particleFrame[x][y][particlesFrame[i]];
                     pc = pc > 255 ? 255 : pc;
                     int pc2 = (pc > 230) ? pc : 0;

                     pixl[WIDE * (y + py) + x + px] = pc | (pc << 8) | (pc2 << 16);

                  }
               }
            }

            //gCanvas.drawImage(screen, 0, 0, null);
            return screen;
    }
    
    /**
     * 
     * @param image
     * @param Color 0xFF000000
     * @return 
     */
     public static  Image TransformColorToTransparency(BufferedImage image, int Color)
  {
    ImageFilter filter = new RGBImageFilter()
    {
      @Override
      public final int filterRGB(int x, int y, int rgb)
      {
        return (rgb << 8) & Color; //0xFF000000
      }
    };
    
    ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
    return Toolkit.getDefaultToolkit().createImage(ip);
  }
     
     /**
 * Converts a given Image into a BufferedImage
 *
 * @param img The Image to be converted
 * @return The converted BufferedImage
 */
public static BufferedImage toBufferedImage(Image img)
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

