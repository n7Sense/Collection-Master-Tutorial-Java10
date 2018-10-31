/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nsense;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author nSense
 */
public class NSense extends Thread{ 

    /**
     * @param args the command line arguments
     */
    
    static ConcurrentHashMap chm= new ConcurrentHashMap();
    @Override
    
    public void run() { 
        try{
            Thread.sleep(2000);
        }catch(InterruptedException e){ }
        chm.put(103, "C");
        System.out.println("Child thread executing & added..."+chm.get(103));
    }

    public static void main(String[] args) throws InterruptedException {
        
        chm.put(101, "A");
        chm.put(102, "B");
        System.out.println("Before : "+chm);
        NSense ns = new NSense();
        ns.start();
        Set s = chm.keySet();
        Iterator itr = s.iterator();
        
        while(itr.hasNext()) {
            Integer i1 = (Integer)itr.next();
            System.out.println("Main Thread Iterating Map : "+i1+" Entry Is : "+chm.get(i1));
            Thread.sleep(3000);
        }
        System.out.println(chm);
    } 
}
