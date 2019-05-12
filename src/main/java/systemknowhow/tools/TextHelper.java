/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow.tools;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * noun, pronoun, adjective, determiner, verb, adverb, preposition, conjunction, and interjection 
 * @author mkfs
 */
public class TextHelper {
    /**
     * Generate the Adj-noun-verb network.
     * 
     * 
     */
    static final Set<String> ADJECTIVES=new HashSet<>();
    static final Set<String> NOUN=new HashSet<>();
    static final Set<String> PRONOUN=new HashSet<>();
    static final Set<String> VERB=new HashSet<>();
    static final Set<String> ADVERBpve=new HashSet<>();
    
    public static void init() {
        String theData = new TextHelper().getFile("data/adjectives.txt");
        String[] splitData = theData.toLowerCase().split("\n");
        ADJECTIVES.addAll(Arrays.asList(splitData));

        theData = new TextHelper().getFile("data/nouns.txt");
        splitData = theData.toLowerCase().split("\n");
        NOUN.addAll(Arrays.asList(splitData));

        theData = new TextHelper().getFile("data/pronoun.txt");
        splitData = theData.toLowerCase().split("\n");
        PRONOUN.addAll(Arrays.asList(splitData));
        
        theData = new TextHelper().getFile("data/verbs.txt");
        splitData = theData.toLowerCase().split("\n");
        VERB.addAll(Arrays.asList(splitData));
        for (String verb : splitData) {
            if (verb.endsWith("e")) {
                VERB.add(verb.substring(0, verb.length() - 1) + "ing");
                VERB.add(verb + "d");
            } else if (verb.endsWith("y")) {
                VERB.add(verb.substring(0, verb.length() - 1) + "ing");
                VERB.add(verb.substring(0, verb.length() - 1) + "ied");
            } else {
                VERB.add(verb + "ing");

                VERB.add(verb + "ed");
            }
        }
        
        theData = new TextHelper().getFile("data/positive_adverb.txt");
        splitData = theData.toLowerCase().split("\n");
        ADVERBpve.addAll(Arrays.asList(splitData));

    }
    
    public static String[] getANVChain(String[] sentence){
        String result[]=new  String[sentence.length];
        int counter=0;
        for(String word:sentence){
            if(ADJECTIVES.contains(word.toLowerCase())){
                result[counter++]="A";
            }else 
            
            if(NOUN.contains(word.toLowerCase())){
                result[counter++]="N";
            }else
            
            if(PRONOUN.contains(word.toLowerCase())){
                result[counter++]="PN";
            }else
            
            if(VERB.contains(word.toLowerCase())){
                result[counter++]="V";
            }else
            
            if(ADVERBpve.contains(word.toLowerCase())){
                result[counter++]="AVP";
            }else{
                result[counter++]=word;
            }
        }
        
        return result;
    }
    
    private  String getFile(String fileName) {

        StringBuilder result = new StringBuilder("");

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();

    }
    
    public static void main(String... args){
        init();
        
        String sentence="What do you want now ? This has been an exciting phase to me. What do you want now ?";
        String[] result=getANVChain( sentence.split(" "));
        System.out.println(Arrays.toString(result));
    }
}
