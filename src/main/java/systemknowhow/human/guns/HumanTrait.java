/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow.human.guns;

/**
 *
 * @author Maneesh
 */
public class HumanTrait {
    
    public HumanTrait(TraitCategoryTagFactory tctf,String Positive,String Negative){
        this.TraitCategory=tctf;
        this.Positive=Positive;
        this.Negative=Negative;
    }
    
        private TraitCategoryTagFactory TraitCategory;

    /**
     * Get the value of TraitCategory
     *
     * @return the value of TraitCategory
     */
    public TraitCategoryTagFactory getTraitCategory() {
        return TraitCategory;
    }

    private String Positive;

    /**
     * Get the value of Positive
     *
     * @return the value of Positive
     */
    public String getPositive() {
        return Positive;
    }

    private String Negative;

    /**
     * Get the value of Negative
     *
     * @return the value of Negative
     */
    public String getNegative() {
        return Negative;
    }

}
