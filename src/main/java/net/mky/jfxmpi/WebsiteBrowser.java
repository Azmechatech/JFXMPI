/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.value.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.*;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.NumberStringConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.stage.DirectoryChooser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.davidmoten.hilbert.HilbertCurve;
import net.mky.clustering.HilbertCurvePatternDetect;
import systemknowhow.Tools;

public class WebsiteBrowser extends Application {

    private static final String HOME_LOC = "http://docs.oracle.com/javafx/2/get_started/animation.htm";
    static ExecutorService executor = Executors.newFixedThreadPool(5);
    private static WebView webView;

    private File captureFile = new File("cap.png");
    static DirectoryChooser dc = new DirectoryChooser();
    static File directory = null;
    static Queue<String> downloadqueue = new LinkedList<>();
    static Queue<String> urlqueue = new LinkedList<>();
    String webContent = "";

    public static void main(String[] args) {
        Application.launch(WebsiteBrowser.class);
    }

    @Override
    public void start(Stage stage) throws Exception {
        webView = new WebView();
        webView.setPrefSize(1500, 900);

        HBox browserControls = new HBox(10);
        final TextField prefWidth = new TextField("1000");
        final TextField prefHeight = new TextField("8000");
        //"img[src]","src",true
        final TextField _element = new TextField("img[src]");
        final TextField _attribute = new TextField("src");
        final TextField fileExt = new TextField(".jpg;.jpeg");
        Label labelForImgs = new Label("HTML element & attribute: ");

        webView.getEngine().getLoadWorker().stateProperty()
                .addListener((obs, oldValue, newValue) -> {
                    if (newValue == State.SUCCEEDED) {
                        System.out.println("finished loading");
                        try {
                            TransformerFactory transformerFactory = TransformerFactory
                                    .newInstance();
                            Transformer transformer = transformerFactory.newTransformer();
                            StringWriter stringWriter = new StringWriter();
                            transformer.transform(new DOMSource(webView.getEngine().getDocument()),
                                    new StreamResult(stringWriter));
                            webContent = stringWriter.getBuffer().toString();

                            // System.out.println(webContent);
                            imageURLS(webContent, _element.getText(), _attribute.getText(), true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        final TextField location = new TextField();
        location.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!location.getText().startsWith("http")) {
                    location.setText("http://" + location.getText());
                }
                webView.getEngine().load(location.getText());
                /**
                 * *************************
                 */
                // Retrieve all direct image URLs
                /**
                 * *************************
                 */
                try {
                    URL url;
                    url = new URL(location.getText());
                    URLConnection urlc = url.openConnection();
                    urlc.setRequestProperty("User-Agent", webView.getEngine().getUserAgent());
                    imageURLS(urlc, _element.getText(), _attribute.getText(), true);

                } catch (MalformedURLException ex) {
                    Logger.getLogger(WebsiteBrowser.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(WebsiteBrowser.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        webView.getEngine().locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldLocation, String newLocation) {
                location.setText(newLocation);
                urlqueue.add(newLocation);
            }
        });

        Button goButton = new Button("Set Save Location");
        Button goBack = new Button("Back");
        Button goForward = new Button("Forward");
        Button goEditURLs = new Button("Edit URLs");
        Button goDownload = new Button("Download");
        Button goURLHistory = new Button("Visited URLs");
        Label stateLabel = new Label();

        stateLabel.setTextFill(Color.RED);
        ProgressBar progressBar = new ProgressBar();
        final WebEngine webEngine = webView.getEngine();
        // A Worker load the page
        Worker<Void> worker = webEngine.getLoadWorker();

        // Listening to the status of worker
        worker.stateProperty().addListener(new ChangeListener<Worker.State>() {

            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                stateLabel.setText("Loading state: " + newValue.toString());
                if (newValue == Worker.State.SUCCEEDED) {
                    stage.setTitle(webEngine.getLocation());
                    stateLabel.setText("Finish!");
                }
            }
        });

        goButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                directory = dc.showDialog(null);
            }
        });

        goBack.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                goBack();
            }
        });

        goForward.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                goForward();
            }
        });

        goDownload.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                executeDownloadQueue(stateLabel);
            }
        });

        goEditURLs.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                openURLEditor();
            }
        });
        
        
        goURLHistory.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                openURLHistory(webView.getEngine());
            }
        });
        // Bind the progress property of ProgressBar
        // with progress property of Worker
        progressBar.progressProperty().bind(worker.progressProperty());

        ScrollPane webViewScroll = new ScrollPane();
        webViewScroll.setContent(webView);

        webViewScroll.setPrefSize(1500, 800);

        final Button capture = new Button("Capture");
        final Button getImageList = new Button("Detect Files to download");
        final Button showPageSource = new Button("<html>");
        getImageList.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                /**
                 * *************************
                 */
                // Retrieve all direct image URLs
                /**
                 * *************************
                 */
                // webContent= getWebContent();
                String[] allImages = imageURLS(webContent, _element.getText(), _attribute.getText(), true);
                if (allImages.length == 0) {
                    imageURLS2(webContent, _element.getText(), _attribute.getText()).toArray(allImages);
                }
                for (String url : allImages) {
                    String[] fileExtelements = fileExt.getText().split(";");
                    for (String fileExtelement : fileExtelements) {
                        if (url.endsWith(fileExtelement)) {
                            downloadqueue.add(url);
                        }
                    }

                }

                stateLabel.setText("Automatically added: " + downloadqueue.size() + " files for download . Ext: " + fileExt.getText());
            }
        });

        showPageSource.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                /**
                 * *************************
                 */
                // VIEW CONTENT
                /**
                 * *************************
                 */
                viewWebContent();
            }
        });
//
        final ProgressIndicator progress = new ProgressIndicator();
        progress.setVisible(false);

        HBox controls = new HBox(10);
        controls.getChildren().addAll(capture, progress, prefWidth, prefHeight, labelForImgs, _element, _attribute, fileExt, getImageList, showPageSource);

        final ImageView imageView = new ImageView();
        ScrollPane imageViewScroll = makeScrollable(imageView);
        imageViewScroll.setPrefSize(800, 300);

        final PauseTransition pt = new PauseTransition();
        pt.setDuration(Duration.millis(500));
        pt.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                WritableImage image = webView.snapshot(null, null);
                if (directory == null) {
                    directory = dc.showDialog(null);
                }
                captureFile = new File(directory.getAbsolutePath() + "/" + "WEBCAPTURE" + System.currentTimeMillis() + ".png");
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                try {
                    ImageIO.write(bufferedImage, "png", captureFile);
                    imageView.setImage(new Image(captureFile.toURI().toURL().toExternalForm()));
                    System.out.println("Captured WebView to: " + captureFile.getAbsoluteFile());
                    stateLabel.setText("Captured WebView to: " + captureFile.getAbsoluteFile());
                    progress.setVisible(false);
                    capture.setDisable(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        capture.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                NumberStringConverter converter = new NumberStringConverter();
                double W = converter.fromString(prefWidth.getText()).doubleValue();
                double H = converter.fromString(prefHeight.getText()).doubleValue();

                // ensure that the capture size has a reasonable min size and is within the limits of what JavaFX is capable of processing.
                if (W < 100) {
                    W = 100;
                    prefWidth.setText("100");
                }

                if (W > 2000) {
                    W = 2000;
                    prefWidth.setText("2000");
                }

                if (H < 100) {
                    H = 100;
                    prefHeight.setText("100");
                }

                if (H > 16000) {
                    H = 16000;
                    prefHeight.setText("16000");
                }

                webView.setPrefWidth(W);
                webView.setPrefHeight(H);

                pt.play();
                capture.setDisable(true);
                progress.setVisible(true);
            }
        });

        createContextMenu(webView, stateLabel); //Creating a custom context menu works
        webView.getEngine().load(HOME_LOC);
        browserControls.getChildren().addAll(stateLabel, progressBar, goButton, goBack, goForward, goEditURLs, goDownload,goURLHistory);
// VBox root = new VBox();
        //      root.getChildren().addAll(addressBar, goButton, stateLabel, progressBar, browser);
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 10; -fx-background-color: cornsilk;");
        layout.getChildren().setAll(
                location, browserControls,
                webViewScroll,
                controls//,
        //  imageViewScroll, //Dont display captured one
        //  new Label("Capture File: " + captureFile.getAbsolutePath() )
        );
        VBox.setVgrow(imageViewScroll, Priority.ALWAYS);

        stage.setScene(new Scene(layout));
        stage.show();
    }

    private static void createContextMenu(WebView webView, Label stateLabel) {

        MenuItem reload = new MenuItem("reload");
        reload.setOnAction(e -> {
            webView.getEngine().reload();
        }
        );
        ContextMenu contextMenu = new ContextMenu(reload);
        webView.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
//                System.out.println(webView.getEngine().executeScript("document.elementFromPoint("
//                        + e.getX()
//                        + "," + e.getY() + ").tagName;"));
//
//                String tagName = webView.getEngine().executeScript("document.elementFromPoint("
//                        + e.getX()
//                        + "," + e.getY() + ").tagName;").toString();
//                System.out.println(webView.getEngine().executeScript("document.elementFromPoint("
//                        + e.getX()
//                        + "," + e.getY() + ").getAttribute(\"src\");"));
                String imgURL = webView.getEngine().executeScript("document.elementFromPoint("
                        + e.getX()
                        + "," + e.getY() + ").getAttribute(\"src\");").toString();
//                JSONObject object = (JSONObject) webView.getEngine().executeScript("document.elementFromPoint("
//                        +e.getX()
//                        +"," +  e.getY()+");");
                //    contextMenu.show(webView, e.getScreenX(), e.getScreenY()); //No display needed

                if (directory != null) {
                    downloadqueue.add(imgURL);

                } else {
                    directory = dc.showDialog(null);
                    downloadqueue.add(imgURL);

                }

                stateLabel.setText("Download >> " + downloadqueue.size() + " files.");

            }  else if (e.getButton() == MouseButton.SECONDARY) {
                    stateLabel.setText("Navigating to >> "+webView.getEngine().getLocation());
                    System.out.println("Navigating to >> "+webView.getEngine().getLocation());
            }else {
                contextMenu.hide();
            }
        });
    }

    public String goBack() {
        final WebHistory history = webView.getEngine().getHistory();
        ObservableList<WebHistory.Entry> entryList = history.getEntries();
        int currentIndex = history.getCurrentIndex();
//    Out("currentIndex = "+currentIndex);
//    Out(entryList.toString().replace("],","]\n"));

        Platform.runLater(new Runnable() {
            public void run() {
                history.go(-1);
            }
        });
        return entryList.get(currentIndex > 0 ? currentIndex - 1 : currentIndex).getUrl();
    }

    public String goForward() {
        final WebHistory history = webView.getEngine().getHistory();
        ObservableList<WebHistory.Entry> entryList = history.getEntries();
        int currentIndex = history.getCurrentIndex();
//    Out("currentIndex = "+currentIndex);
//    Out(entryList.toString().replace("],","]\n"));

        Platform.runLater(new Runnable() {
            public void run() {
                history.go(1);
            }
        });
        return entryList.get(currentIndex < entryList.size() - 1 ? currentIndex + 1 : currentIndex).getUrl();
    }

    public static void openURLEditor() {
        String[] arrayData = {"First", "Second", "Third", "Fourth"};

        ArrayList dialogData = new ArrayList(downloadqueue);

        //   List<String> dialogData = Arrays.asList(arrayData);
        //ChoiceDialog dialog = new ChoiceDialog(dialogData.get(0), dialogData);
        Dialog dialog = new Dialog();
        dialog.setTitle("EDIT URLS");
        dialog.setHeaderText("Select your choice");

        // Create the Lists for the ListViews
        ObservableList<String> urlList = FXCollections.<String>observableArrayList(dialogData);

// Create the ListView for the fruits
        ListView<String> urls = new ListView<String>();
        urls.getItems().addAll(urlList);

        //CREATE THE INPUT BOXES
        Label label1 = new Label("STRING:");
        Label label2 = new Label("REPLACEMENT:");
        Label label3 = new Label("PREFFIX:");
        Label label4 = new Label("SUFFIX:");
        TextField STRING = new TextField();
        TextField REPLACEMENT = new TextField();
        TextField PREFFIX = new TextField();
        TextField SUFFIX = new TextField();
        Button goReplace = new Button("Append");

        goReplace.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                for (String url : urlList) {
                    downloadqueue.add(PREFFIX.getText() + url.replace(STRING.getText(), REPLACEMENT.getText()) + SUFFIX.getText());
                }

                /**
                 * *****************************
                 * Copy from clipboard as well
                 */
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent cc = new ClipboardContent();

                String content = clipboard.getString();
                String[] contentArray = content.split("\n");
                List<String> list = Arrays.asList(contentArray);
                downloadqueue.addAll(list);
            }
        });

        urls.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + urls.getSelectionModel().getSelectedItem());
                STRING.setText(urls.getSelectionModel().getSelectedItem());
            }
        });

        VBox content = new VBox(label1, STRING, label2, REPLACEMENT, label3, PREFFIX, label4, SUFFIX, goReplace, urls);
        dialog.getDialogPane().setContent(content);
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        Optional<String> result = dialog.showAndWait();
        String selected = "cancelled.";

        if (result.isPresent()) {

            //   selected = result.get();
        }

//actionStatus.setText("Selection: " + selected);
    }

    public static void openURLHistory(WebEngine webEngine) {
        String[] arrayData = {"First", "Second", "Third", "Fourth"};

        ArrayList dialogData = new ArrayList(urlqueue);

        //   List<String> dialogData = Arrays.asList(arrayData);
        //ChoiceDialog dialog = new ChoiceDialog(dialogData.get(0), dialogData);
        Dialog dialog = new Dialog();
        dialog.setTitle("EDIT URLS");
        dialog.setHeaderText("Select your choice");

        // Create the Lists for the ListViews
        ObservableList<String> urlList = FXCollections.<String>observableArrayList(dialogData);

// Create the ListView for the fruits
        ListView<String> urls = new ListView<String>();
        urls.getItems().addAll(urlList);

        //CREATE THE INPUT BOXES
        Label label1 = new Label("STRING:");
        Label label2 = new Label("REPLACEMENT:");
        Label label3 = new Label("PREFFIX:");
        Label label4 = new Label("SUFFIX:");
        TextField STRING = new TextField();
        TextField REPLACEMENT = new TextField();
        TextField PREFFIX = new TextField();
        TextField SUFFIX = new TextField();
        Button goReplace = new Button("Append");

        goReplace.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                for (String url : urlList) {
                    downloadqueue.add(PREFFIX.getText() + url.replace(STRING.getText(), REPLACEMENT.getText()) + SUFFIX.getText());
                }

                /**
                 * *****************************
                 * Copy from clipboard as well
                 */
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent cc = new ClipboardContent();

                String content = clipboard.getString();
                String[] contentArray = content.split("\n");
                List<String> list = Arrays.asList(contentArray);
                downloadqueue.addAll(list);
            }
        });

        urls.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + urls.getSelectionModel().getSelectedItem());
                STRING.setText(urls.getSelectionModel().getSelectedItem());
                webEngine.load(urls.getSelectionModel().getSelectedItem());
            }
        });

        VBox content = new VBox(label1, STRING, label2, REPLACEMENT, label3, PREFFIX, label4, SUFFIX, goReplace, urls);
        dialog.getDialogPane().setContent(content);
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        Optional<String> result = dialog.showAndWait();
        String selected = "cancelled.";

        if (result.isPresent()) {

            //   selected = result.get();
        }

//actionStatus.setText("Selection: " + selected);
    }

    
    public void executeDownloadQueue(Label stateLabel) {
        Iterator itr = downloadqueue.iterator();
        Queue<String> removeQueue = new LinkedList<>();
        String urlToDnld="" ;
        try {
            while (itr.hasNext()) {

                // System.out.println(itr.next());
                 urlToDnld = itr.next().toString();
                 if(!urlToDnld.startsWith("http")) continue;
                urlToDnld = urlToDnld.startsWith("http") ? urlToDnld : "https:" + urlToDnld;
                System.out.println("executeDownloadQueue>> " + urlToDnld);
                saveImage(urlToDnld, directory, stateLabel);
               removeQueue.add(urlToDnld);

            }
        } catch (Exception ex) {
            System.out.println("executeDownloadQueue>> Issue with "+urlToDnld+">>"+ex.getMessage());
            removeQueue.add(urlToDnld);
            //ex.printStackTrace();
           // return;//return on exceptions
            //  Logger.getLogger(WebsiteBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }

       downloadqueue.removeAll(removeQueue);
    }

    private ScrollPane makeScrollable(final ImageView imageView) {
        final ScrollPane scroll = new ScrollPane();
        final StackPane centeredImageView = new StackPane();

        centeredImageView.getChildren().add(imageView);
        scroll.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                centeredImageView.setPrefSize(
                        Math.max(imageView.prefWidth(bounds.getHeight()), bounds.getWidth()),
                        Math.max(imageView.prefHeight(bounds.getWidth()), bounds.getHeight())
                );
            }
        });
        scroll.setContent(centeredImageView);

        return scroll;
    }

    public String getWebContent() {
        try {
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(webView.getEngine().getDocument()),
                    new StreamResult(stringWriter));
            return stringWriter.getBuffer().toString();
            // System.out.println(webContent);

            //imageURLS(webContent,_element.getText(), _attribute.getText(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ***********************************
     * WEB CONTENT
     */
    public void viewWebContent() {

        Dialog dialog = new Dialog();
        dialog.setTitle("EDIT URLS");
        dialog.setHeaderText("Select your choice");
        TextArea ta = new TextArea(webContent);

        dialog.getDialogPane().setContent(ta);
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        Optional<TextArea> result = dialog.showAndWait();
        webContent = ta.getText();
        System.out.println(webContent);
        webView.getEngine().loadContent(webContent);

    }

    /**
     * ********************SAVE IMAGES****************************************
     *
     * @param imageUrl
     * @param folderLocation
     * @param stateLabel
     * @throws IOException
     */
    public static void saveImage(String imageUrl, File folderLocation, Label stateLabel) {
        URL url = null;
        try {
            url = new URL(imageUrl);
        } catch (MalformedURLException ex) {
           // Logger.getLogger(WebsiteBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
        URLConnection urlc = null;
        try {
            urlc = url.openConnection();
        } catch (IOException ex) {
           // Logger.getLogger(WebsiteBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
        urlc.setRequestProperty("User-Agent", webView.getEngine().getUserAgent());
        String fileName = url.getFile();
        String destName = folderLocation.getAbsolutePath() + fileName.substring(fileName.lastIndexOf("/"));
        if(destName.length()>100){
            destName = folderLocation.getAbsolutePath() +"/"+ Tools.getMD5(imageUrl) + fileName.substring(fileName.lastIndexOf("."));
        }
        System.out.println(destName);
        BufferedImage bi = null;
        File testFile = new File(destName);
        if (testFile.exists()) {
            bi = HilbertCurvePatternDetect.getTheImage(destName, false);
            destName = folderLocation.getAbsolutePath() + "/" + System.currentTimeMillis() + fileName.substring(fileName.lastIndexOf("/") + 1);
        }
        
        if(destName.length()>100){
            destName = folderLocation.getAbsolutePath() +"/"+ System.currentTimeMillis()+ Tools.getMD5(imageUrl) + fileName.substring(fileName.lastIndexOf("."));
        }
        
        InputStream is = null;
        try {
            is = urlc.getInputStream();
        } catch (IOException ex) {
           // Logger.getLogger(WebsiteBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
        ;//url.openStream();
        OutputStream os = null;
        try {
            os = new FileOutputStream(destName);
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(WebsiteBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }

        byte[] b = new byte[2048];
        int length;
        try {
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        } catch (IOException ex) {
          //  Logger.getLogger(WebsiteBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (bi != null) { //Delete duplicate file
            if (HilbertCurvePatternDetect.match2ImagesExactly(bi, HilbertCurvePatternDetect.getTheImage(destName, false), 10)) {
                File newFile = new File(destName);
                if (newFile.delete()) {
                    System.out.println(destName + " Duplicate File deleted");
                } else {
                    System.out.println(destName + " Duplicate File doesn't exist");
                }
            }

        }

        stateLabel.setText("Saved" + fileName);
    }

    /**
     * ************************imageURLS*************************************
     *
     * @param connection
     * @param _element
     * @param _attribute
     * @param attrVale
     * @return
     */
    static String[] imageURLS(URLConnection connection, String _element, String _attribute, boolean attrVale) {

        try {

            connection.connect();

            // URL oracle = new URL(_URL);
            //BufferedReader in = new BufferedReader(  new InputStreamReader(oracle.openStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

            String inputLine;
            StringBuilder html = new StringBuilder();
            // System.out.println("#_URL " + _URL);
            while ((inputLine = in.readLine()) != null) {
                html.append(inputLine);
                //System.out.println(inputLine);
            }
            in.close();

            return imageURLS(html.toString(), _element, _attribute, attrVale);
        } catch (Exception ex) {
            Logger.getLogger(WebsiteBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * *****************************imageURLS**********************************************
     *
     * @param html
     * @param _element
     * @param _attribute
     * @param attrVale
     * @return
     */
    static String[] imageURLS(String html, String _element, String _attribute, boolean attrVale) {

        try {
            System.out.println(html);
            Document doc = Jsoup.parse(html); // set proper Charset (2nd param) and BaseUri (3rd param) here
            // Elements elements = doc.select("img[src]");
            Elements elements = doc.select(_element);
            String[] result = new String[elements.size()];
            int counter = 0;
            for (Element element : elements) {
                // result[counter++]=element.attr(_attribute);
                // Do something with your links here ...
                System.out.println(element.attr(_attribute));

                if (attrVale) {
                    result[counter++] = element.attr(_attribute);
                } else {
                    result[counter++] = element.html();
                }
                // System.out.println(element.html());
            }

            return result;
        } catch (Exception ex) {
            Logger.getLogger(WebsiteBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * *****************Find Image URLs Via pattern match**********************
     *
     * @param html
     * @param _element
     * @param _attribute
     * @return
     */
    static List<String> imageURLS2(String html, String _element, String _attribute) {

        Pattern p = null;
        Matcher m = null;
        String word0 = null;
        String word1 = null;
        List<String> result = new ArrayList<>();
        p = Pattern.compile(".*<img[^>]*src=\"([^\"]*)", Pattern.CASE_INSENSITIVE);
        m = p.matcher(html);
        while (m.find()) {
            word0 = m.group();
            System.out.println(word0.toString());
            result.add(word0);

        }
        return result;
    }

}
