


package stack;
import java.util.Iterator;
import java.util.Vector;

public class StackIterator{

	public static void main(String[] args) {
		
		Vector v = new Vector();
        for(int i=0; i<10;i++)
            v.add(""+(char)(i+65));
        
        Iterator itr  = v.iterator();
        
        while(itr.hasNext()){
            String itrElement = itr.next().toString();
            System.out.println("Iterator One-By-One : "+itrElement);
        }
        System.out.println("Get All In Once : "+v);
	}
}