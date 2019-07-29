/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.safeStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.geometry.Pos;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import net.mky.tools.CryptoException;
import net.mky.tools.CryptoUtils;
import org.json.JSONObject;

/**
 *
 * @author mkfs
 */
public class EncryptionBatchProcess {

    public static List<File> chooseFiles() {

        try{
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select files");

        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

        return selectedFiles;}
        catch (Exception ex){
        
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            chooser.showOpenDialog(null);
            File[] files = chooser.getSelectedFiles();
            return  Arrays.stream(files).collect(Collectors.toList());

        }

    }
    
    private static List<String> readFileAsList(File file) throws IOException {
    final List<String> ret = new ArrayList<String>();
    final BufferedReader br = new BufferedReader(new FileReader(file));
    try {
        String strLine;
        while ((strLine = br.readLine()) != null) {
            ret.add(strLine);
        }
        return ret;
    } finally {
        br.close();
    }
}
    
    
    
    public static File chooseFolder() {
        String choosertitle = "Select folder location";
        try {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle(choosertitle);
            //File defaultDirectory = new File("c:/dev/javafx");
            //chooser.setInitialDirectory(defaultDirectory);
            return chooser.showDialog(null);
        } catch (Exception ex) {
            JFileChooser chooser;
            chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle(choosertitle);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            //
            // disable the "All files" option.
            //
            chooser.setAcceptAllFileFilterUsed(false);
            //    
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                System.out.println("getCurrentDirectory(): "
                        + chooser.getCurrentDirectory());
                System.out.println("getSelectedFile() : "
                        + chooser.getSelectedFile());
                
                return chooser.getSelectedFile();
            } else {
                System.out.println("No Selection ");
            }
        }
        return null;

    }
    public static String MKFS="mkfs";
    public static String getEncryptionFolderMeta(File folder) throws IOException{
        File dir = new File(".");
        File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(MKFS+".");
            }
        });
        
        //Case handeling for first time creation.
         if(files.length==0){
            JSONObject  firstVersion=new JSONObject();
            firstVersion.put(MKFS, System.currentTimeMillis());
            return firstVersion.toString();
        }
        
        //Find latest file
        Long versions[]=new Long[files.length];
        int counter=0;
        for (File metaFiles : files) {
                System.out.println(metaFiles);
                String name=metaFiles.getName();
                String[] splitText=name.split("\\.");
                if(splitText.length>1){
                    versions[counter]=Long.parseLong(splitText[1]);
                    counter++;
                }
                
            }
        
        long max = Collections.max(Arrays.asList(versions)); 

        return new String(Files.readAllBytes(Paths.get(folder.getAbsolutePath()+"/"+MKFS+"."+max)));
    }
    
    public static String createEncryptionFolderMeta(File folder,HashMap<String,String> newContent) throws IOException{
        String latestData=getEncryptionFolderMeta( folder);
        JSONObject oldData=new JSONObject(latestData);//Latest is old now
        String newTimeStamp=String.valueOf(System.currentTimeMillis());
        oldData.put(MKFS, newTimeStamp);//Change the timeStamp
        for (Map.Entry<String, String> entry : newContent.entrySet()) {
            System.out.println("Key = " + entry.getKey()
                    + ", Value = " + entry.getValue());
            
            oldData.put(entry.getKey(), entry.getValue());
        }
        String newVersion=folder.getAbsolutePath()+"/"+MKFS+"."+newTimeStamp;
        System.out.println("Saving meta>>"+newVersion);
        File file = new File(newVersion);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(oldData.toString());
        fileWriter.flush();
        fileWriter.close();
        return newVersion;
    }
    
    public static String encryptString(String raw, String key) {

        StringBuilder finalString = new StringBuilder();

        /*append character to final string from the two strings */
        int i = 0, j = 0;
        while (i < raw.length() && j < key.length()) {

            finalString.append(raw.charAt(i++));
            finalString.append(key.charAt(j++));
        }

        /* check if both the strings are traversed and 
           if not then append remainder of that string  
           to the final string */
        if (i != raw.length()) {
           // finalString.append(raw.substring(i));
            j=0;
            while (i < raw.length() && j < key.length()) {

            finalString.append(raw.charAt(i++));
            finalString.append(key.charAt(j++));
        }
            
        }
        if (j != key.length()) { 
            finalString.append(key.substring(j)); 
        }
        
        return finalString.toString();

    }

    public static String askForPasswordSwing() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter a password:");
        JPasswordField pass = new JPasswordField(10);
        panel.add(label);
        panel.add(pass);
        String[] options = new String[]{"OK", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, panel, "The title",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[1]);
        if (option == 0) // pressing OK button
        {
            char[] password = pass.getPassword();
            System.out.println("Your password is: " + new String(password));
            return new String(password);
        }
        return null;
    }
    public static String askForPassword() {
        
        try {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("16 Byte key");
            dialog.setHeaderText("Give 16 Byte key ");
            dialog.setGraphic(new Circle(15, Color.RED)); // Custom graphic
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            PasswordField pwd = new PasswordField();
            HBox content = new HBox();
            content.setAlignment(Pos.CENTER_LEFT);
            content.setSpacing(10);
            content.getChildren().addAll(new Label("Enter Key:"), pwd);
            dialog.getDialogPane().setContent(content);
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    return pwd.getText();
                }
                return null;
            });

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                System.out.println(result.get());

                return result.get();
            }

        } catch (Exception ex) {

        }
        return null;
    }
    
    public static int doInteractiveEncryption() throws IOException{
        //Choose the files to be encrypted
        List<File> filesToEncrypt=chooseFiles();
        //Choose a passcode for encryption
        String passKey=askForPasswordSwing();//askForPassword();
        //Choose a location to save
        File folderToSave= chooseFolder();
        int counter=0;
        HashMap<String,String> fileNameMapping=new HashMap<>();
        
        //Perform encryption operation
        for(File file:filesToEncrypt){
            String newFileName=String.valueOf(System.nanoTime());
            String encrypted=encryptString(file.getName(), passKey);
            fileNameMapping.put(newFileName, encrypted);
            File encryptedFile = new File(folderToSave.getAbsolutePath()+"/"+newFileName);
            System.out.println("Saving>>"+encryptedFile.getAbsolutePath());
            try {
                CryptoUtils.encrypt(passKey, file, encryptedFile);
            } catch (CryptoException ex) {
                Logger.getLogger(EncryptionBatchProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        counter++;
        }
        
        //Create meta file
        createEncryptionFolderMeta(folderToSave,fileNameMapping);

        //Return number of files encrypted.
        return counter;
    }

    public static void main(String[] args) {
        
        try {
            doInteractiveEncryption();
        } catch (IOException ex) {
            Logger.getLogger(EncryptionBatchProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        String key = "1234567893695248";
        File inputFile = new File("Downloads\\TestEncrypt.mp4");
        File encryptedFile = new File("Downloads\\document.encrypted");
        File decryptedFile = new File("Downloads\\document.decrypted");

        try {
            CryptoUtils.encrypt(key, inputFile, encryptedFile);
            CryptoUtils.decrypt(key, encryptedFile, decryptedFile);
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

}
