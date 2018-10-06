
package lhs;

import java.util.LinkedHashSet;

public class LinkedHashSetDemo{

    public static void main(String[] args) {
                              
        LinkedHashSet lhs = new LinkedHashSet();
        
        for (int i = 0; i <= 10; i++) {
           lhs.add("" + (char) (i + 65));
        }
        lhs.add(1);
        lhs.add(2);
        lhs.add(3);
        lhs.add(4);
        lhs.add("1");
        lhs.add("2");
        lhs.add("3");
        lhs.add("Rahul");
        lhs.add("Sunita");
        lhs.add(null);
        System.out.println(lhs.add(1));
        System.out.println(lhs.add("Rahul"));
        System.out.println(lhs.add("rahul"));
        System.out.println("Before All : " + lhs);
    } 
}