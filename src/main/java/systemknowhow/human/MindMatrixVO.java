/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow.human;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Maneesh
 */
public class MindMatrixVO {
    
    Map<Integer ,Life > SELF = new HashMap ();
    Map<Integer ,Life > LEVEL_ONE= new HashMap ();
    Map<Integer ,Life > LEVEL_TWO= new HashMap ();
    Map<Integer ,Life > LEVEL_REST= new HashMap ();


    public Map getSELF() {
        return SELF;
    }

    /**
     * First two elements will be considered.
     * @param SELF 
     */
    public void setSELF(Map<Integer ,Life > SELF) {
        this.SELF = SELF;
    }

    public Map getLEVEL_ONE() {
        return LEVEL_ONE;
    }

    
    /**
     * First Four elements will be considered
     * @return 
     */
    public void setLEVEL_ONE(Map<Integer ,Life > LEVEL_ONE) {
        this.LEVEL_ONE = LEVEL_ONE;
    }

    public Map getLEVEL_TWO() {
        return LEVEL_TWO;
    }

    
    /**
     * First Twelve elements will be considered
     * @return 
     */
    public void setLEVEL_TWO(Map<Integer ,Life > LEVEL_TWO) {
        this.LEVEL_TWO = LEVEL_TWO;
    }

    public Map getLEVEL_REST() {
        return LEVEL_REST;
    }

    
    /**
     * Any number of elements are fine.
     * @return 
     */
    public void setLEVEL_REST(Map LEVEL_REST) {
        this.LEVEL_REST = LEVEL_REST;
    }
    
    
}
