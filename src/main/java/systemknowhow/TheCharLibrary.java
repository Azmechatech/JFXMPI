/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package systemknowhow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

/**
 *
 * @author mkfs
 */
public class TheCharLibrary {
    
    public static DefaultListModel listModel = new DefaultListModel();
    public static File[] listOfValidFiles;
    
    public static DefaultListModel TheActiveChar = new DefaultListModel();
    public static String[] TheActiveCharLinks;
    
    public static void load(){
     
        File folder = new File(ThatImage.baseDirChars);
        File[] listOfFiles = folder.listFiles();
        listOfValidFiles=listOfFiles;
        int count = 0;
        listModel.removeAllElements();
        for (int i = 0; i < listOfFiles.length; i++)
        {
            System.out.println("check path"+listOfFiles[i]);
            String name = listOfFiles[i].toString();
            // load only JPEGs
            if ( name.endsWith("jpg")| name.endsWith("png")| name.endsWith("jpeg")) {
                try {
                    listOfValidFiles[count]=listOfFiles[i];
                    ImageIcon ii = new ImageIcon(Tools.getScaledImage(ImageIO.read(listOfFiles[i]),200,200));
                    listModel.add(count++, ii);
                    
                } catch (IOException ex) {
                    Logger.getLogger(TheCharLibrary.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    
    }
    
    
    public static void loadTheActiveChar(CharSession charSession){
        
        
        //File folder = new File(ThatImage.baseDirChars);
        //File[] listOfFiles = new File[charSession.Session.size()];
        
        int count = 0;
        TheActiveChar.removeAllElements();
        TheActiveCharLinks=new String[charSession.Session.size()];
        for(Map.Entry<String,String> entry:charSession.Session.entrySet()){
            
                    if ( entry.getValue().endsWith("jpg")| entry.getValue().endsWith("png")| entry.getValue().endsWith("jpeg")) {
                try {
                    
                    TheActiveCharLinks[count]=entry.getValue();
                    ImageIcon ii = new ImageIcon(Tools.getScaledImage(ImageIO.read(new File(entry.getValue())),200,200));
                    TheActiveChar.add(count++, ii);
                    
                } catch (IOException ex) {
                    System.out.println("entry.getValue()"+entry.getValue());
                    Logger.getLogger(TheCharLibrary.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
        }

    }
    
        public static CharSession getCommonLinks(CharSession charSession,CharSession charSession2){
        
        
        //File folder = new File(ThatImage.baseDirChars);
        //File[] listOfFiles = new File[charSession.Session.size()];
        
            CharSession charSessionComm=new CharSession();
        
        for(Map.Entry<String,String> entry:charSession.Session.entrySet()){
            
                    if ( charSession2.Session.containsValue(entry.getValue())) {
                        charSessionComm.Session.put(entry.getKey(), entry.getValue());
            }
        
        }
        
        return charSessionComm;

    }
    
    public static CharSession getLinkedResources(String Name){
        File file=new File(Name+".map");
        CharSession charSession=new CharSession();
        charSession.fileNmae=Name+".map";
       if( file.exists()){
            try {
                charSession.Session=CharSession.loadToSession_(Name+".map");;
                return charSession;
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(TheCharLibrary.class.getName()).log(Level.SEVERE, null, ex);
            }
       }else{
            try {
                CharSession.writeToDisc(Name+".map", new HashMap<String,String>());
                charSession.Session=CharSession.loadToSession_(Name+".map");;
                charSession.Session.put("self", Name);
                return charSession;
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(TheCharLibrary.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
    return charSession;
    }

    public static boolean writeToCharSession(CharSession charSession){
        
        try {
            CharSession.writeToDisc(charSession.fileNmae, charSession.Session);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(TheCharLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
        
    }
}
