/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nSense;


import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/**
 *
 * @author nSense
 */
public class ArraysDemoAndComparator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        
        int[] a = {10, 5, 6, 3, 15, 11, 16, 19};
        System.out.print("Primitive array before sorting : ");
        for(int k : a)
            System.out.print(k+" : ");
        System.out.println();
        
        Arrays.sort(a);
        System.out.print("Primitive array after sorting : ");
        for(int k : a)
            System.out.print(k+" : ");
        System.out.println();
        
        String [] s = {"A", "Z", "B", "Q", "L", "M"};
        System.out.print("Object array before sorting : ");
        for(String k : s)
            System.out.print(k+" : ");
        System.out.println();
        
        Arrays.sort(s);
        System.out.print("Object array after sorting : ");
        for (String k : s) {
            System.out.print(k + " : ");
        }
        System.out.println();
        
        Arrays.sort(s, new MyComparator());
        System.out.print("Object array after sorting by Comparator : ");
        for(String k : s)
            System.out.print(k+" : ");
        System.out.println();
        
        
    }
}

class MyComparator implements Comparator{

    @Override
    public int compare(Object o1, Object o2) {
        String i1 = o1.toString();
        String i2 = o2.toString();
        return i2.compareTo(i1);
    }
}


run:
Primitive array before sorting : 10 : 5 : 6 : 3 : 15 : 11 : 16 : 19 : 
Primitive array after sorting : 3 : 5 : 6 : 10 : 11 : 15 : 16 : 19 : 
Object array before sorting : A : Z : B : Q : L : M : 
Object array after sorting : A : B : L : M : Q : Z : 
Object array after sorting by Comparator : Z : Q : M : L : B : A : 
BUILD SUCCESSFUL (total time: 0 seconds)
