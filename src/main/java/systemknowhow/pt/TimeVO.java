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
public class TimeVO {
    public enum TimeOfDay{MORNING,EVENING,AFTERNOON,NIGHT,LATE_NIGHT,EARLY_MORNING,FOREVER};
    
    TimeOfDay timeOdDay;

    public TimeOfDay getTimeOdDay() {
        return timeOdDay;
    }

    public void setTimeOdDay(TimeOfDay timeOdDay) {
        this.timeOdDay = timeOdDay;
    }
}
