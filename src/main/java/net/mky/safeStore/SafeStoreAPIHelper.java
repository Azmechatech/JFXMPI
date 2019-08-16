/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.safeStore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.mky.safeStore.pojo.FolderViewResult;

/**
 *
 * @author mkfs
 */
public class SafeStoreAPIHelper {
    
    public static List<FolderViewResult> getListOfFolders(String key){
        List<File> files=new ArrayList<>();
        List<FolderViewResult> result=new ArrayList<>();
        FileReadOperations.listfolders(new File(CONFIG.getSTART_LOCATION()),  files);
        
        for(File file:files){
            if(file.getName().contains(EncryptionBatchProcess.DUMP) | file.getName().contains(EncryptionBatchProcess.STORE)|file.getName().contains(EncryptionBatchProcess.MKFS))
            try {
                FolderViewResult FVR=new FolderViewResult();
                FVR.setName(file.getName().contains("STORE")|file.getName().contains("dump")?EncryptionBatchProcess.encryptString(file.getName(), key):file.getName());
                FVR.setSize(file.length());
                FVR.setLocation(file.getCanonicalPath());
                FVR.setPreviewContent("FOLDER modified at "+file.lastModified());
                result.add(FVR);
            } catch (IOException ex) {
                Logger.getLogger(SafeStoreAPIHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    
    }
    
}
