/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pq;

import java.util.PriorityQueue;

/**
 *
 * @author nSense
 */
public class PriorityQueueDemo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        
        PriorityQueue q = new PriorityQueue();
        //System.out.println(q.peek());
        //System.out.println(q.element());
        for (int i = 0; i <= 10; i++) {
            q.offer(i);
        }
        
        System.out.println(q);
        System.out.println(q.poll());
        System.out.println(q);
    
       
    }
   
}

