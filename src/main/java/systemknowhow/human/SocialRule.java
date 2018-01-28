/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow.human;

import systemknowhow.humanactivity.SocialRelationTags;
import systemknowhow.humanactivity.SocialActionTagFactory;
import systemknowhow.humanactivity.IndoorActivityTagFactory;
import systemknowhow.humanactivity.OutdoorActivityTagFactory;
import systemknowhow.humanactivity.SecretActivityTagFactory;

/**
 * Social rule systems are used to examine all levels of human interaction.[11]
 * They provide more than potential constraints on action possibilities. They
 * also generate opportunities for social actors to behave in ways that would
 * otherwise be impossible, for instance, to coordinate with others, to mobilize
 * and to gain systematic access to strategic resources, to command and allocate
 * substantial human and physical resources, and to solve complex social
 * problems by organizing collective actions. In guiding and regulating
 * interaction, social rules give behavior recognizable, characteristic
 * patterns, and make such patterns understandable and meaningful for those who
 * share in the rule knowledge.
 *
 * @author Maneesh
 */
public class SocialRule extends Rules {
    //All if and elses between Lives
    String rule;
    public SocialRule(SocialRelationTags srff,int IndoorActivitySize,int OutdoorActivitySize,int SecrectActivitySize,int SocialActionSize ){
        rule=srff.toString();
        IndoorActivity=new IndoorActivityTagFactory[IndoorActivitySize];
        OutdoorActivity=new OutdoorActivityTagFactory[OutdoorActivitySize];
        SecretActivity=new SecretActivityTagFactory[SecrectActivitySize];
        SocialActionAllowed=new SocialActionTagFactory[SocialActionSize];
    }
    
        private IndoorActivityTagFactory[] IndoorActivity;

    /**
     * Get the value of IndoorActivity
     *
     * @return the value of IndoorActivity
     */
    public IndoorActivityTagFactory[] getIndoorActivity() {
        return IndoorActivity;
    }

    /**
     * Set the value of IndoorActivity
     *
     * @param IndoorActivity new value of IndoorActivity
     */
    public void setIndoorActivity(IndoorActivityTagFactory[] IndoorActivity) {
        this.IndoorActivity = IndoorActivity;
    }

    /**
     * Get the value of IndoorActivity at specified index
     *
     * @param index the index of IndoorActivity
     * @return the value of IndoorActivity at specified index
     */
    public IndoorActivityTagFactory getIndoorActivity(int index) {
        return this.IndoorActivity[index];
    }

    /**
     * Set the value of IndoorActivity at specified index.
     *
     * @param index the index of IndoorActivity
     * @param IndoorActivity new value of IndoorActivity at specified index
     */
    public void setIndoorActivity(int index, IndoorActivityTagFactory IndoorActivity) {
        this.IndoorActivity[index] = IndoorActivity;
    }

    private OutdoorActivityTagFactory[] OutdoorActivity;

    /**
     * Get the value of OutdoorActivity
     *
     * @return the value of OutdoorActivity
     */
    public OutdoorActivityTagFactory[] getOutdoorActivity() {
        return OutdoorActivity;
    }

    /**
     * Set the value of OutdoorActivity
     *
     * @param OutdoorActivity new value of OutdoorActivity
     */
    public void setOutdoorActivity(OutdoorActivityTagFactory[] OutdoorActivity) {
        this.OutdoorActivity = OutdoorActivity;
    }

    /**
     * Get the value of OutdoorActivity at specified index
     *
     * @param index the index of OutdoorActivity
     * @return the value of OutdoorActivity at specified index
     */
    public OutdoorActivityTagFactory getOutdoorActivity(int index) {
        return this.OutdoorActivity[index];
    }

    /**
     * Set the value of OutdoorActivity at specified index.
     *
     * @param index the index of OutdoorActivity
     * @param OutdoorActivity new value of OutdoorActivity at specified index
     */
    public void setOutdoorActivity(int index, OutdoorActivityTagFactory OutdoorActivity) {
        this.OutdoorActivity[index] = OutdoorActivity;
    }

        private SecretActivityTagFactory[] SecretActivity;

    /**
     * Get the value of SecretActivity
     *
     * @return the value of SecretActivity
     */
    public SecretActivityTagFactory[] getSecretActivity() {
        return SecretActivity;
    }

    /**
     * Set the value of SecretActivity
     *
     * @param SecretActivity new value of SecretActivity
     */
    public void setSecretActivity(SecretActivityTagFactory[] SecretActivity) {
        this.SecretActivity = SecretActivity;
    }

    /**
     * Get the value of SecretActivity at specified index
     *
     * @param index the index of SecretActivity
     * @return the value of SecretActivity at specified index
     */
    public SecretActivityTagFactory getSecretActivity(int index) {
        return this.SecretActivity[index];
    }

    /**
     * Set the value of SecretActivity at specified index.
     *
     * @param index the index of SecretActivity
     * @param SecretActivity new value of SecretActivity at specified index
     */
    public void setSecretActivity(int index, SecretActivityTagFactory SecretActivity) {
        this.SecretActivity[index] = SecretActivity;
    }

    
    private SocialActionTagFactory[] SocialActionAllowed;

    /**
     * Get the value of SocialActionAllowed
     *
     * @return the value of SocialActionAllowed
     */
    public SocialActionTagFactory[] getSocialActionAllowed() {
        return SocialActionAllowed;
    }

    /**
     * Set the value of SocialActionAllowed
     *
     * @param SocialActionAllowed new value of SocialActionAllowed
     */
    public void setSocialActionAllowed(SocialActionTagFactory[] SocialActionAllowed) {
        this.SocialActionAllowed = SocialActionAllowed;
    }

    /**
     * Get the value of SocialActionAllowed at specified index
     *
     * @param index the index of SocialActionAllowed
     * @return the value of SocialActionAllowed at specified index
     */
    public SocialActionTagFactory getSocialActionAllowed(int index) {
        return this.SocialActionAllowed[index];
    }

    /**
     * Set the value of SocialActionAllowed at specified index.
     *
     * @param index the index of SocialActionAllowed
     * @param SocialActionAllowed new value of SocialActionAllowed at specified
     * index
     */
    public void setSocialActionAllowed(int index, SocialActionTagFactory SocialActionAllowed) {
        this.SocialActionAllowed[index] = SocialActionAllowed;
    }

}
