package vector;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;


public class VectorListIterator{

	public static void main(String[] args) {
        
        Stack s = new Stack();
        for(int i=0; i<10;i++)
            s.add(""+(char)(i+65));
        
        ListIterator litr  = s.ListIterator();
        
        while(litr.hasNext()){
            String element = litr.next().toString();
            System.out.println("ListIterator One-By-One : "+element);
        } 
        System.out.println("Get All In Once : "+s);
	}
}