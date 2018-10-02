


package stack;
import java.util.Iterator;
import java.util.Stack;

public class StackIterator{

	public static void main(String[] args) {
		
		Stack s = new Stack();
        for(int i=0; i<10;i++)
            s.add(""+(char)(i+65));
        
        Iterator itr  = s.iterator();
        
        while(itr.hasNext()){
            String itrElement = itr.next().toString();
            System.out.println("Iterator One-By-One : "+itrElement);
        }
        System.out.println("Get All In Once : "+s);
	}
}