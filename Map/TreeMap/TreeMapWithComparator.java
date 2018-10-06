

TreeMapWithComparator

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tm;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.WeakHashMap;

/**
 *
 * @author nSense
 */
public class TreeMapWithComparator {

    /**
     * @param args the command line arguments
     */
       public static void main(String[] args) throws InterruptedException{
       TreeMap tm = new TreeMap(new MyComparator());
       tm.put(100, "aaa");
       tm.put(101, "BBB");
       tm.put(102, "CCC");
       tm.put(151, "XXX");
       tm.put(125, "XXX");
       tm.put("Hel", "HEL");  // Hetro genious allowed
       //tm.put(null, "AA");    //comparison is required and throw RE: java.lang.NullPointerException
       System.out.println("After GC : "+tm);
       
    }
   
}

class MyComparator implements Comparator{

    @Override
    public int compare(Object o1, Object o2) {
        String s1  = o1.toString();
        String s2  = o2.toString();
        return s2.compareTo(s1);
    }
    
}

/*Out/Put
{Hel=HEL, 151=XXX, 125=XXX, 102=CCC, 101=BBB, 100=aaa}
*/