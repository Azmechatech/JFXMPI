/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.mky.tools.StylesForAll;
import org.json.JSONArray;
import org.json.JSONObject;
import systemknowhow.human.Life;
import systemknowhow.human.LifeTagFactory;
import systemknowhow.human.Male;
import systemknowhow.human.guns.CompositeGun;

/**
 *
 * @author Manoj
 */
public class CharacterPane extends StackPane {

    private Rectangle rect;
    private double pressedX, pressedY;
    private LongProperty frame = new SimpleLongProperty();

    FileInputStream input;
    ImageView imageView;
    public String CharacterFile = "";
    Life thisCharcter;//=""

    String name = "";
    String age = "";
    String SeedFile = "";
    LifeTagFactory ltf;
    public String CharacterPool[];
    
    Button playButton;

    private static final double W = 400;
    private static final double H = 400;
    private static final int SHADOW_LENGTH = 100;

    private static final double IMG_X = 20;
    private static final double IMG_Y = 20;

    private static final int FONT_SIZE = 200;

    private static final double SHADOW_SLOPE_FACTOR = 1.5;

    Color SHADOW_COLOR = Color.GRAY.brighter();

    public CharacterPane(final int width, final int height, boolean border) {
        setMinSize(300, 300);
        if (border) {
            setBorder(new Border(new BorderStroke(Color.GOLDENROD,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
        if (border) {
            setStyle("-fx-padding: 10;"
                    + "-fx-border-style: solid inside;"
                    + "-fx-border-width: 10;"
                    + "-fx-border-insets: 0;"
                    + "-fx-border-radius: 5;" + "-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #dc143c, #661a33)"
            /*"-fx-border-color: GOLDENROD;"*/);
        }
        //setStyle("-fx-border-color: blue;");
        //Label count = new Label();
        //count.textProperty().bind(Bindings.convert(frame));
        //getChildren().add(count);
        //count.setMouseTransparent(true);

        Rectangle rectangle = new Rectangle(width, height / 3, Color.rgb(200, 200, 200, 0.5));
        //rectangle.setArcWidth(30);
        //rectangle.setArcHeight(30);
        // rectangle.setFill(Color.rgb(200, 200, 200, 0.5));
        rectangle.setEffect(new DropShadow(10, 5, 5, Color.GRAY));
        //getChildren().add(rectangle);
        //StackPane.setAlignment(rectangle, Pos.CENTER);
        try {
            input = new FileInputStream("C:/Users/Maneesh/Desktop/Kshatrujas2.png");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CharacterPane.class.getName()).log(Level.SEVERE, null, ex);
        }
        Image image = new Image(input, 400, 800, true, true);

        //Image image = getImage();
        imageView = new ImageView(image);
        //float scaleVal=image.getHeight()>image.getWidth()?(float) (image.getWidth()/width):(float) (image.getHeight()/height);
        //Scale scale = new Scale(scaleVal,scaleVal); 
        //imageView.getTransforms().add(scale); //rotate by 45 degrees
        getChildren().add(imageView);

        //StackPane.setAlignment(imageView, Pos.TOP_LEFT);
        setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                pressedX = event.getX();
                pressedY = event.getY();
            }
        });

        EventHandler<ActionEvent> pickPic = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                //dialog.initStyle(StageStyle.TRANSPARENT);

                dialog.initOwner(null);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text("This is a Dialog"));
                //Show image gallery
                ScrollPane root = new ScrollPane();

                //Profile data
                //Creating a GridPane container
                GridPane grid = new GridPane();
                grid.setPadding(new Insets(10, 10, 10, 10));
                grid.setVgap(5);
                grid.setHgap(5);
                //Defining the Name text field
                final TextField nameFiled = new TextField(name);
                nameFiled.setPromptText("Name");
                nameFiled.setPrefColumnCount(10);
                nameFiled.getText();
                GridPane.setConstraints(nameFiled, 0, 0);
                grid.getChildren().add(nameFiled);
                //Defining the Last Name text field
                //final TextField lastName = new TextField();
                final ComboBox<LifeTagFactory> cbxStatus = new ComboBox<>();
                cbxStatus.getItems().setAll(LifeTagFactory.values());
                //lastName.setPromptText("Gender");
                GridPane.setConstraints(cbxStatus, 0, 1);
                grid.getChildren().add(cbxStatus);
                //Defining the Comment text field
                final TextField comment = new TextField(age);
                comment.setPrefColumnCount(4);
                comment.setPromptText("Age");
                GridPane.setConstraints(comment, 0, 2);
                grid.getChildren().add(comment);
                //Defining the Submit button
                EventHandler<ActionEvent> pickSeedFile = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        FileChooser fileChooser = new FileChooser();

                        fileChooser.setTitle("Open Resource File");
                        File file = fileChooser.showOpenDialog(new Stage());
                        //CharacterFile=file.getAbsolutePath();
                        if (file != null) {
                            SeedFile = file.getAbsolutePath();
                        }
                    }
                };
                Button submit = new Button("Seed File");
                GridPane.setConstraints(submit, 0, 3);
                submit.setOnAction(pickSeedFile);
                grid.getChildren().add(submit);
//                //Defining the Clear button
                Label clear = new Label(SeedFile);
                GridPane.setConstraints(clear, 0, 4);
                grid.getChildren().add(clear);

                TilePane tile = new TilePane();
                root.setStyle("-fx-background-color: DAE6F3;");
                tile.setPadding(new Insets(15, 15, 15, 15));
                tile.setHgap(15);

                tile.getChildren().add(grid);

                EventHandler<ActionEvent> setImage = new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        FileInputStream input = null;
                        try {
                            input = new FileInputStream(((Button) event.getSource()).getId());
                            Image image = new Image(input, 400, 600, true, true);
                            imageView.setImage(image);
                            CharacterFile = ((Button) event.getSource()).getId();
                            name = nameFiled.getText();
                            age = comment.getText();
                            ltf = cbxStatus.getValue();
                            thisCharcter=createLife(name, ltf, getLines(SeedFile));
                        } catch (FileNotFoundException ex) {
                            Logger.getLogger(CharacterPane.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            try {
                                input.close();
                            } catch (IOException ex) {
                                Logger.getLogger(CharacterPane.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                };
                for (final String character : CharacterPool) {
                    ImageView imageView;
                    imageView = createImageView(new File(character));
                    //imageView.setOnMouseClicked(selectAction);
                    VBox vbx = new VBox();
                    Button select = new Button("Select");
                    select.setId(character);
                    select.setOnAction(setImage);
                    vbx.getChildren().addAll(imageView, select);
                    tile.getChildren().addAll(vbx);
                }
                root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
                root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
                root.setFitToWidth(true);
                root.setContent(tile);
//                dialog.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
//                dialog.setHeight(Screen.getPrimary().getVisualBounds()
//                        .getHeight());
                Scene dialogScene = new Scene(root, 1024, 800);
                dialogScene.setFill(Color.TRANSPARENT);
                dialog.setScene(dialogScene);
                dialog.show();

//                    FileChooser fileChooser = new FileChooser();
//
//                    fileChooser.setTitle("Open Resource File");
//                    File file = fileChooser.showOpenDialog(new Stage());
//                    //CharacterFile=file.getAbsolutePath();
//                    if (file != null) {
//                        FileInputStream input = new FileInputStream(file.getAbsolutePath());
//                        CharacterFile = file.getAbsolutePath();
//                        Image image = new Image(input, 300, 800, true, true);
//                        //float scaleVal=image.getHeight()>image.getWidth()?(float) (image.getWidth()/width):(float) (image.getHeight()/height);
//                        //Scale scale = new Scale(scaleVal,scaleVal); 
//                        //imageView.getTransforms().add(scale); //rotate by 45 degrees
//                        imageView.setImage(image);
//                        //changeImage(new Image(input),true);
//                    }
            }
        };

        EventHandler<ActionEvent> addMultipleFilesAction = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                List<File> list = fileChooser.showOpenMultipleDialog(new Stage());
                if (list != null) {
                    CharacterPool = new String[list.size()];

                    for (int i = 0; i < CharacterPool.length; i++) {
                        CharacterPool[i] = list.get(i).getAbsolutePath();
                    }

                    //Set nameFiled and other properties
                    final Stage dialog = new Stage();
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.initOwner(null);
                    VBox dialogVbox = new VBox(20);
                    dialogVbox.getChildren().add(new Text("This is a Dialog"));
                    //Show image gallery
                    ScrollPane root = new ScrollPane();
                    TilePane tile = new TilePane();
                    root.setStyle("-fx-background-color: DAE6F3;");
                    tile.setPadding(new Insets(15, 15, 15, 15));
                    tile.setHgap(15);
                    root.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Horizontal
                    root.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll bar
                    root.setFitToWidth(true);
                    root.setContent(dialogVbox);
//                    dialog.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
//                    dialog.setHeight(Screen.getPrimary().getVisualBounds()
//                            .getHeight());
                    Scene dialogScene = new Scene(root, 400, 400);
                    dialog.setScene(dialogScene);
                    dialog.show();
                }

            }
        };

         playButton = new Button(name);
        playButton.getStyleClass().add("play");
        playButton.setStyle(StylesForAll.aliveTheme);
        playButton.setOnAction(pickPic);
        // mainPane.setCenter(homePane);
        HBox buttonPane = new HBox(12, playButton);
        getChildren().add(playButton);
        StackPane.setAlignment(playButton, Pos.BOTTOM_RIGHT);

        Button addAllChars = new Button("Set All");
        addAllChars.setStyle(StylesForAll.aliveTheme);
        addAllChars.setOnAction(addMultipleFilesAction);
        getChildren().add(addAllChars);

        //changeImage( image,false);
        StackPane.setAlignment(addAllChars, Pos.TOP_RIGHT);

        EventHandler<MouseEvent> imgMove = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                pressedX = event.getX() + imageView.getX();
                pressedY = event.getY() + imageView.getY();
            }
        };

        EventHandler<MouseEvent> imgMove2 = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                imageView.setTranslateX(imageView.getTranslateX() + event.getX() - pressedX);
                imageView.setTranslateY(imageView.getTranslateY() + event.getY() - pressedY);

                event.consume();
            }
        };

        imageView.setOnMouseClicked(imgMove);
        imageView.setOnMouseDragged(imgMove2);

        //Image resizing
        EventHandler<ActionEvent> inc = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                float scaleInc = (float) (imageView.getScaleX() + imageView.getScaleX() * .1);
                Scale scale = new Scale(scaleInc, scaleInc);
                imageView.getTransforms().add(scale); //rotate by 45 degrees
            }
        };

        EventHandler<ActionEvent> dec = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                float scaleInc = (float) (imageView.getScaleX() - imageView.getScaleX() * .1);
                Scale scale = new Scale(scaleInc, scaleInc);
                imageView.getTransforms().add(scale); //rotate by 45 degrees
            }
        };

        Button incSize = new Button("+");
        incSize.setStyle(StylesForAll.pigletTheme);
        incSize.setOnAction(inc);
        getChildren().add(incSize);
        StackPane.setAlignment(incSize, Pos.TOP_LEFT);
        Button decSize = new Button("-");
        decSize.setOnAction(dec);
        decSize.setStyle(StylesForAll.pigletTheme);
        getChildren().add(decSize);
        StackPane.setAlignment(decSize, Pos.BOTTOM_LEFT);
        

//    Timeline t = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
//      @Override public void handle(ActionEvent event) {
//        frame.set(frame.get() + 1);
//
//        if (rect != null) {
//          getChildren().remove(rect);
//        }
//
//        rect = new Rectangle(10, 10, 200, 200);
//        rect.setFill(Color.RED);
//        rect.setMouseTransparent(true);
//        getChildren().add(0, rect);
//      }
//    }));
//    t.setCycleCount(Timeline.INDEFINITE);
//    t.play();
    }

    public void changeImage(Image image, boolean replace) {
        Canvas canvas = new Canvas(W, H);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawWithShadowUsingStencil(gc, image, IMG_X, IMG_Y, SHADOW_LENGTH, SHADOW_COLOR);
        //   drawWithShadowUsingColorAdjust(gc, image, IMG_X, IMG_Y, SHADOW_LENGTH);
        if (replace) {
            getChildren().remove(getChildren().size() - 1);
        }
        getChildren().add(getChildren().size() - 1, canvas);
        //return canvas;
    }

    private void drawWithShadowUsingColorAdjust(GraphicsContext gc, Image image, double x, double y, int shadowLength) {
        // here the color adjust for the shadow is based upon the intensity of the input image color
        // which is a weird way to calculate a shadow color, but does come out nicely
        // because it appropriately handles antialiased input images.
        ColorAdjust monochrome = new ColorAdjust();
        monochrome.setBrightness(+0.5);
        monochrome.setSaturation(-1.0);

        gc.setEffect(monochrome);

        for (int offset = shadowLength; offset > 0; --offset) {
            gc.drawImage(image, x + offset, y + offset / SHADOW_SLOPE_FACTOR);
        }

        gc.setEffect(null);

        gc.drawImage(image, x, y);
    }

    private void drawWithShadowUsingStencil(GraphicsContext gc, Image image, double x, double y, int shadowLength, Color shadowColor) {
        Image shadow = createShadowImage(image, shadowColor);

        for (int offset = shadowLength; offset > 0; --offset) {
            gc.drawImage(shadow, x + offset, y + offset / SHADOW_SLOPE_FACTOR);
        }

        gc.drawImage(image, x, y);
    }

    private Image createShadowImage(Image image, Color shadowColor) {
        WritableImage shadow = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
        PixelReader reader = shadow.getPixelReader();
        PixelWriter writer = shadow.getPixelWriter();
        for (int ix = 0; ix < image.getWidth(); ix++) {
            for (int iy = 0; iy < image.getHeight(); iy++) {
                int argb = reader.getArgb(ix, iy);
                int a = (argb >> 24) & 0xFF;
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                // because we use a binary choice, we lose anti-alising info in the shadow so it looks a bit jaggy.
                Color fill = (r > 0 || g > 0 || b > 0) ? shadowColor : Color.TRANSPARENT;

                writer.setColor(ix, iy, fill);
            }
        }
        return shadow;
    }

    private Image getImage() {
        Label label = new Label("a");
        label.setStyle("-fx-text-fill: forestgreen; -fx-background-color: transparent; -fx-font-size: " + FONT_SIZE + "px;");
        Scene scene = new Scene(label, Color.TRANSPARENT);
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        return label.snapshot(snapshotParameters, null);
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

                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {

                        if (mouseEvent.getClickCount() == 2) {
                            try {
                                BorderPane borderPane = new BorderPane();
                                ImageView imageView = new ImageView();
                                //Image image = new Image(new FileInputStream(imageFile));
                                Image image = new Image(new FileInputStream(imageFile), 300, 800, true, true);
                                imageView.setImage(image);
                                imageView.setStyle("-fx-background-color: BLACK");
                                imageView.setFitHeight(1024 - 10);
                                imageView.setPreserveRatio(true);
                                imageView.setSmooth(true);
                                imageView.setCache(true);
                                borderPane.setCenter(imageView);
                                borderPane.setStyle("-fx-background-color: BLACK");
                                Stage newStage = new Stage();
                                newStage.setWidth(1024);
                                newStage.setHeight(800);
                                newStage.setTitle(imageFile.getName());
                                Scene scene = new Scene(borderPane, Color.BLACK);
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

    private Life createLife(String name, LifeTagFactory gender, String[] learnToTalk) {
        Male male1 = new Male(name, 40, 5.5, new double[]{5});
        male1.GUN = new CompositeGun(.1f, .1f, -.3f);
        male1.TAG = gender;
        male1.learnToSentence(learnToTalk);

        return male1;
    }

    public Life getLife() {
        return thisCharcter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setLtf(LifeTagFactory ltf) {
        this.ltf = ltf;
    }

    public JSONObject getCharacterData() {
        JSONObject data = new JSONObject();
        data.put("name", name);
        data.put("age", age);
        data.put("ltf", ltf);
        data.put("cpane", CharacterFile);
        data.put("SeedFile", SeedFile);
        JSONArray charlist = new JSONArray(CharacterPool);
        data.put("CharacterPool", charlist);
        return data;
    }

    public void loadCharacterData(JSONObject data) {
        name = data.has("name") ? data.getString("name") : name;
        age = data.has("age") ? data.getString("age") : age;
        SeedFile = data.has("SeedFile") ? data.getString("SeedFile") : SeedFile;
        ltf = data.has("ltf") ? LifeTagFactory.valueOf(data.getString("ltf")) : ltf;

        if (data.has("CharacterPool")) { //String[] charlist = projData.getJSONArray("charlist").toString().replace("},{", " ,").split(" ");
            List<String> list = new ArrayList<String>();
            for (int i = 0; i < data.getJSONArray("CharacterPool").length(); i++) {
                list.add(data.getJSONArray("CharacterPool").getString(i));
            }
            CharacterPool = list.toArray(new String[0]);
        }
        
        thisCharcter=createLife( name,  ltf, getLines(SeedFile));
        playButton.setText(name);
    }
    
    public String[] getLines(String filePath) {
                Set<String> linesRead=new LinkedHashSet<>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			while (line != null) {
				//System.out.println(line);
				// read next line
				line = reader.readLine();
                                linesRead.add(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return linesRead.toArray(new String[0]);
	}
}
