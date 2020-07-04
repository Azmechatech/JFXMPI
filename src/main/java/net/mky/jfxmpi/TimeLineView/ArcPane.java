/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi.TimeLineView;

import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import net.mky.tools.StylesForAll;

/**
 *
 * @author mkfs
 */
public class ArcPane  extends StackPane {
    
    public ArcPane(SpeechBox sb,Button button,String previewText){
        sb.textArea.setVisible(false);
        Label displayedText = new Label(previewText);
        displayedText.setPadding(new Insets(5));
        displayedText.setWrapText(true);
        displayedText.setStyle(StylesForAll.transparentAliveLabel);
        VBox previewmisc=new VBox(button,displayedText);
        HBox hbox=new HBox(sb,previewmisc);
        getChildren().add(hbox);
        
    }
    
    public ArcPane(List<SpeechBox> sbs,Button button,String previewText){
        try{
        Label displayedText = new Label(previewText);
        displayedText.setPadding(new Insets(5));
        displayedText.setWrapText(true);
        displayedText.setStyle(StylesForAll.transparentAliveLabel);
        
        HBox sbPreviews=new HBox(5);
        for(int i=1;i<sbs.size();i++){
            sbPreviews.getChildren().add(sbs.get(i));
            sbs.get(i).textArea.setVisible(false);
        }
        VBox previewmisc=new VBox(button,displayedText,sbPreviews);
        previewmisc.setSpacing(5);
        HBox hbox=new HBox(sbs.get(0),previewmisc);
        hbox.setSpacing(7);
        
        getChildren().add(hbox);
        }catch(Exception ex){}
    }
}
