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
import org.json.JSONObject;

/**
 *
 * @author mkfs
 */
public class EncryptionBatchProcess {

    public static String MKFS = "mkfs";
    public static String STORE = "STORE";
    
    public static boolean checkEncryptionFolder(File folder) {

        File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(MKFS + ".");
            }
        });

        if (files.length > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Manages the folder structure.
     * @param folder
     * @return 
     */
    public static File createEncryptionFolder(File folder) {

        File file = folder;
        File[] directories = file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {

                return new File(current, name).isDirectory() && name.startsWith(STORE + ".");
            }
        });

        //Find latest file
        Long versions[] = new Long[directories.length];
        int counter = 0;
        for (File metaFiles : directories) {
            //System.out.println(metaFiles);
            String name = metaFiles.getName();
            String[] splitText = name.split("\\.");
            if (splitText.length > 1) {
                versions[counter] = Long.parseLong(splitText[1]);
                counter++;
            }

        }

        long max = Collections.max(Arrays.asList(versions));

        File dir = new File(folder.getAbsolutePath() + "/" + STORE + "." + (1 + max));

        // attempt to create the directory here
        boolean successful = dir.mkdir();
        if (successful) {
            // creating the directory succeeded
            System.out.println("directory was created successfully");
            return dir;
        } else {
            // creating the directory failed
            System.out.println("failed trying to create the directory");
            return null;
        }

        //System.out.println(Arrays.toString(directories));
    }
    
/**
 * 
 * @param folder
 * @return
 * @throws IOException 
 */
    
    
    
    public static String getEncryptionFolderMeta(File folder) throws IOException {
        File dir = new File(".");
        File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(MKFS + ".");
            }
        });

        //Case handeling for first time creation.
        if (files.length == 0) {
            JSONObject firstVersion = new JSONObject();
            firstVersion.put(MKFS, System.currentTimeMillis());
            return firstVersion.toString();
        }

        //Find latest file
        Long versions[] = new Long[files.length];
        int counter = 0;
        for (File metaFiles : files) {
            //System.out.println(metaFiles);
            String name = metaFiles.getName();
            String[] splitText = name.split("\\.");
            if (splitText.length > 1) {
                versions[counter] = Long.parseLong(splitText[1]);
                counter++;
            }

        }

        long max = Collections.max(Arrays.asList(versions));

        return new String(Files.readAllBytes(Paths.get(folder.getAbsolutePath() + "/" + MKFS + "." + max)));
    }
/**
 * 
 * @param folder
 * @param newContent
 * @return
 * @throws IOException 
 */
    public static String createEncryptionFolderMeta(File folder, HashMap<String, String> newContent) throws IOException {
        String latestData = getEncryptionFolderMeta(folder);
        JSONObject oldData = new JSONObject(latestData);//Latest is old now
        String newTimeStamp = String.valueOf(System.currentTimeMillis());
        oldData.put(MKFS, newTimeStamp);//Change the timeStamp
        for (Map.Entry<String, String> entry : newContent.entrySet()) {
            System.out.println("Key = " + entry.getKey()
                    + ", Value = " + entry.getValue());

            oldData.put(entry.getKey(), entry.getValue());
        }
        String newVersion = folder.getAbsolutePath() + "/" + MKFS + "." + newTimeStamp;
        System.out.println("Saving meta>>" + newVersion);
        File file = new File(newVersion);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(oldData.toString());
        fileWriter.flush();
        fileWriter.close();
        return newVersion;
    }
    
    public static String createEncryptionFolderMeta(File folder, String contentTag, HashMap<String, String> newContent) throws IOException {
        String latestData = getEncryptionFolderMeta(folder);
        JSONObject oldData = new JSONObject(latestData);//Latest is old now
        String newTimeStamp = String.valueOf(System.currentTimeMillis());
        
        if(contentTag==null){
            return createEncryptionFolderMeta( folder, newContent);
        }
        
        if (oldData.has(contentTag)) {

            for (Map.Entry<String, String> entry : newContent.entrySet()) {
                System.out.println("Key = " + entry.getKey()
                        + ", Value = " + entry.getValue());

                oldData.getJSONObject(contentTag).put(entry.getKey(), entry.getValue());
            }

        } else {
            oldData.put(contentTag, new JSONObject());

            for (Map.Entry<String, String> entry : newContent.entrySet()) {
                System.out.println("Key = " + entry.getKey()
                        + ", Value = " + entry.getValue());

                oldData.getJSONObject(contentTag).put(entry.getKey(), entry.getValue());
            }
        }
        oldData.put(MKFS, newTimeStamp);//Change the timeStamp
        
        String newVersion = folder.getAbsolutePath() + "/" + MKFS + "." + newTimeStamp;
        System.out.println("Saving meta>>" + newVersion);
        File file = new File(newVersion);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(oldData.toString());
        fileWriter.flush();
        fileWriter.close();
        return newVersion;
    }
/**
 * 
 * @param raw
 * @param key
 * @return 
 */
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
            j = 0;
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
    
    public static String decryptString(String encryptedString) {

        StringBuilder finalString = new StringBuilder();

        /*append character to final string from the two strings */
        int i = 0, j = 0;
        while (i < encryptedString.length() ) {

            finalString.append(encryptedString.charAt(i++));
            
        }

        /* check if both the strings are traversed and 
           if not then append remainder of that string  
           to the final string */
        if (i != encryptedString.length()) {
            // finalString.append(raw.substring(i));
            j = 0;
            while (i < encryptedString.length() ) {

                finalString.append(encryptedString.charAt(i++));
               
            }

        }


        return finalString.toString();

    }
/**
 * 
 * @return 
 */
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
/**
 * 
 * @return 
 */
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
/**
 * 
 * @return
 * @throws IOException 
 */
    public static int doInteractiveEncryption() throws IOException {
        int input = JOptionPane.showConfirmDialog(null, "Pick Yes for file encryption & No for all files in a folder.");
        // 0=yes, 1=no, 2=cancel
        System.out.println(input);
        
        //Choose the files to be encrypted
        List<File> filesToEncrypt = new ArrayList<>();
        if(input==0){
            filesToEncrypt = FileReadOperations.chooseFiles();
        }
        
        if(input==1){
            filesToEncrypt = FileReadOperations.chooseAllFilesInFolder();
        }
        
        if(input==2){
            return 0;
        }
        //Choose a passcode for encryption
        String passKey = askForPasswordSwing();//askForPassword();
        //Choose a location to save
        File folderToSave = FileReadOperations.chooseFolder();
        if(input==1){ //Do folder management by itself
            folderToSave=createEncryptionFolder(folderToSave);
        }
        int counter = 0;
        HashMap<String, String> fileNameMapping = new HashMap<>();

        //Perform encryption operation
        for (File file : filesToEncrypt) {
            String newFileName = String.valueOf(System.nanoTime());
            String encrypted = CryptoUtils.encode(file.getName(), passKey.length());
            fileNameMapping.put(newFileName, encrypted);
            File encryptedFile = new File(folderToSave.getAbsolutePath() + "/" + newFileName);
            System.out.println("Saving>>" + encryptedFile.getAbsolutePath());
            try {
                CryptoUtils.encrypt(passKey, file, encryptedFile);
            } catch (CryptoException ex) {
                Logger.getLogger(EncryptionBatchProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
            counter++;
        }

        //Create meta file
        createEncryptionFolderMeta(folderToSave, fileNameMapping);

        //Return number of files encrypted.
        return counter;
    }
    
    public static int doEncryption(List<File> filesToEncrypt,String passKey,File folderToSave ) throws IOException {
        //Choose the files to be encrypted
       // List<File> filesToEncrypt = chooseFiles();
        //Choose a passcode for encryption
        //String passKey = askForPasswordSwing();//askForPassword();
        //Choose a location to save
        //File folderToSave = chooseFolder();
        int counter = 0;
        HashMap<String, String> fileNameMapping = new HashMap<>();

        //Perform encryption operation
        for (File file : filesToEncrypt) {
            String newFileName = String.valueOf(System.nanoTime());
            String encrypted = CryptoUtils.encode(file.getName(), passKey.length());
            fileNameMapping.put(newFileName, encrypted);
            File encryptedFile = new File(folderToSave.getAbsolutePath() + "/" + newFileName);
            System.out.println("Saving>>" + encryptedFile.getAbsolutePath());
            try {
                CryptoUtils.encrypt(passKey, file, encryptedFile);
            } catch (CryptoException ex) {
                Logger.getLogger(EncryptionBatchProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
            counter++;
        }

        //Create meta file
        createEncryptionFolderMeta(folderToSave, fileNameMapping);

        //Return number of files encrypted.
        return counter;
    }

    
    public static int doEncryption(List<File> filesToEncrypt,String passKey,File folderToSave,String tag ) throws IOException {
        int counter = 0;
        HashMap<String, String> fileNameMapping = new HashMap<>();

        //Perform encryption operation
        for (File file : filesToEncrypt) {
            String newFileName = String.valueOf(System.nanoTime());
            String encrypted = CryptoUtils.encode(file.getName(), passKey.length());
            fileNameMapping.put(newFileName, encrypted);
            File encryptedFile = new File(folderToSave.getAbsolutePath() + "/" + newFileName);
            System.out.println("Saving>>" + encryptedFile.getAbsolutePath());
            try {
                CryptoUtils.encrypt(passKey, file, encryptedFile);
            } catch (CryptoException ex) {
                Logger.getLogger(EncryptionBatchProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
            counter++;
        }

        //Create meta file
        createEncryptionFolderMeta(folderToSave,tag, fileNameMapping);

        //Return number of files encrypted.
        return counter;
    }
    
    
    public static int doEncryption(byte[] inputByte,String passKey,File folderToSave,String name,String tag ) throws IOException {
        int counter = 0;
        HashMap<String, String> fileNameMapping = new HashMap<>();

        //Perform encryption operation
       
            String newFileName = String.valueOf(System.nanoTime());
            String encrypted = CryptoUtils.encode(name, passKey.length());
            fileNameMapping.put(newFileName, encrypted);
            File encryptedFile = new File(folderToSave.getAbsolutePath() + "/" + newFileName);
            System.out.println("Saving>>" + encryptedFile.getAbsolutePath());
            try {
                CryptoUtils.encrypt(passKey, inputByte, encryptedFile);
            } catch (CryptoException ex) {
                Logger.getLogger(EncryptionBatchProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
            counter++;
       

        //Create meta file
        createEncryptionFolderMeta(folderToSave,tag, fileNameMapping);

        //Return number of files encrypted.
        return counter;
    }
    
    public static void main(String[] args) {

        try {
            doInteractiveEncryption();
        } catch (IOException ex) {
            Logger.getLogger(EncryptionBatchProcess.class.getName()).log(Level.SEVERE, null, ex);
        }

//        String key = "1234567893695248";
//        File inputFile = new File("Downloads\\TestEncrypt.mp4");
//        File encryptedFile = new File("Downloads\\document.encrypted");
//        File decryptedFile = new File("Downloads\\document.decrypted");
//
//        try {
//            CryptoUtils.encrypt(key, inputFile, encryptedFile);
//            CryptoUtils.decrypt(key, encryptedFile, decryptedFile);
//        } catch (CryptoException ex) {
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        }
    }

}
