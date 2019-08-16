/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.safeStore;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javax.crypto.Cipher;
import javax.imageio.ImageIO;
import static net.mky.safeStore.EncryptionBatchProcess.getEncryptionFolderMeta;
import org.json.JSONObject;
import org.rapidoid.crypto.Crypto;

public class DirectoryViewer extends Application {

    Map<String, String> MKFSData = new HashMap<>();
    TreeView<String> a = new TreeView<String>();
    String key="";
    @Override
    public void start(Stage primaryStage) {
        
        // create a text input dialog 
        TextInputDialog td = new TextInputDialog("enter any text"); 
  
        // setHeaderText 
        td.setHeaderText("enter your name"); 
        td.showAndWait();
        key=td.getEditor().getText();

        EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
            handleMouseClicked(event);
        };

        a.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventHandle);

        BorderPane b = new BorderPane();
        Button c = new Button("Load Folder");
        c.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                DirectoryChooser dc = new DirectoryChooser();
                dc.setInitialDirectory(new File(System.getProperty("user.home")));
                File choice = dc.showDialog(primaryStage);
                if (choice == null || !choice.isDirectory()) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setHeaderText("Could not open directory");
                    alert.setContentText("The file is invalid.");

                    alert.showAndWait();
                } else {
                    a.setRoot(getNodesForDirectory(choice, false));
                }
            }
        });
        b.setTop(c);
        b.setCenter(a);
        primaryStage.setScene(new Scene(b, 600, 400));
        primaryStage.setTitle("Folder View");
        primaryStage.show();
    }

    private void handleMouseClicked(MouseEvent event) {
        Node node
                = event.getPickResult().getIntersectedNode();
        // Accept clicks only on node cells, and not on empty spaces of the TreeView
        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
            String name = (String) ((TreeItem) a.getSelectionModel().getSelectedItem()).getValue();
            System.out.println("Node click: " + MKFSData.get(name));
            System.out.println(key);
            try {
                //byte[] data = CryptoUtils.doCrypto(Cipher.DECRYPT_MODE, key, Files.readAllBytes(Paths.get( MKFSData.get(name))));
                File outputFile = new File("temp");
                CryptoUtils.decrypt(key, new File(MKFSData.get(name)), outputFile);

                Image image = new Image(new FileInputStream(outputFile.getAbsolutePath()), 200, 300, true, true);
                ImageView imageView = new ImageView(image);
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error", ButtonType.OK);
                alert.setGraphic(imageView);
                alert.showAndWait();

            } catch (CryptoException ex) {
                Logger.getLogger(DirectoryViewer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DirectoryViewer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
   

    public TreeItem<String> getNodesForDirectory(File directory, boolean isMKFSFolder) { //Returns a TreeItem representation of the specified directory
        TreeItem<String> root = new TreeItem<String>(directory.getName());
        for (File f : directory.listFiles()) {

            if (isMKFSFolder | f.getName().contains(EncryptionBatchProcess.DUMP) | f.getName().contains(EncryptionBatchProcess.STORE)
                    | f.getParentFile().getName().contains(EncryptionBatchProcess.DUMP)
                    | f.getParentFile().getName().contains(EncryptionBatchProcess.STORE)
                    | f.getName().contains(EncryptionBatchProcess.MKFS)) {

                if (f.isDirectory()) { //Then we call the function recursively
                    root.getChildren().add(new TreeItem<String>(f.getAbsolutePath()));
                    // root.getChildren().add(getNodesForDirectory(f, true));
                } else {
                    if (f.getName().contains(EncryptionBatchProcess.MKFS)) {
                        root.getChildren().add(new TreeItem<String>(f.getAbsolutePath()));
                        String latestData;
                        try {
                            latestData = getEncryptionFolderMeta(f.getParentFile());
                            JSONObject oldData = new JSONObject(latestData);
                            for (String key : oldData.keySet()) {
                                root.getChildren().add(new TreeItem<String>(CryptoUtils.decode(oldData.getString(key), 16)));

                                MKFSData.put(CryptoUtils.decode(oldData.getString(key), 16), f.getParentFile().getAbsolutePath() + "/" + key);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(DirectoryViewer.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }
            }
        }
        return root;
    }

    public static void main(String... args) {
        launch(args);
    }
}
