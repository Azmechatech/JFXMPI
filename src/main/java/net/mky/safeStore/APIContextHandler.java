/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.safeStore;

import java.util.List;
import net.mky.safeStore.pojo.FolderViewResult;

/**
 *
 * @author mkfs
 */
public class APIContextHandler {
    
    public List<FolderViewResult>browse(String key){
        return SafeStoreAPIHelper.getListOfFolders(key);
    }
}
