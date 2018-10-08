/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pq;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *
 * @author nSense
 */
public class PriorityQueueWithComparator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        
        PriorityQueue q = new PriorityQueue(15, new MyComparator());
        
        q.offer("A");
        q.offer("B");
        q.offer("C");
        q.offer("D");
        q.offer("E");
        q.offer("Z");
        q.offer("P");
        q.offer("L");
        q.offer("I");

        System.out.println(q);
    }
}

class MyComparator implements Comparator{

    @Override
    public int compare(Object o1, Object o2) {
        String s1 = (String)o1;
        String s2 = o2.toString();
        return s2.compareTo(s1);
    }
}