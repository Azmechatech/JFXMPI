/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.safeStore;

import java.util.HashSet;

/**
 *
 * @author mkfs
 */
public class CONFIG {
    
    private static String START_LOCATION=".";

    /**
     * Get the value of START_LOCATION
     *
     * @return the value of START_LOCATION
     */
    public static  String getSTART_LOCATION() {
        return START_LOCATION;
    }

    /**
     * Set the value of START_LOCATION
     *
     * @param START_LOCATION new value of START_LOCATION
     */
    public static  void setSTART_LOCATION(String START_LOCATION) {
        CONFIG.START_LOCATION = START_LOCATION;
    }
    
    
    
   static enum IMAGES{  jpg,png,PNG,JPEG,Jpeg; } 
   static enum TEXTS {text, txt,TEXT,Text,Txt,JSON,Json,json};
    enum VIDS{  mp4,MP4,flv,wmv,mpg,mpeg; } 
    
    public static final HashSet<String> Images=new HashSet<>();
     public static final HashSet<String> Texts=new HashSet<>();
    
    static{
        for(IMAGES img:IMAGES.values())
            Images.add(img.toString());
        
        for(TEXTS txt:TEXTS.values())
            Texts.add(txt.toString());
    }

}
