/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.safeStore;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static net.mky.safeStore.EncryptionBatchProcess.askForPasswordSwing;
import static net.mky.safeStore.EncryptionBatchProcess.getEncryptionFolderMeta;
import org.json.JSONObject;

/**
 *
 * @author mkfs
 */
public class OpenEncryptedFile {
    
    public static File decrypt(File tempWorkSpace, File fileToDecrypt, String key) throws IOException {
        //Find the meta file
        String latestData = getEncryptionFolderMeta(fileToDecrypt.getParentFile());
        JSONObject oldData = new JSONObject(latestData);
        
        //Check if the selected folder is encryption folder. return null
        if(EncryptionBatchProcess.checkEncryptionFolder(tempWorkSpace)){
            System.err.println("Can not use this folder for extraction."
                    + "\n EncryptionBatchProcess.checkEncryptionFolder."
                    + "\n Use another folder.");
            return null;
        }
        
        
        if (oldData.has(fileToDecrypt.getName())) {
            //Find file name in meta
            String originalFileName = CryptoUtils.decode(oldData.getString(fileToDecrypt.getName()), key.length());
            originalFileName = tempWorkSpace.getAbsolutePath() + "/" + originalFileName;
            File decryptedFile = new File(originalFileName);
            try {
                CryptoUtils.decrypt(key, fileToDecrypt, decryptedFile);
                System.out.println("decrypted>>"+decryptedFile);
                return decryptedFile;
            } catch (CryptoException ex) {
                Logger.getLogger(EncryptionBatchProcess.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }

        }

        //Extract to the tempWorkSpace
        //Return the File object 
        //
        return null;

    }
    
    public static void doInteractive(){
        //Choose the files to be encrypted
        List<File> filesToEncrypt = FileReadOperations.chooseFiles();
        //Choose a passcode for encryption
        String passKey = askForPasswordSwing();//askForPassword();
        //Choose a location to save
        File folderToSave = FileReadOperations.chooseFolder();
        int counter = 0;
        
        for (File file : filesToEncrypt) {
            
            try {
                System.out.println("decrypt>>"+file.getName());
                decrypt(folderToSave, file, passKey);
                
            } catch (IOException ex) {
                Logger.getLogger(OpenEncryptedFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
    
    }
    
    public static void main(String... args){
        doInteractive();
    }
    
}
