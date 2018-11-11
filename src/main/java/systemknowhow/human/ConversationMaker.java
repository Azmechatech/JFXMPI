/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow.human;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import systemknowhow.pt.Sentence;

/**
 *
 * @author Maneesh
 */
public class ConversationMaker {
    Life lifeA; Life lifeB; String seedSentenceA; String seedSentenceB;
    public boolean ATalks = true;
            boolean ATalkInfinite = false;
            boolean BUnAnswered = true;
            Set<String> ignoreKeys=new HashSet();
            String seedWordA;
            String seedWordB;
            
    public ConversationMaker(){
        
    }
    
    public void init(Life lifeA, Life lifeB, String seedSentenceA, String seedSentenceB){
    this.lifeA=lifeA;
    this.lifeB=lifeB;
    this.seedSentenceA=seedSentenceA;
    this.seedSentenceB=seedSentenceB;
    
     seedWordA = getMaxFreqWord(seedSentenceA,false);
         seedWordB = getMaxFreqWord(seedSentenceB,false);
        ignoreKeys=new HashSet();
        
    }
    
    public String getOutdoorAction(){
        return lifeA.makeOutdoorDecision(lifeB).toString();
    }
    
    public String getindoorAction(){
        return lifeA.makeIndoorDecision(lifeB).toString();
    }
    
    public String getSecretAction(){
        return lifeA.makeSecretDecision(lifeB).toString();
    }
    
    public String getNextSenetce(){
        String result="";
        long timeDelay=10*(seedSentenceA.length()+seedSentenceB.length());
                if (ATalks) {

                    Set<Sentence> tempSentenceA = lifeA.talk(lifeB, seedWordA);
                    // tempSentenceA.forEach(tp-> System.out.println(tp.getSentenceTemplate().replaceAll("##me##", lifeA.Name).replaceAll("##other##", lifeB.getName())));
                    if (tempSentenceA.size() == 0) {
                        seedWordA = getMaxFreqWord(seedSentenceA,true);
                        getNextSenetce();
                    }
                    for (Iterator it = tempSentenceA.iterator(); it.hasNext();) {
                        Sentence keys = (Sentence) it.next();
                        if(ignoreKeys.contains(keys.hashCode()+"")) continue;
                        seedSentenceA=keys.getSentenceTemplate().replaceAll("##me##", lifeA.Name).replaceAll("##other##", lifeB.getName());
                        System.out.println(timeDelay+":"+seedWordA+">" + seedSentenceA);
                        result= seedSentenceA;
                        //String temp=seedWord;
                        seedWordB = getMaxFreqWord(seedSentenceA,false); //Exchange the seedWord
                        //if(temp.equals(seedWord)) continue;//Pick another seed word

                        if (BUnAnswered) {
                            BUnAnswered = false;
                            
                            break;
                        }
                        ignoreKeys.add(keys.hashCode()+"");
                        ATalks = !ATalks;
                        break;
                    }
                } else {

                    Set<Sentence> tempSentenceB = lifeB.talk(lifeA, seedWordB);
                    //tempSentenceB.forEach(tp-> System.out.println(tp.getSentenceTemplate().replaceAll("##me##", lifeB.Name).replaceAll("##other##", lifeA.getName())));
                    if (tempSentenceB.size() == 0) {
                        seedWordB = getMaxFreqWord(seedSentenceB,true);
                        BUnAnswered = true;
                        getNextSenetce();
                    }
                    for (Iterator it = tempSentenceB.iterator(); it.hasNext();) {
                        Sentence keys = (Sentence) it.next();
                        if(ignoreKeys.contains(keys.hashCode()+"")) continue;
                        seedSentenceB=keys.getSentenceTemplate().replaceAll("##me##", lifeB.getName()).replaceAll("##other##",lifeA.Name );
                        System.out.println(timeDelay+":"+seedWordB+">" + seedSentenceB);
                        result= seedSentenceB;
                        seedWordA = getMaxFreqWord(seedSentenceB,false);
                        ATalks = !ATalks;
                        ignoreKeys.add(keys.hashCode()+"");
                        BUnAnswered = false;
                        break;
                    }
                }

//                try {
//                    Thread.sleep(timeDelay);
//                } catch (InterruptedException ex) {
//                    Logger.getLogger(ConversationMaker.class.getName()).log(Level.SEVERE, null, ex);
//                }
        
        return result;
    
    }
    
    public static HashMap<String, String> getConversation(Life lifeA, Life lifeB, String seedSentenceA, String seedSentenceB) {

        String seedWordA = getMaxFreqWord(seedSentenceA,false);
        String seedWordB = getMaxFreqWord(seedSentenceB,false);
        Set<String> ignoreKeys=new HashSet();

        //seedWordB=seedWordA;
        
        //Set<Sentence> sentenceA = lifeA.talk(lifeB, seedWordA);
        //sentenceA.forEach(tp-> System.out.println(tp.getSentenceTemplate().replaceAll("##me##", lifeA.Name).replaceAll("##other##", lifeB.getName())));

        //Set<Sentence> sentenceB = lifeB.talk(lifeA, seedWordB);
        //sentenceB.forEach(tp-> System.out.println(tp.getSentenceTemplate().replaceAll("##me##", lifeB.getName()).replaceAll("##other##", lifeA.getName())));

    
            boolean ATalks = true;
            boolean ATalkInfinite = false;
            boolean BUnAnswered = true;
            while (!ATalkInfinite) {
                long timeDelay=10*(seedSentenceA.length()+seedSentenceB.length());
                if (ATalks) {

                    Set<Sentence> tempSentenceA = lifeA.talk(lifeB, seedWordA);
                    // tempSentenceA.forEach(tp-> System.out.println(tp.getSentenceTemplate().replaceAll("##me##", lifeA.Name).replaceAll("##other##", lifeB.getName())));
                    if (tempSentenceA.size() == 0) {
                        seedWordA = getMaxFreqWord(seedSentenceA,true);
                        continue;
                    }
                    for (Iterator it = tempSentenceA.iterator(); it.hasNext();) {
                        Sentence keys = (Sentence) it.next();
                        if(ignoreKeys.contains(keys.hashCode()+"")) continue;
                        seedSentenceA=keys.getSentenceTemplate().replaceAll("##me##", lifeA.Name).replaceAll("##other##", lifeB.getName());
                        System.out.println(timeDelay+":"+seedWordA+">" + seedSentenceA);
                        
                        //String temp=seedWord;
                        seedWordB = getMaxFreqWord(seedSentenceA,false); //Exchange the seedWord
                        //if(temp.equals(seedWord)) continue;//Pick another seed word

                        if (BUnAnswered) {
                            BUnAnswered = false;
                            
                            break;
                        }
                        ignoreKeys.add(keys.hashCode()+"");
                        ATalks = !ATalks;
                        break;
                    }
                } else {

                    Set<Sentence> tempSentenceB = lifeB.talk(lifeA, seedWordB);
                    //tempSentenceB.forEach(tp-> System.out.println(tp.getSentenceTemplate().replaceAll("##me##", lifeB.Name).replaceAll("##other##", lifeA.getName())));
                    if (tempSentenceB.size() == 0) {
                        seedWordB = getMaxFreqWord(seedSentenceB,true);
                        BUnAnswered = true;
                        continue;
                    }
                    for (Iterator it = tempSentenceB.iterator(); it.hasNext();) {
                        Sentence keys = (Sentence) it.next();
                        if(ignoreKeys.contains(keys.hashCode()+"")) continue;
                        seedSentenceB=keys.getSentenceTemplate().replaceAll("##me##", lifeB.getName()).replaceAll("##other##",lifeA.Name );
                        System.out.println(timeDelay+":"+seedWordB+">" + seedSentenceB);
                        
                        seedWordA = getMaxFreqWord(seedSentenceB,false);
                        ATalks = !ATalks;
                        ignoreKeys.add(keys.hashCode()+"");
                        BUnAnswered = false;
                        break;
                    }
                }

                try {
                    Thread.sleep(timeDelay);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ConversationMaker.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //A does the begning
        

        return null;
    }

    public static String getMaxFreqWord(String sentence,boolean random) {
        String[] words = sentence.split(" ");
        Map<String, Integer> map = new HashMap<>();
        int Max = 0;
        String seedWord = "";
        for (String w : words) {
            Integer n = map.get(w);
            n = (n == null) ? 1 : ++n;
            map.put(w, n);
            if (w.length() > 2) {
                seedWord = n > Max ? w : seedWord;
                Max = n > Max ? n : Max;
            }

        }

        return random?words[(int) (words.length * Math.random())]:seedWord;
    }
}
