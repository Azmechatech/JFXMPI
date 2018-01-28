/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow.pt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Maneesh
 */
public class SentenceFactory {
    
    public static void add(Sentence sentence){
        Sentences.put(sentence.hashCode()+"", sentence);

        if(sentenceAndKeyMap.containsKey(sentence.getKey())){
                    sentenceAndKeyMap.get(sentence.getKey()).add(sentence.hashCode()+"");
                }else{
                    Set<String> newSet=new HashSet();
                    newSet.add(sentence.hashCode()+"");
                    sentenceAndKeyMap.put(sentence.getKey(), newSet);
                }
        
        
        //Split all words and tag all words and tag them with keys
        String[] allwords=sentence.getSentenceTemplate().split(" ");
        for(String word :allwords){
            if(word.length()>2)
            {
                if(searchTags.containsKey(word)){
                    searchTags.get(word).add(sentence.getKey());
                }else{
                    Set<String> newSet=new HashSet();
                    newSet.add(sentence.getKey());
                    searchTags.put(word, newSet);
                }
            }
        }
    }
    
    
    public static Set<Sentence> getSentence(String SearchKey,String identityKey){
         String[] allwords=SearchKey.split(" ");
         Set<Sentence> result=new HashSet<>();
          for(String word :allwords){
              Set tempSet=searchTags.get(word);
              if(tempSet!=null && tempSet.contains(identityKey)){
                  Set sentnetcekeys=sentenceAndKeyMap.get(identityKey);
                  for (Iterator it = sentnetcekeys.iterator(); it.hasNext();) {
                      String keys = (String) it.next();
                      result.add(Sentences.get(keys));
                  }
              }
          }
       return  result;
    }
    
    private static HashMap<String, Sentence> Sentences=new HashMap<>();

    /**
     * Get the value of Sentences
     *
     * @return the value of Sentences
     */
    public static HashMap getSentences() {
        return Sentences;
    }

    /**
     * Set the value of Sentences
     *
     * @param Sentences new value of Sentences
     */
    public static void setSentences(HashMap Sentences) {
        SentenceFactory.Sentences = Sentences;
    }

    public static HashMap<String,Set<String>> searchTags=new HashMap<>();
    public static HashMap<String,Set<String>> sentenceAndKeyMap=new HashMap<>();
    
}
