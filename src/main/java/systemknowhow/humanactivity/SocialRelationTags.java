/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow.humanactivity;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import systemknowhow.TheCharLibrary;
import static systemknowhow.TheCharLibrary.listOfValidFiles;
import systemknowhow.Tools;

/**
 *
 * @author Maneesh
 */
public enum SocialRelationTags {
    SINGLE_PERSON,
FAMILY,
SINGLE_PARENT,
NUCLEAR_FAMILY_,
SPOUSE,
HUSBAND,
WIFE,
PARENT,
FATHER,
MOTHER,
CHILD,
SON,
DAUGHTER,
SIBLING,
BROTHER,
SISTER,
EXTENDED_FAMILY,
GRANDPARENT,
GRANDFATHER,
GRANDMOTHER,
GRANDSON,
GRANDDAUGHTER,
UNCLE,
AUNT,
COUSIN,
NEPHEW,
NIECE,
FAMILY_IN_LAW,
FATHER_IN_LAW,
MOTHER_IN_LAW,
BROTHER_IN_LAW,
SISTER_IN_LAW,
COMPLEX_FAMILY,
STEPFAMILY,
STEPFATHER,
STEP_MOTHER,
STEP_SON,
STEPDAUGHTER,
STEP_BROTHER,
STEP_SISTER,
STEP_AUNT,
STEPGRANDFATHER,
STEPGRANDMOTHER,
STEPUNCLE,
DYSFUNCTIONAL_FAMILY,
KINSHIP,
CONSANGUINITY,
AFFINITY,
FICTIVE_KINSHIP,
HOUSEHOLD_CHANGE,
MARRIAGE,
ADOPTION,
HOUSEHOLD_END,
BREAKUP,
DIVORCE,
DISOWNMENT,
WIDOWHOOD,
CAREGIVER,
BREADWINNER,
PEER_GROUP,
SPECIAL_INTEREST_GROUP,
ORGANIZATION_MEMBERSHIP,
CORPORATIONS,
GOVERNMENTS,
NON_GOVERNMENTAL_ORGANIZATIONS,
INTERNATIONAL_ORGANIZATIONS,
ARMED_FORCES,
CHARITABLE_ORGANIZATIONS,
NOT_FOR_PROFIT_CORPORATIONS,
PARTNERSHIPS,
COOPERATIVES,
UNIVERSITIES,
NEIGHBOR,
MEMBER_OF_SOCIETY,
COHABITATION,
INTIMATE_RELATIONSHIP,
COMMITTED_RELATIONSHIP,
COURTSHIP,
LONG_TERM_RELATIONSHIP_,//(LTR),
MONOGAMY,//_HAVING_A_SINGLE_LONG-TERM_SEXUAL_PARTNER.
POLYGAMY,//_HAVING_MULTIPLE_LONG-TERM_SEXUAL_PARTNERS.
POLYANDRY,//_HAVING_MULTIPLE_LONG-TERM_MALE_SEXUAL_PARTNERS.
POLYGYNY,//_HAVING_MULTIPLE_LONG-TERM_FEMALE_SEXUAL_PARTNERS.
ENGAGEMENT,

MARRIAGE_PARTNERS,

TYPES_OF_MARRIAGE,
ARRANGED_MARRIAGE,
FORCED_MARRIAGE,
COUSIN_MARRIAGE,
OPEN_MARRIAGE,
CIVIL_UNION,
DOMESTIC_PARTNERSHIP,
BOYFRIEND,
GIRLFRIEND,
FAMILIAL_RELATIONSHIP,//_RELATIONSHIP_BETWEEN_MEMBERS_OF_A_FAMILY._FAMILY_MEMBERS_TEND_TO_FORM_CLOSE_PERSONAL_RELATIONSHIPS._SEE_FAMILY_SECTION_ABOVE.
FRIENDSHIP,
EXTRAMARITAL_AFFAIR,
LOVE_HATE_RELATIONSHIP,
POLYAMOROUS_RELATIONSHIP,
NON_MONOGAMY,
POLYGAMOUS_RELATIONSHIP,//_HAVING_MULTIPLE_LONG-TERM_SEXUAL_PARTNERS.
POLYFIDELITY,
POLYGYNANDRY,
ROMANTIC_FRIENDSHIP,
RELATIONSHIP_ANARCHY,
SAME_SEX_RELATIONSHIP,//-A_RELATIONSHIP_BETWEEN_TWO_PEOPLE_OF_SAME_SEX,_BUT_NOT_NECESSARILY_THE_SAME_GENDER
CASUAL_RELATIONSHIP,
FEMALE_LED_RELATIONSHIP,//_WOMAN_OR_WIFE_LED_RELATIONSHIP_(FLR)

PROFESSIONAL_RELATIONSHIPS,

EMPLOYER_WORKER_RELATIONSHIP;
static DefaultListModel listModel;
    public static DefaultListModel load() {

        listModel = new DefaultListModel();
        int count = 0;
        listModel.removeAllElements();
        for (int i = 0; i < SocialRelationTags.values().length; i++) {

            listModel.addElement(SocialRelationTags.values()[i].toString());

        }
        return listModel;
    }

}
    

