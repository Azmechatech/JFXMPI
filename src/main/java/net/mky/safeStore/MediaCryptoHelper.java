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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

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
            bi = getThumbnail(bi);
            inputBytes = getByteArrayFor(bi);
            EncryptionBatchProcess.doEncryption(inputBytes, key, outputFile, name, "thumbnail");
        } catch (IOException ex) {
            Logger.getLogger(MediaCryptoHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
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
