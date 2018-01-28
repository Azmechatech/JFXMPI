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
public class LandVO {
    public enum Type{OPEN_SPACE,ROOM,BUILDING,PARK,PLAYGROUND,FOREST};
    
    Type landType;

    public Type getLandType() {
        return landType;
    }

    public void setLandType(Type landType) {
        this.landType = landType;
    }
    
}
