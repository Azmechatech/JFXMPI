/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.image;

import com.truegeometry.mkhilbertml.HilbertCurvePatternDetect;
import java.awt.Graphics2D;
import static java.awt.Image.SCALE_SMOOTH;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import static net.mky.image.DuplicateImageDetection.checkIfFileHasExtension;
import static net.mky.image.DuplicateImageDetection.createImageViewV2;
import net.mky.jfxmpi.WebsiteBrowser;
import net.mky.tools.StylesForAll;

/**
 *
 * @author mkfs
 */
public class DuplicateImageDetection extends Application {

    /**
     * 1. Read all files in a folder, recursively 2. Read on by one and find
     * mkHilbert signature 3. Load into a set 4. Flag duplicates
     */
    public static List<File> doInteractive() throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File fileDir = directoryChooser.showDialog(null);
        List<File> duplicates = new LinkedList<>();
        HashMap<long[][], File> signature = new HashMap<>();

        //File fileDir = directoryChooser.showDialog(mainStage);
        if (fileDir != null) { // if the user chose something:
            Path dir = fileDir.toPath();
            int depth = 10; // maximum depth to search, use Integer.MAX_VALUE to search everything
            Files.find(dir, depth, (path, attributes)
                    -> path.getFileName().toString().toLowerCase().endsWith(".mp3")) // select only mp3 files
                    .forEach(fileName -> {
                        try {
                            long[][] hilbertSignature = HilbertCurvePatternDetect.image2HC(ImageIO.read(fileName.toFile()), 32);
                            if (signature.containsKey(hilbertSignature)) {
                                duplicates.add(fileName.toFile());
                            } else {
                                signature.put(hilbertSignature, fileName.toFile());
                            }

                        } catch (IOException ex) {
                            Logger.getLogger(DuplicateImageDetection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
        }

        System.out.println("Duplicate files " + duplicates.size());
        System.out.println("================");
        duplicates.stream().forEach(file -> {
            System.out.println(file.getAbsolutePath());
        });

        return duplicates;
    }

    public static void main(String... args) throws IOException {
 launch(args);
       
    }

    
    Stage stage;
    @Override
    public void start(Stage primaryStage)  {
         stage = primaryStage;
        
        ScrollPane root = new ScrollPane();
        TilePane tile = new TilePane();
        Button deleteDuplicates=new Button("Delete Duplicates");
        root.setStyle("-fx-background-color: DAE6F3;");
        tile.setPadding(new Insets(15, 15, 15, 15));
        tile.setHgap(15);
        Button startButton = new Button("Select Directory");
        
        Label Status=new Label("Image indexing");
       

        
        HashMap<File,Integer> duplicates = new HashMap<>();
        HashSet<File> toBeDeleted=new HashSet<>();
        HashMap<Integer, File> signature = new HashMap<>();
        
        startButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
        File fileDir = directoryChooser.showDialog(null);
        
        
            //File fileDir = directoryChooser.showDialog(mainStage);
            duplicates.clear();
            toBeDeleted.clear();
            signature.clear();
            if (fileDir != null) { // if the user chose something:
                FileIndesingTask fileIndesingTask = new FileIndesingTask(signature, duplicates, toBeDeleted, Status, tile, stage, fileDir.toPath());
                new Thread(fileIndesingTask).start();
            }
        
            ////PROGRESS BAR
//            ProgressForm pForm = new ProgressForm();
//
//            // In real life this task would do something useful and return 
//            // some meaningful result:
//            Task<Void> task = new Task<Void>() {
//                @Override
//                public Void call() throws InterruptedException {
//                   //updateProgress(toBeDeleted.size(), signature.size()+toBeDeleted.size());
//                    return null;
//                }
//            };
//
//            // binds progress of progress bars to progress of task:
//            pForm.activateProgressBar(task);
//
//            // in real life this method would get the result of the task
//            // and update the UI based on its value:
//            task.setOnSucceeded(event -> {
//                pForm.getDialogStage().close();
//
//            });
//
//            pForm.getDialogStage().show();
//
//            Thread thread = new Thread(task);
//            thread.start();

        });

        
        Button deleteDuplicateButton = new Button("Try Deleting");
        deleteDuplicateButton.setOnAction(e -> {
            System.out.println("Duplicate files " + duplicates.size());
            System.out.println("================");
            StringBuilder exceptionText = new StringBuilder();
            duplicates.keySet().stream().forEach(file -> {
                System.out.println(file.getAbsolutePath());
                exceptionText.append(file.getAbsolutePath()).append("\n");
            });

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Look, a Confirmation Dialog");
            alert.setContentText("Are you ok with this?");

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
            alert.getDialogPane().setExpandableContent(expContent);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                toBeDeleted.stream().forEach(fileSig -> {
                    System.out.println("Deleted" + fileSig.getAbsolutePath());
                    File file = fileSig;

                    if (file.delete()) {
                        System.out.println("File deleted successfully");
                    } else {
                        System.out.println("Failed to delete the file");
                    }
                });
            } else {
                // ... user chose CANCEL or closed the dialog
            }

        });
        
        Button previewDeleteDuplicateButton = new Button("Preview results");
        previewDeleteDuplicateButton.setOnAction(e -> {
            tile.getChildren().clear();//Clear window
            duplicates.entrySet().stream().forEach(fileSig -> {
                ImageView imageView;
                imageView = createImageViewV2(fileSig.getKey(),stage);
                tile.getChildren().addAll(imageView);

                ImageView imageView2;
                imageView2 = createImageViewV2(signature.get(fileSig.getValue()),stage);
                tile.getChildren().addAll(imageView2);
            });

      
        });

       

        root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
        root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
        root.setFitToWidth(true);
        root.setContent(tile);

        startButton.setStyle(StylesForAll.transparentAlive);
        deleteDuplicateButton.setStyle(StylesForAll.transparentAlive);
        previewDeleteDuplicateButton.setStyle(StylesForAll.transparentAlive);
        Status.setStyle(StylesForAll.transparentAlive);
        
        HBox buttonBox=new HBox(startButton,previewDeleteDuplicateButton,deleteDuplicateButton);
        HBox statusBox=new HBox(Status);
        VBox vbox = new VBox(buttonBox,statusBox, root);
        vbox.setStyle("-fx-background-color: linear-gradient(to right, rgb(203,53,107,0.25), rgb(189,63,50,0.25));");

        primaryStage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        primaryStage.setHeight(Screen.getPrimary().getVisualBounds()
                .getHeight());
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();

        
    }
    
     private ImageView createImageView(final File imageFile) {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower)
        // resizing

        ImageView imageView = null;
        try {
            final Image image = new Image(new FileInputStream(imageFile), 150, 0, true,
                    true);
            imageView = new ImageView(image);
            imageView.setFitWidth(150);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent mouseEvent) {

                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){

                        if(mouseEvent.getClickCount() == 2){
                            try {
                                BorderPane borderPane = new BorderPane();
                                ImageView imageView = new ImageView();
                                Image image = new Image(new FileInputStream(imageFile));
                                imageView.setImage(image);
                                imageView.setStyle("-fx-background-color: BLACK");
                                imageView.setFitHeight(stage.getHeight() - 10);
                                imageView.setPreserveRatio(true);
                                imageView.setSmooth(true);
                                imageView.setCache(true);
                                borderPane.setCenter(imageView);
                                borderPane.setStyle("-fx-background-color: BLACK");
                                Stage newStage = new Stage();
                                newStage.setWidth(stage.getWidth());
                                newStage.setHeight(stage.getHeight());
                                newStage.setTitle(imageFile.getName());
                                Scene scene = new Scene(borderPane,Color.BLACK);
                                newStage.setScene(scene);
                                newStage.show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            });
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return imageView;
    }
    
     
   
   public static ImageView createImageViewV2(final File imageFile, Stage stage) {
        // DEFAULT_THUMBNAIL_WIDTH is a constant you need to define
        // The last two arguments are: preserveRatio, and use smooth (slower)
        // resizing

        ImageView imageView = null;
        try {
            BufferedImage buffImage = ImageIO.read(imageFile);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(buffImage, "jpeg", os);                          // Passing: ​(RenderedImage im, String formatName, OutputStream output)
            InputStream is = new ByteArrayInputStream(os.toByteArray());

            final Image image = new Image(is, 150, 0, true,true);
            imageView = new ImageView(image);
            imageView.setFitWidth(150);
            imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent mouseEvent) {

                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){

                        if(mouseEvent.getClickCount() == 2){
                            try {
                                BorderPane borderPane = new BorderPane();
                                ImageView imageView = new ImageView();
                                Image image = new Image(new FileInputStream(imageFile));
                                imageView.setImage(image);
                                imageView.setStyle("-fx-background-color: BLACK");
                                imageView.setFitHeight(stage.getHeight() - 10);
                                imageView.setPreserveRatio(true);
                                imageView.setSmooth(true);
                                imageView.setCache(true);
                                borderPane.setCenter(imageView);
                                borderPane.setStyle("-fx-background-color: BLACK");
                                Stage newStage = new Stage();
                                newStage.setWidth(stage.getWidth());
                                newStage.setHeight(stage.getHeight());
                                newStage.setTitle(imageFile.getName());
                                Scene scene = new Scene(borderPane,Color.BLACK);
                                newStage.setScene(scene);
                                newStage.show();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            });
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(DuplicateImageDetection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return imageView;
    }
    
     
      
    public static boolean checkIfFileHasExtension(String s, String[] extn) {
    return Arrays.stream(extn).anyMatch(entry -> s.endsWith(entry));
}
}

class FileIndesingTask extends Task {

    Label stateLabel = new Label();
     HashMap<File,Integer> duplicates = new HashMap<>();
        HashSet<File> toBeDeleted=new HashSet<>();
        HashMap<Integer, File> signature = new HashMap<>();
        TilePane tile;
        Stage stage;
        Path dir;

    FileIndesingTask(HashMap<Integer, File> signature,HashMap<File,Integer> duplicates,HashSet<File> toBeDeleted,Label stateLabel,TilePane tile,Stage stage,Path dir) {
        this.signature=signature;
        this.duplicates=duplicates;
        this.toBeDeleted=toBeDeleted;
        this.stateLabel = stateLabel;
        this.tile=tile;
        this.stage=stage;
        this.dir=dir;
        
        // initialize
    }

    @Override
    protected Object call() throws Exception {
        
                    
                 
                       // Path dir = fileDir.toPath();
                        int depth = 10; // maximum depth to search, use Integer.MAX_VALUE to search everything
                        try {
                            Files.find(dir, depth, (path, attributes)
                                    -> checkIfFileHasExtension(path.getFileName().toString().toLowerCase(), new String[]{".jpg", ".jpeg", ".png"})) // select only mp3 files
                                    .forEach(fileName -> {
                                        try {
                                            long[][] hilbertSignature = HilbertCurvePatternDetect.image2HC(ImageIO.read(fileName.toFile()), 32);
                                            int hash = java.util.Arrays.deepHashCode(hilbertSignature);
                                            if (signature.containsKey(hash)) {//Found duplicate
                                                duplicates.put(fileName.toFile(), hash);
                                                System.out.println("Duplicate files " + duplicates.size());
                                                toBeDeleted.add(fileName.toFile().lastModified() < signature.get(hash).lastModified() ? fileName.toFile() : signature.get(hash));
                                                
                                                ImageView imageView;
                                                imageView = createImageViewV2(signature.get(hash),stage);
                                                tile.getChildren().addAll(imageView);

                                                ImageView imageView2;
                                                imageView2 = createImageViewV2(signature.get(fileName),stage);
                                                tile.getChildren().addAll(imageView2);
                
                                                
                                            } else {
                                                signature.put(hash, fileName.toFile());
                                            }

                                            
                                            
                                            Platform.runLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    stateLabel.setText(toBeDeleted.size() + " Duplicates in " + signature.size() + " images.");;

                                                }
                                            });
                                           
                                        } catch (Exception ex) {
                                            Logger.getLogger(DuplicateImageDetection.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    });
                        } catch (IOException ex) {
                            Logger.getLogger(DuplicateImageDetection.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    
        return null;
    }
}
