/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow.human;

import systemknowhow.humanactivity.IndoorActivityTagFactory;
import systemknowhow.humanactivity.OutdoorActivityTagFactory;
import systemknowhow.humanactivity.SecretActivityTagFactory;
import systemknowhow.humanactivity.SenseNameTagFactory;
import systemknowhow.humanactivity.SocialActionTagFactory;

/**
 *
 * @author Maneesh
 */
public class Female extends Life {
    public Female(String Name,int Age,double Height,double other[]){
        this.Name=Name;
        setAge(Age);
        this.Height=Height;
        this.Other=other;
        init();
        senses.put(SenseNameTagFactory.HEARING, new Ear());
        senses.put(SenseNameTagFactory.SIGHT, new Ear());
        senses.put(SenseNameTagFactory.SMELL, new Ear());
        senses.put(SenseNameTagFactory.TASTE, new Ear());
        senses.put(SenseNameTagFactory.TOUCH, new Ear());
    }
    
        private double[] Other;

    /**
     * Get the value of Other
     *
     * @return the value of Other
     */
    public double[] getOther() {
        return Other;
    }

    /**
     * Set the value of Other
     *
     * @param Other new value of Other
     */
    public void setOther(double[] Other) {
        this.Other = Other;
    }

    /**
     * Get the value of Other at specified index
     *
     * @param index the index of Other
     * @return the value of Other at specified index
     */
    public double getOther(int index) {
        return this.Other[index];
    }

    /**
     * Set the value of Other at specified index.
     *
     * @param index the index of Other
     * @param Other new value of Other at specified index
     */
    public void setOther(int index, double Other) {
        this.Other[index] = Other;
    }


    private double Height=4;

    /**
     * Get the value of Height
     *
     * @return the value of Height
     */
    public double getHeight() {
        return Height;
    }

    /**
     * Set the value of Height
     *
     * @param Height new value of Height
     */
    public void setHeight(double Height) {
        this.Height = Height;
    }

        private String Name;

    /**
     * Get the value of Name
     *
     * @return the value of Name
     */
    public String getName() {
        return Name;
    }

    /**
     * Set the value of Name
     *
     * @param Name new value of Name
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    @Override
    public SocialActionTagFactory makeSocialDecision(Life withThis) {
        SocialRule sr=(SocialRule) RuleFactory.rules.get(relations.get(withThis));
    return sr.getSocialActionAllowed((int) (Math.random()*3));
    }

    @Override
    public SecretActivityTagFactory makeSecretDecision(Life withThis) {
    SocialRule sr=(SocialRule) RuleFactory.rules.get(relations.get(withThis));
    return sr.getSecretActivity((int) (Math.random()*3));  }

    @Override
    public OutdoorActivityTagFactory makeOutdoorDecision(Life withThis) {
        SocialRule sr=(SocialRule) RuleFactory.rules.get(relations.get(withThis));
    return sr.getOutdoorActivity((int) (Math.random()*3));  
    }

    @Override
    public IndoorActivityTagFactory makeIndoorDecision(Life withThis) {
       SocialRule sr=(SocialRule) RuleFactory.rules.get(relations.get(withThis));
    return sr.getIndoorActivity((int) (Math.random()*3));  
    }


    @Override
    public Goal setGoal(Goal tag) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
