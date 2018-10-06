
package hm;
import java.util.HashMap;

public class HashMapKeyInsertionMechanism{

	public static void main(String[] args) {
		
		Integer i1 = new Integer(10);
		Integer i2 = new Integer(10);

		HashMap hm =new HashMap();
		hm.put(i1, "Rahul");
		hm.put(i2, "Sunita");

		System.out.println(hm);
	}
}

/* out/put is
{10 = Sunita}
*/