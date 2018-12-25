package systemknowhow.tools;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.sequences.DocumentReaderAndWriter;
import edu.stanford.nlp.util.Triple;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;


/** This is a demo of calling CRFClassifier programmatically.
 *  <p>
 *  Usage: {@code java -mx400m -cp "*" NERHelper [serializedClassifier [fileName]] }
 *  <p>
 *  If arguments aren't specified, they default to
 *  classifiers/english.all.3class.distsim.crf.ser.gz and some hardcoded sample text.
 *  If run with arguments, it shows some of the ways to get k-best labelings and
 *  probabilities out with CRFClassifier. If run without arguments, it shows some of
 *  the alternative output formats that you can get.
 *  <p>
 *  To use CRFClassifier from the command line:
 *  </p><blockquote>
 *  {@code java -mx400m edu.stanford.nlp.ie.crf.CRFClassifier -loadClassifier [classifier] -textFile [file] }
 *  </blockquote><p>
 *  Or if the file is already tokenized and one word per line, perhaps in
 *  a tab-separated value format with extra columns for part-of-speech tag,
 *  etc., use the version below (note the 's' instead of the 'x'):
 *  </p><blockquote>
 *  {@code java -mx400m edu.stanford.nlp.ie.crf.CRFClassifier -loadClassifier [classifier] -testFile [file] }
 *  </blockquote>
 *
 *  @author Jenny Finkel
 *  @author Christopher Manning
 */

public class NERHelper {
    
    
    static String serializedClassifier = "C:/NetBeansProjects/stanford-ner-2018-10-16/classifiers/english.all.3class.distsim.crf.ser.gz";
    static AbstractSequenceClassifier<CoreLabel> classifier;

    static {
        
        try {
            classifier = CRFClassifier.getClassifier(serializedClassifier);
        } catch (IOException ex) {
            Logger.getLogger(NERHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassCastException ex) {
            Logger.getLogger(NERHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(NERHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static String getMarked(String sentence) throws IOException, ClassCastException, ClassNotFoundException{
        
        if(sentence!=null)
        {List<Triple<String,Integer,Integer>> triples = classifier.classifyToCharacterOffsets(sentence);
        String[] replaceTexts=new String[triples.size()];
        String[] replaceTextsWith=new String[triples.size()];
        int i=0;
        for (Triple<String,Integer,Integer> trip : triples) {
          //System.out.printf("%s over character offsets [%d, %d) in sentence %d.%n",trip.first(), trip.second(), trip.third, j);
          replaceTexts[i]=sentence.substring(trip.second(), trip.third);
          replaceTextsWith[i++]=trip.first();
        }
        
        for(int j=0;j<replaceTexts.length;j++){
            sentence=sentence.replaceAll(replaceTexts[j], "{{"+replaceTextsWith[j]+"}}");
        }
        System.out.println(sentence);
        }
        
        return sentence;
    }

    public static HashMap getCharacterSpaceData(String sentence) throws IOException, ClassCastException, ClassNotFoundException {
        HashMap<String, String> lHmap = new LinkedHashMap<>();
        if (sentence != null) {
            List<Triple<String, Integer, Integer>> triples = classifier.classifyToCharacterOffsets(sentence);
            String[] replaceTexts = new String[triples.size()];
            String[] replaceTextsWith = new String[triples.size()];
            int i = 0;
            for (Triple<String, Integer, Integer> trip : triples) {
                //System.out.printf("%s over character offsets [%d, %d) in sentence %d.%n",trip.first(), trip.second(), trip.third, j);
                replaceTexts[i] = sentence.substring(trip.second(), trip.third);
                replaceTextsWith[i] = trip.first();
                lHmap.put( replaceTexts[i],trip.first);
                i++;
            }
            lHmap.put( "sentence",sentence);

//        for(int j=0;j<replaceTexts.length;j++){
//            sentence=sentence.replaceAll(replaceTexts[j], "{{"+replaceTextsWith[j]+"}}");
//        }
//        System.out.println(sentence);
        }

        return lHmap;
    }
    
    public static List<HashMap<String,String>> getStorySequence(String fullStory) throws IOException, ClassCastException, ClassNotFoundException{
        Pattern END_OF_SENTENCE = Pattern.compile("\\.\\s+");
        List<HashMap<String,String>> result=new LinkedList();
            //int jk=0;
            for (String sentence : END_OF_SENTENCE.split(fullStory)) {
                result.add(getCharacterSpaceData(sentence));
                //System.out.println(sentence);
                //System.out.println(jk++ +">>" +Collections.singletonList(getCharacterSpaceData(sentence)));
            }
            
            return result;
    }
    
  public static void main(String[] args) throws Exception {

    
    if (args.length > 0) {
      serializedClassifier = args[0];
    }

    AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifier(serializedClassifier);

    /* For either a file to annotate or for the hardcoded text example, this
       demo file shows several ways to process the input, for teaching purposes.
    */

    if (args.length > 1) {

      /* For the file, it shows (1) how to run NER on a String, (2) how
         to get the entities in the String with character offsets, and
         (3) how to run NER on a whole file (without loading it into a String).
      */

      String fileContents = IOUtils.slurpFile(args[1]);
      List<List<CoreLabel>> out = classifier.classify(fileContents);
      for (List<CoreLabel> sentence : out) {
        for (CoreLabel word : sentence) {
          System.out.print(word.word() + '/' + word.get(CoreAnnotations.AnswerAnnotation.class) + ' ');
        }
        System.out.println();
      }

      System.out.println("---");
      out = classifier.classifyFile(args[1]);
      for (List<CoreLabel> sentence : out) {
        for (CoreLabel word : sentence) {
          System.out.print(word.word() + '/' + word.get(CoreAnnotations.AnswerAnnotation.class) + ' ');
        }
        System.out.println();
      }

      System.out.println("---");
      List<Triple<String, Integer, Integer>> list = classifier.classifyToCharacterOffsets(fileContents);
      for (Triple<String, Integer, Integer> item : list) {
        System.out.println(item.first() + ": " + fileContents.substring(item.second(), item.third()));
      }
      System.out.println("---");
      System.out.println("Ten best entity labelings");
      DocumentReaderAndWriter<CoreLabel> readerAndWriter = classifier.makePlainTextReaderAndWriter();
      classifier.classifyAndWriteAnswersKBest(args[1], 10, readerAndWriter);

      System.out.println("---");
      System.out.println("Per-token marginalized probabilities");
      classifier.printProbs(args[1], readerAndWriter);

      // -- This code prints out the first order (token pair) clique probabilities.
      // -- But that output is a bit overwhelming, so we leave it commented out by default.
      // System.out.println("---");
      // System.out.println("First Order Clique Probabilities");
      // ((CRFClassifier) classifier).printFirstOrderProbs(args[1], readerAndWriter);

    } else {

      /* For the hard-coded String, it shows how to run it on a single
         sentence, and how to do this and produce several formats, including
         slash tags and an inline XML output format. It also shows the full
         contents of the {@code CoreLabel}s that are constructed by the
         classifier. And it shows getting out the probabilities of different
         assignments and an n-best list of classifications with probabilities.
      */

      String[] example = {"We're having a sleepove",
                          "I go to school at Stanford University, which is located in California." };
      for (String str : example) {
      //  System.out.println(classifier.classifyToString(str));
      }
      System.out.println("---");

      for (String str : example) {
        // This one puts in spaces and newlines between tokens, so just print not println.
       // System.out.print(classifier.classifyToString(str, "slashTags", false));
      }
      System.out.println("---");

      for (String str : example) {
        // This one is best for dealing with the output as a TSV (tab-separated column) file.
        // The first column gives entities, the second their classes, and the third the remaining text in a document
        //System.out.print(classifier.classifyToString(str, "tabbedEntities", false));
      }
      System.out.println("---Markup---");

      for (String str : example) {
        System.out.println(classifier.classifyWithInlineXML(str));
      }
      System.out.println("---");

      for (String str : example) {
        //System.out.println(classifier.classifyToString(str, "xml", true));
      }
      System.out.println("---");

      for (String str : example) {
        //System.out.print(classifier.classifyToString(str, "tsv", false));
      }
      System.out.println("---Replace to make Generic---");

      // This gets out entities with character offsets
      int j = 0;
      for (String str : example) {
        j++;
        List<Triple<String,Integer,Integer>> triples = classifier.classifyToCharacterOffsets(str);
        for (Triple<String,Integer,Integer> trip : triples) {
          System.out.printf("%s over character offsets [%d, %d) in sentence %d.%n",
                  trip.first(), trip.second(), trip.third, j);
        }
      }
      System.out.println("---");

      // This prints out all the details of what is stored for each token
      int i=0;
      for (String str : example) {
        for (List<CoreLabel> lcl : classifier.classify(str)) {
          for (CoreLabel cl : lcl) {
            System.out.print(i++ + ": ");
            System.out.println(cl.toShorterString());
          }
        }
      }

      System.out.println("---");

            //Method test getCharacterSpaceData
            File file = new File("C:/$AVG/baseDir/Imports/Sprites/txts/admiral.txt");
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();

            String str = new String(data, "UTF-8");
            System.out.println(Collections.singletonList(getStorySequence(str)));
//            System.out.println("---By Line--");
//            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
//                String line;
//                while ((line = br.readLine()) != null) {
//                    System.out.println(Collections.singletonList(getCharacterSpaceData(line)));
//                }
//            }
//            System.out.println("---By Sentence--");
//            Pattern END_OF_SENTENCE = Pattern.compile("\\.\\s+");
//            int jk=0;
//            for (String sentence : END_OF_SENTENCE.split(str)) {
//                System.out.println(sentence);
//                System.out.println(jk++ +">>" +Collections.singletonList(getCharacterSpaceData(sentence)));
//            }

        }
    }

}
