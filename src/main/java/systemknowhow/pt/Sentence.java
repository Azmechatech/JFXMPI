/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow.pt;

import systemknowhow.human.guns.CompositeGun;
import systemknowhow.humanactivity.VerbTagFactory;

/**
 *
 * @author Maneesh
 */
public class Sentence {
    
        private String key;

    /**
     * Get the value of key
     *
     * @return the value of key
     */
    public String getKey() {
        return key;
    }

    /**
     * Set the value of key
     *
     * @param key new value of key
     */
    public void setKey(String key) {
        this.key = key;
    }

    
    private CompositeGun gun;

    /**
     * Get the value of gun
     *
     * @return the value of gun
     */
    public CompositeGun getGun() {
        return gun;
    }

    /**
     * Set the value of gun
     *
     * @param gun new value of gun
     */
    public void setGun(CompositeGun gun) {
        this.gun = gun;
    }

        private String SentenceTemplate;

    /**
     * Get the value of SentenceTemplate
     *
     * @return the value of SentenceTemplate
     */
    public String getSentenceTemplate() {
        return SentenceTemplate;
    }

    /**
     * Set the value of SentenceTemplate
     *
     * @param SentenceTemplate new value of SentenceTemplate
     */
    public void setSentenceTemplate(String SentenceTemplate) {
        this.SentenceTemplate = SentenceTemplate;
    }
    
    private String LifeActionTag;

    /**
     * Get the value of LifeActionTag
     *
     * @return the value of LifeActionTag
     */
    public String getLifeActionTag() {
        return LifeActionTag;
    }

    /**
     * Set the value of LifeActionTag
     *
     * @param LifeActionTag new value of LifeActionTag
     */
    public void setLifeActionTag(String LifeActionTag) {
        this.LifeActionTag = LifeActionTag;
    }

        private VerbTagFactory verbTag;

    /**
     * Get the value of verbTag
     *
     * @return the value of verbTag
     */
    public VerbTagFactory getVerbTag() {
        return verbTag;
    }

    /**
     * Set the value of verbTag
     *
     * @param verbTag new value of verbTag
     */
    public void setVerbTag(VerbTagFactory verbTag) {
        this.verbTag = verbTag;
    }


}
