/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sense;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author nSense
 */
public class ProblemWithColectionDemo extends Thread{

    /**
     * @param args the command line arguments
     */
    static ArrayList al = new ArrayList();
    @Override
    public void run() {
          try{
              Thread.sleep(2000);
          }catch(InterruptedException e){          }
          System.out.println("Child Thread updating List");
          al.add(45);
    }

    public static void main(String[] args)throws InterruptedException {
        
        al.add(10);
        al.add(20);
        al.add(50);
        
        ProblemWithColectionDemo t1 = new ProblemWithColectionDemo();
        t1.start();
        
        Iterator itr= al.iterator();
        while(itr.hasNext()){
            String s = itr.next().toString();
            System.out.println("Main Thread Iteration List and Concurrent Object is : "+s);
            Thread.sleep(3000);
            
        }
        System.out.println(al);
    }
}


ant -f C:\\Users\\nSense\\Documents\\NetBeansProjects\\nSense -Dnb.internal.action.name=run run
init:
Deleting: C:\Users\nSense\Documents\NetBeansProjects\nSense\build\built-jar.properties
deps-jar:
Updating property file: C:\Users\nSense\Documents\NetBeansProjects\nSense\build\built-jar.properties
Compiling 1 source file to C:\Users\nSense\Documents\NetBeansProjects\nSense\build\classes
Note: C:\Users\nSense\Documents\NetBeansProjects\nSense\src\nsense\NSense.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
compile:
run:
Main Thread Iteration List and Concurrent Object is : 10
Child Thread updating List
Exception in thread "main" java.util.ConcurrentModificationException
  at java.base/java.util.ArrayList$Itr.checkForComodification(ArrayList.java:937)
  at java.base/java.util.ArrayList$Itr.next(ArrayList.java:891)
  at nsense.NSense.main(NSense.java:43)
C:\Users\nSense\Documents\NetBeansProjects\nSense\nbproject\build-impl.xml:1328: The following error occurred while executing this line:
C:\Users\nSense\Documents\NetBeansProjects\nSense\nbproject\build-impl.xml:948: Java returned: 1
BUILD FAILED (total time: 7 seconds)

