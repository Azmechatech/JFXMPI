/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow.human;

import systemknowhow.humanactivity.SocialRelationTags;
import systemknowhow.humanactivity.SocialActionTagFactory;
import java.util.HashMap;
import java.util.Map;
import systemknowhow.humanactivity.IndoorActivityTagFactory;
import systemknowhow.humanactivity.OutdoorActivityTagFactory;
import systemknowhow.humanactivity.SecretActivityTagFactory;

/**
 *
 * @author Maneesh
 */
public class RuleFactory {
    
    public static Map<SocialRelationTags,Rules> rules=new HashMap();
    
  static void init() {
      rules=new HashMap();
      
      SocialRule srule=new SocialRule(SocialRelationTags.GIRLFRIEND,IndoorActivityTagFactory.values().length,OutdoorActivityTagFactory.values().length,SecretActivityTagFactory.values().length,SocialActionTagFactory.values().length);
      
      int counter=0;
      for (IndoorActivityTagFactory iatf : IndoorActivityTagFactory.values()) {
         srule.setIndoorActivity(counter++, iatf);
            // do what you want
        }
      
      counter=0;
      for (OutdoorActivityTagFactory oatf : OutdoorActivityTagFactory.values()) {
          srule.setOutdoorActivity(counter++, oatf);
            // do what you want
        }
   
      srule.setSocialActionAllowed(0, SocialActionTagFactory.CASUAL_DATING);
      srule.setSocialActionAllowed(1, SocialActionTagFactory.AFFECTION);
      srule.setSocialActionAllowed(2, SocialActionTagFactory.ATTACHMENT_IN_ADULTS);
      srule.setSocialActionAllowed(3, SocialActionTagFactory.BOYFRIEND_GIRLFRIEND);
      srule.setSocialActionAllowed(4, SocialActionTagFactory.BREAKING_UP);
      srule.setSocialActionAllowed(5, SocialActionTagFactory.BROKEN_HEART);
      srule.setSocialActionAllowed(6, SocialActionTagFactory.CASUAL_RELATIONSHIP);
      srule.setSocialActionAllowed(7, SocialActionTagFactory.COUPLE_DANCING);
      srule.setSocialActionAllowed(8, SocialActionTagFactory.DATING);
      srule.setSocialActionAllowed(9, SocialActionTagFactory.DYSFUNCTIONAL_RELATIONS);
      srule.setSocialActionAllowed(10, SocialActionTagFactory.ARMS_AROUND_ABDOMEN);
      srule.setSocialActionAllowed(11, SocialActionTagFactory.ARM_AROUND_SHOULDER);
      srule.setSocialActionAllowed(12, SocialActionTagFactory.EMOTIONAL_INTIMACY);
      srule.setSocialActionAllowed(13, SocialActionTagFactory.EYE_CONTACT);
      srule.setSocialActionAllowed(14, SocialActionTagFactory.FLIRTING);
      srule.setSocialActionAllowed(15, SocialActionTagFactory.FACE_TO_FACE);
      srule.setSocialActionAllowed(16, SocialActionTagFactory.HEAD_ON_SHOULDER);
      srule.setSocialActionAllowed(17, SocialActionTagFactory.HOLDING_HANDS);
      srule.setSocialActionAllowed(18, SocialActionTagFactory.INTERNET_DATING);
      
      srule.setSecretActivity(0, SecretActivityTagFactory.KISS);
      srule.setSecretActivity(2, SecretActivityTagFactory.NOT_SLEEPING_ALL_NIGHT);
      srule.setSecretActivity(3, SecretActivityTagFactory.MASTURBATE);
      srule.setSecretActivity(4, SecretActivityTagFactory.READ_EROTICA);
      srule.setSecretActivity(5, SecretActivityTagFactory.HAVE_PHONE_SEX);
      srule.setSecretActivity(6, SecretActivityTagFactory.SENDING_NAKED_PHOTOS_TO_EACH_ANOTHER);
      srule.setSecretActivity(7, SecretActivityTagFactory.USE_A_VIBRATOR);
      srule.setSecretActivity(8, SecretActivityTagFactory.WATCH_PORN_ALONE);
      rules.put(SocialRelationTags.BOYFRIEND, srule);
      SocialRule srule2=new SocialRule(SocialRelationTags.BOYFRIEND,IndoorActivityTagFactory.values().length,OutdoorActivityTagFactory.values().length,9,19);
      srule2.setIndoorActivity(srule.getIndoorActivity());
      srule2.setOutdoorActivity(srule.getOutdoorActivity());
      srule2.setSecretActivity(srule.getSecretActivity());
      srule2.setSocialActionAllowed(srule.getSocialActionAllowed());
      rules.put(SocialRelationTags.GIRLFRIEND,srule2);
      
      
      srule2=new SocialRule(SocialRelationTags.GIRLFRIEND,IndoorActivityTagFactory.values().length,OutdoorActivityTagFactory.values().length,SecretActivityTagFactory.values().length,SocialActionTagFactory.values().length);
      counter=0;
      for (IndoorActivityTagFactory iatf : IndoorActivityTagFactory.values()) {
         srule2.setIndoorActivity(counter++, iatf);
            // do what you want
        }
      
      counter=0;
      for (OutdoorActivityTagFactory oatf : OutdoorActivityTagFactory.values()) {
          srule2.setOutdoorActivity(counter++, oatf);
            // do what you want
        }
      
      counter=0;
      for (SecretActivityTagFactory satf : SecretActivityTagFactory.values()) {
          srule2.setSecretActivity(counter++, satf);
            // do what you want
        }
      
      counter=0;
      for (SocialActionTagFactory satf : SocialActionTagFactory.values()) {
          srule2.setSocialActionAllowed(counter++, satf);
            // do what you want
        }
      rules.put(SocialRelationTags.WIFE, srule2);
      
      
      srule=new SocialRule(SocialRelationTags.WIFE,IndoorActivityTagFactory.values().length,OutdoorActivityTagFactory.values().length,9,19);
      srule.setIndoorActivity(srule2.getIndoorActivity());
      srule.setOutdoorActivity(srule2.getOutdoorActivity());
      srule.setSecretActivity(srule2.getSecretActivity());
      srule.setSocialActionAllowed(srule2.getSocialActionAllowed());
      rules.put(SocialRelationTags.HUSBAND, srule);
      
      srule=new SocialRule(SocialRelationTags.STEP_SON,IndoorActivityTagFactory.values().length,OutdoorActivityTagFactory.values().length,9,19);
      srule.setIndoorActivity(srule2.getIndoorActivity());
      srule.setOutdoorActivity(srule2.getOutdoorActivity());
      rules.put(SocialRelationTags.STEP_MOTHER, srule);
      
      srule2=new SocialRule(SocialRelationTags.STEP_MOTHER,IndoorActivityTagFactory.values().length,OutdoorActivityTagFactory.values().length,9,19);
      srule2.setIndoorActivity(srule.getIndoorActivity());
      srule2.setOutdoorActivity(srule.getOutdoorActivity());
      rules.put(SocialRelationTags.STEP_SON, srule2);
      
      
      srule=new SocialRule(SocialRelationTags.STEP_SISTER,IndoorActivityTagFactory.values().length,OutdoorActivityTagFactory.values().length,9,19);
      srule.setIndoorActivity(srule2.getIndoorActivity());
      srule.setOutdoorActivity(srule2.getOutdoorActivity());
      rules.put(SocialRelationTags.STEP_BROTHER, srule);
      
      srule2=new SocialRule(SocialRelationTags.STEP_BROTHER,IndoorActivityTagFactory.values().length,OutdoorActivityTagFactory.values().length,9,19);
      srule2.setIndoorActivity(srule.getIndoorActivity());
      srule2.setOutdoorActivity(srule.getOutdoorActivity());
      rules.put(SocialRelationTags.STEP_SISTER, srule2);
      
      
      srule=new SocialRule(SocialRelationTags.STEP_AUNT,IndoorActivityTagFactory.values().length,OutdoorActivityTagFactory.values().length,9,19);
      srule.setIndoorActivity(srule2.getIndoorActivity());
      srule.setOutdoorActivity(srule2.getOutdoorActivity());
      rules.put(SocialRelationTags.STEP_SON, srule);
      
      srule2=new SocialRule(SocialRelationTags.STEP_SON,IndoorActivityTagFactory.values().length,OutdoorActivityTagFactory.values().length,9,19);
      srule2.setIndoorActivity(srule.getIndoorActivity());
      srule2.setOutdoorActivity(srule.getOutdoorActivity());
      rules.put(SocialRelationTags.STEP_AUNT, srule2);
         
      srule=new SocialRule(SocialRelationTags.FRIENDSHIP,IndoorActivityTagFactory.values().length,OutdoorActivityTagFactory.values().length,9,19);
      srule.setIndoorActivity(srule2.getIndoorActivity());
      srule.setOutdoorActivity(srule2.getOutdoorActivity());
      rules.put(SocialRelationTags.FRIENDSHIP, srule);
      
  }
    
}
