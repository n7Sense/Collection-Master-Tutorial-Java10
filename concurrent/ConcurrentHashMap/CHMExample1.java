/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nsense;

import java.util.concurrent.ConcurrentHashMap;


/**
 *
 * @author nSense
 */
public class CHMExample1 extends Thread{ 

    /**
     * @param args the command line arguments
     */
  
    public static void main(String[] args) throws InterruptedException {
            
        ConcurrentHashMap chm= new ConcurrentHashMap();
        chm.put(101, "A");
        chm.put(102, "B");
        chm.putIfAbsent(103, "C");
        chm.putIfAbsent(101, "D");
        chm.remove(101, "D");
        chm.replace(102, "B", "E");
        System.out.println(chm);
    }
    
}

Out/Put:
Note: Recompile with -Xlint:unchecked for details.
compile:
run:
{101=A, 102=E, 103=C}
BUILD SUCCESSFUL (total time: 1 second)