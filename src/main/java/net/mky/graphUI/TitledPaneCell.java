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


import javafx.scene.control.TitledPane;



public class TitledPaneCell extends net.mky.graphUI.Cell {

    public TitledPaneCell(String id) {
        super(id);

        TitledPane view = new TitledPane();
        view.setPrefSize(100, 80);

        setView(view);

    }

}