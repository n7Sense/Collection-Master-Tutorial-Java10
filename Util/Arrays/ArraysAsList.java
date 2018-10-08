/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nSense;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author nSense
 */
public class ArraysAsList {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String[] s = {"A", "Z", "B", "Q", "L", "M"};
        List l = Arrays.asList(s);
        System.out.println(l);
        s[0] = "D";
        System.out.println(l);
        l.set(1, "X");
        for(String s1 : s)
            System.out.println(s1);
        
        //l.add("nSense"); //throws UnsupportedOperationException
        //l.remove(2);  //throws UnsupportedOperationException
        //l.set(3, new Integer(10)); // throws ArrayStoreException
        
        
    }
}


Out/Put:

run:
[A, Z, B, Q, L, M]
[D, Z, B, Q, L, M]
D
X
B
Q
L
M
BUILD SUCCESSFUL (total time: 0 seconds)

Exception in thread "main" java.lang.UnsupportedOperationException
            at java.base/java.util.AbstractList.add(AbstractList.java:153)
            at java.base/java.util.AbstractList.add(AbstractList.java:111)

Exception in thread "main" java.lang.UnsupportedOperationException
            at java.base/java.util.AbstractList.remove(AbstractList.java:167)
            
Exception in thread "main" java.lang.ArrayStoreException: java.lang.Integer
            at java.base/java.util.Arrays$ArrayList.set(Arrays.java:4356)