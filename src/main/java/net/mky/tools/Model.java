/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.tools;

/**
 *
 * @author Maneesh
 */
import net.mky.graphUI.RectangleCell;
import net.mky.graphUI.TriangleCell;
import net.mky.graphUI.CellType;
import net.mky.graphUI.Edge;
import net.mky.graphUI.Cell;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.mky.graphUI.ButtonCell;
import net.mky.graphUI.ImageCell;
import systemknowhow.human.Life;


public class Model {

    Cell graphParent;

    List<Cell> allCells;
    List<Cell> addedCells;
    List<Cell> removedCells;

    List<Edge> allEdges;
    List<Edge> addedEdges;
    List<Edge> removedEdges;

    Map<String,Cell> cellMap; // <id,cell>

    public Model() {

         graphParent = new Cell( "_ROOT_");

         // clear model, create lists
         clear();
    }

    public void clear() {

        allCells = new ArrayList<>();
        addedCells = new ArrayList<>();
        removedCells = new ArrayList<>();

        allEdges = new ArrayList<>();
        addedEdges = new ArrayList<>();
        removedEdges = new ArrayList<>();

        cellMap = new HashMap<>(); // <id,cell>

    }

    public void clearAddedLists() {
        addedCells.clear();
        addedEdges.clear();
    }

    public List<Cell> getAddedCells() {
        return addedCells;
    }

    public List<Cell> getRemovedCells() {
        return removedCells;
    }

    public List<Cell> getAllCells() {
        return allCells;
    }

    public List<Edge> getAddedEdges() {
        return addedEdges;
    }

    public List<Edge> getRemovedEdges() {
        return removedEdges;
    }

    public List<Edge> getAllEdges() {
        return allEdges;
    }

    public void addCell(String id, CellType type) {

        switch (type) {

        case RECTANGLE:
            RectangleCell rectangleCell = new RectangleCell(id);
            addCell(rectangleCell);
            break;

        case TRIANGLE:
            TriangleCell circleCell = new TriangleCell(id);
            addCell(circleCell);
            break;

            case BUTTON:
                ButtonCell ButtonCell = new ButtonCell(id);
            addCell(ButtonCell);
            break;
         
                case IMAGE:
                ImageCell imageCell = new ImageCell(id,"");
            addCell(imageCell);
            break;
            
         case LIFE:
            ImageCell imageCell2 = new ImageCell(id,"");
            
            addLifeCell(imageCell2);
            break;
            
        default:
            throw new UnsupportedOperationException("Unsupported type: " + type);
        }
    }

    public void addCell(Life life, CellType type) {

        switch (type) {

         case LIFE:
            ImageCell imageCell = new ImageCell(life.getName(),"");
            imageCell.setThisLife(life);
            addLifeCell(imageCell);
            break;
            
        default:
            throw new UnsupportedOperationException("Unsupported type: " + type);
        }
    }
    
    private void addCell( Cell cell) {

        addedCells.add(cell);

        cellMap.put( cell.getCellId(), cell);

    }
    
    private void addLifeCell( Cell cell) {

        addedCells.add(cell);

        cellMap.put( cell.getThisLife().getName(), cell);

    }

    public void addEdge( String sourceId, String targetId) {

        Cell sourceCell = cellMap.get( sourceId);
        Cell targetCell = cellMap.get( targetId);

        Edge edge = new Edge( sourceCell, targetCell);

        addedEdges.add( edge);

    }
    
    public void addEdge( Life sourceId, Life  targetId) {

        Cell sourceCell = cellMap.get( sourceId.getName());
        Cell targetCell = cellMap.get( targetId.getName());

        Edge edge = new Edge( sourceCell, targetCell);

        addedEdges.add( edge);

    }

    /**
     * Attach all cells which don't have a parent to graphParent 
     * @param cellList
     */
    public void attachOrphansToGraphParent( List<Cell> cellList) {

        for( Cell cell: cellList) {
            if( cell.getCellParents().size() == 0) {
                graphParent.addCellChild( cell);
            }
        }

    }

    /**
     * Remove the graphParent reference if it is set
     * @param cellList
     */
    public void disconnectFromGraphParent( List<Cell> cellList) {

        for( Cell cell: cellList) {
            graphParent.removeCellChild( cell);
        }
    }

    public void merge() {

        // cells
        allCells.addAll( addedCells);
        allCells.removeAll( removedCells);

        addedCells.clear();
        removedCells.clear();

        // edges
        allEdges.addAll( addedEdges);
        allEdges.removeAll( removedEdges);

        addedEdges.clear();
        removedEdges.clear();

    }
}