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
public class BinarySearchWithComparator {

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
        Collections.sort(al, new MyComparator());
        System.out.println("After Sorting : " + al);
        System.out.println("Binary Search for 10 : " + Collections.binarySearch(al, 10, new MyComparator()));
        System.out.println("Binary Search for 13 : " + Collections.binarySearch(al, 13, new MyComparator()));
        System.out.println("Binary Search for 09 : " + Collections.binarySearch(al, 9, new MyComparator()));
        System.out.println("Binary Search for 15 : " + Collections.binarySearch(al, 15, new MyComparator()));
        System.out.println("Binary Search for 19 : " + Collections.binarySearch(al, 19, new MyComparator()));
        System.out.println("Binary Search for 20 : " + Collections.binarySearch(al, 20, new MyComparator()));
        System.out.println("Binary Search for 21 : " + Collections.binarySearch(al, 21, new MyComparator()));
        System.out.println("Binary Search for 22 : " + Collections.binarySearch(al, 22, new MyComparator()));
        System.out.println("Binary Search for 00 : " + Collections.binarySearch(al, 0, new MyComparator()));
        System.out.println("Binary Search for -1 : " + Collections.binarySearch(al, -1, new MyComparator()));
        System.out.println("Binary Search for -2 : " + Collections.binarySearch(al, -2, new MyComparator()));
        System.out.println("Binary Search for -3 : " + Collections.binarySearch(al, -3, new MyComparator()));
        System.out.println("Binary Search for 16 : " + Collections.binarySearch(al, 16) );
    }
}

class MyComparator implements Comparator{

    @Override
    public int compare(Object o1, Object o2) {
        Integer i1 = (Integer)o1;
        Integer i2 = (Integer)o2;
        return i2.compareTo(i1);
    }
}