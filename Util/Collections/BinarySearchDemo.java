/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nSense;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;

/**
 *
 * @author nSense
 */
public class BinarySearchDemo {

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
        Collections.sort(al);
        System.out.println("After Sorting : " + al);
        //Collections.sort(al, new MyComparator());
        System.out.println("Binary Search for Z : " + Collections.binarySearch(al, "Z"));
        System.out.println("Binary Search for O : " + Collections.binarySearch(al, "O"));
        System.out.println("Binary Search for a : " + Collections.binarySearch(al, "a"));
        System.out.println("Binary Search for b : " + Collections.binarySearch(al, "b"));
        System.out.println("Binary Search for B : " + Collections.binarySearch(al, "B"));
    }
}

    Before Sorting Element In ArrayList
-----------------------------------------------

INSERTION POINT -->         -1      -2      -3      -4      -5      -6      -7      -8          
                        +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                        |   A   |    D   |   C   |   E   |   K   |   Z   |    N   |   P   |  
                        +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
INDEX -->                   0        1       2       3       4       5       6       7     


    After Sorting Element In ArrayList
-----------------------------------------------

INSERTION POINT -->         -1      -2      -3      -4      -5      -6      -7      -8      -9
                        ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                        |   A   |    C   |   D   |   E   |   K   |   N   |   P   |   Z   |  
                        ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
INDEX -->                   0        1       2       3       4       5       6       7     



run:
Before Sorting : [A, D, C, E, K, Z, N, P]
After Sorting : [A, C, D, E, K, N, P, Z]
Binary Search for Z : 7
Binary Search for O : -7
Binary Search for a : -9
Binary Search for b : -9
Binary Search for B : -2
BUILD SUCCESSFUL (total time: 0 seconds)
