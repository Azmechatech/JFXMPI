/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package systemknowhow;

import java.util.HashMap;

/**
 *
 * @author mkfs
 */
public class QuestionFramework {
    
    public static final  HashMap<Double,String> RankedQuestions=new HashMap<Double,String>();
    
    static{
        RankedQuestions.put(new Double(0), "What are we loking at?");
        RankedQuestions.put(new Double(1), "Are there people in picture?");
        RankedQuestions.put(new Double(2), "Oh..Who are they?");
        RankedQuestions.put(new Double(3), "I see, since you can not recognize them, can I name them?");
        RankedQuestions.put(new Double(4), "Very well, how many males are there?");
        RankedQuestions.put(new Double(5), "How about this name?");
        RankedQuestions.put(new Double(6), "Are there females there?");
        RankedQuestions.put(new Double(7), "How about this name?");
        RankedQuestions.put(new Double(8), "Oh..what was his age?");
        RankedQuestions.put(new Double(9), "Oh.. what was her age?");
        RankedQuestions.put(new Double(10), "Please describe what is happenign here?");
        
    }
    
    
}
