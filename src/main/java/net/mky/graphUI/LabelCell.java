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


import javafx.scene.control.Label;


public class LabelCell extends net.mky.graphUI.Cell {

    public LabelCell(String id) {
        super(id);

        Label view = new Label(id);

        setView(view);

    }

}
