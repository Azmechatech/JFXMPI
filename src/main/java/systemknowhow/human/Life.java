/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow.human;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.hypergraphdb.HyperGraph;
import systemknowhow.human.guns.CompositeGun;
import systemknowhow.humanactivity.IndoorActivityTagFactory;
import systemknowhow.humanactivity.OutdoorActivityTagFactory;
import systemknowhow.humanactivity.SecretActivityTagFactory;
import systemknowhow.humanactivity.SenseNameTagFactory;
import systemknowhow.humanactivity.SocialActionTagFactory;
import systemknowhow.humanactivity.SocialRelationTags;
import systemknowhow.pt.Sentence;
import systemknowhow.pt.SentenceFactory;
import systemknowhow.tools.NERHelper;

/**
 * Life is a characteristic distinguishing physical entities having biological
 * processes (such as signaling and self-sustaining processes) from those that
 * do not,[1][2] either because such functions have ceased (death), or because
 * they lack such functions and are classified as inanimate.
 *
 * @author Maneesh
 */
public abstract class Life {
    
    void init(){
        //Construct its memory as graph and blockchain
//        HyperGraph graph = new HyperGraph("/path/to/workdir/bje");
//        String hello = graph.get(graph.add("Hello World")); 
//        System.out.println(hello.toLowerCase());
//        graph.close();
    }
    public HashMap<SenseNameTagFactory,Sense> senses=new HashMap<>();;
    
    public Map<Life,SocialRelationTags > relations=new HashMap<>();;
    public CompositeGun GUN;
    public LifeTagFactory TAG;
    
    private int age = 18;

    /**
     * Get the value of age
     *
     * @return the value of age
     */
    public int getAge() {
        return age;
    }

    /**
     * Set the value of age
     *
     * @param age new value of age
     */
    public void setAge(int age) {
        this.age = age;
    }

        private HashMap SentencePattern;

    /**
     * Get the value of SentencePattern
     *
     * @return the value of SentencePattern
     */
    public HashMap getSentencePattern() {
        return SentencePattern;
    }

    /**
     * Set the value of SentencePattern
     *
     * @param SentencePattern new value of SentencePattern
     */
    public void setSentencePattern(HashMap SentencePattern) {
        this.SentencePattern = SentencePattern;
    }

     String Name;

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

    //Life runs senses and generate Behaviour or FixedActionPattern.
    abstract SocialActionTagFactory makeSocialDecision(Life withThis);

    abstract SecretActivityTagFactory makeSecretDecision(Life withThis);

    abstract OutdoorActivityTagFactory makeOutdoorDecision(Life withThis);

    abstract IndoorActivityTagFactory makeIndoorDecision(Life withThis);
    
    abstract Goal setGoal(Goal tag);
    
    public void learnToSentence(String[] sentences){
        /**
         * 1. In each sentence find this name
         * 2. Tag all of them with this name in hashMap
         * 3. Search the other Names
         * 4. Tag those sentences with other
         * 5. Also mark the common ones.
         */ 
        for(String sentence: sentences){
            Sentence sentenceObject=new Sentence();
            sentenceObject.setGun(GUN);
            try {
                sentenceObject.setSentenceTemplate(NERHelper.getMarked(sentence));
                sentenceObject.setKey(GUN.toString()+"L"+TAG);
            }  catch (Exception ex) {
                Logger.getLogger(Life.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(sentence!=null)
            SentenceFactory.add(sentenceObject);
        }
    }
    
    /**
     * getSentenceTemplate().replaceAll("{{me}}", Name).replaceAll("{{other}}", toThisPerson.getName())
     * @param toThisPerson
     * @param topicSearch
     * @return 
     */
    public Set<Sentence> talk(Life toThisPerson,String topicSearch){
        //.getSentenceTemplate().replaceAll("{{me}}", Name).replaceAll("{{other}}", toThisPerson.getName())
        return SentenceFactory.getSentence(topicSearch,GUN.toString()+"L"+TAG);
    }

}
