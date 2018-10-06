
package ts;
import java.util.TreeSet;

public class TreeSetWithComparable{

    public static void main(String[] args) {
         
        TreeSet t = new TreeSet();
        t.add("K");
        t.add("Z");
        t.add("A");
        t.add("A");
        t.add("K");
        System.out.println(t);
    }
}

Out/Put: 

[A, k, Z]

Insertion in backgroun by JVM
		obj1.compareTo(obj2);

		obj1 {the Object which is to be Inserted }
		and
		obj2{an Object which is already executed.}

			 K
			/ \
		-ve/   \ +ve
		  /     \     
		 A  	Z



