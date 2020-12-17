/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi.TimeLineView;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import net.mky.image.ImageProcessingHelper;
import net.mky.image.VideoHelper;
import static net.mky.jfxmpi.MainApp.awtImageToFX;
import static net.mky.jfxmpi.MainApp.getImageFromClipboard;
import net.mky.safeStore.MapDB;
import net.mky.tools.StylesForAll;
import net.mky.tools.ZipHelper;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 *
 * @author mkfs
 */
public class TimeLineStory extends VBox {

    private String conversationPartner;
    private int prefHeight, prefWidth;
    public ObservableList<Node> speechBubbles = FXCollections.observableArrayList();
    FileChooser fileChooser = new FileChooser();
    DirectoryChooser dirChooser = new DirectoryChooser();
    String activeChatFile="";
    public File activeFolder=new File("");

    private Label contactHeader;
    public ScrollPane messageScroller;
    private VBox messageContainer;
    private HBox inputContainer;
    private HBox contactHeaderBar;
     ComboBox<SpeechBox.SpeechTheme> cbxStatus;
    
    public final HashMap<String,String> userPics=new HashMap<>();
    
    //Time
    public String dateTime="Today";

    public TimeLineStory(String conversationPartner, int prefHeight, int prefWidth) {
        super(5);
        this.conversationPartner = conversationPartner;
        this.prefHeight = prefHeight;
        this.prefWidth = prefWidth;
        setStyle("-fx-background-color: transparent;");
        setupElements();
    }
    
    public TimeLineStory(File chapter, String conversationPartner, int prefHeight, int prefWidth, MapDB mapdb) {
        super(5);
        this.conversationPartner = conversationPartner;
        this.prefHeight = prefHeight;
        this.prefWidth = prefWidth;
        setStyle("-fx-background-color: transparent;");
        setupElements();
        activeFolder = chapter.getParentFile();

        activeChatFile = chapter.getName();
        
        load(chapter, mapdb);
    }
    

    private void setupElements() {
        setupContactHeader();
        setupMessageDisplay();
        setupInputDisplay();
        getChildren().setAll(contactHeaderBar, messageScroller, inputContainer);
        setPadding(new Insets(5));
    }

    private void setupContactHeader() {
        contactHeaderBar = new HBox(5);
        contactHeaderBar.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        
        cbxStatus = new ComboBox<>();
        cbxStatus.setItems( FXCollections.observableArrayList( SpeechBox.SpeechTheme.values()));
        cbxStatus.setValue(SpeechBox.SpeechTheme.NEUTRAL);

         TextField userInput = new TextField();
        userInput.setPromptText("...");
        userInput.setPrefWidth(450);//Widht control
        
        //Save and load
        //For testing purposes
        Button saveChat = new Button("Save");
        saveChat.setStyle(StylesForAll.transparentAlive);
        saveChat.setOnAction(event -> {
            
            //Get file Prefix
             // create a text input dialog 
            if (activeChatFile.contentEquals("")) {
                TextInputDialog td = new TextInputDialog("Chat file name");

                // setHeaderText 
                td.setHeaderText("Chat file name");
                td.getEditor().setText(activeChatFile);
                td.showAndWait();
                activeChatFile = td.getEditor().getText();
            }
                       
            //Write JSON file
            // try (FileWriter file = new FileWriter(dir.getAbsolutePath()+"/Chat_"+System.currentTimeMillis()+".json")) {
            Writer fstream = null;
            BufferedWriter out = null;
            String dataToWrite=getChapterData().toString();
            String fileToWrite="";
            try {
                if (activeChatFile.contentEquals("")) {
                    File dir = dirChooser.showDialog(null);
                    fileToWrite=dir.getAbsolutePath() + "/Chat_" + activeChatFile + System.currentTimeMillis() + ".json"+".zip";
                    //fstream = new OutputStreamWriter(new FileOutputStream(fileToWrite), StandardCharsets.UTF_8);
                } else {
                     fileToWrite=activeFolder.getAbsolutePath()+"/"+activeChatFile+".zip";
                    //fstream = new OutputStreamWriter(new FileOutputStream(activeFolder.getAbsolutePath()+"/"+activeChatFile+".zip"), StandardCharsets.UTF_8);
                }
                //fstream.write(new String(ZipHelper.compress(dataToWrite)));
                //fstream.flush();
                ZipHelper.writeToFile(dataToWrite, new File(fileToWrite));

                System.out.println("Saved the file");
                Alert a = new Alert(AlertType.CONFIRMATION);
                a.setContentText("Saved the file"); 
                a.show(); 

            } catch (Exception e) {
                 Writer fstreamf = null;
                 fileToWrite=activeFolder.getAbsolutePath() + "/Chat_" + activeChatFile + System.currentTimeMillis() + ".json"+".tmp";
                try {
                    fstream = new OutputStreamWriter(new FileOutputStream(fileToWrite), StandardCharsets.UTF_8);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(TimeLineStory.class.getName()).log(Level.SEVERE, null, ex);
                }
              
                Alert a = new Alert(AlertType.CONFIRMATION);
                a.setContentText("Failde to save the file"); 
                a.show(); 
                e.printStackTrace();
            }

        });

        Button loadChat = new Button("Load");
        loadChat.setStyle(StylesForAll.transparentAlive);
        loadChat.setOnAction(event -> {
            File selectedFile = fileChooser.showOpenDialog(null);
            activeChatFile=selectedFile.getName();
            //Read all the file and conver to json
            //Render sppech boxes.
            try {
                //String content = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(new FileInputStream(selectedFile), "UTF8"));
                String str;
                StringBuilder contentSb = new StringBuilder();
                while ((str = in.readLine()) != null) {
                    System.out.println(str);
                    contentSb.append(str);

                }

                in.close();
                
                
                JSONObject metaData = new JSONObject(contentSb.toString());
                JSONArray chat_data = metaData.getJSONArray("chat_data");

                for (int i = 0; i < chat_data.length(); i++) {
                    if (chat_data.getJSONObject(i).has("base64Image")) {
                        speechBubbles.add(new SpeechBox(chat_data.getJSONObject(i).getString("message"), chat_data.getJSONObject(i).getString("base64Image"), SpeechBox.SpeechDirection.valueOf(chat_data.getJSONObject(i).getString("direction")),i));

                    } else {
                        speechBubbles.add(new SpeechBox(chat_data.getJSONObject(i).getString("message"), SpeechBox.SpeechDirection.valueOf(chat_data.getJSONObject(i).getString("direction"))));
                    }
                }

            } catch (Exception ex) {
            }

        });

        Button loadGameImages = new Button("Game Images");
        loadGameImages.setStyle(StylesForAll.transparentAlive);
        loadGameImages.setOnAction(event -> {
            List<File> imageList = fileChooser.showOpenMultipleDialog(null);
            //Read all the file and conver to json
            //Render sppech boxes.
            try {
                int counter=0;
                for (File file : imageList) {

                    speechBubbles.add(new SpeechBox("What happened next?", getImageB64From(file), SpeechBox.SpeechDirection.CENTER,counter++));

                }

            } catch (Exception ex) {
            }

        });
        
        //Clipboard image
        Button bnPaste = new Button("Ctrl+V");
        bnPaste.setStyle(StylesForAll.transparentAlive);
        bnPaste.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    java.awt.Image image = getImageFromClipboard();
                    if (image != null) {
                        javafx.scene.image.Image fimage = awtImageToFX(image);
                        /**/
                        
                        
                        //pe.imageView.setFitHeight(scene.getHeight());
                        // pe.imageView.setFitWidth(scene.getWidth());
                        String b64Image = getImageB64From(fimage);
                        SpeechBox sb=new SpeechBox(userInput.getText(), b64Image, SpeechBox.SpeechDirection.CENTER,cbxStatus.getValue());
                        speechBubbles.add(sb);
                        userInput.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        
          //Clipboard image
        Button vidExtract = new Button("Vid Extract");
        vidExtract.setStyle(StylesForAll.transparentAlive);
        vidExtract.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    File selectedFile = fileChooser.showOpenDialog(null);
                    List<BufferedImage> listOfImgs = VideoHelper.getUniqueImages(selectedFile.getAbsolutePath(), .2f);

                    for (BufferedImage bimg : listOfImgs) {
                        final ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ImageIO.write(bimg, "png", os);
                        byte pgnBytes[] = os.toByteArray();
                        Base64.Encoder base64_enc = Base64.getEncoder();
                        //return base64_enc.encodeToString(pgnBytes);

                        speechBubbles.add(new SpeechBox("What happened next?", base64_enc.encodeToString(pgnBytes), SpeechBox.SpeechDirection.CENTER, cbxStatus.getValue()));

                    }

                } catch (Exception e) {
                    //e.printStackTrace();
                }
            }
        });

        contactHeader = new Label(conversationPartner);
        contactHeader.setAlignment(Pos.CENTER);
        contactHeader.setFont(Font.font("Comic Sans MS", 14));
        contactHeaderBar.getChildren().add(contactHeader);
        contactHeaderBar.getChildren().add(loadGameImages);
        contactHeaderBar.getChildren().add(vidExtract);
        contactHeaderBar.getChildren().add(saveChat);
        contactHeaderBar.getChildren().add(loadChat);
        contactHeaderBar.getChildren().add(userInput);
        contactHeaderBar.getChildren().add(bnPaste);
        contactHeaderBar.getChildren().add(cbxStatus);
    }
    
    public static SpeechBox previewHelper(File selectedFile,MapDB mapdb){
         SpeechBox sb=new SpeechBox("No chapters here.", SpeechBox.SpeechDirection.CENTER);
          try {
                String content = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));
                content = content.replace("\n", "").replace("\r", "");
                JSONObject metaData = new JSONObject(content);
                JSONArray chat_data = metaData.getJSONArray("chat_data");
               
                for (int i = 0; i < chat_data.length(); i++) {
                    if (chat_data.getJSONObject(i).has("base64Image")) {
                        Image image=SpeechBox.imagefromBase64(chat_data.getJSONObject(i).getString("base64Image"),200,200);
                        mapdb.save(selectedFile.getName()+i, getImageB64From(image), getImageB64From(getScaledImage(getImageB64From(image), 100, 100),"jpeg"));
                        sb=  new SpeechBox(chat_data.getJSONObject(i).getString("message"), getImageB64From(image), SpeechBox.SpeechDirection.valueOf(chat_data.getJSONObject(i).getString("direction")),200,200,true);
                        if(chat_data.getJSONObject(i).has("dateTime")){
                            sb.setDateTime(chat_data.getJSONObject(i).getString("dateTime"));
                        }else{
                            sb.setDateTime("Some time recently...");
                        }
                        
                    } else {
                       sb=new SpeechBox(chat_data.getJSONObject(i).getString("message"), SpeechBox.SpeechDirection.valueOf(chat_data.getJSONObject(i).getString("direction")));
                    }
                    
                    break;
                }

            } catch (Exception ex) {
            }
        return sb;
    }
    
    public static List<SpeechBox> previewHelper(File selectedFile,int Count,MapDB mapdb){
        List<SpeechBox> previews=new LinkedList<>();
        String content = null ;
         //
          try {
                //Check if zip version is available 
               if (new File(selectedFile.getAbsolutePath() + ".zip").exists()) {
                  if (mapdb.store.containsKey("P-"+selectedFile.getName())) {//Check cache as well.
                      content = mapdb.store.get("P-"+selectedFile.getName());
                  } else {
                      content = ZipHelper.decompress(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath() + ".zip")));
                  }

              } else {
                  content = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));
              }
                content = content.replace("\n", "").replace("\r", "");
                JSONObject metaData = new JSONObject(content);
                JSONArray chat_data = metaData.getJSONArray("chat_data");
                
                JSONObject metaDataTemp = new JSONObject();
                JSONArray chat_dataTemp =new JSONArray();
                        
                boolean firstDone=false;
                for (int i = 0; i < (Count < chat_data.length() ? Count : chat_data.length()); i++) {
                  chat_dataTemp.put(chat_data.getJSONObject(i));
                  SpeechBox sb = new SpeechBox("No chapters here.", SpeechBox.SpeechDirection.CENTER);
                  if (chat_data.getJSONObject(i).has("base64Image")) {
                      int width = firstDone ? 200 : 300;
                      int height = firstDone ? 200 : 300;
                      firstDone = true;
                      Image image = SpeechBox.imagefromBase64(chat_data.getJSONObject(i).getString("base64Image"), width, 200);

                      sb = new SpeechBox(chat_data.getJSONObject(i).getString("message"), getImageB64From(image), SpeechBox.SpeechDirection.valueOf(chat_data.getJSONObject(i).getString("direction")), width, height, true);

                      if (chat_data.getJSONObject(i).has("dateTime")) {
                          sb.setDateTime(chat_data.getJSONObject(i).getString("dateTime"));
                      } else {
                          sb.setDateTime("Some time recently...");
                      }

                  } else {
                      sb = new SpeechBox(chat_data.getJSONObject(i).getString("message"), SpeechBox.SpeechDirection.valueOf(chat_data.getJSONObject(i).getString("direction")));
                  }

                  previews.add(sb);

              }
                
                metaDataTemp.put("chat_data", chat_dataTemp);
                mapdb.store.put("P-"+selectedFile.getName(), metaDataTemp.toString());

            } catch (Exception ex) {
                //System.out.println(selectedFile.getAbsolutePath()+">>"+content.substring(0, 500));
                ex.printStackTrace();
                
            }
        return previews;
    }

    public void load(File selectedFile ,MapDB mapdb){
       // File selectedFile = fileChooser.showOpenDialog(null);
            activeChatFile=selectedFile.getName();
            String content = null ;
            //Read all the file and conver to json
            //Render sppech boxes.
            try {
                //Check if zip version is available 
                if (new File(selectedFile.getAbsolutePath() + ".zip").exists()) {
                    content = ZipHelper.decompress(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath()+ ".zip")));

                } else {
                    content = new String(Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath())));
                }
                
                JSONObject metaData = new JSONObject(content);
                JSONArray chat_data = metaData.getJSONArray("chat_data");

             for (int i = 0; i < chat_data.length(); i++) {
                SpeechBox sb = null;
                SpeechBox.SpeechTheme th=SpeechBox.SpeechTheme.NEUTRAL;
                if (chat_data.getJSONObject(i).has("theme")) {
                    th = SpeechBox.SpeechTheme.valueOf(chat_data.getJSONObject(i).getString("theme"));
                   cbxStatus.setValue(th);
                }
                if (chat_data.getJSONObject(i).has("base64Image")) {
                    sb = new SpeechBox(chat_data.getJSONObject(i).getString("message"), chat_data.getJSONObject(i).getString("base64Image"), SpeechBox.SpeechDirection.valueOf(chat_data.getJSONObject(i).getString("direction")),th);
                    mapdb.save(selectedFile.getName()+i, chat_data.getJSONObject(i).getString("base64Image"), getImageB64From(getScaledImage(chat_data.getJSONObject(i).getString("base64Image"), 100, 100),"jpeg"));
                       
                } else {
                    sb = new SpeechBox(chat_data.getJSONObject(i).getString("message"), SpeechBox.SpeechDirection.valueOf(chat_data.getJSONObject(i).getString("direction")));

                }
                
                

                

                speechBubbles.add(sb);
            }

        } catch (Exception ex) {
        }
    }
    public JSONObject getChapterData(){
          //Read all the speech box 
            //and write to JSON
            JSONObject metaData = new JSONObject();
            JSONArray completeChat = new JSONArray();
            for (Node spb : speechBubbles) {
                JSONObject spbj = new JSONObject();
                SpeechBox sb = (SpeechBox) spb;
                spbj.put("direction", sb.direction);
                spbj.put("theme", sb.theme);
                
                spbj.put("message", new String(sb.message.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
                
                spbj.put("base64Image", sb.base64Image);
                completeChat.put(spbj);

               //  System.out.println(spbj);
            }

            JSONObject userPicsCache = new JSONObject(userPics);

            metaData.put("chat_data", completeChat);
            metaData.put("userPicsCache", completeChat);
            metaData.put("dateTime", dateTime);
            
            return metaData;
    }
    private void setupMessageDisplay() {
        messageContainer = new VBox(5);
        Bindings.bindContentBidirectional(speechBubbles, messageContainer.getChildren());

        messageScroller = new ScrollPane(messageContainer);
        messageScroller.setStyle("-fx-background-color: transparent;");
        messageContainer.setStyle("-fx-background-color: transparent;");
       
        messageScroller.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        messageScroller.setHbarPolicy(ScrollBarPolicy.NEVER);
        messageScroller.setMaxHeight(prefHeight);//420
        //messageScroller.setPrefWidth(prefWidth);//420
        messageScroller.prefWidthProperty().bind(messageContainer.prefWidthProperty().subtract(5));
        messageScroller.setFitToWidth(true);
        //Make the scroller scroll to the bottom when a new message is added
        speechBubbles.addListener((ListChangeListener<Node>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    messageScroller.setVvalue(messageScroller.getVmax());
                }
            }
        });
    }

      /**
     * join two BufferedImage
     * you can add a orientation parameter to control direction
     * you can use a array to join more BufferedImage
     * @param img1
     * @param img2
     * @return 
     */

    public static BufferedImage joinBufferedImage(BufferedImage img1,BufferedImage img2) {

        //do some calculate first
        int offset  = 5;
        int wid = img1.getWidth()+img2.getWidth()+offset;
        int height = Math.max(img1.getHeight(),img2.getHeight())+offset;
        //create a new buffer and draw two image into the new image
        BufferedImage newImage = new BufferedImage(wid,height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        java.awt.Color oldColor = g2.getColor();
        //fill background
        g2.setPaint(java.awt.Color.WHITE);
        g2.fillRect(0, 0, wid, height);
        //draw image
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, img1.getWidth()+offset, 0);
        g2.dispose();
        return newImage;
    }
    
    /**
     * Vertical stitch similar to joinBufferedImage
     * @param img1
     * @param img2
     * @return 
     */
    public static BufferedImage joinBufferedImageV(BufferedImage img1,BufferedImage img2) {

        //do some calculate first
        int offset  = 5;
        int height= img1.getHeight()+img2.getHeight()+offset;
        int  wid  = Math.max(img1.getWidth(),img2.getWidth())+offset;
        //create a new buffer and draw two image into the new image
        BufferedImage newImage = new BufferedImage(wid,height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        java.awt.Color oldColor = g2.getColor();
        //fill background
        g2.setPaint(java.awt.Color.WHITE);
        g2.fillRect(0, 0, wid, height);
        //draw image
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, 0 , img1.getHeight()+offset);
        g2.dispose();
        return newImage;
    }
    
    /**
     * 
     * @param b641
     * @param b642
     * @param rightStitch
     * @return
     * @throws IOException 
     */
    public static BufferedImage joinBufferedImage(String b641, String b642, boolean rightStitch) throws IOException {
        // create a buffered image
        BufferedImage image = null;
        byte[] imageByte;

        //BASE64Decoder decoder = new BASE64Decoder();
        Base64.Decoder decoder = Base64.getDecoder();
        imageByte = decoder.decode(b641);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
        image = ImageIO.read(bis);
        bis.close();

        BufferedImage image2 = null;
        byte[] imageByte2;

        //BASE64Decoder decoder2 = new BASE64Decoder();
        imageByte2 = decoder.decode(b642);
        ByteArrayInputStream bis2 = new ByteArrayInputStream(imageByte2);
        image2 = ImageIO.read(bis2);
        bis2.close();

        if (rightStitch) {
            return joinBufferedImage(image, getScaledImage(image2, image.getWidth(), image.getHeight()));
        } else {
            return joinBufferedImageV(image, getScaledImage(image2, image.getWidth(), image.getHeight()));
        }
    }
    
    /**
     * 
     * @param b641
     * @param w
     * @param h
     * @return
     * @throws IOException 
     */
    public static BufferedImage getScaledImage(String b641, int w, int h) throws IOException {

        // create a buffered image
        BufferedImage image = null;
        byte[] imageByte;

        //BASE64Decoder decoder = new BASE64Decoder();
        Base64.Decoder decoder = Base64.getDecoder();
        imageByte = decoder.decode(b641);
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
        image = ImageIO.read(bis);
        bis.close();

        return getScaledImage(image, w, h);
    }
    
    /**
     * 
     * @param src
     * @param w
     * @param h
     * @return 
     */
    public static BufferedImage getScaledImage(BufferedImage src, int w, int h){
    int original_width = src.getWidth();
    int original_height = src.getHeight();
    int bound_width = w;
    int bound_height = h;
    int new_width = original_width;
    int new_height = original_height;

    // first check if we need to scale width
    if (original_width > bound_width) {
        //scale width to fit
        new_width = bound_width;
        //scale height to maintain aspect ratio
        new_height = (new_width * original_height) / original_width;
    }

    // then check if we need to scale even with the new height
    if (new_height > bound_height) {
        //scale height to fit instead
        new_height = bound_height;
        //scale width to maintain aspect ratio
        new_width = (new_height * original_width) / original_height;
    }

    BufferedImage resizedImg = new BufferedImage(new_width, new_height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = resizedImg.createGraphics();
    g2.setBackground(java.awt.Color.WHITE);
    g2.clearRect(0,0,new_width, new_height);
    g2.drawImage(src, 0, 0, new_width, new_height, null);
    g2.dispose();
    return resizedImg;
}
    
    public static String getImageB64From(File selectedFile) {
        try {

            FileInputStream fis = new FileInputStream(selectedFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = fis.read(buffer)) > -1) {
                baos.write(buffer, 0, read);
            }
            fis.close();
            baos.close();
            byte pgnBytes[] = baos.toByteArray();
            Base64.Encoder base64_enc = Base64.getEncoder();
            return base64_enc.encodeToString(pgnBytes);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(TimeLineStory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TimeLineStory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static String getImageB64From(Image image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", baos);
        } catch (IOException e) {
            //bla
        }
        byte pgnBytes[] = baos.toByteArray();
        Base64.Encoder base64_enc = Base64.getEncoder();
        String base64_image = base64_enc.encodeToString(pgnBytes);

        return base64_enc.encodeToString(pgnBytes);

    }
    
    public static String getImageB64From(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
 
        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();
 
            //BASE64Encoder encoder = new BASE64Encoder();
            Base64.Encoder encoder = Base64.getEncoder();
            imageString = encoder.encodeToString(imageBytes);
 
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    private void setupInputDisplay() {
        inputContainer = new HBox(5);

        TextField userInput = new TextField();
        userInput.setPromptText("Enter message");
        userInput.setPrefWidth(700);//Widht control

        Button sendMessageButton = new Button("Set");
        sendMessageButton.disableProperty().bind(userInput.lengthProperty().isEqualTo(0));
        sendMessageButton.setOnAction(event -> {
            sendMessage(userInput.getText());
            userInput.setText("");
        });

        //For testing purposes
        Button receiveMessageButton = new Button("Get");
        receiveMessageButton.disableProperty().bind(userInput.lengthProperty().isEqualTo(0));
        receiveMessageButton.setOnAction(event -> {
            receiveMessage(userInput.getText());
            userInput.setText("");
        });

        //For testing purposes
        Button attachImage = new Button("+");
        attachImage.disableProperty().bind(userInput.lengthProperty().isEqualTo(0));
        attachImage.setOnAction(event -> {

            try {
                java.awt.Image image = getImageFromClipboard();
                if (image != null) {
                    javafx.scene.image.Image fimage = awtImageToFX(image);
                    //pe.imageView.setFitHeight(scene.getHeight());
                    // pe.imageView.setFitWidth(scene.getWidth());
                    String b64Image = getImageB64From(fimage);
                    receivePhoto(userInput.getText(), b64Image);
                } else {
                    File selectedFile = fileChooser.showOpenDialog(null);
                    receivePhoto(userInput.getText(), getImageB64From(selectedFile));
                }
            } catch (Exception ex) {

            }

            userInput.setText("");

        });

        //For testing purposes
        Button attachImage2 = new Button("+");
        attachImage2.disableProperty().bind(userInput.lengthProperty().isEqualTo(0));
        attachImage2.setOnAction(event -> {
//            File selectedFile = fileChooser.showOpenDialog(null);
//      
//           sendPhoto(userInput.getText(),getImageB64From(selectedFile));
//           
            try {
                java.awt.Image image = getImageFromClipboard();
                if (image != null) {
                    javafx.scene.image.Image fimage = awtImageToFX(image);
                    //pe.imageView.setFitHeight(scene.getHeight());
                    // pe.imageView.setFitWidth(scene.getWidth());
                    String b64Image = getImageB64From(fimage);
                    sendPhoto(userInput.getText(), b64Image);
                } else {
                    File selectedFile = fileChooser.showOpenDialog(null);
                    sendPhoto(userInput.getText(), getImageB64From(selectedFile));
                }
            } catch (Exception ex) {

            }

            userInput.setText("");

        });

        inputContainer.getChildren().setAll(receiveMessageButton, attachImage, userInput, sendMessageButton, attachImage2);
    }

    public void sendMessage(String message) {
        speechBubbles.add(new SpeechBox(message, SpeechBox.SpeechDirection.RIGHT));
    }

    public void receiveMessage(String message) {
        speechBubbles.add(new SpeechBox(message, SpeechBox.SpeechDirection.LEFT));
    }

    public void receivePhoto(String message, String imageB64) {
        try {
            speechBubbles.add(new SpeechBox(message, imageB64, SpeechBox.SpeechDirection.LEFT,cbxStatus.getValue()));
        } catch (IOException ex) {
            Logger.getLogger(TimeLineStory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendPhoto(String message, String imageB64) {
        try {
            speechBubbles.add(new SpeechBox(message, imageB64, SpeechBox.SpeechDirection.RIGHT,cbxStatus.getValue()));
        } catch (IOException ex) {
            Logger.getLogger(TimeLineStory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public HashMap<String, String> getUserPics() {
        return userPics;
    }
    
    
    
}
