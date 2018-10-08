/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sense;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 *
 * @author nSense
 */
public class NavigableMapWithtTreeMap {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        
        TreeMap<String, String> t =new TreeMap<String, String>();
        t.put("b", "banana");
        t.put("c", "cat");
        t.put("a", "apple");
        t.put("d", "sog");
        t.put("g", "gun");
        t.put("f", "flaxy seed");
        t.put("e", "elephent");
        t.put("h", "hen");
        System.out.println(t);
        System.out.println("ceiling : "+t.ceilingKey("c"));
        System.out.println("higher : "+t.higherKey("e"));
        System.out.println("floor : "+t.floorKey("e"));
        System.out.println("lower : "+t.lowerKey("e"));
        System.out.println("lower : "+t.lowerKey("e"));
        System.out.println("pollFirst : "+t.pollFirstEntry());
        System.out.println("pollLast : "+t.pollLastEntry());
        System.out.println("descendingMap : "+t.descendingMap());
        System.out.println("full : "+t);
    }
}

/**Out/Put
    
    run:
    {a=apple, b=banana, c=cat, d=sog, e=elephent, f=flaxy seed, g=gun, h=hen}
    ceiling : c
    higher : f
    floor : e
    lower : d
    lower : d
    pollFirst : a=apple
    pollLast : h=hen
    descendingMap : {g=gun, f=flaxy seed, e=elephent, d=sog, c=cat, b=banana}
    full : {b=banana, c=cat, d=sog, e=elephent, f=flaxy seed, g=gun}
    BUILD SUCCESSFUL (total time: 0 seconds)
    
*/