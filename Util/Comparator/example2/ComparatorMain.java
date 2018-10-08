/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sense;

import java.util.Comparator;
import java.util.TreeSet;

/**
 *
 * @author nSense
 */
public class ComparatorMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Employee e1 = new Employee("Rahul", 100);
        Employee e2 = new Employee("Sunita", 101);
        Employee e3 = new Employee("Akchhata", 102);
        Employee e4 = new Employee("Manorma", 103);
        Employee e5 = new Employee("Amarjeet", 104);
        TreeSet t = new TreeSet();
        t.add(e1);
        t.add(e2);
        t.add(e3);
        t.add(e4);
        t.add(e5);
        System.out.println("TreeSet t"+t);
        TreeSet t2 = new TreeSet(new EmployeeComparator());
        t2.add(e1);
        t2.add(e2);
        t2.add(e3);
        t2.add(e4);
        t2.add(e5);
        System.out.println("TreeSet t2"+t2);
        
    }
   
}