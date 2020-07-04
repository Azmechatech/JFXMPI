/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.image;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
 
import javax.imageio.ImageIO;
 
/**
 * This program demonstrates how to capture screenshot of a portion of screen.
 * @author www.codejava.net
 * https://www.codejava.net/java-se/graphics/how-to-capture-screenshot-programmatically-in-java
 *
 */
public class PartialScreenCapture {
 
    public static void main(String[] args) {
        try {
            Robot robot = new Robot();
            String format = "png";
            String fileName = "C:\\$AVG\\baseDir\\Imports\\Uncensored\\SkymenClan\\"+System.currentTimeMillis()+"." + format;
             
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle captureRect = new Rectangle(664,121, 1465, 464);
            BufferedImage screenFullImage = robot.createScreenCapture(captureRect);
            ImageIO.write(screenFullImage, format, new File(fileName));
             
            System.out.println("A partial screenshot saved!");
        } catch (AWTException | IOException ex) {
            System.err.println(ex);
        }
    }
}