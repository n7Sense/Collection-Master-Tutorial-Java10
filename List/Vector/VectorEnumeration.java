
package stack;
import java.util.Enumeration;
import java.util.Vector;

public class StackEnumeration{

	public static void main(String[] args) {
		
		Vector v = new Vector();
        for(int i=0; i<10;i++)
            v.add(""+(char)(i+65));
        
        Enumeration e = v.elements();
        while(e.hasMoreElements()){
            String element = e.nextElement().toString();
            System.out.println("One-By-One : "+element);
        }
        System.out.println("Get All In Once : "+v);
	}
}