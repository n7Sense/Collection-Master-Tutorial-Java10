package vector;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Vector;


public class VectorListIterator{

	public static void main(String[] args) {
			Vector v = new Vector();
        for(int i=0; i<10;i++)
            v.add(""+(char)(i+65));
        
        ListIterator litr = v.elements();
        while(litr.hasNext()){
            String element = litr.next().toString();
            System.out.println("ListIterator One-By-One : "+element);
        } 
        System.out.println("Get All In Once : "+v);
	}
}