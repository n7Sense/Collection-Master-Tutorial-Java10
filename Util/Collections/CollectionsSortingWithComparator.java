/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sense;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;

/**
 *
 * @author nSense
 */
public class CollectionsSortingWithComparator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        
        ArrayList al = new ArrayList();
        al.add("A");
        al.add("D");
        al.add("C");
        al.add("E");
        al.add("K");
        al.add("Z");
        al.add("N");
        al.add("P");
        //al.add(100);  // throws ClassCastException
        //al.add(null); // throws NullPointerException
        System.out.println("Before Sorting : " + al);
        Collections.sort(al, new MyComparator());
        System.out.println("After Sorting : " + al);
    }
}

class MyComparator implements Comparator{

    @Override
    public int compare(Object o1, Object o2) {
        String s1 = (String)o1;
        String s2 = o2.toString();
        return s2.compareTo(s1);
    }
}

run:
Before Sorting : [A, D, C, E, K, Z, N, P]
After Sorting : [Z, P, N, K, E, D, C, A]
BUILD SUCCESSFUL (total time: 0 seconds)