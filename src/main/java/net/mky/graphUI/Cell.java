/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.graphUI;

/**
 *
 * @author Maneesh
 */
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import systemknowhow.human.Life;

public class Cell extends StackPane {

    String cellId;
    
    List<Cell> children = new ArrayList<>();
    List<Cell> parents = new ArrayList<>();

    Node view;

    public Cell(String cellId) {
        this.cellId = cellId;
        Label chatMessage = new Label(cellId);
        getChildren().add(chatMessage);
        StackPane.setAlignment(chatMessage, Pos.BOTTOM_CENTER);
    }

    public void addCellChild(Cell cell) {
        children.add(cell);
    }

    public List<Cell> getCellChildren() {
        return children;
    }

    public void addCellParent(Cell cell) {
        parents.add(cell);
    }

    public List<Cell> getCellParents() {
        return parents;
    }

    public void removeCellChild(Cell cell) {
        children.remove(cell);
    }

    public void setView(Node view) {

        this.view = view;
        getChildren().add(view);
        StackPane.setAlignment(view, Pos.TOP_CENTER);

    }

    public Node getView() {
        return this.view;
    }

    public String getCellId() {
        return cellId;
    }
    
    Life thisLife;

    public Life getThisLife() {
        return thisLife;
    }

    public void setThisLife(Life thisLife) {
        this.thisLife = thisLife;
    }
}