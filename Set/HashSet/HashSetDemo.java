
package hs;

import java.util.HashSet;

public class HashSetDemo{

	public static void main(String[] args) {
		
		HashSet h = new HashSet();
        
        for (int i = 0; i <= 10; i++) {
           h.add("" + (char) (i + 65));
        }
        h.add(1);
        h.add(2);
        h.add(3);
        h.add(4);
        h.add("1");
        h.add("2");
        h.add("3");
        h.add("Rahul");
        h.add("Sunita");
        h.add(null);
        System.out.println(h.add(1));
        System.out.println(h.add("Rahul"));
        System.out.println(h.add("rahul"));
        System.out.println("Before All : " + h);
        
	}
}