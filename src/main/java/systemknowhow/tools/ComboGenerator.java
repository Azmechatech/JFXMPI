/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Maneesh
 */
public class ComboGenerator {

    public static void GeneratePermutations(List<List<Character>> Lists, List<String> result, int depth, String current) {
        if (depth == Lists.size()) {
            result.add(current);
            return;
        }

        for (int i = 0; i < Lists.get(depth).size(); ++i) {
            GeneratePermutations(Lists, result, depth + 1, current + Lists.get(depth).get(i));
        }
    }
    
    public static void testGeneratePermutations(){
        
        List Result=new ArrayList();
        List lists=new ArrayList();
        //lists.add(list_3);lists.add(list_1);lists.add(list_2);lists.add(list_1);lists.add(list0); lists.add(list1);lists.add(list2);lists.add(list3);lists.add(list6);lists.add(list4);lists.add(list5);lists.add(list6);
        GeneratePermutations(lists, Result, 0, "");
        
        int counter=1;
        for(Object string:Result){ System.out.println(counter++ +": "+string);}
        System.out.println(" "+Result.size());
    }
    
    public static void main(String... args){
        testGeneratePermutations();
    }
}
