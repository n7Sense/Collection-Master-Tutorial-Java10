
package lhm;
import java.util.LinkedHashMap;

public class LinkedHashMapKeyInsertionMechanism{

	public static void main(String[] args) {
		
		Integer i1 = new Integer(10);
		Integer i2 = new Integer(10);

		LinkedHashMap lhm =new LinkedHashMap();
		lhm.put(i1, "Rahul");
		lhm.put(i2, "Sunita");

		System.out.println(lhm);
	}
}

/* out/put is
{10 = Rahul, 10= Sunita}
*/