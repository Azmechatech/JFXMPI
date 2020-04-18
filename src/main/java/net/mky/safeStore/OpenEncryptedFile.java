/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.safeStore;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import static net.mky.safeStore.EncryptionBatchProcess.askForPasswordSwing;
import static net.mky.safeStore.EncryptionBatchProcess.getEncryptionFolderMeta;
import org.json.JSONObject;

/**
 *
 * @author mkfs
 */
public class OpenEncryptedFile {
    
    public static String[] getDecryptedFolderNames(File directory,String key) {

        String[] directories = directory.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        for(int i=0;i<directories.length;i++){
            directories[i]=CryptoUtils.decode(directories[i], key.length());
        }
       // System.out.println(Arrays.toString(directories));
       return directories;  
    }
    
    /**
     * 
     * @param directory
     * @param key
     * @return
     * @throws IOException 
     */
    public static HashMap<String, String> getMKFSFolderContent(File directory, String key) throws IOException {
        JSONObject folderContent = new JSONObject(EncryptionBatchProcess.getEncryptionFolderMeta(directory));
        HashMap<String, String> MKFSData = new HashMap<>();
        for (String file : folderContent.keySet()) {
            MKFSData.put(CryptoUtils.decode(folderContent.getString(file), 16), directory.getAbsolutePath() + "/" + file);
        }
        return MKFSData;
    }
    
    class DirFilter implements FilenameFilter {

        private Pattern pattern;

        public DirFilter(String reg) {
            pattern = Pattern.compile(reg);
        }

        @Override
        public boolean accept(File dir, String name) {
            // Strip path information, search for regex:
            return pattern.matcher(new File(name).getName()).matches();
        }
    }
    
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
            String originalFolderName = CryptoUtils.decode(fileToDecrypt.getParentFile().getName(), key.length());
             
            if (originalFolderName.contains("__")) {//Create Folder Structure
                //Creating a File object
                File file = new File(tempWorkSpace.getAbsolutePath() +"/"+ originalFolderName.split("__")[0]+"/"+ originalFolderName.split("__")[1]);
                //Creating the directory
                boolean bool = file.mkdirs();
               // if(!bool)System.out.println("Failed to create>> "+file.getAbsolutePath() );

               originalFileName = file.getAbsolutePath() + "/" + originalFileName;
            } else {
                originalFileName = tempWorkSpace.getAbsolutePath() + "/" + originalFileName;
            }
            
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
