/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow.human;

import com.sun.javafx.css.Rule;
import systemknowhow.humanactivity.IndoorActivityTagFactory;
import systemknowhow.humanactivity.OutdoorActivityTagFactory;
import systemknowhow.humanactivity.SecretActivityTagFactory;
import systemknowhow.humanactivity.SocialActionTagFactory;
import systemknowhow.humanactivity.SocialRelationTags;

/**
 * These patterns are the reference to how often the desired behavior actually
 * occurs. Before a behavior actually occurs, antecedents focus on the stimuli
 * that influence the behavior that is about to happen. After the behavior
 * occurs, consequences fall into place. They can come in the form of rewards or
 * punishments.
 *
 * Consequentialism is the class of normative ethical theories holding that the
 * consequences of one's conduct are the ultimate basis for any judgment about
 * the rightness or wrongness of that conduct. Thus, from a consequentialist
 * standpoint, a morally right act (or omission from acting) is one that will
 * produce a good outcome, or consequence.
 *
 * Every Environment will need it's own set of Consequence implementation.
 * @author Maneesh
 */
 public abstract class Consequence {
    SocialRule sr;
    NaturalRule nr;
    
     abstract public Object evaluate(Rule rule, SocialRelationTags srt) ;
     
    abstract public Object xevaluate(Rule rule,SecretActivityTagFactory satf, SocialRelationTags srt);
     
    abstract public Object xevaluate(Rule rule,IndoorActivityTagFactory iatf, SocialRelationTags srt) ;
     
     abstract public Object xevaluate(Rule rule,OutdoorActivityTagFactory oatf, SocialRelationTags srt) ;
     
     abstract public Object xevaluate(Rule rule,SocialActionTagFactory satf, SocialRelationTags srt);
    
    
}
