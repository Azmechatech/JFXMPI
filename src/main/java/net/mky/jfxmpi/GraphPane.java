/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.jfxmpi;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.BorderPane;
import net.mky.graphUI.CellType;
import net.mky.graphUI.Graph;
import net.mky.tools.Model;
import systemknowhow.human.Life;

/**
 *
 * @author Maneesh
 */
public class GraphPane extends BorderPane  {
    Graph graph;
    
    public GraphPane(){
            graph = new Graph();
            graph.getScrollPane().setStyle("-fx-background-color: transparent;");
            setCenter(graph.getScrollPane());
            setStyle("-fx-background-color: transparent;");
            //addGraphComponents();
    }
    
    private void addGraphComponents() {

        Model model = graph.getModel();

        graph.beginUpdate();

        model.addCell("Cell A", CellType.RECTANGLE);
        model.addCell("Cell B", CellType.RECTANGLE);
        model.addCell("Cell C", CellType.RECTANGLE);
        model.addCell("Cell D", CellType.TRIANGLE);
        model.addCell("Cell E", CellType.TRIANGLE);
        model.addCell("Cell F", CellType.RECTANGLE);
        model.addCell("Cell G", CellType.RECTANGLE);

        model.addEdge("Cell A", "Cell B");
        model.addEdge("Cell A", "Cell C");
        model.addEdge("Cell B", "Cell C");
        model.addEdge("Cell C", "Cell D");
        model.addEdge("Cell B", "Cell E");
        model.addEdge("Cell D", "Cell F");
        model.addEdge("Cell D", "Cell G");

        graph.endUpdate();

    }
    
     public void addGraphComponents(ArrayList<Life> lives) {

        Model model = graph.getModel();

        graph.beginUpdate();

        lives.parallelStream().forEach(l -> model.addCell(l, CellType.LIFE));
//        
//        model.addCell("Cell A", CellType.RECTANGLE);
//        model.addCell("Cell B", CellType.RECTANGLE);
//        model.addCell("Cell C", CellType.RECTANGLE);
//        model.addCell("Cell D", CellType.TRIANGLE);
//        model.addCell("Cell E", CellType.TRIANGLE);
//        model.addCell("Cell F", CellType.RECTANGLE);
//        model.addCell("Cell G", CellType.RECTANGLE);

        lives.parallelStream().forEach(l -> l.relations.entrySet().parallelStream().forEach(other -> model.addEdge(l, other.getKey())));
//        model.addEdge("Cell A", "Cell B");
//        model.addEdge("Cell A", "Cell C");
//        model.addEdge("Cell B", "Cell C");
//        model.addEdge("Cell C", "Cell D");
//        model.addEdge("Cell B", "Cell E");
//        model.addEdge("Cell D", "Cell F");
//        model.addEdge("Cell D", "Cell G");

        graph.endUpdate();

    }

     public void addLifeComponents(List<CharacterPane> lives) {

        Model model = graph.getModel();

        graph.beginUpdate();

        lives.forEach(l -> model.addCell(l.getLife().getName(), CellType.TRIANGLE));


        lives.forEach(l -> l.getLife().relations.entrySet().forEach(other -> model.addEdge(l.getLife().getName(), other.getKey().getName())));


        graph.endUpdate();

    }

   
     

}
