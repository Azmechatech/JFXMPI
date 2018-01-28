/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mkfs
 */
public class QnASession {

    public static HashMap<String, String> Session = new HashMap<String, String>();

    public static void resetSession(){
        Session = new HashMap<String, String>();
    };
    public static boolean writeToDisc(String CanPath) throws FileNotFoundException, IOException {
        {
            File file = new File(CanPath);
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream s = new ObjectOutputStream(f);
            s.writeObject(Session);
            s.close();
        }
        return true;

    }
    
    public static boolean writeToDisc(String CanPath,HashMap<String, String> Session) throws FileNotFoundException, IOException {
        {
            File file = new File(CanPath);
            FileOutputStream f = new FileOutputStream(file);
            ObjectOutputStream s = new ObjectOutputStream(f);
            s.writeObject(Session);
            s.close();
        }
        return true;

    }

    public static HashMap<String, String> loadToSession_(String FileCanPath) throws FileNotFoundException, IOException, ClassNotFoundException {
        HashMap<String, String> Session = null;
        resetSession();
        File file = new File(FileCanPath);
        if (file.exists()) {
            FileInputStream f = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(f);
            Session = (HashMap<String, String>) s.readObject();
            s.close();
            return Session;
        }else{
            Session = new HashMap<String, String>();
        }

        return Session;
    }
    
    public static boolean loadToSession(String FileCanPath) throws FileNotFoundException, IOException, ClassNotFoundException {

        resetSession();
        File file = new File(FileCanPath);
        if (file.exists()) {
            FileInputStream f = new FileInputStream(file);
            ObjectInputStream s = new ObjectInputStream(f);
            Session = (HashMap<String, String>) s.readObject();
            s.close();
            return true;
        }

        return false;
    }
    
    
    public static void printSession(String Name){
        System.out.println("Printing::"+Name);
        for (Map.Entry<String, String> entry : Session.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key);
            System.out.println(value);
  // do stuff
        }


    }
    
    
    

}
