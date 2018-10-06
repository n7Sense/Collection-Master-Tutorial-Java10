

package ts;

import java.util.TreeSet;

public class TreeSetWithStringBuffer{

	public static void main(String[] args) {
		  
		  TreeSet t = new TreeSet();
   
        t.add(new StringBuffer("A"));
        t.add(new StringBuffer("B"));
        t.add(new StringBuffer("L"));
        t.add(new StringBuffer("Z"));
        t.add(new StringBuffer("Q"));
        t.add(new StringBuffer("R"));
        System.out.println("Before All : " + t);
	}
}
out/put:Runtime Exception in thread "main" java.lang.ClassCastException: 
		java.base/java.lang.StringBuffer cannot be cast to java.base/java.lang.Comparable