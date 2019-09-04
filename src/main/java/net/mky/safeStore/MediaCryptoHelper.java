/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.safeStore;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TreeItem;
import javax.imageio.ImageIO;
import org.json.JSONObject;

/**
 *
 * @author mkfs
 */
public class MediaCryptoHelper {
    
    //main purpose is to create thumbnail or preview of the content
    
    public static void saveCryptoMedia(String key, BufferedImage bi, File outputFile) {
        try {
            byte[] inputBytes = getByteArrayFor(bi);
            String name = String.valueOf(System.currentTimeMillis());
            EncryptionBatchProcess.doEncryption(inputBytes, key, outputFile, name, null);
            
            //Saving a thumbnail
//            bi = getThumbnail(bi);
//            inputBytes = getByteArrayFor(bi);
//            EncryptionBatchProcess.doEncryption(inputBytes, key, outputFile, name, "thumbnail");
        } catch (IOException ex) {
            Logger.getLogger(MediaCryptoHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public static void saveCryptoText(String key, String dataToWrite, File folderToSave,String name) {
        try {
            byte[] inputBytes = dataToWrite.getBytes();
            //String name = String.valueOf(System.currentTimeMillis());
            //String newFilePath=EncryptionBatchProcess.doEncryption(inputBytes, key, folderToSave, name, null);
            
            String newFilePath=EncryptionBatchProcess.doEncryption(dataToWrite.getBytes(), key, folderToSave, "CRACK.txt", null);
            
            System.out.println(CryptoUtils.decrypt(key, new File(newFilePath)));
            
        } catch (IOException ex) {
            Logger.getLogger(MediaCryptoHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CryptoException ex) {
            Logger.getLogger(MediaCryptoHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
     public static String readCryptoText(String key, File encryptedFile) {
        try {

            //String name = String.valueOf(System.currentTimeMillis());
            return CryptoUtils.decrypt(key, encryptedFile);
        } catch (CryptoException ex) {
            Logger.getLogger(MediaCryptoHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
     
    public static HashMap<String, String> getAvailableFiles(File mkfsFile, String subStringToMatch) throws FileNotFoundException, IOException {
        HashMap<String, String> MKFSData = new HashMap<>();
        FileInputStream input = new FileInputStream(mkfsFile.getAbsolutePath());

        byte[] data = new byte[(int) mkfsFile.length()];
        input.read(data);
        input.close();

        String latestData = new String(data, "UTF-8");
        JSONObject oldData = new JSONObject(latestData);
        for (String key : oldData.keySet()) {
            if(oldData.get(key)  instanceof  String )
            MKFSData.put(CryptoUtils.decode(oldData.getString(key), 16), mkfsFile.getParentFile().getAbsolutePath() + "/" + key);
        }
        return MKFSData;
    }
    
    /**
     * 
     * @param bi
     * @return
     * @throws IOException 
     */
    public static byte[] getByteArrayFor(BufferedImage bi) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, "png", baos);
            baos.flush();
            byte[] inputBytes = baos.toByteArray();
            baos.close();
            
            return inputBytes;
    }
    
    /**
     * 
     * @param before
     * @return 
     */
    public static BufferedImage getThumbnail(BufferedImage before) {

        //BufferedImage before = getBufferedImage(encoded);
        int w = before.getWidth();
        int h = before.getHeight();
        BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(.1, .1);
        AffineTransformOp scaleOp
                = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        after = scaleOp.filter(before, after);
        
        return after;
    }
    
}
