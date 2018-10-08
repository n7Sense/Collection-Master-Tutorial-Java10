/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sense;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;

/**
 *
 * @author nSense
 */
public class NavigableSetWithTreeSet {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        
        TreeSet<Integer> t =new TreeSet<Integer>();
        t.add(11000);
        t.add(22000);
        t.add(63000);
        t.add(34000);
        t.add(95000);
        t.add(76000);
        t.add(9000);
        t.add(7000);
        System.out.println(t);
        System.out.println("ceiling : "+t.ceiling(35000));
        System.out.println("higher : "+t.higher(2000));
        System.out.println("floor : "+t.floor(12000));
        System.out.println("lower : "+t.lower(12000));
        System.out.println("lower : "+t.lower(11000));
        System.out.println("pollFirst : "+t.pollFirst());
        System.out.println("pollLast : "+t.pollLast());
        System.out.println("descendingSet : "+t.descendingSet());
        System.out.println("descendingIterator : "+t.descendingIterator());
        System.out.println("full : "+t);
    }
}

/**Out/Put
    
    run:
    [7000, 9000, 11000, 22000, 34000, 63000, 76000, 95000]
    ceiling : 63000
    higher : 7000
    floor : 11000
    lower : 11000
    lower : 9000
    pollFirst : 7000
    pollLast : 95000
    descendingSet : [76000, 63000, 34000, 22000, 11000, 9000]
    descendingIterator : java.util.TreeMap$NavigableSubMap$DescendingSubMapKeyIterator@3b81a1bc
    full : [9000, 11000, 22000, 34000, 63000, 76000]
    BUILD SUCCESSFUL (total time: 0 seconds)

*/