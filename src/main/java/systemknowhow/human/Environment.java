/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow.human;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import systemknowhow.human.guns.CompositeGun;
import systemknowhow.humanactivity.IndoorActivityTagFactory;
import systemknowhow.humanactivity.OutdoorActivityTagFactory;
import systemknowhow.humanactivity.SecretActivityTagFactory;
import systemknowhow.humanactivity.SocialActionTagFactory;
import systemknowhow.humanactivity.SocialRelationTags;

/**
 *
 * @author Maneesh
 */
public class Environment {

    public void setLives(Set<Life> lives) {
        this.lives = lives;
    }

    public void setSignStimulus(Set<SignStimulus> SignStimulus) {
        this.SignStimulus = SignStimulus;
    }

    public void setReleaser(Set<Releaser> releaser) {
        this.releaser = releaser;
    }
    
    public boolean  addLife(Life life){
        return lives.add(life);
    }
    
    Set<Life> lives;
    
    Set<SignStimulus> SignStimulus;
    
    Set<Releaser> releaser;
    
    
    //setup the queue for Setup, New Situation,Progress,Complications,Final Push,After Math
    
    //Define life Relations
    //Example
    Male male1=new Male("Aker",40,5.5,new double[]{5});
    //Load text learning file
    
    Male male2=new Male("Taka",40,5.5,new double[]{5});
    
    Female female1=new Female("Wiaker",40,5.5,new double[]{5});
    Female female2=new Female("Nuyo",40,5.5,new double[]{5});
    //Environment puts lives through Releaser, for given SignStimulus
    
    void init(){
        lives=new HashSet<>();
     male1.GUN=new CompositeGun(.1f, .1f, -.3f);
     male1.TAG=LifeTagFactory.MALE;
     male1.relations.put(female1, SocialRelationTags.GIRLFRIEND);
     
     
      male2.GUN=new CompositeGun(.1f, .9f, -.3f);
     male2.TAG=LifeTagFactory.MALE;
     male2.relations.put(female2, SocialRelationTags.WIFE);
    
     female1.GUN=new CompositeGun(.6f, .1f, -.3f);
     female1.TAG=LifeTagFactory.FEMALE;
     female1.relations.put(male1, SocialRelationTags.BOYFRIEND);
     
     female2.GUN=new CompositeGun(.6f, .1f, -.3f);
     female2.TAG=LifeTagFactory.FEMALE;
     female2.relations.put(male2, SocialRelationTags.HUSBAND);
     
     //The text learn
//     male1.learnToSentence(getLines("C:/$AVG/baseDir/Imports/Sprites/CharOneLearn.txt"));
//     female1.learnToSentence(getLines("C:/$AVG/baseDir/Imports/Sprites/CharTwoLearn.txt"));
     
     lives.add(male1);
     lives.add(female1);
     lives.add(male2);
     lives.add(female2);
    }
    
    void testPossiblities(){
       RuleFactory.init();
        SocialRule sr = (SocialRule) RuleFactory.rules.get(male1.relations.get(female1));
        SecretActivityTagFactory[] satf=sr.getSecretActivity();
//         System.out.println(male1.GUN.getGunInclination());
//         for(int i=0;i<satf.length;i++){
//             if(satf[i]!=null)
//             System.out.println(satf[i]);
//         }
        
         
          System.out.println(male1.getName()+" #makeSecretDecision "+male1.makeSecretDecision(female1));
          System.out.println(female1.getName()+" #makeSecretDecision "+female1.makeSecretDecision(male1));
          
        //  ConversationMaker.getConversation(male1, female1, "बुआजी आईं तो मुझे आवाज लगाई लेकिन","तुम यहाँ क्या कर रही हो? ");

          
          System.out.println(male1.getName()+" #makeSecretDecision "+male1.makeIndoorDecision(female1));
          System.out.println(female1.getName()+" #makeSecretDecision "+female1.makeIndoorDecision(male1));
          
          System.out.println(male1.getName()+" #makeSecretDecision "+male1.makeOutdoorDecision(female1));
          System.out.println(female1.getName()+" #makeSecretDecision "+female1.makeOutdoorDecision(male1));
          
          System.out.println(male1.getName()+" #makeSecretDecision "+male1.makeSocialDecision(female1));
          System.out.println(female1.getName()+" #makeSecretDecision "+female1.makeSocialDecision(male1));
          
          System.out.println(male2.getName()+" #makeSecretDecision "+male2.makeSecretDecision(female2));
          System.out.println(female2.getName()+" #makeSecretDecision "+female2.makeSecretDecision(male2));
    }
    
    public static void main(String... args){
        Environment env=new Environment();
        env.init();
        env.testPossiblities();
    }
    
    public String[] getLines(String filePath) {
                Set<String> linesRead=new LinkedHashSet<>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			while (line != null) {
				//System.out.println(line);
				// read next line
				line = reader.readLine();
                                linesRead.add(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return linesRead.toArray(new String[0]);
	}
    
}

class ConsequenceEngine extends Consequence{

    @Override
    public Object evaluate(Rules rule, SocialRelationTags srt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object xevaluate(Rules rule, SecretActivityTagFactory satf, SocialRelationTags srt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object xevaluate(Rules rule, IndoorActivityTagFactory iatf, SocialRelationTags srt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object xevaluate(Rules rule, OutdoorActivityTagFactory oatf, SocialRelationTags srt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object xevaluate(Rules rule, SocialActionTagFactory satf, SocialRelationTags srt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
