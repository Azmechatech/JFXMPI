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
public class CompositeGun {
    // Scale
    //-.5 to .5 
    // -1 to 0
    // 0 to 1
    float SATO_GUN_TATVA_GYANI=0f;//Max 1
    float RAJO_GUN_BRAHM_GYANI=-.5f;//Max .5
    float TAMO_GUN_RUDRA_GYANI=-1f;//Max 0
    float GUN_INCR=.2f;//Increase faster
    float GUN_DECR=-.1f;//Decrease slower
    
    public  CompositeGun(float SATO,float RAJO,float TAMO){
        SATO_GUN_TATVA_GYANI=SATO>0 & SATO<1 ?SATO:1/2;
        RAJO_GUN_BRAHM_GYANI=RAJO>-.5 & RAJO<.5 ?RAJO:0/2;
        TAMO_GUN_RUDRA_GYANI=TAMO>-1 & TAMO<0 ?TAMO:-1/2;
    }
    
    public float getGunIndex(){
        return SATO_GUN_TATVA_GYANI+RAJO_GUN_BRAHM_GYANI+TAMO_GUN_RUDRA_GYANI;
    }
    
    public GunTagFactory getGunInclination(){
        return getGunIndex()>0& getGunIndex()<1?GunTagFactory.SATO_GUN_TATVA_GYANI:
                getGunIndex()>-.5& getGunIndex()<.5?GunTagFactory.RAJO_GUN_BRAHM_GYANI:
                getGunIndex()>-1& getGunIndex()<0?GunTagFactory.TAMO_GUN_RUDRA_GYANI:GunTagFactory.NIRGUN;
    }
    
    public boolean isGunTowards(GunTagFactory gtf){
        return getGunInclination()==gtf;
    }
    
    public float distanceTo(GunTagFactory gtf) {
        switch (gtf) {
            case SATO_GUN_TATVA_GYANI:
                return 1 - getGunIndex();
            case RAJO_GUN_BRAHM_GYANI:
                return .5f - getGunIndex();
            case TAMO_GUN_RUDRA_GYANI:
                return 0 - getGunIndex();
            default:
                return 0;
        }
    }
    
    public void increment(GunTagFactory gtf){
        switch (gtf) {
            case SATO_GUN_TATVA_GYANI:
                SATO_GUN_TATVA_GYANI = SATO_GUN_TATVA_GYANI < 1 ? SATO_GUN_TATVA_GYANI + GUN_INCR : SATO_GUN_TATVA_GYANI;
                break;
            case RAJO_GUN_BRAHM_GYANI:
                RAJO_GUN_BRAHM_GYANI = RAJO_GUN_BRAHM_GYANI < .5 ? RAJO_GUN_BRAHM_GYANI + GUN_INCR : RAJO_GUN_BRAHM_GYANI;
                break;
            case TAMO_GUN_RUDRA_GYANI:
                TAMO_GUN_RUDRA_GYANI = TAMO_GUN_RUDRA_GYANI < 0 ? TAMO_GUN_RUDRA_GYANI + GUN_INCR : TAMO_GUN_RUDRA_GYANI;
                break;
        }   
    }
    
    public void decrease(GunTagFactory gtf){
        switch (gtf) {
            case SATO_GUN_TATVA_GYANI:
                SATO_GUN_TATVA_GYANI = SATO_GUN_TATVA_GYANI > 0 ? SATO_GUN_TATVA_GYANI + GUN_DECR : SATO_GUN_TATVA_GYANI;
                break;
            case RAJO_GUN_BRAHM_GYANI:
                RAJO_GUN_BRAHM_GYANI = RAJO_GUN_BRAHM_GYANI > -.5 ? RAJO_GUN_BRAHM_GYANI + GUN_DECR : RAJO_GUN_BRAHM_GYANI;
                break;
            case TAMO_GUN_RUDRA_GYANI:
                TAMO_GUN_RUDRA_GYANI = TAMO_GUN_RUDRA_GYANI > -1 ? TAMO_GUN_RUDRA_GYANI + GUN_DECR : TAMO_GUN_RUDRA_GYANI;
                break;
        }   
    }
    
    @Override
    public String toString(){
        
        return "S"+SATO_GUN_TATVA_GYANI+"R"+RAJO_GUN_BRAHM_GYANI+"T"+TAMO_GUN_RUDRA_GYANI;
}
}
