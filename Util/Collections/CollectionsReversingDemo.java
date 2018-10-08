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
public class CollectionsReversingDemo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        
        ArrayList al = new ArrayList();
        al.add(10);
        al.add(0);
        al.add(15);
        al.add(6);
        al.add(8);
        al.add(8);
        al.add(7);
        al.add(20);
        al.add(-1);
        //al.add("100");  // throws ClassCastException
        //al.add(null); // throws NullPointerException
        System.out.println("Before Sorting : " + al);
        Collections.sort(al);
        System.out.println("After Sorting : " + al);
        Collections.reverse(al);
        System.out.println("After Reversing : " + al);

    }
}


run:
Before Sorting : [10, 0, 15, 6, 8, 8, 7, 20, -1]
After Sorting : [-1, 0, 6, 7, 8, 8, 10, 15, 20]
After Reversing : [20, 15, 10, 8, 8, 7, 6, 0, -1]
BUILD SUCCESSFUL (total time: 0 seconds)
