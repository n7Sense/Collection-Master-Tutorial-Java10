
package cur;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.Vector;

public class CursorImplimentationClassName{

	public static void main(String[] args) {

		LinkedList ll = new LinkedList();
		ListIterator ltr = ll.listIterator();
        Iterator itr = ll.iterator();
        Enumeration e= new Vector().elements();

        System.out.println("ListIterator : "+ltr.getClass().getName());
        System.out.println("Iterator : "+itr.getClass().getName());
        System.out.println("Enumeration : "+e.getClass().getName());
	}
}