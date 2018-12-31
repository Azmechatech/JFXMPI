/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi.DataHelpers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import net.mky.tools.SubtractImage;
import static net.mky.tools.SubtractImage.displayImage;
import static net.mky.tools.SubtractImage.getBoundingCurveXYImage;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author mkfs
 */
public class ImageFactory {
    static JSONArray IMAGES_M_DATA=new JSONArray();
    
    public static void init()  {
        try {
            init("JFMPIImageDataM.json");
        } catch (IOException ex) {
            System.out.println("Check if \"JFMPIImageDataW/M.json\" is(are) available in Application folder. ");
            Logger.getLogger(ImageFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void init(String dataFileFullPath) throws FileNotFoundException, IOException {
        File file = new File(dataFileFullPath);
        FileInputStream fis = new FileInputStream(file);
        byte[] textdata = new byte[(int) file.length()];
        fis.read(textdata);
        fis.close();

        String str = new String(textdata, "UTF-8");

        IMAGES_M_DATA = new JSONArray(str);

    }
    
    public static BufferedImage getRandomBufferedImage()  {
        if(IMAGES_M_DATA.length()==0) init(); //Make sure data is available
        
        JSONArray ja = IMAGES_M_DATA;
        double tempMean = 0;
        for (Object jb : ja) {
            if (Math.random() > .5) {
                continue;
            }
            JSONObject jb_ = (JSONObject) jb;

            JSONArray data = jb_.getJSONArray("data");
            double mean = jb_.getJSONObject("statistics").getDouble("mean");
            return getBoundingCurveXYImage(data, jb_.getInt("type"));

        }

        //Default
        JSONObject jb_ = ja.getJSONObject(0);

        JSONArray data = jb_.getJSONArray("data");
        double mean = jb_.getJSONObject("statistics").getDouble("mean");
        return getBoundingCurveXYImage(data, jb_.getInt("type"));

    }
    
    public static void visualTest() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            SubtractImage.testGeneratedData(selectedFile.getAbsolutePath());
        }

    }
    
    public static void main(String... args){
        try {
            visualTest();
        } catch (IOException ex) {
            Logger.getLogger(ImageFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
