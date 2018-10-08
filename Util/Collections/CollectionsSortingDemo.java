
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;

/**
 *
 * @author nSense
 */
public class CollectionsSortingDemo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        
        ArrayList al= new ArrayList();
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
        System.out.println("Before Sorting : "+al);
        Collections.sort(al);
        System.out.println("After Sorting : "+al);
    }
}


run:
Before Sorting : [A, D, C, E, K, Z, N, P]
After Sorting : [A, C, D, E, K, N, P, Z]
BUILD SUCCESSFUL (total time: 0 seconds)
