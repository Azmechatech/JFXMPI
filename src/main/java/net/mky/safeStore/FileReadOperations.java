/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.safeStore;

import com.truegeometry.mkhilbertml.HilbertCurvePatternDetect;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import net.mky.image.DuplicateImageDetection;

/**
 *
 * @author mkfs
 */
public class FileReadOperations {

    /**
     * Recursively
     * @param directory
     * @param files 
     */
    public static void listf(File directory, List<File> files) {
    //File directory = new File(directoryName);

    // Get all files from a directory.
    File[] fList = directory.listFiles();
    if(fList != null)
        for (File file : fList) {      
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                listf(file, files);
            }
        }
    }
    /**
     * Files in that folder only
     * @param directory
     * @return 
     */
    public static List<File> listfiles(File directory) {
        //File directory = new File(directoryName);
        List<File> files = new ArrayList<>();
        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if (fList != null) {
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    // listf(file, files);
                }
            }
        }

        return files;
    }

    /**
     * Recursively
     * @param directory
     * @param files 
     */
    
    public static void listfolders(File directory, List<File> files) {
    //File directory = new File(directoryName);

    // Get all files from a directory.
    File[] fList = directory.listFiles();
    if(fList != null)
        for (File file : fList) {      
            if (file.isFile()) {
                //files.add(file);
            } else if (file.isDirectory()) {
                files.add(file);
                listfolders(file, files);
            }
        }
    }

    public static void dirTree(File dir) {
        File[] subdirs = dir.listFiles();
        for (File subdir : subdirs) {
            if (subdir.isDirectory()) {
                dirTree(subdir);
            } else {
                doFile(subdir);
            }
        }
    }

    public static void doFile(File file) {
        System.out.println(file.getAbsolutePath());
    }
    
    /**
 * 
 * @return 
 */
    public static List<File> chooseFiles() {

        try {
            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Select files");

            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

            return selectedFiles;
        } catch (Exception ex) {

            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
             chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            chooser.showOpenDialog(null);
           

            if (chooser.getSelectedFile().isDirectory()) {
                List<File> listOfFiles = new LinkedList<>();
                try {
                    Path dir = chooser.getSelectedFile().toPath();
                    Files.find(dir, 10, (path, attributes)
                            -> path.getFileName().toString().toLowerCase().endsWith("")) // select all
                            .forEach(filePath -> {
                                listOfFiles.add(filePath.toFile());
                            });
                } catch (IOException ex1) {
                    Logger.getLogger(FileReadOperations.class.getName()).log(Level.SEVERE, null, ex1);
                }
                return listOfFiles;
            }

            File[] files = chooser.getSelectedFiles();
            return Arrays.stream(files).collect(Collectors.toList());

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
    /**
     * Read UTF8 encoding
     * @param file
     * @return
     * @throws IOException 
     */
    public static StringBuilder readFileAsString(File file) throws IOException {
        StringBuilder sb=new StringBuilder();
      //  final BufferedReader br = new BufferedReader(new FileReader(file));
        
        BufferedReader br = new BufferedReader(
		   new InputStreamReader(
                      new FileInputStream(file), "UTF8"));
        try {
            String strLine;
            while ((strLine = br.readLine()) != null) {
                sb.append(strLine);
            }
            return sb;
        } finally {
            br.close();
        }
    }
    
/**
 * 
 * @return 
 */
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
    
    
    public static List<File> chooseAllFilesInFolder() {
        String choosertitle = "Select folder location";
        List<File> listOfFiles=new ArrayList<>();
        
        try {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle(choosertitle);
            //File defaultDirectory = new File("c:/dev/javafx");
            //chooser.setInitialDirectory(defaultDirectory);
            listf(chooser.showDialog(null), listOfFiles);
            return listOfFiles;
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

                listf(chooser.getSelectedFile(), listOfFiles);
                return listOfFiles;
            } else {
                System.out.println("No Selection ");
            }
        }
        return null;

    }

    
    public static List<File> chooseAllFoldersInFolder(boolean includeSelf) {
        String choosertitle = "Select folder location";
        List<File> listOfFiles=new ArrayList<>();
        
        try {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle(choosertitle);
            //File defaultDirectory = new File("c:/dev/javafx");
            //chooser.setInitialDirectory(defaultDirectory);
            listfolders(chooser.showDialog(null), listOfFiles);
            return listOfFiles;
        } catch (Exception ex) {
            JFileChooser chooser;
            chooser = new JFileChooser();
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle(choosertitle);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setMultiSelectionEnabled(true);
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

                for (File folder : chooser.getSelectedFiles()) {
                    if (includeSelf) {
                        listOfFiles.add(folder);
                    }
                    listfolders(folder, listOfFiles);
                }
                
                return listOfFiles;
            } else {
                System.out.println("No Selection ");
            }
        }
        return null;

    }
}
