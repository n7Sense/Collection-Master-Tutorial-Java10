
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
public class TreeMapDemo {

    /**
     * @param args the command line arguments
     */
       public static void main(String[] args) throws InterruptedException{
       TreeMap tm = new TreeMap();
       tm.put(100, "aaa");
       tm.put(101, "BBB");
       tm.put(102, "CCC");
       tm.put(151, "XXX");
       tm.put(125, "XXX");
       //tm.put("Hel", "HEL");  // java.lang.ClassCastException
       tm.put(null, "AA");    //comparison is required and throw RE: java.lang.NullPointerException
       System.out.println(tm);
       
    }
   
}


/* Out/Put
 {100=aaa, 101=BBB, 102=CCC, 125=XXX, 151=XXX}
*/