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
public class AirVO {
    double temperature;

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    
    double speed;

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    enum Smell{NATURAL,ROSE,STINK,FOOD};
    
    Smell SmellOfAir;

    public Smell getSmellOfAir() {
        return SmellOfAir;
    }

    public void setSmellOfAir(Smell SmellOfAir) {
        this.SmellOfAir = SmellOfAir;
    }
}
