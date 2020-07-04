/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.mky.jfxmpi.TimeLineView;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import net.mky.jfxmpi.CharacterPane;
import net.mky.jfxmpi.bookView.BW;
import net.mky.tools.StylesForAll;

/**
 *
 * @author mkfs
 */
public class CharactersMenu  extends StackPane  {
    public List<CharacterPane> charactersArray=new LinkedList<>();
    Set<CharacterPane> selectedCharacters=new LinkedHashSet<>();
    HBox menu=new HBox();
    ToggleGroup Characters = new ToggleGroup();
    
    public CharactersMenu(CharacterPane system){
        charactersArray.add(system);
        
        
        EventHandler<ActionEvent> addCharacterPane = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Adding character.");
                CharacterPane newCharacter = new CharacterPane((int) system.getWidth(), (int) system.getHeight(), false);
                charactersArray.add(newCharacter);
                addCharacter(newCharacter);

            }
        };

        Button addCharPane = new Button("Add Character");
        //addCharPane.getStyleClass().add("play");
        addCharPane.setOnAction(addCharacterPane);
        addCharPane.setStyle(StylesForAll.transparentAlive);
        menu.getChildren().add(addCharPane);
        
        getChildren().add(menu);
    }
    
    public void addCharacter(CharacterPane character){
        
        System.out.println("Adding character.");
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
    public HashMap<String,Set<CharacterPane>> generateCombinations(){
        HashMap<String,Set<CharacterPane>> result=new HashMap<>();
        for(CharacterPane cp:charactersArray){
            Set<CharacterPane> cpSet=new LinkedHashSet<>();
            cpSet.add(cp);
            for(CharacterPane cpIn:charactersArray){
                cpSet.add(cpIn);
                
                //Copy set into another set
                Set<CharacterPane> cpTempSet=new LinkedHashSet<>();
                TreeSet<String> uniq=new TreeSet<>();
                for(CharacterPane cpTemp:cpSet){
                    cpTempSet.add(cpTemp);
                    uniq.add(cpTemp.getLife().getName());
                    
                }
                result.put(""+uniq.hashCode(), cpTempSet);
            }
        }
        return result;
    }
    
    

}
