package al;

import java.util.ArrayList;
import java.util.Iterator;


public class ArrayListIterator{

	public static void main(String[] args) {
		

        ArrayList al = new ArrayList();
        for(int i=0; i<=10;i++)
            al.add(i);
        
        System.out.println("Before All : "+al);
        Iterator itr  = al.iterator();
        
        while(itr.hasNext()){
            Integer i = (Integer)itr.next();
            if(i%2==0)
                System.out.println("Even no. : "+i);
            else
                itr.remove();
        }      
        System.out.println("After ALL : "+al);
        
	}
}

/**OutPut
	run:
	Before All : [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
	Even no. : 0
	Even no. : 2
	Even no. : 4
	Even no. : 6
	Even no. : 8
	Even no. : 10
	After ALL : [0, 2, 4, 6, 8, 10]
*/