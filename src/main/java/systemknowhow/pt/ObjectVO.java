/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow.pt;

/**
 *
 * @author Maneesh
 */
public class ObjectVO {
    public static enum PhysicalObject{CHAIR,TABLE,BED,SOFA,TV,TEATABLE,BEDSHEET,CURTAIN,WINDOW,STOVE,PHONE};
    
    PhysicalObject physicalObject;

    public PhysicalObject getPhysicalObject() {
        return physicalObject;
    }

    public void setPhysicalObject(PhysicalObject physicalObject) {
        this.physicalObject = physicalObject;
    }
    
    //Add size and shapes.
}
