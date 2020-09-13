/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.mky.jfxmpi.TimeLineView;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javax.imageio.ImageIO;
import net.mky.jfxmpi.CharacterPane;
import net.mky.jfxmpi.Collage;
import net.mky.jfxmpi.bookView.BW;
import net.mky.tools.StylesForAll;
import systemknowhow.human.Female;
import systemknowhow.human.Life;
import systemknowhow.human.LifeTagFactory;
import systemknowhow.human.Male;

/**
 *
 * @author mkfs
 */
public class CharactersMenu  extends StackPane  {
    public List<CharacterPane> charactersArray=new LinkedList<>();
    Set<CharacterPane> selectedCharacters=new LinkedHashSet<>();
    HBox menu=new HBox();
     VBox menuV=new VBox();
      boolean vertical;
    ToggleGroup Characters = new ToggleGroup();
    public static enum LIFE_OPTIONS{MALE,FEMALE,SYSTEM};
    public static enum CHARACTER_ROLE{PRIMARY,SECONDARY};
    public CharactersMenu(CharacterPane system){
       // addCharacter(system);
        EventHandler<ActionEvent> addCharacterPane = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Adding character.");
                CharacterPane newCharacter = new CharacterPane((int) system.getWidth(), (int) system.getHeight(), false);
                addCharacter(newCharacter,menu);

            }
        };

        Button addCharPane = new Button("Add Character");
        //addCharPane.getStyleClass().add("play");
        addCharPane.setOnAction(addCharacterPane);
        addCharPane.setStyle(StylesForAll.transparentAlive);
        menu.getChildren().add(addCharPane);
        
        getChildren().add(menu);
    }
    
     public CharactersMenu(CharacterPane system, boolean vertical){
         this.vertical=vertical;
         ScrollPane messageScroller;
        messageScroller = new ScrollPane(menuV);
        messageScroller.setStyle("-fx-background-color: transparent;");
        messageScroller.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        messageScroller.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        messageScroller.setMaxHeight(800);//420
        messageScroller.setPrefWidth(400);//420
       // addCharacter(system);
        EventHandler<ActionEvent> addCharacterPane = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Adding character.");
                CharacterPane newCharacter = new CharacterPane((int) system.getWidth(), (int) system.getHeight(), false);
                addCharacter(newCharacter,menuV);

            }
        };

        Button addCharPane = new Button("Add Character");
        //addCharPane.getStyleClass().add("play");
        addCharPane.setOnAction(addCharacterPane);
        addCharPane.setStyle(StylesForAll.transparentAlive);
        menuV.getChildren().add(addCharPane);
        
        getChildren().add(messageScroller);
    }
     
    /**
     * 
     * @param character 
     */
    public void addCharacter(CharacterPane character, Pane menu) {

        System.out.println("Adding character.");
        if (character.getLife() == null) {
            TextInputDialog td = new TextInputDialog("Name of character");

            // setHeaderText 
            td.setHeaderText("Name of character");
            td.getEditor().setText("Unnamed");
            td.showAndWait();

            String nameOfChar = td.getEditor().getText();
            

            ChoiceDialog<LIFE_OPTIONS> choiceDialog = new ChoiceDialog<>();
            choiceDialog.setHeaderText("Gender of the character");
            choiceDialog.setSelectedItem(LIFE_OPTIONS.SYSTEM);
            choiceDialog.getItems().addAll(LIFE_OPTIONS.values());
            choiceDialog.showingProperty().addListener((ov, b, b1) -> {

                if (b1) {
                    choiceDialog.setContentText("");
                } else {
                    choiceDialog.setContentText(null);
                }

            });

            Optional<LIFE_OPTIONS> optionalResult = choiceDialog.showAndWait();
            optionalResult.ifPresent(result -> {
                System.out.println(result);
                switch (result) {
                    case MALE:
                        Life lifeM = new Male(nameOfChar, 20, 5, new double[]{});
                        character.setLtf(LifeTagFactory.MALE);
                        character.setLife(lifeM);
                        character.setName(nameOfChar);
                        break;
                    case FEMALE:
                        Life lifeF = new Female(nameOfChar, 20, 5, new double[]{});
                        character.setLtf(LifeTagFactory.FEMALE);
                        character.setLife(lifeF);
                        character.setName(nameOfChar);
                        break;
                    case SYSTEM:
                        Life life = new Male(nameOfChar, 20, 5, new double[]{});
                        character.setLtf(LifeTagFactory.MALE);
                        character.setLife(life);
                        character.setName(nameOfChar);
                        break;

                }

            });
            
            
                        
            ChoiceDialog<CHARACTER_ROLE> choiceDialog2 = new ChoiceDialog<>();
            choiceDialog2.setHeaderText("Role of the character");
            choiceDialog2.setSelectedItem(CHARACTER_ROLE.SECONDARY);
            choiceDialog2.getItems().addAll(CHARACTER_ROLE.values());
            choiceDialog2.showingProperty().addListener((ov, b, b1) -> {

                if (b1) {
                    choiceDialog2.setContentText("");
                } else {
                    choiceDialog2.setContentText(null);
                }

            });

             Optional<CHARACTER_ROLE> optionalResult2 = choiceDialog2.showAndWait();
            optionalResult2.ifPresent(result -> {
                System.out.println(result);
                switch (result) {
                    case PRIMARY:
                        character.Role=String.valueOf(CHARACTER_ROLE.PRIMARY);
                        break;
                    case SECONDARY:
                        character.Role=String.valueOf(CHARACTER_ROLE.SECONDARY);
                        break;

                }

            });
            
            
        }
        // CharacterPane newCharacter = new CharacterPane(400, 600, false);
        charactersArray.add(character);
        Button thisCharacter = new Button(character.getLife().getName());
        thisCharacter.setStyle(StylesForAll.transparentAlive);
        thisCharacter.setOnAction(eventIn -> {

            Dialog dialog = new Dialog();
            dialog.getDialogPane().setStyle("-fx-background-color:linear-gradient(to right, #fc5c7d, #6a82fb);");
            dialog.getDialogPane().getStylesheets().add(BW.class.getResource("/book/bw.css").toExternalForm());

            dialog.getDialogPane().setContent(character);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
            dialog.showAndWait();
        });

        if (menu == null) {
            if (!vertical) {
                this.menu.getChildren().add(thisCharacter); //Default is vertical add.
            }
            if (vertical) {
                this.menuV.getChildren().add(thisCharacter); //Default is vertical add.
            }
        }
        else
            menu.getChildren().add(thisCharacter);

    }
    
    public void refreshMenu(){
        menu.getChildren().clear();
       
            
        charactersArray.stream().forEach(character->{
            
         ToggleButton thisCharacter = new ToggleButton("Add");
            thisCharacter.setOnAction(event -> {
               if(thisCharacter.isSelected()){
                   selectedCharacters.add(character);
               }else {
                   selectedCharacters.remove(character);
               }
            });
            
            menu.getChildren().add(thisCharacter);
        });
        
    }
   
    /**
     * Map of all possible combinations. 
     * Half of diagonally symmetric matrix.
     * @return 
     */
    public HashMap<String,List<CharacterPane>> generateCombinations(){
        HashMap<String,List<CharacterPane>> result=new HashMap<>();
        for (int i = 1, max = 1 << charactersArray.size(); i < max; ++i) {
             List<CharacterPane> cpSet=new LinkedList<>();
             String keyrev = "";
            for (int j = 0, k = 1; j < charactersArray.size(); ++j, k <<= 1)
                if ((k & i) != 0)
                {
                    System.out.print(charactersArray.get(j).getLife().getName() + " ");
                    keyrev=keyrev+"X"+charactersArray.get(j).getLife().getName();
                    cpSet.add(charactersArray.get(j));
                }
            
            System.out.println();
            result.put(keyrev, cpSet);
        }
        return result;
    }
    
    
    public HashMap<String,List<CharacterPane>> generateCombinations(List<CharacterPane> charactersArray ){
        HashMap<String,List<CharacterPane>> result=new HashMap<>();
        for (int i = 1, max = 1 << charactersArray.size(); i < max; ++i) {
             List<CharacterPane> cpSet=new LinkedList<>();
             String keyrev = "";
            for (int j = 0, k = 1; j < charactersArray.size(); ++j, k <<= 1)
                if ((k & i) != 0)
                {
                    System.out.print(charactersArray.get(j).getLife().getName() + " ");
                    keyrev=keyrev+"X"+charactersArray.get(j).getLife().getName();
                    cpSet.add(charactersArray.get(j));
                }
            
            System.out.println();
            result.put(keyrev.trim(), cpSet);
        }
        return result;
    }
    
    /**
     * 
     * @param charactersArray
     * @return 
     */
    public boolean hasPrimaryCharacter(List<CharacterPane> charactersArray) {
        for (CharacterPane chars : charactersArray) {
            if (CHARACTER_ROLE.valueOf(chars.Role).equals(CHARACTER_ROLE.PRIMARY)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 
     * @param charactersArray
     * @return
     * @throws IOException 
     */
    public String b64ImageCharacter(List<CharacterPane> charactersArray) throws IOException {
        BufferedImage bimg = ImageIO.read(new File(charactersArray.get(0).CharacterFile));
        for (int i = 1; i < charactersArray.size(); i++) {
            bimg = TimeLineStory.joinBufferedImage(ImageIO.read(new File(charactersArray.get(i).CharacterFile)), bimg);
        }
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bimg, "png", os);
        byte pgnBytes[] = os.toByteArray();
        Base64.Encoder base64_enc = Base64.getEncoder();
        return base64_enc.encodeToString(pgnBytes);

    }

}
