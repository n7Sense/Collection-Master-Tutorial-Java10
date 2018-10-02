
package stack;
import java.util.Enumeration;
import java.util.Stack;

public class StackEnumeration{

	public static void main(String[] args) {
		
		Stack s = new Stack();
        for(int i=0; i<10;i++)
            s.add(""+(char)(i+65));
        
        Enumeration e = s.elements();
        while(e.hasMoreElements()){
            String element = e.nextElement().toString();
            System.out.println("One-By-One : "+element);
        }
        System.out.println("Get All In Once : "+s);
	}
}