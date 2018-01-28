/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow;

import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 *
 * @author mkfs
 */
public class CharSession extends QnASession {

    public  HashMap<String, String> Session = new HashMap<String, String>();
    public String fileNmae=new String("");

    public String addResource(String newPath ){
        String tag = JOptionPane.showInputDialog("Tag:");
        if (!Session.containsKey(tag)) {
            Session.put(tag, newPath);
        } else {
            addResource(newPath);
        }
        return tag;
    }
}
